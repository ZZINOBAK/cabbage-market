package com.cabbage.biz.noti.noti.impl;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.cabbage.biz.noti.noti.NotiService;
import com.cabbage.biz.noti.noti.NotiVO;
import com.cabbage.biz.post.post.PostVO;
import com.cabbage.biz.qa.qa.QaVO;
import com.cabbage.biz.userInfo.user.UserVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@Service("notiService")
public class NotiServiceImpl implements NotiService {
    
	private final NotiDAO notiDAO;
	private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60; //1시간 동안 연결 유
	
    // SSE 연결 지속 시간 설정
	@Autowired 
	private SseEmitters emitterRepository;
	
	// [1] subscribe()
    public SseEmitter subscribe(String userId, String lastEventId) { 
        System.out.println("connect 구독 요청");
    	// (1-1)
    	String emitterId = userId + "_" + System.currentTimeMillis();
        // (1-2)
        SseEmitter emitter = emitterRepository.save(emitterId, new SseEmitter(DEFAULT_TIMEOUT)); // (1-3)
        // (1-4)
        emitter.onCompletion(() -> emitterRepository.deleteById(emitterId));
        emitter.onTimeout(() -> emitterRepository.deleteById(emitterId));

        // (1-5) 503 에러를 방지하기 위한 더미 이벤트 전송
        sendNotification(emitter, emitterId, "이것은 더미더미데이터 오바");
        
        // (1-6) 클라이언트가 미수신한 Event 목록이 존재할 경우 전송하여 Event 유실을 예방
        if (!lastEventId.isEmpty()) {
            Map<String, Object> events = emitterRepository.findAllEventCacheStartWithId(userId);
//            Map<String, Object> events = emitterRepository.findAllEventCacheStartWithId(String.valueOf(userId));
            events.entrySet().stream()
                  .filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
                  .forEach(entry -> sendNotification(emitter, entry.getKey(), entry.getValue()));
        }
        System.out.println("connect 구독 완료");
    	System.out.println("-----------------------------------------------");

        return emitter; // (1-7)
    }
    
    //더미 데이터 전송 메소드
    public void sendNotification(SseEmitter emitter, String emitterId, Object data) { // (4)
    	System.out.println(emitterId + "로 전송 시작!!!!!");
    	try {
            emitter.send(SseEmitter.event()
                    .id(emitterId)
                    .name("sseServer")
                    .data(data)
            );
        } catch (IOException exception) {
            emitterRepository.deleteById(emitterId);
            System.out.println("연결 끊긴 에미터 삭제됨" + emitterId);
//            throw new RuntimeException("연결 오류!");
        }
    	System.out.println(emitterId + "로 전송 완료!!!!!");
    	System.out.println("-----------------------------------------------");
    }
    
    //알림 보내기
    public void send(String wishUserId, String notificationType, Object content) {
    	System.out.println("노티서비스임플_샌드 메소드");
    	System.out.println(wishUserId + "로 전송");
    	Map<String, SseEmitter> emitters = emitterRepository.findAllEmitterStartWithId(wishUserId); // (2-4)
        System.out.println("에미터 사이즈 : " + emitters.size());
    	System.out.println("-----------------------------------------------");
    	emitters.forEach( // (2-5)
            (key, emitter) -> {
                emitterRepository.saveEventCache(key, content);
                sendNotification(emitter, key, content);
            }
        );
    }
    
