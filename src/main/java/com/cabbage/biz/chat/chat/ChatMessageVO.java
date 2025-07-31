package com.cabbage.biz.chat.chat;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ChatMessageVO {
	private long messageId;
	private long chatRoomId;
	private String senderId;
	private String messageContent;
	@JsonFormat(pattern = "M/d/yyyy HH:mm:ss zzz", timezone = "UTC") // UTC 시간으로 보내고 자바스크립트에서 로컬 시간으로 바꿈
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") // 저장할 때 이렇게 받아서 저장
	private LocalDateTime messageTime;
	private String messageType; // CHAT, PIC, EMOJI, DEL, PAY
	
	private int partnerUnreadCount;
	
	private long countByRoom; // 각 채팅방마다 몇번째 채팅인지 (페이징을 위해 필요)
	
	private String picFileName; // messageType 이 PIC 인 경우 파일이름을 여기에 저장
	private String emojiFileName; // messageType 이 EMOJI 인 경우 파일이름을 여기에 저장
	private MultipartFile uploadPic; // 채팅에서 사진 전송하면 여기에 담김
	
	private long emojiId;
	
	// ======= 결제 ========
	private String requestUserId;
	private long postId;
	private String finalPriceInput;
	private long finalPrice;
	private String payStatus; // WAITING, FINISH, CANCEL;
}
