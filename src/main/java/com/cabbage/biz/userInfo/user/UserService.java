package com.cabbage.biz.userInfo.user;

import java.util.List;

public interface UserService {
	
	UserVO getUser(UserVO vo);
	UserVO getUserByIdAndEmail(String userId, String userEmail);

	void joinUser(UserVO vo);

	boolean selectId(String userId);
	
	boolean selectNick(String userNickname);
	
	boolean selectPhone(String userPhone);

	UserVO findId(UserVO vo);
	
	UserVO findPw(UserVO vo);

	List<UserVO> purchaseList(UserVO vo);
	
	List<UserVO> salesList(UserVO vo);
	
	List<UserVO> wishList(UserVO vo);
	
	UserVO userInfo(UserVO vo);

	void profileUpload(UserVO vo);
	
	void userUpdate(UserVO vo);
	
	void wishKeyword(UserVO vo);
	List<UserVO> keywordList(UserVO vo);
	void deleteWish(UserVO vo);
	void deleteWishKeyword(UserVO vo);
	void deleteUser(UserVO vo);
	void reviewInput(UserVO vo);
	
	void ondoUpDown(UserVO vo);
}
