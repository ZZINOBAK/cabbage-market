package com.cabbage.biz.qa.faq.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cabbage.biz.qa.faq.FaqService;
import com.cabbage.biz.qa.faq.FaqVO;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service("faqService")
public class FaqServiceImpl implements FaqService {
	
	private final FaqDAO FaqDAO;
	
	
	@Override
	public void insertFaq(FaqVO vo) {
		FaqDAO.insertFaq(vo);
	}

	@Override
	public void updateFaq(FaqVO vo) {
		FaqDAO.updateFaq(vo);
	}

	@Override
	public void deleteFaq(FaqVO vo) {
		FaqDAO.deleteFaq(vo);
	}

	@Override
	public FaqVO getFaq(FaqVO vo) {
		return FaqDAO.getFaq(vo);
	}

	@Override
	public List<FaqVO> getFaqList(FaqVO vo) {
		return null;
	}

	// FaqList
	@Override
	public List<FaqVO> getFaqList() {
		return FaqDAO.getFaqList();
	}
	
	// FaqKeywordList
	@Override
	public List<FaqVO> getFaqKeywordList(String searchCondition, String searchKeyword) {
		return FaqDAO.getFaqKeywordList(searchCondition, searchKeyword);
	}
}
