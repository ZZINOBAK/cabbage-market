package com.cabbage.biz.chat.post.impl;

import org.springframework.stereotype.Service;

import com.cabbage.biz.chat.chat.impl.ChatDAO;
import com.cabbage.biz.chat.post.PostService;
import com.cabbage.biz.chat.post.PostVO;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PostServiceImpl implements PostService{
	
	private final ChatDAO chatDAO;

	@Override
	public PostVO getPostById(long postId) {
		return chatDAO.selectPostById(PostVO.builder()
									.postId(postId)
									.build());
	}

	
	
}
