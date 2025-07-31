package com.cabbage.biz.search.search.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cabbage.biz.search.post.PostVO;
import com.cabbage.biz.search.search.SearchService;
import com.cabbage.biz.search.search.SearchVO;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@Service("searchService")
public class SearchServiceImpl implements SearchService {
	
	private final SearchDAO dao;
	
	
	@Override
	public List<PostVO> selectListPost(String keyword, int begin, int end) {
		return dao.selectListPost(keyword, begin, end);
	}
	@Override
	public void insertKeyword(SearchVO vo){
		dao.insertKeyword(vo);
	}

	@Override
	public void deleteKeyword(SearchVO vo) {
		dao.deleteKeyword(vo);
	}


	@Override
	public List<String> getAutocompleteResults(String query) {
		return dao.getAutocompleteResults(query);
	}

	@Override
	public List<String> recentSearchLog(SearchVO vo) {
		return dao.getRecentSearchLog(vo);
	}

	@Override
	public List<String> TopSearched() {
		return dao.getTopSearched();
	}

	@Override
	public List<PostVO> findByCategoryPost(String category, int begin, int end) {
		return dao.findByCategoryPost(category, begin, end);
	}

	@Override
	public int countVcAll() {
		return dao.countVcAll();
	}

	@Override
	public List<PostVO> getPostListForRcByVcAll(int begin, int end) {
		return dao.getPostListForRcByVcAll(begin, end);
	}

	@Override
	public int countCategoryPostList(String category) {
		return dao.countCategoryPostList(category);
	}

	@Override
	public int countKeywordPostList(String keyword) {
		return dao.countKeywordPostList(keyword);
	}
	@Override
	public void deleteAllKeyword(String userId) {
		dao.deleteAllKeyword(userId);
	}
}
