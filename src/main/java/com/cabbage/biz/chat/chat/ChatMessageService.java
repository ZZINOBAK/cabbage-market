package com.cabbage.biz.chat.chat;

import java.util.List;
import java.util.Map;

public interface ChatMessageService {
	
	void sendChat(ChatMessageVO chatMessage);
	void readChat(ChatRoomVO chatRoom);
	String deleteChat(ChatMessageVO chatMessage);
	List<ChatMessageVO> getChatMessageListByChatRoomId(long roomId, String requestUserId, Long lastCount);
	List<Map<String, String>> getEmojiList();
	String cancelPay(ChatMessageVO chatMessage);
	String finishPay(ChatMessageVO chatMessage);
	ChatMessageVO getChatMessageForPayment(ChatMessageVO chatMessage);
	
}
