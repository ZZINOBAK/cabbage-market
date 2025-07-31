package com.cabbage.biz.qa.faq;

import java.util.List;


public interface FaqService {
	//CRUD 기능 구현 메소드 정의
	void insertFaq(FaqVO vo);
	void updateFaq(FaqVO vo);
	void deleteFaq(FaqVO vo);
	FaqVO getFaq(FaqVO vo);
		
	List<FaqVO> getFaqList(FaqVO vo);
	
	// FaqVO로 리스트 가져오기
	List<FaqVO> getFaqList();
	List<FaqVO> getFaqKeywordList(String searchCondition, String searchKeyword);
}
