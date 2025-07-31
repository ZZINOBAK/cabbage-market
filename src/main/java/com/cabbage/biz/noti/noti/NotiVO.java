package com.cabbage.biz.noti.noti;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class NotiVO {
	private Long notiId;
	private String userId;
	private Long postId;
	private Long qaId;
	private String notiType;
	private String notiContent;
	private String notiUrl;
	private Date notiDate;
	private String notiStatus;

	private int count;
	private String wishKeyword;
	private String fileName;
	
		
}
