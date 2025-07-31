package com.cabbage.biz.post.post;

import java.util.List;
import java.util.Map;

public interface PostService {
	//게시글 등록 관련
	void insertPost(PostVO vo);		//post 테이블 insert
	void insertFiles(PostVO vo);	//files 테이블 insert
	int getMaxPostId();				//최근 postId 찾기
	List<PostVO> getFileId_PostId(Long postId);	//PostPic 데이터
	void insertPostPic(PostVO vo);	//postPic 테이블 insert
	
	//게시글 수정/삭제 관련
	PostVO getUpdatePage(PostVO vo);	//update페이지 정보
	void updatePost(PostVO vo); 		//게시글 수정 (사진 파일 제외)
	void deleteFiles(String fileId);	//사진파일 삭제
	void deletePost(PostVO vo);			//게시글 삭제
	void reservePost(PostVO vo);		//예약중 처리
	void finishPost(PostVO vo);			//판매완료 처리
	void enablePost(PostVO vo);			//거래중 처리
	
	//게시글 상세 관련
	PostVO getPost(PostVO vo);		//post상세
	List<PostVO> getPostPic(PostVO vo); //post상세를 위한 postPic 가져오기 
	List<PostVO> getPostList(PostVO vo); //post리스트
	PostVO getUser(PostVO vo);		//상세보기에 필요한 USER 데이터 가져오기
	List<PostVO> getAnotherPost(PostVO vo); //판매자의 다른 상품 리스트 가져오기
	void plusPostViews(PostVO vo);	//조회수 증가
	
	//찜
	void addWishList(PostVO vo);	//찜하기
	void deleteWishList(PostVO vo); //찜취소
	PostVO checkWishList(PostVO vo);//찜 게시글인지 체크
	int countWish(PostVO vo);		//게시글 관심 수 (찜 수)
	
	//결제 관련
	PostVO getPayPost(PostVO vo);
	String getOnePic(PostVO vo);
	
	List<PostVO> getChatUser(PostVO vo);	//해당 게시글에서 채팅한 사람 아이디, 닉네임 가져오기

	int insertBuyer(Map map);	//구매자 넣기
}
