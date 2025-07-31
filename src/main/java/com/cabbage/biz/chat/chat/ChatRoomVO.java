package com.cabbage.biz.chat.chat;

import java.util.Date;
import java.util.List;

import com.cabbage.biz.chat.user.UserVO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ChatRoomVO {
	private String requestUserId;
	
	private long chatRoomId;
	private long postId;
	private Date chatRoomRegdate;
	
	private String lastMessage; // 마지막 메세지 내용
	private String lastMessageType; // 마지막 메세지 타입 (텍스트인지 사진인지)
	private long lastMessageTime; // 현재 시간과 마지막 메세지 시간의 차이 (초)
	private int unreadMessageCount; // 해당 채팅방에 안읽은 메세지 개수
	private long lastMessageCountByRoom; // 마지막 메세지가 이 채팅방에서 몇번째 채팅인지
	
	private String lastPayStatus;
	
	private long exitPoint;
	
	private String otherUserNickname; // 상대방 닉네임
	private String otherUserProfilePic; // 상대방 프사
	
	private String firstPostPic; // 썸네일로 보일 사진
	
	private List<UserVO> users; // CHAT_ROOM_USER 테이블에서 CHAT_ROOM_ID 로 검색후 USER_ID로 USERS 테이블에서 검색
	
	private Long lastCount; // 채팅 리스트를 요청할 때 어디까지 불러왔는지 전해주기 위해 필요 (페이징과 비슷) (null 들어갈 수 있도록 Long으로 설정)
}
