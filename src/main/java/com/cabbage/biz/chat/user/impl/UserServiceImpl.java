package com.cabbage.biz.chat.user.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.cabbage.biz.chat.chat.ChatRoomVO;
import com.cabbage.biz.chat.chat.impl.ChatDAO;
import com.cabbage.biz.chat.user.UserService;
import com.cabbage.biz.chat.user.UserVO;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
	
	private final ChatDAO chatDAO;

	@Override
	public UserVO getUserById(String userId) {
		
		UserVO user = chatDAO.selectUserById(UserVO.builder()
												   .userId(userId)
												   .build());
		
		// 프사가 null이면 기본프사로 설정
		if(user.getProfilePic() == null)
			user.setProfilePic(PROFILE_DEFAULT);
		
		return user;
	}

	@Override
	public List<UserVO> selectUsersInChatRoom(ChatRoomVO chatRoom) {
		List<UserVO> users = chatDAO.selectUsersInChatRoom(chatRoom);
		
		// 프사가 null이면 기본프사로 설정
		users.forEach( user -> {
			if(user.getProfilePic() == null)
				user.setProfilePic(PROFILE_DEFAULT);
			if(user.getUserNickname() == null)
				user.setUserNickname("탈퇴한 회원");
		});
		
		return users;
	}
	
	@Override
	public void insertUsersInChatRoom(long chatRoomId, String sellerId, String userId) {
		List<Map<String, Object>> data = new ArrayList<Map<String,Object>>();
		
		data.add(makeDataForInsertUsersInChatRoom(chatRoomId, sellerId));
		data.add(makeDataForInsertUsersInChatRoom(chatRoomId, userId));
		chatDAO.insertUsersInChatRoom(data);
		
	}
	
//====================================== private method ========================================
	
	private Map<String, Object> makeDataForInsertUsersInChatRoom(long chatRoomId, String userId){
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("chatRoomId", chatRoomId);
		data.put("userId", userId);
		return data;
	}

	
}
