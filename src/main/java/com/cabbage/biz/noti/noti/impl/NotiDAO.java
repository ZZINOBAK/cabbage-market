package com.cabbage.biz.noti.noti.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.cabbage.biz.noti.noti.NotiVO;
import com.cabbage.biz.post.post.PostVO;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository("notiDAO")
public class NotiDAO {

	private final SqlSessionTemplate mybatis;
	
	//글입력
	public void insertNoti(NotiVO vo) {
		mybatis.insert("notiDAO.insertNoti", vo);
	}

	//알림 테이블 unread -> read
	public void updateNoti(String id) {
		mybatis.update("notiDAO.updateNoti", id);
	}

	//글삭제
	public void deleteNoti(NotiVO vo) {
		mybatis.delete("notiDAO.deleteNoti", vo.getNotiId());
	}

	//게시글 1개 조회
	public NotiVO getNoti(NotiVO vo) {
		return mybatis.selectOne("notiDAO.getBoard", vo);
	}

	//전체 게시글 조회
	public List<NotiVO> getNotiList() {
		return null;
	}
	
	public List<Map<String, String>> checkWishKeyWord(PostVO postVo) {
		Map<String, String> map = new HashMap<>();
		map.put("postTitle", postVo.getPostTitle());
		map.put("postContent", postVo.getPostContent());
		return mybatis.selectList("notiDAO.checkWishKeyWord", map);
	}
	
	public List<String> checkPostWishList(PostVO postVo) {
		return mybatis.selectList("notiDAO.checkPostWishList", postVo.getPostId().intValue());
	}
	
	public int getNotiCountById(String id) {
		return mybatis.selectOne("notiDAO.getNotiCountById", id);
	}
	
	public List<NotiVO> getNotiListById(String id) {
		return mybatis.selectList("notiDAO.getNotiListById", id);
	}

	public String getPostPicForNoti(PostVO postVo) {
		return mybatis.selectOne("notiDAO.getPostPicForNoti", postVo.getPostId().intValue());
	}

	public String getPostTitleForNoti(int postId) {
		return mybatis.selectOne("notiDAO.getPostTitleForNoti", postId);
	}
		
}
