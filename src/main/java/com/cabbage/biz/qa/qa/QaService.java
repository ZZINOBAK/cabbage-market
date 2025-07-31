package com.cabbage.biz.qa.qa;

import java.util.List;

public interface QaService {
	//CRUD 기능 구현 메소드 정의
	void insertQa(QaVO vo);
	void updateQa(QaVO vo);
	void deleteQa(QaVO vo);
	QaVO getQa(QaVO vo);
	
	List<QaVO> getQaList(QaVO vo);
	
	List<QaVO> getQaList(String userId);
	
	List<QaVO> getQaKeywordList(String searchCondition, String searchKeyword);
	
	List<QaVO> getQaCategoryList();
	
	String getQaCatContentByCategoryId(String categoryId);
	
	QaVO getQaFormDetail(QaVO vo);
	
	void deleteQaFormDetail(QaVO vo);

	
	// Admin 관리자 부분 ------------------------------------
	List<QaVO> getAdminQaList();
	
	void deleteAdminComment(QaVO vo);
	
	QaVO getAdminQaFormDetail(QaVO vo);
	
	void insertAdminComment(QaVO vo);
	
	
	
}

