package com.cabbage.biz.post.view;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.cabbage.biz.chat.chat.ChatRoomService;
import com.cabbage.biz.chat.user.UserService;
import com.cabbage.biz.common.CommonData;
import com.cabbage.biz.noti.noti.NotiService;
import com.cabbage.biz.noti.noti.NotiVO;
import com.cabbage.biz.post.post.PostService;
import com.cabbage.biz.post.post.PostVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/post")
public class PostController {
	
	private final UserService userService;
	private final PostService postService;
	private final ChatRoomService chatRoomService;
	private final NotiService notiService;
	//private List<MultipartFile> uploadFile = new ArrayList<>();

	//post 작성
    @GetMapping("/create")
    public String getPostCreate(Model model, HttpSession session) {
    	String userId = (String)session.getAttribute("userId");
    	Integer unreadChatCount = chatRoomService.getUnreadCount(userId);
		
		model.addAttribute("unreadChatCount", unreadChatCount);
    	
    	model.addAttribute("userId", userId);
    	
    	int alrim = notiService.getNotiCountById(userId);
		List<NotiVO> alrim2 = notiService.getNotiListById(userId);
		model.addAttribute("alrim", alrim);
		model.addAttribute("alrim2", alrim2);
    	
        return "post/postCreate";
    }
    
    //수정 창
    @GetMapping("updatePostPage")
    public String updatePostPage(HttpSession session, PostVO vo, Model model) {
    	String userId = (String)session.getAttribute("userId");
    	
    	PostVO post = postService.getUpdatePage(vo);
    	if(!post.getSellerId().equals(userId)) {
    		return "post/error";
    	}
    	
    	List<PostVO> postPic = postService.getPostPic(vo);
    	
    	Integer unreadChatCount = chatRoomService.getUnreadCount(userId);
		
		model.addAttribute("unreadChatCount", unreadChatCount);
    	
    	model.addAttribute("postPic", postPic);
    	model.addAttribute("post", post);
    	
    	int alrim = notiService.getNotiCountById(userId);
		List<NotiVO> alrim2 = notiService.getNotiListById(userId);
		model.addAttribute("alrim", alrim);
		model.addAttribute("alrim2", alrim2);
    	
    	return "post/updatePost";
    }
    //수정
    @PostMapping("/updatePost")
    public String updatePost(HttpSession session, PostVO vo) throws IllegalStateException, IOException {
    	String userId = (String)session.getAttribute("userId");
    	vo.setUserId(userId);
    	vo.setSellerId(userId);
    	
    	PostVO oldPost = postService.getUpdatePage(vo);

    	//제거한 이미지 처리
    	if(vo.getFileIdArr() == null) {
    		//System.out.println("삭제할 이미지 없음");
    	}else {
    		for(String id : vo.getFileIdArr()) {
    			postService.deleteFiles(id);
    		}
    	}
    	
    	//게시글 정보 수정
    	postService.updatePost(vo);
    	
    	//추가 이미지 처리
    	List<MultipartFile> uploadFile = vo.getUploadFile();
    	
    	if(uploadFile == null) {
    		//System.out.println("uploadFile 파라미터 전달x");
    	}else if(uploadFile.isEmpty()) {
    		//System.out.println("업로드 파일 없음");
    	}else {
    		List<String> filesList = new ArrayList<String>();
    		
    		for(MultipartFile list : uploadFile) {
    			String fileName = list.getOriginalFilename();
    			int lastDot = fileName.lastIndexOf('.');	//확장자 찾기
    			String fileExtension = fileName.substring(lastDot + 1);
    			
    			if(fileName.trim() != "") {
    			String saveFileName = "post" + vo.getPostId() + "_" + UUID.randomUUID().toString() + "." + fileExtension;
    			
    			list.transferTo(new File(CommonData.FILE_UPLOAD_ROOT + "/postPic/" + saveFileName));
    			filesList.add(saveFileName);
    			}
    		}
    		if(!filesList.isEmpty()) {
	    		for(String list : filesList) {
	    			vo.setFileName(list);
	    			postService.insertFiles(vo);
	    		}
	    		
	    		//PostPic 테이블에 데이터 넣기
	    		List<PostVO> picVO = postService.getFileId_PostId(vo.getPostId());
	    		for(PostVO picvo : picVO) {
	    			postService.insertPostPic(picvo);
	    		}
    		}
    		
    	}
    	
    	
    	if(oldPost.getPostPrice() != vo.getPostPrice()) {

    		notiService.afterUpdatePost(vo);
    	
    	} // 250120에 내가 {} 넣음. 이거 원래 없고 그냥 if문만 있었음.
    	// 그 코드는 말이 안되는데, 잘 작동이 되었었나 모르겠음.
    	return "redirect:/post/getPostList";
    }
    
