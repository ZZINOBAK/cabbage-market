package com.cabbage.biz.userInfo.user.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cabbage.biz.userInfo.user.UserService;
import com.cabbage.biz.userInfo.user.UserVO;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@Service("userService")
public class UserServiceImpl implements UserService {

	private final UserDAO userDAO;

	@Override
	public UserVO getUser(UserVO vo) {
		return userDAO.getUser(vo);
	}
	@Override
	public UserVO getUserByIdAndEmail(String userId, String userEmail) {
		return userDAO.getUserByIdAndEmail(UserVO.builder().userId(userId).userEmail(userEmail).build());
	}
	
	@Override
	public void joinUser(UserVO vo) {
		userDAO.joinUser(vo);
	}

	@Override
	public boolean selectId(String userId) {
		return userDAO.selectId(userId);
	}
	
	@Override
	public boolean selectNick(String userNickname) {
		return userDAO.selectNick(userNickname);
	}
	
	@Override
	public boolean selectPhone(String userPhone) {
		return userDAO.selectPhone(userPhone);
	}
	
	@Override
	public UserVO findId(UserVO vo) {
		return userDAO.findId(vo);
	}
	
	@Override
	public UserVO findPw(UserVO vo) {
		return userDAO.findPw(vo);
	}
	
	@Override
	public List<UserVO> purchaseList(UserVO vo) {
		return userDAO.purchaseList(vo);
	}
	
	@Override
	public List<UserVO> salesList(UserVO vo) {
		return userDAO.salesList(vo);
	}
	
	@Override
	public List<UserVO> wishList(UserVO vo) {
		return userDAO.wishList(vo);
	}
	
	@Override
	public UserVO userInfo(UserVO vo) {
		return userDAO.userInfo(vo);
	}
	
	
	@Override
	public void profileUpload(UserVO vo) {
		userDAO.profileUpload(vo);
	}
	
	
	@Override
	public void userUpdate(UserVO vo) {
		userDAO.userUpdate(vo);
	}
	
	
	@Override
	public void wishKeyword(UserVO vo) {
		userDAO.wishKeyword(vo);
	}
	
	@Override
	public List<UserVO> keywordList(UserVO vo) {
		return userDAO.keywordList(vo);
	}
	
	@Override
	public void deleteWish(UserVO vo) {
		userDAO.deleteWish(vo);
	}
	
	@Override
	public void deleteWishKeyword(UserVO vo) {
		userDAO.deleteWishKeyword(vo);
	}
	@Override
	public void deleteUser(UserVO vo) {
		userDAO.deleteUser(vo);
	}
	
	@Override
	public void reviewInput(UserVO vo) {
		userDAO.reviewInput(vo);
	}
	
	@Override
	public void ondoUpDown(UserVO vo) {
		userDAO.ondoUpDown(vo);
	}
	
}
