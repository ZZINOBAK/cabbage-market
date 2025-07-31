package com.cabbage.biz.search.view;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cabbage.biz.chat.chat.ChatRoomService;
import com.cabbage.biz.common.ListContainer;
import com.cabbage.biz.noti.noti.NotiService;
import com.cabbage.biz.noti.noti.NotiVO;
import com.cabbage.biz.search.post.PostVO;
import com.cabbage.biz.search.search.SearchService;
import com.cabbage.biz.search.search.SearchVO;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class SearchController {

    private final SearchService searchService;
    private final ChatRoomService chatRoomService;
    private final NotiService notiService;
    private final ListContainer listContainer;
    
    
    
    @GetMapping("/delete-keyword")
    @ResponseBody
    public void deleteKeyword(@RequestParam("keyword") String searchKeyword, HttpSession session) {
    	String userId = (String)session.getAttribute("userId");
        SearchVO vo = new SearchVO();
        vo.setUserId(userId);
        vo.setSearchKeyword(searchKeyword);
        searchService.deleteKeyword(vo);
    }

    @GetMapping("/autocomplete")
    @ResponseBody
    public List<String> autocomplete(@RequestParam("query") String query) {
        return searchService.getAutocompleteResults(query);
    }

    @GetMapping("/recent")
    @ResponseBody
    public List<String> recent(HttpSession session) {
    	String userId = (String)session.getAttribute("userId");
        SearchVO vo = new SearchVO();
        vo.setUserId(userId);
        return searchService.recentSearchLog(vo);
    }

    @GetMapping("/get_rolling")
    @ResponseBody
    public List<String> getRolling() {
    	//return searchService.TopSearched();
    	return listContainer.top10Search;
    }

    @GetMapping("/postList")
    public String showPostList( HttpSession session, String category, String keyword, Model model) {
    	String userId = (String)session.getAttribute("userId");
        int totalC = 0;
        int paging = 8;
        // 카테고리가 있으면
        if (category != null) {
            totalC =searchService.countCategoryPostList(category);
            model.addAttribute("posts", searchService.findByCategoryPost(category,1, paging));
        }
        // 카테고리가 없고 키워드가 있으면
        else if(keyword != null) {
            totalC =searchService.countKeywordPostList(keyword);
            model.addAttribute("posts", this.searchPost(session, keyword, 1, paging));
            model.addAttribute("keyword", keyword);
        }
        // 둘 다 없으면
        else {
            totalC =searchService.countCategoryPostList(category);
            model.addAttribute("posts", searchService.findByCategoryPost("1", 1, paging));
        }
        Integer unreadChatCount = chatRoomService.getUnreadCount(userId);
		
		model.addAttribute("unreadChatCount", unreadChatCount);
        
        model.addAttribute("totalC", totalC);
        
        int alrim = notiService.getNotiCountById(userId);
		List<NotiVO> alrim2 = notiService.getNotiListById(userId);
		model.addAttribute("alrim", alrim);
		model.addAttribute("alrim2", alrim2);

        return "search/postList";
    }

    @RequestMapping("/morePostList")
    public String getAllRV(@RequestParam(name = "curPage", required = false) Integer curPage,
                           HttpSession session, String category, String keyword,  Model model) {
        if  (category != null) {
            //int totalC =searchService.countCategoryPostList(category);
        	//int totalP = (int) Math.ceil((double) totalC / paging);
            int paging = 8;

            int test = 1;
            if(curPage != null) {
                test = curPage;
            }
            int end = test * paging;
            int begin = end - paging + 1;

            List<PostVO> postRV = searchService.findByCategoryPost(category, begin, end);
            model.addAttribute("list",postRV);
        }

        else if (keyword != null) {
                //int totalC =searchService.countKeywordPostList(keyword);
        	//int totalP = (int) Math.ceil((double) totalC / paging);
                int paging = 8;

                int test = 1;
                if(curPage != null) {
                    test = curPage;
                }
                int end = test * paging;
                int begin = end - paging + 1;

                List<PostVO> postRV = this.searchPost(session, keyword, begin, end);
                model.addAttribute("list",postRV);
        }

        return "/main/all_Ajax";

    }
    
    private List<PostVO> searchPost(HttpSession session, String keyword, int begin, int end) {
        String userId = (String)session.getAttribute("userId");
    	SearchVO vo = new SearchVO();
        vo.setUserId(userId);
        vo.setSearchKeyword(keyword.trim());
        if(session != null && userId != null)
        	searchService.insertKeyword(vo);
        
        return searchService.selectListPost(vo.getSearchKeyword(), begin, end);
    }
    
    @GetMapping("/deleteAllKeyword")
    @ResponseBody
    public void deleteAllKeyword(HttpSession session){
        String userId = (String) session.getAttribute("userId");
        if (userId == null) {
            userId = "admin";
        }
        searchService.deleteAllKeyword(userId);
    }
}