    //게시글 논리적 삭제
    @PostMapping("deletePost")
    public String deletePost(PostVO vo) {
    	
    	postService.deletePost(vo);
    	return "redirect:/post/getPostList";
    }
    
    //예약중 처리
    @PostMapping("reservePost")
    public String reservePost(PostVO vo) {
    	Long postId = vo.getPostId();
    	
    	postService.reservePost(vo);
    	
    	return "redirect:/post/getPost/" + postId;
    }
    //거래완료 처리
    @PostMapping("finishPost")
    public String finishPost(PostVO vo) {
    	Long postId = vo.getPostId();
    	
    	postService.finishPost(vo);
    	
    	return "redirect:/post/getPost/" + postId;
    }
    //거래중 처리
    @PostMapping("enablePost")
    public String enablePost(PostVO vo) {
    	Long postId = vo.getPostId();
    	
    	postService.enablePost(vo);
    	
    	return "redirect:/post/getPost/" + postId;
    }
    
    //결제 창
    @PostMapping("directPayPage")
    public String directPayPage(HttpSession session, long finalPrice, long chatRoomId, long countByRoom, PostVO vo, Model model) {
    	String userId = (String)session.getAttribute("userId");
    	String userNickname = userService.getUserById(userId).getUserName();
    	PostVO post = postService.getPayPost(vo);
    	String sellerNickname = userService.getUserById(post.getSellerId()).getUserName();
    	
    	String fileName = postService.getOnePic(vo);
    	if(fileName != null) {
    		post.setFileName(fileName);
    	}
    	
    	model.addAttribute("userId", userId);
    	model.addAttribute("userNickname", userNickname);
    	model.addAttribute("sellerNickname", sellerNickname);
    	model.addAttribute("post", post);
    	model.addAttribute("finalPrice", finalPrice);
    	model.addAttribute("chatRoomId", chatRoomId);
    	model.addAttribute("countByRoom", countByRoom);
    	
    	return "post/directPayPage";
    }
    
    
    //게시글 상세보기
    @GetMapping("/getPost/{postId}")
    public String getPostDetail(HttpSession session, @PathVariable long postId, PostVO vo, Model model) throws JsonProcessingException {
    	String userId = (String)session.getAttribute("userId");
    	vo.setPostId(postId);
    	vo.setUserId(userId);
    	
    	//post상세정보
    	PostVO post = postService.getPost(vo);
    	if(post == null)
    		return "/post/error";
    	
    	//조회수 증가
    	postService.plusPostViews(vo);
    	
    	
    	vo.setSellerId(post.getSellerId());	//해당 포스트에 sellerId 추가
    	
    	if(post.getPostStatus().equals("DELETE")) {
    		return "post/deletePostPage";
    	}
    	
    	//해당 post 이미지들
    	List<PostVO> postPic = postService.getPostPic(vo);
    	
    	//해당 post등록 user정보
    	PostVO user = postService.getUser(vo);
    	
    	//해당 판매자의 다른 글
    	List<PostVO> anotherPost = postService.getAnotherPost(vo);
    	
    	//찜한 게시글인지 조회
    	PostVO wishPost = postService.checkWishList(vo);
    	
    	//관심 수
    	int countWish = postService.countWish(vo);
    	
    	Integer unreadChatCount = chatRoomService.getUnreadCount(userId);
    	
    	//해당 게시글에서 채팅한 회원 아이디, 닉네임
    	List<PostVO> chatUser = postService.getChatUser(vo);
    	if(!chatUser.isEmpty()) {
    		ObjectMapper objectMapper = new ObjectMapper();
    		String chatUserJson = objectMapper.writeValueAsString(chatUser);
    		model.addAttribute("chatUserJson", chatUserJson);
    	}
		
		model.addAttribute("unreadChatCount", unreadChatCount);
    	
    	model.addAttribute("post", post);
    	model.addAttribute("postPic", postPic);
    	model.addAttribute("user", user);
    	model.addAttribute("anotherPost", anotherPost);
    	model.addAttribute("wishPost", wishPost);
    	model.addAttribute("countWish", countWish);
    	model.addAttribute("userId", userId);
    	
    	int alrim = notiService.getNotiCountById(userId);
		List<NotiVO> alrim2 = notiService.getNotiListById(userId);
		model.addAttribute("alrim", alrim);
		model.addAttribute("alrim2", alrim2);
    	
    	return "post/postDetail";
    }
    
    
    
