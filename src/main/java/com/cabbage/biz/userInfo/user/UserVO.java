package com.cabbage.biz.userInfo.user;

import java.sql.Date;

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

public class UserVO {
	
	//USER 부분
	private String userId;
	private String userName;
	private String userPassword;
	private String userNickname;
	private String userPhone;
	private float userOndo;
	private String userStatus;
	@JsonFormat(pattern = "yyyy-M-d", timezone = "KST")
	private Date userRegdate;
	private String userEmail;
	private String userProfile;
	private String wishKeyword;
	
	//POST 부분
	private int postId;
	private String sellerId;
	private int postCatId;
	private String postTitle;
	private int postPrice;
	private String postContent;
	private String postStatus;
	@JsonFormat(pattern = "yyyy-M-d")
	private Date postRegdate;
	private int postViews;
	private String buyerId;
	private String review;
	
	// FILE부분
	private int fileId;
	private String fileName;
	private MultipartFile uploadFile;
	
	private long fileIdCount;
	
	float incrementValue;
}
