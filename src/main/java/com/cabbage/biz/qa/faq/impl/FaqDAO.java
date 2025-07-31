package com.cabbage.biz.qa.faq.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.cabbage.biz.qa.faq.FaqVO;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@Repository
public class FaqDAO {

	private final SqlSessionTemplate mybatis;
	
	//게시글 1개 조회
	public FaqVO getFaq(FaqVO vo) {
		return mybatis.selectOne("FaqDAO.getFaq", vo);
	}

	//전체 게시글 조회
	public List<FaqVO> getFaqList() {
		return mybatis.selectList("FaqDAO.getFaqList");
	}
	
	
	//글입력
	public void insertFaq(FaqVO vo) {
		mybatis.insert("FaqDAO.insertFaq", vo);
	}

	//글수정
	public void updateFaq(FaqVO vo) {
		mybatis.update("FaqDAO.updateFaq", vo);
	}

	//글삭제
	public void deleteFaq(FaqVO vo) {
		mybatis.delete("FaqDAO.deleteFaq", vo);
	}
	
	// faqKeywordList
	public List<FaqVO> getFaqKeywordList(String searchCondition, String searchKeyword) {
		    
	    Map<String, Object> parameter = new HashMap<>();
		    
	    parameter.put("searchCondition", searchCondition);
	    parameter.put("searchKeyword", searchKeyword);
		    
	    return mybatis.selectList("QaDAO.getFaqKeywordList_T", parameter);
	}

			
	
}