    //게시글 등록
    @PostMapping("/insertPost")
    public String insertPost(PostVO vo, HttpSession session) throws IllegalStateException, IOException {
    	String userId = (String)session.getAttribute("userId");
    	vo.setSellerId(userId);
    	//post테이블 데이터 insert
    	postService.insertPost(vo);
    	
    	//files테이블 데이터 insert
    	List<MultipartFile> uploadFile = vo.getUploadFile();

    	
    	if(uploadFile == null) {
    		//System.out.println("uploadFile 파라미터 전달x");
    	}else if(uploadFile.isEmpty()) {
    		//System.out.println("업로드 파일 없음");
    	}else {
    		int maxPostId = postService.getMaxPostId();
    		List<String> filesList = new ArrayList<String>();
    		
    		for(MultipartFile list : uploadFile) {

    			String fileName = list.getOriginalFilename();

    	    	int lastDot = fileName.lastIndexOf('.');	//확장자 찾기
    			String fileExtension = fileName.substring(lastDot + 1);
    			
    			if(fileName.trim() != "") {
    			String saveFileName = "post" + maxPostId + "_" + UUID.randomUUID().toString() + "." + fileExtension;
    			
    			list.transferTo(new File(CommonData.FILE_UPLOAD_ROOT + "/postPic/" + saveFileName));
    			filesList.add(saveFileName);

    			}
    		}
    		// 파일 업로드가 모두 끝난 후 한 번만 호출
    		if (!filesList.isEmpty()) {
    		    String saveFileName = filesList.get(0); // 제일 첫 번째 파일명을 가져옴
    		    notiService.afterInsertPost(vo, saveFileName);
    		}
    		
    		vo.setUserId(vo.getSellerId());
    		if(!filesList.isEmpty()) {
	    		for(String list : filesList) {
	    			vo.setFileName(list);
	    			postService.insertFiles(vo);
	    		}
	    		
	    		//PostPic 테이블에 데이터 넣기
	    		List<PostVO> picVO = postService.getFileId_PostId((long)maxPostId);
	    		for(PostVO picvo : picVO) {
	    			postService.insertPostPic(picvo);
	    		}
    		}
    		
    	}
    	
//    	notiService.afterInsertPost(vo, saveFileName);
    	
    	return "redirect:/post/getPost/"+vo.getPostId();
    }
    
    // 안씀
    @RequestMapping("/getPostList")
    public String getPostList(PostVO vo, Model model) {
    	
    	List<PostVO> postList = postService.getPostList(vo);
    	
    	model.addAttribute("postList", postList);
    	
    	return "redirect:/";
    }
    
    //찜
    @PostMapping("/addWishList")
    public ResponseEntity<String> addWishList(@RequestBody PostVO vo) {
        ResponseEntity<String> entity = null;

        try {
            postService.addWishList(vo);
            entity = new ResponseEntity<String>("SUCCESS", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            entity = new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return entity;
    }
    //찜 해제
    @PostMapping("/deleteWishList")
    public ResponseEntity<String> deleteWishList(@RequestBody PostVO vo) {
    	ResponseEntity<String> entity = null;
    	
    	try {
    		postService.deleteWishList(vo);
    		entity = new ResponseEntity<String>("SUCCESS", HttpStatus.OK);
    	} catch (Exception e) {
    		e.printStackTrace();
    		entity = new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
    	}
    	
    	return entity;
    }
    
	// 거래완료하면 buyerId 추가
	@PostMapping("insertBuyer")
	@ResponseBody
	public int insertBuyer(@RequestParam("userNickname") String userNickname, @RequestParam("postId") long postId) {
		Map<String, Object> param = new HashMap<>();
		param.put("userNickname", userNickname);
		param.put("postId", postId);

		int success = postService.insertBuyer(param);

		return success;
	}
}
