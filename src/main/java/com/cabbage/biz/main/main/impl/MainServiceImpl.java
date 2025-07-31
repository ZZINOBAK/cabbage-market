package com.cabbage.biz.main.main.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cabbage.biz.main.main.MainService;
import com.cabbage.biz.main.main.MainVO;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service("mainService")
public class MainServiceImpl implements MainService {
	@Autowired //타입이 일치하는 객체(인스턴스) 주입
	private final MainDAO postDAO;
	
	@Override
	public List<MainVO> getPostListForNew() {
		return postDAO.getPostListForNew();	
	}


	@Override
	public List<MainVO> getPostListForRcByVc() {
		return postDAO.getPostListForRcByVc();	
	}


	@Override
	public List<MainVO> getPostListForRcById(String id) {
		return postDAO.getPostListForRcById(id);	
	}

	@Override
	public List<MainVO> getPostListForNewAll(int begin, int end) {
		return postDAO.getPostListForNewAll(begin, end);	
	}

	@Override
	public List<MainVO> getPostListForRcByVcAll(int begin, int end) {
		return postDAO.getPostListForRcByVcAll(begin, end);	
	}
	
	@Override
	public int countNewAll() {
		return postDAO.countNewAll();	
	}

	@Override
	public int countVcAll() {
		return postDAO.countVcAll();	
	}
	
	@Override
	public int countIdAll(String id) {
		return postDAO.countIdAll(id);	
	}
	@Override
	public List<MainVO> getPostListForRcByIdAll(String id, int begin, int end) {
		return postDAO.getPostListForRcByIdAll(id, begin, end);	
	}

	@Override
	public void insertPost(MainVO vo) {
		postDAO.insertPost(vo);		
	}

	@Override
	public void updatePost(MainVO vo) {
		postDAO.updatePost(vo);
		
	}


	@Override
	public List<MainVO> getTop100Post() {
		return postDAO.getTop100Post();
	}
	
}
