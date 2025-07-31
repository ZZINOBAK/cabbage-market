package com.cabbage.biz.chat.user;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UserVO {
	private String userId;
	private String userName;
	private String userPassword;
	private String userNickname;
	private String userPhone;
	private double userOndo;
	private String userStatus;
	private Date userRegdate;
	
	private String profilePic; // 프사 파일명
}
