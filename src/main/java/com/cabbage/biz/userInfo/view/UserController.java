package com.cabbage.biz.userInfo.view;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cabbage.biz.chat.chat.ChatRoomService;
import com.cabbage.biz.common.CommonData;
import com.cabbage.biz.noti.noti.NotiService;
import com.cabbage.biz.noti.noti.NotiVO;
import com.cabbage.biz.userInfo.user.UserService;
import com.cabbage.biz.userInfo.user.UserVO;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/user")
@Controller
public class UserController {
	
	private final UserService userService;
	private final ChatRoomService chatRoomService;
	private final NotiService notiService;

	@GetMapping("/login")
	public String getLoginPage() {
		System.out.println("---유저-> 로그 ---");
		return "user/login";
	}

	@GetMapping("/findAccount")
	public String getfindPage() {
		return "user/findAccount";
	}

	@GetMapping("/signUp")
	public String getsignUpPage() {
		return "user/signUp";
	}
	
	@GetMapping("/deleteUser")
	public String deletePage() {
		return "user/deleteUser";
	}
	
	@GetMapping("/deleteModal")
	public String deleteModal() {
		return "user/deleteModal";
	}

	@GetMapping("/myInfo")
    public String getinfoPage(UserVO vo, Model model, HttpSession session) {
		String userId = (String)session.getAttribute("userId");
		vo.setUserId(userId);
        // userId로 사용자 정보를 가져오는 예시
		vo = userService.userInfo(vo);
		if (vo.getUserProfile() == null) {
			vo.setUserProfile("profile_default.png");
		}
		Integer unreadChatCount = chatRoomService.getUnreadCount(userId);
		
		model.addAttribute("unreadChatCount", unreadChatCount);
        // 모델에 사용자 정보를 추가하여 뷰로 전달
        model.addAttribute("user", vo);
        model.addAttribute("nowUserId", vo.getUserId());
        
        int alrim = notiService.getNotiCountById(userId);
		List<NotiVO> alrim2 = notiService.getNotiListById(userId);
		model.addAttribute("alrim", alrim);
		model.addAttribute("alrim2", alrim2);

        return "user/myInfo";
    }
	
	@GetMapping("/info/{id}")
    public String getinfoPage(UserVO vo, Model model, HttpSession session, @PathVariable String id) {
		String userId = (String)session.getAttribute("userId");
		vo.setUserId(id);
        // userId로 사용자 정보를 가져오는 예시
		UserVO user = userService.userInfo(vo);
		if (user.getUserProfile() == null) {
			user.setUserProfile("profile_default.png");
		}
		Integer unreadChatCount = chatRoomService.getUnreadCount(userId);
		
		model.addAttribute("unreadChatCount", unreadChatCount);
        // 모델에 사용자 정보를 추가하여 뷰로 전달
        model.addAttribute("user", user);
        model.addAttribute("nowUserId", userId);
        
        int alrim = notiService.getNotiCountById(userId);
		List<NotiVO> alrim2 = notiService.getNotiListById(userId);
		model.addAttribute("alrim", alrim);
		model.addAttribute("alrim2", alrim2);

        return "user/myInfo";
    }
	
	@GetMapping("/infoUpdate")
	public String getinfoUpdatePage(UserVO vo, Model model, HttpSession session) {
		vo.setUserId((String)session.getAttribute("userId"));
		UserVO user = userService.userInfo(vo);
		
		model.addAttribute("user", user);
		return "user/infoUpdate";
	}
	
	@GetMapping("/userUpdate")
	public String userUpdate(UserVO vo) {
		userService.userUpdate(vo);
		return "redirect:/user/myInfo";
	}
	
	@PostMapping("/userUpdate")
	public String updateUser(UserVO vo, HttpSession session) {
	    // 기존 정보를 가져오기 위해 userId로 DB에서 정보 조회
		vo.setUserId((String)session.getAttribute("userId"));
		UserVO user = userService.userInfo(vo);
		

	    // 기존 정보를 유지하기 위해 빈 값인 경우 기존 정보로 대체
	    if (vo.getUserNickname() == null || vo.getUserNickname().isEmpty()) {
	        vo.setUserNickname(user.getUserNickname());
	    }

	    if (vo.getUserPassword() == null || vo.getUserPassword().isEmpty()) {
	        vo.setUserPassword(user.getUserPassword());
	    }

	    if (vo.getUserPhone() == null || vo.getUserPhone().isEmpty()) {
	        vo.setUserPhone(user.getUserPhone());
	    }

	    // DB에 수정된 정보 업데이트
	    userService.userUpdate(vo);

	    return "redirect:/user/myInfo"; // 수정 완료 후 프로필 페이지로 리다이렉트
	}
	
	
	
