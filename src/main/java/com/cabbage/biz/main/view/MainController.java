package com.cabbage.biz.main.view;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cabbage.biz.chat.chat.ChatRoomService;
import com.cabbage.biz.common.ListContainer;
import com.cabbage.biz.main.main.MainService;
import com.cabbage.biz.main.main.MainVO;
import com.cabbage.biz.noti.noti.NotiService;
import com.cabbage.biz.noti.noti.NotiVO;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class MainController {
	@Qualifier("mainService")
    private final MainService mainService;
	private final ChatRoomService chatRoomService;
	private final NotiService notiService;
	private final ListContainer listContainer;
	
	// 프로젝트 시작 시 실행
	@RequestMapping(value = "/")
	public String home(Model model, HttpSession session) {

		List<MainVO> postS = mainService.getPostListForNew();
		List<MainVO> postRV = listContainer.top100Post.subList(0, 8);
		
		if (null == session.getAttribute("userId")) {
			model.addAttribute("somethingNew", postS);
			model.addAttribute("recomendPostByVeiwCount", postRV);
		} else {
			String id = (session.getAttribute("userId")).toString();

			List<MainVO> postRI = mainService.getPostListForRcById(id);
			Integer unreadChatCount = chatRoomService.getUnreadCount(id);
			model.addAttribute("unreadChatCount", unreadChatCount);
			model.addAttribute("recomendPostById", postRI);
			model.addAttribute("somethingNew", postS);
			model.addAttribute("recomendPostByVeiwCount", postRV);
			
			// 알림 개수 조회
			int alrim = notiService.getNotiCountById(id);
			// 알림 내용 조회
			List<NotiVO> alrim2 = notiService.getNotiListById(id);
			
			model.addAttribute("alrim", alrim);
			model.addAttribute("alrim2", alrim2);
		}
				

		return "/main/main";
	}
	
	// 모두보기 클릭 시 실행
	@GetMapping("/All")
	public String getAll(@RequestParam(name = "type", required = false) String type, 
								HttpSession session, Model model) {
		
		 // 세션에서 id 값을 얻기
		String id = (String) session.getAttribute("userId");
		int totalC = 0;
        int totalP = 0;
		int curPage = 1;
		int end = curPage * 12;
		int begin = end - 12 + 1;
		List<MainVO> post = null;
		
		if ("RI".equals(type)) {
			totalC = mainService.countIdAll(id);
	        totalP = (int) Math.ceil((double) totalC / 12);
			post = mainService.getPostListForRcByIdAll(id, begin, end);
		} else if ("N".equals(type)) {
			totalC = mainService.countNewAll();
	        totalP = (int) Math.ceil((double) totalC / 12);
			post = mainService.getPostListForNewAll(begin, end);
		} else if ("RV".equals(type)) {
			totalC = mainService.countVcAll();
	        totalP = (int) Math.ceil((double) totalC / 12);
			//post = postService.getPostListForRcByVcAll(begin, end);
	        post = listContainer.top100Post.subList(begin-1, end);
		}
		
		Integer unreadChatCount = chatRoomService.getUnreadCount(id);
		
		model.addAttribute("unreadChatCount", unreadChatCount);
		
		model.addAttribute("list", post);		
        model.addAttribute("totalP", totalP);
        model.addAttribute("curPage", curPage);
        model.addAttribute("type", type);
        
        int alrim = notiService.getNotiCountById(id);
		List<NotiVO> alrim2 = notiService.getNotiListById(id);
		model.addAttribute("alrim", alrim);
		model.addAttribute("alrim2", alrim2);
	    return "/main/all";

	}
	
	// 무한스크롤 용 메소드 for New
	@PostMapping("/All")
	public String getAllAjax(@RequestParam(name = "curPage", required = false) Integer curPage,
								HttpSession session, Model model, String type) {
		String id = (String) session.getAttribute("userId");
		
		int totalC = 0;
        int totalP = 0;
        List<MainVO> post = null;
        int end = curPage * 12;
		int begin = end - 12 + 1;
		
		switch(type) {
		case "N":
			totalC = mainService.countNewAll();
	        post = mainService.getPostListForNewAll(begin, end);
			break;
		case "RV":
			totalC = mainService.countVcAll();
			post = listContainer.top100Post.subList(begin-1, end);
			break;
		case "RI":
			totalC = mainService.countIdAll(id);
			post = mainService.getPostListForRcByIdAll(id, begin, end);
			break;
		}
		
		totalP = (int) Math.ceil((double) totalC / 12);

		model.addAttribute("list", post);
		model.addAttribute("totalP", totalP);
        model.addAttribute("curPage", curPage);

		return "/main/all_Ajax";
	}

	

}
