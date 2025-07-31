package com.cabbage.biz.chat.view;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cabbage.biz.chat.chat.ChatMessageService;
import com.cabbage.biz.chat.chat.ChatMessageVO;
import com.cabbage.biz.chat.chat.ChatRoomService;
import com.cabbage.biz.chat.chat.ChatRoomVO;
import com.cabbage.biz.chat.sse.ChatSSEService;
import com.cabbage.biz.chat.user.UserVO;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/chatMessage")
@RestController
public class ChatMessageController {
	
	private final ChatMessageService chatMessageService;
	private final ChatRoomService chatRoomService;
	private final ChatSSEService chatSSEService;

	
	@PostMapping("/send")
	public void sendChat(ChatMessageVO chatMessage) {
		chatMessageService.sendChat(chatMessage);
	}
	
	@PostMapping("/read")
	public void readChat(HttpSession session, ChatRoomVO chatRoom) {
		String userId = (String)session.getAttribute("userId");
		UserVO partnerUserVO = null;
		
		chatRoom = chatRoomService.getChatRoomById(chatRoom.getChatRoomId());
		
		if(chatRoom == null)
			return;
		
		chatRoom.setRequestUserId(userId);
		for(UserVO user : chatRoom.getUsers()) {
			if(!user.getUserId().equals(userId))
				partnerUserVO = user;
		}
		chatMessageService.readChat(chatRoom);
		// 해당 채팅방 상대 유저에게 상대가 읽었다는 이벤트 뿌리기 (데이터는 널 방지로 아무거나 보냄)
		chatSSEService.sendEvent(partnerUserVO, chatRoom, ChatSSEService.EVENT_NAME_CHAT_READ);
	}
	
	@PostMapping("/delete")
	public String deleteChat(HttpSession session, ChatMessageVO chatMessage) {
		String userId = (String)session.getAttribute("userId");
		chatMessage.setSenderId(userId);
		
		
		String result = chatMessageService.deleteChat(chatMessage);
		
		if(result.equals("success")) {
			chatMessage.setMessageType("DEL");
			chatMessageService.sendChat(chatMessage);
		}
		
		return result;
	}
	
	@PostMapping(value = {"/getList/{chatRoomId}/{lastCount}", "/getList/{chatRoomId}"})
	public List<ChatMessageVO> getChatMessageList(@PathVariable long chatRoomId, @PathVariable(required = false) Long lastCount, HttpSession session) {
		String reqestUserId = (String)session.getAttribute("userId");
		
		return chatMessageService.getChatMessageListByChatRoomId(chatRoomId, reqestUserId, lastCount);
	}
	
	@PostMapping("/cancelPay")
	public String cancelPay(ChatMessageVO chatMessage, HttpSession session) {
		String userId = (String)session.getAttribute("userId");
		chatMessage.setSenderId(userId);
		
		// 요청자와 채팅 작성자가 다르면 fail을 리턴한다
		String result = chatMessageService.cancelPay(chatMessage);
		
		if(result.equals("success")) 
			chatMessageService.sendChat(chatMessage);
		
		
		return result;
	}
	
	// chatRoomId, countByRoom이 넘어온다
	@PostMapping("/successPay")
	public void finishPay(ChatMessageVO chatMessage) {
		chatMessage.setMessageType("PAY");
		chatMessage.setPayStatus("FINISH");
		
		String result = chatMessageService.finishPay(chatMessage);
		
		// 오류 대비해서 성공했을 시에만 이벤트 보냄
		if(result.equals("success"))
			chatMessageService.sendChat(chatMessage);
	}
	
	@PostMapping("/dataForPayment")
	public ChatMessageVO getDataForPayment(ChatMessageVO chatMessage, HttpSession session) {
		String userId = (String)session.getAttribute("userId");
		chatMessage.setRequestUserId(userId);
		
		ChatMessageVO data = chatMessageService.getChatMessageForPayment(chatMessage);
		
		return data;
	}
	
	
}
