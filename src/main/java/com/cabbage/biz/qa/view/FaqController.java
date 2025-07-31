package com.cabbage.biz.qa.view;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cabbage.biz.qa.faq.FaqService;
import com.cabbage.biz.qa.faq.FaqVO;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@Controller
@RequestMapping("/faq")
public class FaqController {
	@Autowired
	private final FaqService faqService;
	
	
	
	@ModelAttribute("conditionMap")
 	public Map<String, String> searchConditionMap() {
 		Map<String, String> conditionMap = new HashMap<String, String>();
 		conditionMap.put("제목", "FAQ_TITLE");
 		conditionMap.put("내용", "FAQ_CONTENT");
 		return conditionMap;
 	}
	
	
	
	
	// FAQ 리스트 가져오기
    @RequestMapping("/faq")
    public String showFaqPage(Model model, HttpSession session) {
    	
        List<FaqVO> faqList = faqService.getFaqList(); 
        model.addAttribute("faqList", faqList);
        
        // 세션에서 로그인된 사용자 정보 읽어오기
        String loggedInUser = (String) session.getAttribute("userId");
        model.addAttribute("loggedInUser", loggedInUser);
        
        
        return "qa/qa";
    }
    
    
    // FAQ 리스트에서 검색하기 faqKeywordList
    @RequestMapping("/faqKeywordList")
    public String getFaqKeywordList(@RequestParam(name = "searchCondition", required = false) String searchCondition,
									@RequestParam(name = "searchKeyword", required = false) String searchKeyword,
									@ModelAttribute("conditionMap") Map<String, String> conditionMap,
									Model model, HttpSession session) {
    	
        // 세션에서 로그인된 사용자 정보 읽어오기
    	String loggedInUser = (String) session.getAttribute("userId");
        model.addAttribute("loggedInUser", loggedInUser);
        
    	List<FaqVO> faqList = faqService.getFaqKeywordList(searchCondition, searchKeyword); 
        model.addAttribute("faqList", faqList);
         
        
        return "qa/qa";
    }
    
    
    
	
}
