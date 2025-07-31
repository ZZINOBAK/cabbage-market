package com.cabbage.biz.main.main;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class MainVO {
	private Long postId;
	private String sellerId;
	private Long postCatId;
	private String postTitle;
	private int postPrice;
	private String postContent;
    private String postStatus;
    private Date postRegdate;
    private Long postViews;
    private String buyerId;
    
    private String userNickname;
    
    private Long fileId;
    private String fileName;
    
    private int postWishCount;
    private int chatRoomCount;
    private double totalCounts;
    
	
}
