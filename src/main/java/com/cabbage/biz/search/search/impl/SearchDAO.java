package com.cabbage.biz.search.search.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.cabbage.biz.search.post.PostVO;
import com.cabbage.biz.search.search.SearchVO;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class SearchDAO {
    private final SqlSessionTemplate mybatis;

    //키워드 입력
    public void insertKeyword(SearchVO vo) {
    	
    	mybatis.update("Search.keywordMerge", vo);

//        // 데이터베이스에서 같은 아이디와 서치 키워드를 조회합니다.
//        SearchVO existingKeyword = mybatis.selectOne("Search.selectKeyword", vo);
//
//        // 조회된 결과가 없을 경우에만 vo를 입력합니다.
//        if (existingKeyword == null) {
//            mybatis.insert("Search.insertKeyword", vo);
//        }else if (existingKeyword != null) {
//            mybatis.update("Search.updateSearchDate", vo);
//        }
    }

    //키워드 삭제
    public void deleteKeyword(SearchVO vo) {
        mybatis.delete("Search.deleteKeyword", vo);
    }

    public List<PostVO> selectListPost(String keyword, int begin, int end) {
        Map map = new HashMap();
        map.put("keyword", keyword);
        map.put("begin", begin);
        map.put("end", end);
        return mybatis.selectList("Search.searchPost", map);
    }

//    public List<Map<String, Object>> autocomplete(Map<String, Object> paramMap) {
//        return mybatis.selectList("Search.autocomplete",paramMap);
//    }
    public List<String> getAutocompleteResults(String query) {
        return mybatis.selectList("Search.autocomplete", query);
    }

    public List<String> getRecentSearchLog(SearchVO vo) {
        return mybatis.selectList("Search.recentSearchLog", vo);
    }

    public List<String> getTopSearched() {
        return mybatis.selectList("Search.relatedSearch");
    }
    public List<PostVO> findByCategoryPost(String category, int begin, int end) {
        Map map = new HashMap();
        map.put("category", category);
        map.put("begin", begin);
        map.put("end", end);
        return mybatis.selectList("Search.categoryPost", map);
    }

    public int countVcAll() {
        return mybatis.selectOne("Search.countVcAll");
    }

    public List<PostVO> getPostListForRcByVcAll(int begin, int end) {
        Map<String, Integer> map = new HashMap<>();
        map.put("begin", begin);
        map.put("end", end);

        return mybatis.selectList("Search.getPostListForRcByVcAll", map);
    }

    public int countCategoryPostList(String category) {
        return mybatis.selectOne("Search.countCategoryPostList", category);
    }

    public int countKeywordPostList(String keyword) {
        return mybatis.selectOne("Search.countKeywordPostList", keyword);
    }
    public void deleteAllKeyword(String userId) {
        mybatis.update("Search.deleteAllKeyword",userId);
    }
}
