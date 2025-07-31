package com.cabbage.biz.main.main.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cabbage.biz.main.main.MainVO;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@Repository
public class MainDAO {
	
	private final SqlSessionTemplate mybatis;
	
	//글입력
    @Transactional
	public void insertPost(MainVO vo) {
		mybatis.insert("mainDAO.insertPost", vo);
		
	}

	//글수정
	public void updatePost(MainVO vo) {
		mybatis.update("mainDAO.updatePost", vo);
	}
	
	//최신포스트 출력하기 20개
	public List<MainVO> getPostListForNew() {
		return mybatis.selectList("mainDAO.getPostListForNew");
	}
	
	//추천포스트 뷰카운트로 출력하기 20개
	public List<MainVO> getPostListForRcByVc() {
		return mybatis.selectList("mainDAO.getPostListForRcByVc");
	}
		
		
	//추천포스트 아이디로 출력하기 20개
	public List<MainVO> getPostListForRcById(String id) {
		return mybatis.selectList("mainDAO.getPostListForRcById", id);
	}
	
	//최신포스트 출력하기 전체
	public List<MainVO> getPostListForNewAll(int begin, int end) {
		Map<String, Integer> map = new HashMap<>();
		map.put("begin", begin);
		map.put("end", end);
		return mybatis.selectList("mainDAO.getPostListForNewAll", map);
	}
	
	public int countNewAll() {
		return mybatis.selectOne("mainDAO.countNewAll");
	}
	
	//추천포스트 뷰카운트로 출력하기 전체
	public List<MainVO> getPostListForRcByVcAll(int begin, int end) {
		Map<String, Integer> map = new HashMap<>();
		map.put("begin", begin);
		map.put("end", end);
		
		return mybatis.selectList("mainDAO.getPostListForRcByVcAll", map); 
	}
		
	public int countVcAll() {
		return mybatis.selectOne("mainDAO.countVcAll");
	}
		
	//추천포스트 아이디로 출력하기 전체
	public List<MainVO> getPostListForRcByIdAll(String id, int begin, int end) {
		Map<String, Object> map = new HashMap<>();
		map.put("id", id);
		map.put("begin", begin);
		map.put("end", end);
		return mybatis.selectList("mainDAO.getPostListForRcByIdAll", map);
	}
	
	public int countIdAll(String id) {
		return mybatis.selectOne("mainDAO.countIdAll", id);
	}
	
	public List<MainVO> getTop100Post() {
		return mybatis.selectList("mainDAO.getTop100Post");
	}
		
}
