package com.cabbage.biz.chat.chat.impl;

import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.cabbage.biz.chat.chat.ChatMessageVO;
import com.cabbage.biz.chat.chat.ChatRoomVO;
import com.cabbage.biz.chat.post.PostVO;
import com.cabbage.biz.chat.user.UserVO;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class ChatDAO {
	
	private final SqlSessionTemplate mybatis;
	
	
//================================================= chatMessage ====================================================
	public void insertChatMessage(ChatMessageVO chatMessage) {
		mybatis.insert("chatMessage.insertChatMessage", chatMessage);
	}
	
	public void updateUnreadMessageCount(ChatMessageVO chatMessage) {
		mybatis.update("chatMessage.addUnreadCount", chatMessage);
	}
	
	public void updateUnreadMessageCountToZERO(ChatRoomVO chatRoom) {
		mybatis.update("chatMessage.updateUnreadMessageCountToZERO", chatRoom);
	}
	
	public void updateMessageTypeToDELETE(ChatMessageVO chatMessage) {
		mybatis.update("chatMessage.updateMessageTypeToDELETE", chatMessage);
	}
	
	public int updateMessagePayStatus(ChatMessageVO chatMessage) {
		return mybatis.update("chatMessage.updateMessagePayStatus", chatMessage);
	}
	
	public int finishMessagePay(ChatMessageVO chatMessage) {
		return mybatis.update("chatMessage.finishMessagePay", chatMessage);
	}
	
	public Integer selectMessageTimeTermToDELETE(ChatMessageVO chatMessageVO) {
		return mybatis.selectOne("chatMessage.selectMessageTimeTermToDELETE", chatMessageVO);
	}
	
	public List<ChatMessageVO> selectChatMessageListByChatRoomId(ChatRoomVO chatRoom) {
		return mybatis.selectList("chatMessage.getChatMessageListByRoomId", chatRoom);
	}
	
	public List<Map<String, String>> selectEmojiList(){
		return mybatis.selectList("chatMessage.getEmojiList");
	}
	
	public ChatMessageVO selectChatMessageForPaying(ChatMessageVO chatMessage) {
		return mybatis.selectOne("chatMessage.selectChatMessageForPaying", chatMessage);
	}

	
//================================================= chatRoom ====================================================
	public ChatRoomVO selectChatRoomById(ChatRoomVO chatRoom) {
		return mybatis.selectOne("chatRoom.getChatRoom", chatRoom);
	}
	
	public ChatRoomVO selectChatRoomAndResetCount(ChatRoomVO chatRoom) {
		mybatis.update("chatRoom.getChatRoomAndResetCount", chatRoom); // update 하고 select까지
		return chatRoom;
	}
	
	public ChatRoomVO selectChatRoomWhenRequest(ChatRoomVO chatRoom) {
		return mybatis.selectOne("chatRoom.getChatRoomWhenRequest", chatRoom);
	}
	
	public List<ChatRoomVO> selectChatRoomListByUserId (UserVO user){
		return mybatis.selectList("chatRoom.getChatRoomListByUserId", user);
	}
	
	public ChatRoomVO selectChatRoomVOByUserIdAndChatRoomId(ChatRoomVO chatRoom) {
		return mybatis.selectOne("chatRoom.getChatRoomByUserIdAndChatRoomId", chatRoom);
	}
	
	public List<ChatRoomVO> selectLastMessageTimeListByChatRoomId(List<Integer> chatRoomIdList) {
		return mybatis.selectList("chatRoom.getLastMessageTimeListByChatRoomId", chatRoomIdList);
	}
	
	public void insertChatRoom(ChatRoomVO chatRoom) {
		mybatis.insert("chatRoom.insertChatRoom", chatRoom);
	}
	
	public void updateUserExitPoint(ChatRoomVO chatRoom) {
		mybatis.update("chatRoom.updateUserExitPoint", chatRoom);
	}
	
	public Integer selectUnreadCount(ChatRoomVO chatRoom) {
		return mybatis.selectOne("chatRoom.getUnreadCount", chatRoom);
	}
	
	
//================================================= post ====================================================
	public PostVO selectPostById(PostVO post) {
		return mybatis.selectOne("post.getPost", post);
	}
	
	
//================================================= user ====================================================
	public UserVO selectUserById(UserVO user) {
		return mybatis.selectOne("user.getUser", user.getUserId());
	}
	
	public List<UserVO> selectUsersInChatRoom(ChatRoomVO chatRoom){
		return mybatis.selectList("user.getUsersInChatRoom", chatRoom);
	}
	
	// 채팅방 아이디, 유저아이디 (두번씩 실행할 것) (채팅방에 유저가 두명이니까)
	public void insertUsersInChatRoom(List<Map<String, Object>> data) {
		mybatis.insert("user.insertUsersInChatRoom", data);
	}
	
	
	
	
}
