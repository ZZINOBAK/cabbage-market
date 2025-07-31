package com.cabbage.biz.chat.sse.impl;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.cabbage.biz.chat.sse.ChatSSEService;
import com.cabbage.biz.chat.user.UserVO;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ChatSSEServiceImpl implements ChatSSEService{
	// emitter랑 메세지 캐시 가지고있는 레포지토리
	private final EmitterRepository emitterRepository;
	
	// /chat에 들어왔을 때 SSE 구독
	@Override
	public SseEmitter subscribe(String userId, String lastEventId) {
		/* emitter 아이디는 유저 아이디에 현재 시간을 붙여서 사용한다
		(페이지가 새로고침되면 새로운 emitter가 생기고, 이벤트를 보낼때 userId로 시작하는 모든 emitter 에 보낸다.
		그러면 현재 살아있는 emitter 하나에만 전송 성공하고 나머지는 실패한다.
		전송에 실패하면 EmitterRepository 에서 해당 emitter를 삭제한다.)   
		*/
		String emitterId = userId + "_" + System.currentTimeMillis();
		
		// 새로운 emitter를 만료시간을 설정해서 repository에 저장
		SseEmitter emitter = emitterRepository.saveEmitter(emitterId, new SseEmitter(DEFAULT_TIMEOUT));
		
		
		// emitter가 만료되면 repository에서 삭제하라는 설정
		emitter.onCompletion(() -> emitterRepository.deleteEmitterById(emitterId));
		emitter.onTimeout(() -> emitterRepository.deleteEmitterById(emitterId));
		
		// 오류 방지를 위해 데이터 하나를 보냄 (503)
		String eventId = makeEventId(userId, ChatSSEService.EVENT_NAME_CONNECTED);
		sendToClient(emitter, emitterId, eventId, getEventName(eventId), "EventStream Created. [id = " + emitterId + "]");
		
		/* 마지막으로 받은 이벤트의 아이디 값이 존재하면
		repository에 있는 event cache에서 해당 아이디 이후에 보내진 이벤트들을 전부 보내줌
		(이벤트 아이디 또한 userId_currentTimeMillis 이기 때문에 비교해서 값이 큰거만 찾으면 됨)
		*/
		if(!lastEventId.isEmpty()) {
			Map<String, Object> events = emitterRepository.findAllEventCacheStartWithId(userId); // userId로 시작하는 이벤트 캐시들을 찾는다
			List<String> keyList = events.keySet().stream()
										 .filter(key -> lastEventId.compareTo(key) < 0) // 마지막 이벤트 아이디보다 아이디가 큰 것들만 남김
										 .collect(Collectors.toList()); // 리스트로 변환해서 저장
			keyList.sort((s1, s2) -> s1.compareTo(s2)); // key들을 오름차순으로 정렬한다
			// 정렬된 순서대로 전송
			for(String key : keyList) {
				sendToClient(emitter, emitterId, key, getEventName(key), events.get(key));
			}
			
			// 전송하고 해당 유저의 이벤트들 캐시에서 제거
			emitterRepository.deleteAllEventStartWithId(userId);
		}
		
		return emitter;
	}
	
	
	// 유저정보와 채팅메세지 정보를 받아서 유저에게 채팅 메세지를 뿌린다
	@Override
	public void sendEvent(UserVO user, Object data, String eventName) {
		// 일단 이벤트를 캐시에 저장한다
		
		// 해당 유저에게 현재 시간에 뿌린 이벤트다 라는 의미로 userId 랑 현재 시간을 섞은 것을 key로 저장한다.
		// 그리고 뒤에 이벤트 이름도 붙임
		// ex) jsw4795_188118616886_chat
		String eventKey = makeEventId(user.getUserId(), eventName);
		// 지금 만들어진 키로 이벤트 캐시에 데이터를 저장한다
		emitterRepository.saveEventCache(eventKey, data);
		
		
		// 현재 user의 emitter를 모두 가져와서 존재하면 모든 emitter에게 뿌린다 (실제로 전송되는건 브라우저당 하나뿐)
		Map<String, SseEmitter> sseEmitters = emitterRepository.findAllEmitterByUserId(user.getUserId());
		if(sseEmitters.size() > 0) {
			sseEmitters.forEach( (emitterId, emitter) -> {
				sendToClient(emitter, emitterId, eventKey, eventName, data);
			});
		}
	}
	
	
	
//======================================= private method ===============================================
	
	// userId와 현재 시간, eventName을 더해서 eventId를 만들어준다
	private String makeEventId(String userId, String eventName) {
		return userId + "_" + System.currentTimeMillis() + "_" + eventName;
	}
	
	// eventId 에서 eventName 부분만 추출해준다 ex) jsw4795_123214712_chat 에서 chat만 넘겨준다
	private String getEventName(String eventId) {
		return eventId.split("_")[2];
	}
	
	// emitter를 사용해서 아벤트 아이디와 데이터를 전송한다 
	private void sendToClient(SseEmitter emitter, String emitterId, String eventId, String eventName, Object data) {
		try {
			emitter.send(SseEmitter.event()
								   .id(eventId)
								   .name(eventName)
								   .data(data));
		} catch (IOException e) {
			// TODO : emitter가 아니라 이벤트 아이디로 삭제하는 오류 해결
			emitterRepository.deleteEmitterById(emitterId); // 전송에 실패하면 repository에서 해당 emitter 삭제 (더이상 필요 없다는 뜻)
			System.out.println("[SSE 오류] emitter 만료");
		}
		
	}

}
