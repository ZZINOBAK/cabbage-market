package com.cabbage.biz.main.main;

import java.util.List;

public interface MainService {
	//CRUD 기능 구현 메소드 정의
	void insertPost(MainVO vo);
	void updatePost(MainVO vo);
	
	//메인페이지용 메소드 정의
	List<MainVO> getPostListForNew();
	List<MainVO> getPostListForRcByVc();
	List<MainVO> getPostListForRcById(String id);
	
	List<MainVO> getPostListForNewAll(int begin, int end);
	List<MainVO> getPostListForRcByVcAll(int begin, int end);
	List<MainVO> getPostListForRcByIdAll(String id, int begin, int end);

	int countVcAll();
	int countNewAll();
	int countIdAll(String id);
	
	List<MainVO> getTop100Post();
}
