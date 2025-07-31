package com.cabbage.biz.chat.chat.impl;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cabbage.biz.chat.chat.ChatMessageService;
import com.cabbage.biz.chat.chat.ChatMessageVO;
import com.cabbage.biz.chat.chat.ChatRoomService;
import com.cabbage.biz.chat.chat.ChatRoomVO;
import com.cabbage.biz.chat.sse.ChatSSEService;
import com.cabbage.biz.chat.user.UserVO;
import com.cabbage.biz.common.CommonData;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ChatMessageServiceImpl implements ChatMessageService {
	
		
	private final ChatDAO chatDAO;
	private final ChatRoomService chatRoomService;
	private final ChatSSEService chatSSEService;
	
	
	/* 사용자가 채팅을 보냈을 때 사용될 메소드
	새로운 메세지가 전송되면 해당 채팅방에 있는 모든 유저(2명)에게 메세지를 보냄
	 */
	@Override
	public void sendChat(ChatMessageVO chatMessage) {
		String messageType = chatMessage.getMessageType();
		
		// 사진 전송했을 때 처리
		if(messageType.equals("PIC")) {
			MultipartFile uploadPic = chatMessage.getUploadPic();
			String originalFileName = uploadPic.getOriginalFilename();
			String fileExtension = originalFileName.substring(originalFileName.lastIndexOf(".")); // 파일 확장자
			String randomFileName = "CHAT" + "_" + UUID.randomUUID().toString()+ fileExtension; // 카테고리_랜덤글자.확장자
			
			File saveFile = new File(CommonData.FILE_UPLOAD_ROOT + "/chatPic/", randomFileName);
			
			try {
				uploadPic.transferTo(saveFile);
			} catch (IllegalStateException | IOException e) {
				e.printStackTrace();
			}
			
			chatMessage.setPicFileName(randomFileName);
		}
		
		// 결제요청일 때 처리 (₩100,000  --> 100000) (finalPriceInput --> finalPrice)
		if(messageType.equals("PAY") && chatMessage.getPayStatus().equals("WAITING")) {
			String[] finalPriceArr = chatMessage.getFinalPriceInput().substring(1).split(",");
			StringBuilder sb = new StringBuilder();
			long finalPrice;
			for(String num : finalPriceArr) {
				sb.append(num);
			}
			// 숫자로 변환 안되면 리턴
			try {
				finalPrice = Long.parseLong(sb.toString());
			} catch (Exception e) {
				return;
			}
			chatMessage.setFinalPrice(finalPrice);
		}
		
		String payStatus = chatMessage.getPayStatus();

		// 삭제 요청, 결제 취소요청 일 때는 안해도 됨
		if (!messageType.equals("DEL") ) {
			
			if(!messageType.equals("PAY") || (messageType.equals("PAY") && payStatus.equals("WAITING"))) {
				// 혹시나 1000자 넘어서 들어오면 차단
				int contentLength = chatMessage.getMessageContent() == null ? 0 : chatMessage.getMessageContent().length();
				if(contentLength > 1000 || contentLength < 1)
					return;
				
				// 현재 chatMessageVO를 DB에 저장
				chatDAO.insertChatMessage(chatMessage);
				// 현재 채팅방의 상대 유저에 안읽은 메세지 개수 1 증가
				chatDAO.updateUnreadMessageCount(chatMessage);
				
				chatMessage.setUploadPic(null); // 업로드 파일 데이터 삭제 하고 전송
				
			}
			
		}
		
		
		
		// 입력받은 채팅방 아이디로 ChatRoomVO를 가져와서 유저 리스트에 있는 유저에게 전송
		ChatRoomVO chatRoom = chatRoomService.getChatRoomById(chatMessage.getChatRoomId());
		List<UserVO> users = chatRoom.getUsers();
		
		// 리스트에 들어있는 모든 유저에게 채팅 내용을 뿌린다
		users.forEach( user -> {
			// 현재 user의 emitter를 모두 가져와서 존재하면 모든 emitter에게 뿌린다 (실제로 전송되는건 브라우저당 하나뿐)
			chatSSEService.sendEvent(user, chatMessage, ChatSSEService.EVENT_NAME_CHAT_MESSAGE);
		});
		
	}
	
	// 안읽은 채팅 0으로 초기화 (읽음)
	@Override
	public void readChat(ChatRoomVO chatRoom) {
		chatDAO.updateUnreadMessageCountToZERO(chatRoom);
		
	}
	
	@Override
	public String deleteChat(ChatMessageVO chatMessage) {
		String result = "";
		
		Integer timeTermInMinute = chatDAO.selectMessageTimeTermToDELETE(chatMessage);
		
		if(timeTermInMinute == null)
			result = "notMine"; // 유저명 불일치
		else if(timeTermInMinute > 5)
			result = "timeOut"; // 5분 초과
		else
			result = "success";
		
		if(result.equals("success"))
			chatDAO.updateMessageTypeToDELETE(chatMessage);
		
		// timeOut, notMine, success
		return result;
	}


	// lastCount 가 null이면 최근 20개 불러옴
	@Override
	public List<ChatMessageVO> getChatMessageListByChatRoomId(long roomId, String requestUserId, Long lastCount) {
		return chatDAO.selectChatMessageListByChatRoomId(ChatRoomVO.builder()
																		  .chatRoomId(roomId)
																		  .requestUserId(requestUserId)
																		  .lastCount(lastCount)
																		  .build());
	}

	@Override
	public List<Map<String, String>> getEmojiList() {
		return chatDAO.selectEmojiList();
	}

	@Override
	public String cancelPay(ChatMessageVO chatMessage) {
		String result = null;
		// TODO : 요청 유저랑 메세지 보낸 유저랑 같은지 확인 후 다르면 리턴
		int count = chatDAO.updateMessagePayStatus(chatMessage);
		
		if(count < 1) 
			result = "fail";
		else 
			result = "success";
		
		return result;
		
	}
	
	public String finishPay(ChatMessageVO chatMessage) {
		String result = null;
		int count = chatDAO.finishMessagePay(chatMessage);
		
		if (count < 1)
			result = "fail";
		else
			result = "success";
		
		return result;
		
	}

	// 결제 대상자와 요청자가 일치하지 않으면 데이터가 찾아지지 않는다.
	@Override
	public ChatMessageVO getChatMessageForPayment(ChatMessageVO chatMessage) {
		ChatMessageVO data = chatDAO.selectChatMessageForPaying(chatMessage);
		
		return data;
	}
	
	

	




	
	
	

	
	
}

