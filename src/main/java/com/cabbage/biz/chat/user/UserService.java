package com.cabbage.biz.chat.user;

import java.util.List;

import com.cabbage.biz.chat.chat.ChatRoomVO;

public interface UserService {
	String PROFILE_DEFAULT = "profile_default.png"; 
	
	UserVO getUserById(String user);
	List<UserVO> selectUsersInChatRoom(ChatRoomVO chatRoom);
	void insertUsersInChatRoom(long chatRoomId, String sellerId, String userId);

}
