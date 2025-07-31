package com.cabbage.biz.chat.post;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class PostVO {
	private long postId;
	private String sellerId;
	private long post_catId;
	private String postTitle;
	private long postPrice;
	private String postContent;
	private String postStatus;
	private Date postRegdate;
	private long postViews;
	private String buyerId;
	
	private String postPic; // 채팅에서는 사진 하나만 있으면 됨
}