	@PostMapping("/loginIn")
	public String login(UserVO vo, HttpSession session, RedirectAttributes redirectAttributes) {
		if (vo.getUserId() == null || vo.getUserId().isEmpty()) {
			redirectAttributes.addFlashAttribute("message", "아이디를 입력해주세요.");
			
			return "redirect:/user/login";
		}

		if (vo.getUserPassword() == null || vo.getUserPassword().isEmpty()) {
			redirectAttributes.addFlashAttribute("message", "비밀번호를 입력해주세요.");
			return "redirect:/user/login";
		}

		UserVO user = userService.getUser(vo);

		if (user != null) {
			if ("WITHDRAWAL".equals(user.getUserStatus())) {
	            redirectAttributes.addFlashAttribute("message", "탈퇴된 회원입니다.");
	            return "redirect:/user/login";
	        }
			
			session.setAttribute("userId", user.getUserId());
			
			if(user.getUserId().equals("admin"))
				return "redirect:/qa/adminQaFormList";
			
			return "redirect:/";
		} else {
			redirectAttributes.addFlashAttribute("message", "아이디 또는 비밀번호를 확인해주세요.");
			return "redirect:/user/login";
		}
	}

	@RequestMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();

		return "redirect:/";
	}

	@RequestMapping("/joinUser")
	public String joinAction(UserVO vo) {
		userService.joinUser(vo);
		return "redirect:/user/login";
	}

	// Id 중복 확인
	@PostMapping("/ConfirmId")
	@ResponseBody
	public ResponseEntity<Boolean> confirmId(String userId) {
		boolean result = true;
		if (userId.trim().isEmpty()) {
			result = false;
		} else {
			if (userService.selectId(userId)) {
				result = false;
			} else {
				result = true;
			}
		}

		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	// 닉네임 중복 확인
	@PostMapping("/ConfirmNick")
	@ResponseBody
	public ResponseEntity<Boolean> confirmNick(String userNickname) {
		boolean result = true;
		if (userNickname.trim().isEmpty()) {
			result = false;
		} else {
			if (userService.selectNick(userNickname)) {
				result = false;
			} else {
				result = true;
			}	
		}

		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	// 핸드폰 중복 확인
	@PostMapping("/ConfirmPhone")
	@ResponseBody
	public ResponseEntity<Boolean> confirmPhone(String userPhone) {
		boolean result = true;
		if (userPhone.trim().isEmpty()) {
			result = false;
		} else {
			if (userService.selectPhone(userPhone)) {
				result = false;
			} else {
				result = true;
			}
		}

		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	// 아이디 찾기
	@PostMapping("/findId")
	public String findId(UserVO vo, RedirectAttributes redirectAttributes) {
		if (vo.getUserName() == null || vo.getUserName().isEmpty()) {
			redirectAttributes.addFlashAttribute("message", "이름을 입력해주세요.");
			return "redirect:/user/findAccount";

		}

		if (vo.getUserPhone() == null || vo.getUserPhone().isEmpty()) {
			redirectAttributes.addFlashAttribute("message", "전화번호를 입력해주세요.");
			return "redirect:/user/findAccount";
		}

		UserVO user = userService.findId(vo);

		if (user != null) {
			redirectAttributes.addFlashAttribute("message", "회원님의 아이디는  [" + user.getUserId() + "] 입니다");
			return "redirect:/user/login";
		} else {
			redirectAttributes.addFlashAttribute("message", "계정이 존재하지않아요");
			return ("redirect:/user/login");
		}
	}

	// 비밀번호 찾기
	@PostMapping("/findPw")
	public String findPw(UserVO vo, RedirectAttributes redirectAttributes) {
		if (vo.getUserEmail() == null || vo.getUserEmail().isEmpty()) {
			redirectAttributes.addFlashAttribute("message", "이메일을 입력해주세요.");
			return "redirect:/user/findAccount";
		}

		if (vo.getUserId() == null || vo.getUserId().isEmpty()) {
			redirectAttributes.addFlashAttribute("message", "아아디를 입력해주세요.");
			return "redirect:/user/findAccount";
		}

		UserVO user = userService.findPw(vo);

		if (user != null) {
			redirectAttributes.addFlashAttribute("message", "회원님의 비밀번호는  [" + user.getUserPassword() + "] 입니다");
			return "redirect:/user/login";
		} else {
			redirectAttributes.addFlashAttribute("message", "계정이 존재하지않아요");
			return "redirect:/user/login";
		}
	}

	@RequestMapping("/purchaseList")
	@ResponseBody
	public List<UserVO> purchaseList(UserVO vo, HttpSession session) {
	    vo.setUserId((String) session.getAttribute("userId"));
	    List<UserVO> purchaseList = userService.purchaseList(vo);

	    if (purchaseList != null) {
	        for (UserVO product : purchaseList) {
	            
	            String review = product.getReview();
	            if (review != null && !review.isEmpty()) {
	                product.setReview("평가 완료");
	            } else {
	                product.setReview("거래 평가");
	            }
	        }
	    }

	    return purchaseList;
	}
	
	@RequestMapping("/salesList")
	@ResponseBody
	public List<UserVO> salesList(UserVO vo, HttpSession session) {
		List<UserVO> salesList = userService.salesList(vo);
		return salesList;
	}
	
	@RequestMapping("/wishList")
	@ResponseBody
	public List<UserVO> wishList(UserVO vo, HttpSession session) {
		vo.setUserId((String)session.getAttribute("userId"));
		List<UserVO> wishList = userService.wishList(vo);
		return wishList;
	}
	
	@PostMapping("/profileUpload")
	public String profileUpload(MultipartFile uploadFile, HttpSession session, UserVO vo) throws IllegalStateException, IOException {
		
		uploadFile = vo.getUploadFile();
		
		if (uploadFile == null) {
			//System.out.println("::: uploadFile 파라미터가 전달되지 않은 경우");
		} else if (uploadFile.isEmpty()) {
			//System.out.println("::: 전달받은 파일 데이터가 없는 경우");
		} else { //업로드 파일이 존재하는 경우
			
			//원본파일명 구하기
			String filename = uploadFile.getOriginalFilename();
			String extension = filename.substring(filename.lastIndexOf("."), filename.length());
			String savedFilename = "PROFILE_PIC" + UUID.randomUUID().toString() + extension;
			
			//물리적 파일 복사
			String destPathFile = CommonData.FILE_UPLOAD_ROOT + "/profilePic/" + savedFilename;
			uploadFile.transferTo(new File(destPathFile));
			vo.setFileName(savedFilename);
			vo.setUserId((String)session.getAttribute("userId"));
			userService.profileUpload(vo);
		}
			
			return "redirect:/user/myInfo";
			
	}
	
	@RequestMapping("/wishKeyword")
	public String wishKeyword(UserVO vo, HttpSession session) {
		vo.setUserId((String)session.getAttribute("userId"));
		vo.setWishKeyword(vo.getWishKeyword().trim());
		
		if(vo.getWishKeyword().length() == 0) return "redirect:/user/myInfo";
		
		List<UserVO> keywordList = userService.keywordList(vo);
		
		for(UserVO temp : keywordList) {
			if(vo.getWishKeyword().equals(temp.getWishKeyword())) return "redirect:/user/myInfo";
		}
		
		userService.wishKeyword(vo);
		return "redirect:/user/myInfo";
	}
	
	
	  //관심 키워드 삭제
    @RequestMapping("/deleteWishKeyword")
	@ResponseBody
    public String deleteWishKeyword(HttpServletRequest request, HttpSession session, UserVO vo) {
    	
    	vo.setUserId((String)session.getAttribute("userId"));
        String[] ajaxMsg = request.getParameterValues("valueArr");
        int size = ajaxMsg.length;
        
        for (int i = 0; i < size; i++) {
            vo.setWishKeyword(ajaxMsg[i]);
            userService.deleteWishKeyword(vo);
        }
        
        return "redirect:/user/myInfo";
    }
	

	
	@RequestMapping("/keywordList")
	@ResponseBody
	public List<UserVO> keywordList(UserVO vo, HttpSession session) {
		String userId = (String)session.getAttribute("userId");
		if(!vo.getUserId().equals(userId))
			return null;
		List<UserVO> keywordList = userService.keywordList(vo);
		return keywordList;
	}
	
	
	  //게시물 선택삭제
    @RequestMapping("/deleteWish")
	@ResponseBody
    public String deleteWish(HttpServletRequest request, HttpSession session, UserVO vo) {
    	
    	vo.setUserId((String)session.getAttribute("userId"));
        String[] ajaxMsg = request.getParameterValues("valueArr");
        int size = ajaxMsg.length;
        
        for (int i = 0; i < size; i++) {
            vo.setPostId(Integer.parseInt(ajaxMsg[i]));
            userService.deleteWish(vo);
        }
        
        return "redirect:/user/myInfo";
    }
	
    @RequestMapping("/leaveUser")
	public String leaveUser(UserVO vo, HttpSession session) {
		vo.setUserId((String)session.getAttribute("userId"));
		userService.deleteUser(vo);
		session.invalidate();
		return "user/login";
	}

	// 닉네임(회원정보수정) 중복 확인
	@PostMapping("/nickUpdate")
	@ResponseBody
	public ResponseEntity<Boolean> nickUpdate(UserVO vo, String userNickname, HttpSession session) {

		vo.setUserId((String) session.getAttribute("userId"));
		UserVO user = userService.userInfo(vo);

		boolean result = true;
		if (userNickname.trim().isEmpty()) {
			result = false;
		} else {
			if (user.getUserNickname().equals(userNickname)) {
				result = true;
			} else {
				result = !userService.selectNick(userNickname);
			}
		}

		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	

    @PostMapping("/ondoUpDown")
    public String ondoUpDown(UserVO vo, String action, HttpSession session) {
        if ("up".equals(action)) {
        	vo.setIncrementValue(0.1f);
            userService.ondoUpDown(vo);
            
        } else if ("down".equals(action)) {
        	vo.setIncrementValue(-0.1f);
        	userService.ondoUpDown(vo);
        }
        
        String userId = (String)session.getAttribute("userId");
        String sessionNickname = userService.userInfo(UserVO.builder().userId(userId).build()).getUserNickname();
        
        notiService.afterUpdateUserOndo(vo, sessionNickname);
        
        userService.reviewInput(vo);
        return "redirect:/user/myInfo";
    }
}
