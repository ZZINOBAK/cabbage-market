package com.cabbage.biz.post.post.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cabbage.biz.post.post.PostService;
import com.cabbage.biz.post.post.PostVO;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service("postService")
public class PostServiceImpl implements PostService {

	@Autowired
	private final PostDAO postDAO;

	// post 등록
	@Override
	public void insertPost(PostVO vo) {
		postDAO.insertPost(vo);
	}

	// 최근 postId 가져오기
	@Override
	public int getMaxPostId() {
		return postDAO.getMaxPostId();
	}

	// files 등록
	@Override
	public void insertFiles(PostVO vo) {
		postDAO.insertFiles(vo);
	}

	// post상세보기
	@Override
	public PostVO getPost(PostVO vo) {
		return postDAO.getPost(vo);
	}

	// post 상세에 있는 이미지 가져오기
	public List<PostVO> getPostPic(PostVO vo) {
		return postDAO.getPostPic(vo);
	}

	// post 전체 리스트
	@Override
	public List<PostVO> getPostList(PostVO vo) {
		return postDAO.getPostList(vo);
	}

	// 상세보기에 표시할 USER데이터 가져오기
	@Override
	public PostVO getUser(PostVO vo) {
		return postDAO.getUser(vo);
	}

	// 판매자의 다른 상품 리스트와 대표사진 가져오기
	@Override
	public List<PostVO> getAnotherPost(PostVO vo) {
		return postDAO.getAnotherPost(vo);
	}

	// PostPic 데이터
	@Override
	public List<PostVO> getFileId_PostId(Long postId) {
		return postDAO.getFileId_PostId(postId);
	}

	// PostPic 테이블 insert
	@Override
	public void insertPostPic(PostVO vo) {
		postDAO.insertPostPic(vo);
	}

	// 조회수 증가
	@Override
	public void plusPostViews(PostVO vo) {
		postDAO.plusPostViews(vo);
	}

	// 찜
	@Override
	public void addWishList(PostVO vo) {
		postDAO.addWishList(vo);
	}

	// 찜 취소
	@Override
	public void deleteWishList(PostVO vo) {
		postDAO.deleteWishList(vo);
	}

	// 찜 목록 조회
	@Override
	public PostVO checkWishList(PostVO vo) {
		return postDAO.checkWishList(vo);
	}

	// 게시글 찜 수 (관심 수)
	@Override
	public int countWish(PostVO vo) {
		return postDAO.countWish(vo);
	}

	// 간단 결제 정보
	@Override
	public PostVO getPayPost(PostVO vo) {
		return postDAO.getPayPost(vo);
	}

	// 업데이트 페이지 정보
	@Override
	public PostVO getUpdatePage(PostVO vo) {
		return postDAO.getUpdatePage(vo);
	}

	// 게시글 수정
	@Override
	public void updatePost(PostVO vo) {
		postDAO.updatePost(vo);
	}

	// 사진파일 삭제
	@Override
	public void deleteFiles(String fileId) {
		postDAO.deleteFiles(fileId);
	}

	// 논리적 삭제
	@Override
	public void deletePost(PostVO vo) {
		postDAO.deletePost(vo);
	}

	// 예약중 처리
	@Override
	public void reservePost(PostVO vo) {
		postDAO.reservePost(vo);
	}

	// 거래완료 처리
	@Override
	public void finishPost(PostVO vo) {
		postDAO.finishPost(vo);
	}

	// 거래중 처리
	@Override
	public void enablePost(PostVO vo) {
		postDAO.enablePost(vo);
	}

	// 결제 창에 사진 한 장 가져오기
	@Override
	public String getOnePic(PostVO vo) {
		return postDAO.getOnePic(vo);
	}
	
	// 게시글에서 채팅한 유저 아이디, 닉네임
	@Override
	public List<PostVO> getChatUser(PostVO vo) {
		return postDAO.getChatUser(vo);
	}

	// 구매자 넣기
	@Override
	public int insertBuyer(Map map) {
		return postDAO.insertBuyer(map);
	}
}
