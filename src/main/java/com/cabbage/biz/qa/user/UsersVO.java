package com.cabbage.biz.qa.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UsersVO {
	private String userId;
	private String  userName;
	private String userPassword;
	private String userNickname;
	private String userPhone; 
	private float userOndo; 
	private String userStatus;
	private String userRegdate;

}
