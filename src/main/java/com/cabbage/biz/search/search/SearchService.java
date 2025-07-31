package com.cabbage.biz.search.search;

import java.util.List;

import com.cabbage.biz.search.post.PostVO;

public interface SearchService {
    void insertKeyword(SearchVO vo);
    void deleteKeyword(SearchVO vo);

    List<PostVO> selectListPost(String keyword, int begin, int end);

    List<String> getAutocompleteResults(String query);

    List<String> recentSearchLog(SearchVO vo);

    List<String> TopSearched();

    List<PostVO> findByCategoryPost(String category, int begin, int end);

    int countVcAll();

    List<PostVO> getPostListForRcByVcAll(int begin, int end);

    int countCategoryPostList(String category);

    int countKeywordPostList(String keyword);
    
    void deleteAllKeyword(String userId);
}
