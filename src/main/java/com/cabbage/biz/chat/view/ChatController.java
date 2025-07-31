package com.cabbage.biz.chat.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.cabbage.biz.chat.chat.ChatMessageService;
import com.cabbage.biz.chat.chat.ChatRoomService;
import com.cabbage.biz.chat.chat.ChatRoomVO;
import com.cabbage.biz.chat.post.PostService;
import com.cabbage.biz.chat.post.PostVO;
import com.cabbage.biz.chat.sse.ChatSSEService;
import com.cabbage.biz.chat.user.UserService;
import com.cabbage.biz.chat.user.UserVO;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/chat")
@Controller
public class ChatController {
	
	private final ChatSSEService chatSSEService;
	private final ChatRoomService chatRoomService;
	private final ChatMessageService chatMessageService;
	private final PostService postService;
	private final UserService userService;
	
	
	@GetMapping("/testIndex")
	public String getBetaTestIndex() {
		return "/test/betaTest";
	}
	
	@GetMapping("/testRequest")
	public String testRequest(HttpSession session, String userId, String postId) {
		session.setAttribute("userId", userId);
		return "redirect:/chat/request/"+postId;
	}
	
	
	//////////////////////////////
	
	@GetMapping("")
	public String getChatMain(HttpSession session, Model model) {
		String userId = (String)session.getAttribute("userId");

		
		model.addAttribute("nowUserVO", userService.getUserById(userId));
		model.addAttribute("chatRoomList", chatRoomService.getChatRoomListByUserId(userId));
		
		
		return "/chat/chatMainTemplate";
	}
	
	@GetMapping("/room/{chatRoomId}")
	public String getChatMain(HttpSession session, Model model, @PathVariable long chatRoomId) {
		String userId = (String)session.getAttribute("userId");
		
		
		model.addAttribute("nowUserVO", userService.getUserById(userId));
		model.addAttribute("chatRoomList", chatRoomService.getChatRoomListByUserId(userId));
		
		return "/chat/chatMainTemplate";
	}
	
	// 포스트에서 사용자가 채팅하기 요청 보내면
	// 방을 찾아서 그 방아이디를 가지고 /room/{id}로 리다이렉트 시킴 (없으면 만들어줌)
	// post seller와 요청한 아이디가 같으면 그냥 채팅방 메인으로 리다이렉트 시킨다
	@GetMapping("/request/{postId}")
	public String requestChatting(@PathVariable long postId, HttpSession session) {
		
		String userId = (String)session.getAttribute("userId");
		
		PostVO post = postService.getPostById(postId);
		
		//  요청한 포스트 아이디가 없거나, 판매자와 요청자가 같으면 채팅 메인으로 리다이렉트
		if(post == null || userId.equals(post.getSellerId()))
			return "redirect:/chat";
		
		ChatRoomVO chatRoom = chatRoomService.getChatRoomWhenRequest(postId, userId);
		
		// 채팅방 없으면 만들어
		if(chatRoom == null) {
			chatRoom = chatRoomService.makeNewChatRoom(postId, userId);
			
			// 게시글 판매자가 탈퇴한 상태라면 채팅 메인으로 리다이렉트
			UserVO seller = userService.getUserById(post.getSellerId());
			if(seller.getUserStatus().equals("WITHDRWAL"))
				return "redirect:/chat";
		}
		
		return "redirect:/chat/room/" + chatRoom.getChatRoomId();
	}
	
//============================================ AJAX (POST) =================================================
	
	// 왼쪽 동그란 프사 누르면 빈 화면 (AJAX)
	@PostMapping("")
	public String getEmptyChat() {
		
		return "/chat/chatContent-empty";
	}
	
	
	// 방 번호 받아서 html 보내줌 (AJAX) (내용은 빼고 보내줌)
	@PostMapping("/room/{chatRoomId}")
	public String getRoom(HttpSession session, @PathVariable long chatRoomId, Model model) {
		String userId = (String)session.getAttribute("userId");
		
		ChatRoomVO chatRoom = chatRoomService.getChatRoomById(chatRoomId);
		
		if(chatRoom == null)
			return "/chat/chatContent-empty";
		
		model.addAttribute("nowUserVO", userService.getUserById(userId));
		model.addAttribute("postVO", postService.getPostById(chatRoom.getPostId()));
		
		// 현재 채팅방에 있는 유저중에 현재 세션의 아이디랑 다르면 상대방 유저로 등록한다
		chatRoom.getUsers().forEach( user -> {
			if(!user.getUserId().equals(userId)) 
				model.addAttribute("partnerUserVO", user);
		});
		
		return "/chat/chatRoomTemplate";
	}
	
	// 새로운 채팅방에서 메세지 왔을 때 리스트에 채팅방 만들기위한 데이터 요청
	@PostMapping("/roomInList/{chatRoomId}")
	@ResponseBody
	public ChatRoomVO getRoomInList(HttpSession session, @PathVariable long chatRoomId) {
		String userId = (String)session.getAttribute("userId");
		ChatRoomVO chatRoom = ChatRoomVO.builder()
										.chatRoomId(chatRoomId)
										.requestUserId(userId)
										.build();
		
		chatRoom = chatRoomService.getChatRoomVOByUserIdAndChatRoomId(chatRoom);
		
		
		return chatRoom;
	}
	
	@PostMapping("/updateTimeTerm")
	@ResponseBody
	public List<ChatRoomVO> getLastTimeTermList(@RequestParam(value="chatRoomIdList[]") ArrayList<Integer> chatRoomIdList) {
		return chatRoomService.getLastMessageTimeListByChatRoomId(chatRoomIdList);
	}
	
	// ajax로 SSE 구독 요청 받음
	@GetMapping(value = "/subscribe/{id}", produces = "text/event-stream")
	public SseEmitter subscribe(@PathVariable String id, 
						@RequestHeader(value = "Last-Event-ID", required = false, defaultValue =  "") String lastEventId) {
		
		return chatSSEService.subscribe(id, lastEventId);
	}
	
	@PostMapping("/exit")
	@ResponseBody
	public String exitChatRoom(HttpSession session, ChatRoomVO chatRoom) {
		String userId = (String)session.getAttribute("userId");
		chatRoom.setRequestUserId(userId);
		chatRoomService.exitChatRoom(chatRoom);
		return "success";
	}
	
	@PostMapping("/emojiList")
	public String getEmojiList(Model model) {
		List<Map<String, String>>  emojiList = chatMessageService.getEmojiList();
		model.addAttribute("emojis", emojiList);
		
		return "/chat/chatEmoji_Template";
	}
	
}
