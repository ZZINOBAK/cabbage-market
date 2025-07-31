package com.cabbage.biz.post.post.impl;

import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.cabbage.biz.post.post.PostVO;


@Repository
public class PostDAO {
	
	@Autowired
	private SqlSessionTemplate mybatis;
	
	// 결제관련
	public String getOnePic(PostVO vo) {
		return mybatis.selectOne("postDAO.getOnePic", vo);
	}
	
	/* 게시글 등록 기능 관련 */		//------------------------------------------------------
	//게시글 등록
	public void insertPost(PostVO vo) {
		mybatis.insert("postDAO.insertPost", vo);
	}
	
	//Files 테이블에 이미지 정보 등록
	public void insertFiles(PostVO vo) {
		mybatis.insert("postDAO.insertFiles", vo);
	}
	//현재 가장 최근 게시글 번호 가져오기
	public int getMaxPostId() {
		return mybatis.selectOne("postDAO.getMaxPostId");
	}
	//PostPic 데이터
	public List<PostVO> getFileId_PostId(Long postId){
		return mybatis.selectList("postDAO.getFileId_PostId", postId);
	}
	public void insertPostPic(PostVO vo) {
		mybatis.insert("postDAO.insertPostPic", vo);
	}
	
	//전체 Post 조회
	public List<PostVO> getPostList(PostVO vo){
		return mybatis.selectList("postDAO.getPostList", vo);
	}
	
	/* 게시글 수정/삭제 관련 */	//------------------------------------------------------
	//수정 페이지에 정보전달
	public PostVO getUpdatePage(PostVO vo) {
		return mybatis.selectOne("postDAO.getUpdatePage", vo);
	}
	//업데이트
	public void updatePost(PostVO vo) {
		mybatis.update("postDAO.updatePost", vo);
	}
	//파일 데이터 삭제
	public void deleteFiles(String fileId) {
		mybatis.delete("postDAO.deleteFiles", fileId);
	}
	//게시글 삭제(논리적)
	public void deletePost(PostVO vo) {
		mybatis.update("postDAO.deletePost", vo);
	}
	//예약중 처리
	public void reservePost(PostVO vo) {
		mybatis.update("postDAO.reservePost", vo);
	}
	//거래완료 처리
	public void finishPost(PostVO vo) {
		mybatis.update("postDAO.finishPost", vo);
	}
	public void enablePost(PostVO vo) {
		mybatis.update("postDAO.enablePost", vo);
	}
	
	/* 게시글 상세 기능*/		//------------------------------------------------------
	//Post 상세보기
	public PostVO getPost(PostVO vo) {
		return mybatis.selectOne("postDAO.getPost", vo);
	}
	//상세보기에 가져올 이미지 file
	public List<PostVO> getPostPic(PostVO vo){
		return mybatis.selectList("postDAO.getPostPic", vo);
	}
	//상세보기에 표시할 USER 데이터 가져오기
	public PostVO getUser(PostVO vo) {
		return mybatis.selectOne("postDAO.getUser", vo);
	}
	//상세보기에 표시할 다른 상품 리스트와 대표사진 가져오기
	public List<PostVO> getAnotherPost(PostVO vo){
		return mybatis.selectList("postDAO.getAnotherPost", vo);
	}
	//상세보기시 조회수 업데이트
	public void plusPostViews(PostVO vo) {
		mybatis.update("postDAO.plusPostViews", vo);
	}
	
	
	/* 찜 기능 */				//------------------------------------------------------
	//찜하기
	public void addWishList(PostVO vo) {
		mybatis.insert("postDAO.addWishList", vo);
	}
	//찜취소
	public void deleteWishList(PostVO vo) {
		mybatis.delete("postDAO.deleteWishList", vo);
	}
	//찜목록 조회
	public PostVO checkWishList(PostVO vo) {
		return mybatis.selectOne("postDAO.checkWishList", vo);
	}
	//게시글에 대한 찜 수 조회
	public int countWish(PostVO vo) {
		return mybatis.selectOne("postDAO.countWish", vo);
	}
	
	/* 결제 관련 기능 */
	//결제 누른 게시글 정보
	public PostVO getPayPost(PostVO vo) {
		return mybatis.selectOne("postDAO.getPayPost", vo);
	}
	
	// 해당 게시글에서 채팅한 회원의 아이디, 닉네임
	public List<PostVO> getChatUser(PostVO vo) {
		return mybatis.selectList("postDAO.getChatUser", vo);
	}
	
	// 거래완료시 구매자 업데이트
	public int insertBuyer(Map map) {
		return mybatis.update("postDAO.insertBuyer", map);
	}
}
