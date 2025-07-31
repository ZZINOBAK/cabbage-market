package com.cabbage.biz.qa.qa.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.cabbage.biz.qa.qa.QaVO;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@Repository
public class QaDAO {

	private final SqlSessionTemplate mybatis;
		
	
	// QA 1:1 문의 INSERT
	public void insertQa(QaVO vo) {
		mybatis.insert("QaDAO.insertQa", vo);
	}

	//글수정
	public void updateQa(QaVO vo) {
		mybatis.update("QaDAO.updateQa", vo);
	}

	//글삭제
	public void deleteQa(QaVO vo) {
		mybatis.delete("QaDAO.deleteQa", vo);
	}

	//게시글 1개 조회
	public QaVO getQa(QaVO vo) {
		return mybatis.selectOne("QaDAO.getQa", vo);
	}
	
	
	// 전체 QaVO 조회 (QaVO)
	public List<QaVO> getQaList(QaVO vo) {
		return mybatis.selectList("QaDAO.getQaList", vo);
	}
	
	
	// 전체 QA 조회
	public List<QaVO> getQaList(String userId) {
		return mybatis.selectList("QaDAO.getQaList", userId);
	}
	
	// 검색 키워드 조회
	public List<QaVO> getQaListWithDynamicQuery(String dynamicQuery) {
		Map<String, Object> parameter = new HashMap<>();
	    parameter.put("dynamicQuery", dynamicQuery);
	    return mybatis.selectList("QaDAO.getQaListWithDynamicQuery", parameter);
	}

	// 전체 QA 조회 (키워드) 검색 키워드 조회
	public List<QaVO> getQaKeywordList(String searchCondition, String searchKeyword) {
	    
	    Map<String, Object> parameter = new HashMap<>();
	    
	    parameter.put("searchCondition", searchCondition);
	    parameter.put("searchKeyword", searchKeyword);
	    
	    return mybatis.selectList("QaDAO.getQaKeywordList_T", parameter);
	}
	
	// 1:1 문의내역 QA_CONTENT 조회
	public QaVO getQaFormDetail(QaVO vo) {
		return mybatis.selectOne("QaDAO.getQaFormDetail", vo);
	}
	
	// 1:1 문의내역 삭제 DELETE
	public void deleteQaFormDetail(QaVO vo) {
		mybatis.delete("QaDAO.deleteQaFormDetail", vo);
	}
	
	
	// QA CATEGORY --------------------------------------------------------------------
	// QaCatCategory 카테고리 출력
	public List<QaVO> getQaCategoryList() {
        return mybatis.selectList("QaDAO.getQaCategoryList");
    }
	
	
	// 카테고리별로 해당하는 내용 가져오기
	public String getQaCatContentByCategoryId(String categoryId) {
		return mybatis.selectOne("QaDAO.getQaCatContentByCategoryId", categoryId);
	}
	
	
	
	// Admim 관리자모드 --------------------------------------------------------------------
	
	// Admin 관리자모드에서 사용자 문의내역 LIST 조회
	public List<QaVO> getAdminQaList() {
		return mybatis.selectList("QaDAO.getAdminQaList");
	}
	// Admin 관리자모드에서 문의내역 댓글 삭제 DELETE
	public void deleteAdminComment(QaVO vo) {
		mybatis.delete("QaDAO.deleteAdminComment", vo);
	}
	
	// Admin 관리자모드에서 문의내역 CONTENT 조회 
	public QaVO getAdminQaFormDetail(QaVO vo) {
		return mybatis.selectOne("QaDAO.getAdminQaFormDetail", vo);
	}
	
	// Admin 관리자모드에서 댓글 입력 INSERT
	public void insertAdminComment(QaVO vo) {
		mybatis.insert("QaDAO.insertAdminComment", vo);
	}
	

	
}