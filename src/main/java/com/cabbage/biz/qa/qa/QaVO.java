package com.cabbage.biz.qa.qa;

import java.sql.Date;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder

public class QaVO {

	private long qaId;
	private String userId;
	private long qaCatId;
	private String qaTitle;
	private String qaContent;
	private Date qaRegdate;
	private String qaStatus;
	private String qaComment;
	
	// 추가 : QA_CAT_NAME, QA_CAT_CONTENT (QA_CATEGORY 테이블)
    private String qaCatName; 
    private String qaCatContent;
	
    //추가 : FILE_ID (QA_PIC 테이블)
    private long fileId;
    
    private String fileName;
    
	
	//검색조건 처리용
	//@JsonIgnore //JSON 데이터 변경 제외
	private String searchCondition;
	//@JsonIgnore
	private String searchKeyword;
		
	//파일업로드
	//@JsonIgnore
	private MultipartFile uploadFile;
	
	
	
}
