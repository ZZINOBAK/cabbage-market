package com.cabbage.biz.qa.qa.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cabbage.biz.qa.qa.QaService;
import com.cabbage.biz.qa.qa.QaVO;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service("qaService")
public class QaServiceImpl implements QaService {
	

	private final QaDAO QaDAO;

	@Override
	public void insertQa(QaVO vo) {
		QaDAO.insertQa(vo);
	}

	@Override
	public void updateQa(QaVO vo) {
		QaDAO.updateQa(vo);
	}

	@Override
	public void deleteQa(QaVO vo) {
		QaDAO.deleteQa(vo);
	}

	@Override
	public QaVO getQa(QaVO vo) {
		return QaDAO.getQa(vo);
	}
	
	@Override
	public List<QaVO> getQaList(QaVO vo) {
		return QaDAO.getQaList(vo);
	}
	
	
	@Override
	public List<QaVO> getQaList(String userId) {
		return QaDAO.getQaList(userId);
	}
	
	@Override
    public List<QaVO> getQaCategoryList() {
        return QaDAO.getQaCategoryList();
    }

	
	// QaForm.jsp에서 QA_CAT_CONTENT 출력!
	@Override
    public String getQaCatContentByCategoryId(String categoryId) {
        return QaDAO.getQaCatContentByCategoryId(categoryId);
    }
	
	// 1:1 문의내역 내용 조회
	@Override
	public QaVO getQaFormDetail(QaVO vo) {
		return QaDAO.getQaFormDetail(vo);
	}
	
	
	@Override
	public List<QaVO> getQaKeywordList(String searchCondition, String searchKeyword) {
		 return QaDAO.getQaKeywordList(searchCondition, searchKeyword);
	}
	
	
	// 1:1 문의내역 삭제 DELETE
	@Override
	public void deleteQaFormDetail(QaVO vo) {
		QaDAO.deleteQaFormDetail(vo);
	}

	
	
	// Admim 관리자모드 ----------------------------------------------------
	// Admin 관리자모드에서 사용자 문의 리스트
	@Override
	public List<QaVO> getAdminQaList() {
		return QaDAO.getAdminQaList();
	}
	
	// Admin 관리자모드에서 문의내역 댓글 삭제
	@Override
	public void deleteAdminComment(QaVO vo) {
		QaDAO.deleteAdminComment(vo);
		
	}

	@Override
	public QaVO getAdminQaFormDetail(QaVO vo) {
		return QaDAO.getAdminQaFormDetail(vo);
	}

	@Override
	public void insertAdminComment(QaVO vo) {
		QaDAO.insertAdminComment(vo);
	}

	

	

	
}
