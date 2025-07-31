package com.cabbage.biz.post.post;

import java.sql.Date;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PostVO {
	
	private Long postId;
	private String sellerId;
	private Long postCatId;
	private String postTitle;
	private long postPrice;
	private String postContent;
	private String postStatus;
	private Date postRegdate;
	private int postViews;
	private String buyerId;
	
	//파일(Files) VO
	private int fileId;
	private String userId;
	private String fileName;
	private Date fileRegdate;
	private String[] fileIdArr;
	
	//다중 파일 업로드
	private List<MultipartFile> uploadFile;
	
	//카테고리
	private String postCatName;
	
	//USER VO
	private String userName;
	private String userPassword;
	private String userNickname;
	private String userPhone;
	private Float userOndo;
	private String userStatus;
	private Date userRegdate;
	
}
