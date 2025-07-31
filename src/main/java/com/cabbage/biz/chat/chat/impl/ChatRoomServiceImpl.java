package com.cabbage.biz.chat.chat.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cabbage.biz.chat.chat.ChatRoomService;
import com.cabbage.biz.chat.chat.ChatRoomVO;
import com.cabbage.biz.chat.post.PostService;
import com.cabbage.biz.chat.post.PostVO;
import com.cabbage.biz.chat.user.UserService;
import com.cabbage.biz.chat.user.UserVO;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ChatRoomServiceImpl implements ChatRoomService{
	
	private final ChatDAO chatDAO;
	private final UserService userService;
	private final PostService postService;
	
	
	@Override
	public ChatRoomVO getChatRoomById(long chatRoomId) {
		ChatRoomVO chatRoom = ChatRoomVO.builder()
										.chatRoomId(chatRoomId)
										.build();
		
		chatRoom = chatDAO.selectChatRoomById(chatRoom);
		if(chatRoom == null)
			return null;
		chatRoom.setUsers(userService.selectUsersInChatRoom(chatRoom));
		
		
		return chatRoom;
	}
	
	
	@Override
	public List<ChatRoomVO> getChatRoomListByUserId(String userId) {
		List<ChatRoomVO> chatRooms = chatDAO.selectChatRoomListByUserId(UserVO.builder()
																				  .userId(userId)
																				  .build());
		// 프사 없는 유저들 기본프사로 설정
		// 닉네임 안가져와지면 탈퇴한 회원으로 설정
		// 그 전에 유저가 나간 시점보다 마지막 카운트가 작거나 같으면 리스트에서 삭제
		for(int i = 0; i < chatRooms.size(); i++) {
			ChatRoomVO chatRoom = chatRooms.get(i);
			
			if(chatRoom.getExitPoint() >= chatRoom.getLastMessageCountByRoom()) {
				chatRooms.remove(i--);
				continue;
			}
			
			if(chatRoom.getOtherUserNickname() == null)
				chatRoom.setOtherUserNickname("탈퇴한 회원");
			
			if(chatRoom.getOtherUserProfilePic() == null)
				chatRoom.setOtherUserProfilePic(UserService.PROFILE_DEFAULT);
		}
		
		chatRooms.forEach( chatRoom -> {
			if(chatRoom.getOtherUserProfilePic() == null)
				chatRoom.setOtherUserProfilePic(UserService.PROFILE_DEFAULT);
		});
		
		// 최근 메세지 시간으로 오름차순 정렬 (현재시간 - 보낸시간) (초)
		chatRooms.sort( (s1, s2) -> Long.compare(s1.getLastMessageTime(), s2.getLastMessageTime()) );
		
		return chatRooms;
	}

	@Override
	public ChatRoomVO getChatRoomVOByUserIdAndChatRoomId(ChatRoomVO chatRoom) {
		chatRoom = chatDAO.selectChatRoomVOByUserIdAndChatRoomId(chatRoom);
		
		if(chatRoom.getOtherUserProfilePic() == null)
			chatRoom.setOtherUserProfilePic(UserService.PROFILE_DEFAULT);
		
		return chatRoom;
	}
	
	@Override
	public List<ChatRoomVO> getLastMessageTimeListByChatRoomId(List<Integer> chatRoomIdList) {
		return chatDAO.selectLastMessageTimeListByChatRoomId(chatRoomIdList);
	}


	@Override
	public ChatRoomVO getChatRoomWhenRequest(long postId, String requestUserId) {
		
		ChatRoomVO chatRoom = ChatRoomVO.builder()
										.requestUserId(requestUserId)
										.postId(postId)
										.build();
		
		return chatDAO.selectChatRoomWhenRequest(chatRoom);
	}


	@Override
	public ChatRoomVO makeNewChatRoom(long postId, String userId) {
		PostVO post = postService.getPostById(postId);
		String sellerId = post.getSellerId();
		
		// 글 작성자랑 채팅 건 사람이 같으면 채탕방 생성 안하고 null 리턴
		if(sellerId.equals(userId))
			return null;
		
		ChatRoomVO chatRoom = ChatRoomVO.builder()
				.postId(postId)
				.build();
		
		chatDAO.insertChatRoom(chatRoom); // 인서트하고 chatRoomId가 charRoom에 들어간다
		
		// post (list로 동시에 들어가나 테스트)
		userService.insertUsersInChatRoom(chatRoom.getChatRoomId(), sellerId, userId);
		
		return chatRoom;
	}


	@Override
	public void exitChatRoom(ChatRoomVO chatRoom) {
		chatDAO.updateUserExitPoint(chatRoom);
	}

	@Override
	public Integer getUnreadCount(String userId) {
		return chatDAO.selectUnreadCount(ChatRoomVO.builder().requestUserId(userId).build());
	}


	


	

	
	
	
//====================================== private method ========================================
	
	
}
