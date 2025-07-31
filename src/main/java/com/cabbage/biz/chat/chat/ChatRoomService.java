package com.cabbage.biz.chat.chat;

import java.util.List;

public interface ChatRoomService {
	
	ChatRoomVO getChatRoomById(long chatRoomId);
	ChatRoomVO getChatRoomWhenRequest(long postId, String requestUserId);
	List<ChatRoomVO> getChatRoomListByUserId(String userId);
	ChatRoomVO getChatRoomVOByUserIdAndChatRoomId(ChatRoomVO chatRoom);
	List<ChatRoomVO> getLastMessageTimeListByChatRoomId(List<Integer> chatRoomIdList);
	ChatRoomVO makeNewChatRoom(long postId, String userId);
	void exitChatRoom(ChatRoomVO chatRoom);
	Integer getUnreadCount(String userId);
}