    @Override
   	public void afterInsertPost(PostVO vo, String saveFileName) {
    	List<Map<String, String>>  a = checkWishKeyWord(vo);
    	String sellerId = vo.getSellerId();

		if (a != null) {
			for (Map<String, String> row : a) {
			    String wishUserId = row.get("USER_ID");
			    
			    if(wishUserId.equals(sellerId)) continue;
			    
			    String wishKeyword = row.get("WISH_KEYWORD");
			    String b = wishKeyword + " - " + vo.getPostTitle();
			    String url = "/post/getPost/" + vo.getPostId();
			    NotiVO voN = new NotiVO();
			    voN.setUserId(wishUserId);
				voN.setPostId(vo.getPostId());
				voN.setNotiType("키워드");
				voN.setNotiContent(b);
				voN.setNotiUrl(url);
		    	//노티 테이블 insert
		    	insertNoti(voN);

		    	// Map을 생성하고 데이터를 추가하는 부분을 한 줄로 간소화
		    	Map<String, Object> data = new HashMap<>();
		    	data.put("url", url);
		    	data.put("notiContent", b);
		    	data.put("notiType", "키워드");
		    	data.put("fileName", saveFileName);
		    	data.put("notiId", voN.getNotiId());
		    	// JSON 형식으로 변환하는 부분을 한 줄로 간소화
		    	String jsonData;
				try {
					jsonData = new ObjectMapper().writeValueAsString(data);
			    	send(wishUserId, "관심상품업로드", jsonData);

				} catch (JsonProcessingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
   	}
    
    @Override
	public void afterUpdatePost(PostVO vo) {
    	List<String> a = checkPostWishList(vo);
    	String sajin = getPostPicForNoti(vo);
    	
		for (String wishUserId : a) {
			// 숫자 포맷 설정 (쉼표 추가)
			NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.getDefault());

			String formattedPrice = numberFormat.format(vo.getPostPrice());
			
			String b = vo.getUserNickname() + "님이 \""
			        + vo.getPostTitle() + "\"의 가격을 " + formattedPrice + "원으로 수정했어요. ";
		    String url = "/post/getPost/" + vo.getPostId();
		    NotiVO voN = new NotiVO();
		    voN.setUserId(wishUserId);
			voN.setPostId(vo.getPostId());
			voN.setNotiType("가격 변동");
			voN.setNotiContent(b);
			voN.setNotiUrl(url);
			
	    	insertNoti(voN);
	    	
	    	// Map을 생성하고 데이터를 추가하는 부분을 한 줄로 간소화
	    	Map<String, Object> data = new HashMap<>();
	    	data.put("url", url);
	    	data.put("notiContent", b);
	    	data.put("notiType", "가격 변동");
	    	data.put("fileName", sajin);
	    	data.put("notiId", voN.getNotiId());
	    	
	    	// JSON 형식으로 변환하는 부분을 한 줄로 간소화
	    	String jsonData;
			try {
				jsonData = new ObjectMapper().writeValueAsString(data);
		    	send(wishUserId, "관심상품가격변동", jsonData);
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
    
	@Override
	public void afterUpdateUserOndo(UserVO vo, String buyerId) {
    	String jaemok = getPostTitleForNoti(vo.getPostId());

		// vo.getUserOndo해서 몇도로 변경됐는지...
		String b = buyerId + "님이 \"" + jaemok 
					+ "\"의 거래 후기를 남겼습니다. 변동된 온도를 확인해 보세요.";
		String url = "/user/myInfo";
	    NotiVO voN = new NotiVO();
		voN.setUserId(vo.getSellerId());
		voN.setNotiType("온도 변동");
		voN.setNotiContent(b);
		voN.setNotiUrl(url);

		insertNoti(voN);
    	
    	// Map을 생성하고 데이터를 추가하는 부분을 한 줄로 간소화
    	Map<String, Object> data = new HashMap<>();
    	data.put("url", url);
    	data.put("notiContent", b);
    	data.put("notiType", "온도 변동");
    	data.put("notiId", voN.getNotiId());
    	
    	// JSON 형식으로 변환하는 부분을 한 줄로 간소화
    	String jsonData;
		try {
			jsonData = new ObjectMapper().writeValueAsString(data);
	    	send(vo.getSellerId(), "온도변동", jsonData);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	@Override
	public void afterInsertQaAnwser(QaVO vo) {
		String b = "문의하신 \"" + vo.getQaTitle() 
					+ "\"의 답변이 등록되었어요.";
		String url = "/qa/qaFormDetail?qaId=" + vo.getQaId();
				
		NotiVO voN = new NotiVO();

		voN.setUserId(vo.getUserId());
		voN.setQaId(vo.getQaId());
		voN.setNotiType("문의글 답변");
		voN.setNotiContent(b);
		voN.setNotiUrl(url);
		
    	insertNoti(voN);

    	// Map을 생성하고 데이터를 추가하는 부분을 한 줄로 간소화
    	Map<String, Object> data = new HashMap<>();
    	data.put("url", url);
    	data.put("notiContent", b);
    	data.put("notiType", "문의글 답변");
    	data.put("notiId", voN.getNotiId());
    	
    	// JSON 형식으로 변환하는 부분을 한 줄로 간소화
    	String jsonData;
		try {
			jsonData = new ObjectMapper().writeValueAsString(data);
	    	send(vo.getUserId(), "문의글답변", jsonData);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public List<String>  checkPostWishList(PostVO postVo) {
    	return notiDAO.checkPostWishList(postVo);
    }
	
	public String  getPostPicForNoti(PostVO postVo) {
    	return notiDAO.getPostPicForNoti(postVo);
    }
    
	private String getPostTitleForNoti(int postId) {
    	return notiDAO.getPostTitleForNoti(postId);
	}
	
	public List<Map<String, String>>  checkWishKeyWord(PostVO postVo) {
    	return notiDAO.checkWishKeyWord(postVo);
    }
    
	@Override
	public int getNotiCountById(String userId) {
		return notiDAO.getNotiCountById(userId);
	}
	
	@Override
	public List<NotiVO> getNotiListById(String userId) {
		return notiDAO.getNotiListById(userId);
	}

	@Override
	public void insertNoti(NotiVO vo) {
		notiDAO.insertNoti(vo);
	}

	@Override
	public void updateNoti(String id) {
		notiDAO.updateNoti(id);
	}

	@Override
	public void deleteNoti(NotiVO vo) {
		notiDAO.deleteNoti(vo);
	}

	@Override
	public NotiVO getNoti(NotiVO vo) {
		return notiDAO.getNoti(vo);
	}

	@Override
	public List<NotiVO> getNotiList() {
		return  notiDAO.getNotiList();
	}

	
	
}
