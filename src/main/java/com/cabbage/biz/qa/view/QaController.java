package com.cabbage.biz.qa.view;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;

import com.cabbage.biz.common.CommonData;
import com.cabbage.biz.noti.noti.NotiService;
import com.cabbage.biz.qa.qa.QaService;
import com.cabbage.biz.qa.qa.QaVO;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/qa")
public class QaController {

	private final QaService qaService;
	private final NotiService notiService;
	


	// 메소드 선언부에 선언된 @ModelAttribute 는 리턴된 데이터를 VIEW 에 전달
 	// @ModelAttribute 선언된 메소드는 @RequestMapping 메소드보다 먼저 실행
 	// 뷰에 전달될 때 설정된 명칭(예: conditionMap)
 	@ModelAttribute("conditionMap")
 	public Map<String, String> searchConditionMap() {
 		Map<String, String> conditionMap = new HashMap<String, String>();
 		conditionMap.put("제목", "QA_TITLE");
 		conditionMap.put("내용", "QA_CONTENT");
 		return conditionMap;
 	}
    
	
	
	@RequestMapping("/qa")
	public String getQa(QaVO vo, Model model, HttpSession session) {

		QaVO qa = qaService.getQa(vo);
		
		//Model 객체 사용해서 View로 데이터 전달
		model.addAttribute("getQa", qa); 
		
		
		// 세션에서 로그인된 사용자 정보 읽어오기
		String loggedInUser = (String) session.getAttribute("userId");
        model.addAttribute("loggedInUser", loggedInUser);
		
		return "/qa/qaFormList";
	}

	
	// 자주묻는질문 페이지 --> "문의하기" 버튼을 눌렀을 때 메소드 (qaCategory.jsp로 이동)
    @RequestMapping("/qaCategory")
    public String showQaCategoryPage(Model model, HttpSession session) {
        List<QaVO> qaCategoryList = qaService.getQaCategoryList(); // QaService에 getQaCategoryList 메서드를 추가
        model.addAttribute("qaCategoryList", qaCategoryList);
        
        
        // 세션에서 로그인된 사용자 정보 읽어오기
        String loggedInUser = (String) session.getAttribute("userId");
        model.addAttribute("loggedInUser", loggedInUser);
        
        return "/qa/qaCategory";
    }
    
    
    // 카테고리를 qaForm.jsp로 보내기!
    @RequestMapping("/qaForm")
    public String showQaFormPage(@RequestParam(name = "categoryId", required = false) String categoryId, 
    								Model model, HttpSession session) {
    	
    	// 세션에서 로그인된 사용자 정보 읽어오기
    	String loggedInUser = (String) session.getAttribute("userId");
        model.addAttribute("loggedInUser", loggedInUser);
        
        // categoryId에 따라서 qacatcontent를 가져와서 세션에 저장하고 모델에 추가
        String qaCatContent = qaService.getQaCatContentByCategoryId(categoryId);
        session.setAttribute("qaCatContent", qaCatContent);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("qaCatContent", qaCatContent);
       
        
    	return "/qa/qaForm";
    }
   
    
    // 1:1 문의 부분 --------------------------------------------------------------------
    // 1:1 문의하기 (qaForm.jsp -> qaFormList.jsp로 문의글 전달)
    @PostMapping("/qaFormInsert")
    public String insertQa(QaVO vo, Model model, HttpSession session) throws IllegalStateException, IOException {

        // 사용자 ID를 세션에서 가져와서 해당 사용자의 1:1 문의 목록을 가져옴
    	String loggedInUser = (String) session.getAttribute("userId");
        model.addAttribute("loggedInUser", loggedInUser);
        
        vo.setUserId(loggedInUser);
        
        
        
        // 파일 업로드
        MultipartFile uploadFile = vo.getUploadFile();
		
		if (uploadFile == null) {
			//System.out.println("::: uploadFile 파라미터가 전달되지 않은 경우");
		} else if (uploadFile.isEmpty()) {
			//System.out.println("::: 전달받은 파일 데이터가 없는 경우");
		} else { //업로드 파일이 존재하는 경우
			
		    
		    // 확장자명 구하기
		    String fileExtension = getFileExtension(uploadFile);
		    
		    // 새로운 파일명 생성
		    String newFileName = "QA_" + UUID.randomUUID() + "." + fileExtension;
		    
			// 물리적 파일 복사
			//uploadFile.transferTo(new File("C:/MyStudy/temp/" + savedFilename));
//			String destPathFile = "/Users/juyeongpark/Downloads/" + fileId;
			String destPathFile = CommonData.FILE_UPLOAD_ROOT + "/qaPic/" + newFileName;
			
			uploadFile.transferTo(new File(destPathFile));
			
//			// 파일명을 fileId로 설정
//		    vo.setFileId(fileId);
			vo.setFileName(newFileName);
		    
		}
		
        // DB에 저장
        qaService.insertQa(vo);
        
        // 1:1 문의 목록 가져옴
        List<QaVO> qaList = qaService.getQaList(loggedInUser);
        
        // 모델에 추가
        model.addAttribute("qaList", qaList);
		
		return "redirect:/qa/qaFormList";
    }
    
    private String getFileExtension(MultipartFile file) {
        String originalFileName = file.getOriginalFilename();
        if (originalFileName != null && originalFileName.contains(".")) {
            return originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
        }
        return null;
    }

    
	// 1:1 문의내역 리스트 보기
    @RequestMapping("/qaFormList")
    public String getQaList(@RequestParam(name = "searchCondition", required = false) String searchCondition,
    						@RequestParam(name = "searchKeyword", required = false) String searchKeyword,
    						@ModelAttribute("conditionMap") Map<String, String> conditionMap,
            				Model model, HttpSession session) {
    	
    	// 세션에서 로그인된 사용자 정보 읽어오기
        String loggedInUser = (String) session.getAttribute("userId");
        model.addAttribute("loggedInUser", loggedInUser);
        
        // 검색 조건 맵을 모델에 추가
        model.addAttribute("conditionMap", searchConditionMap());
        model.addAttribute("searchCondition", searchCondition);
        model.addAttribute("searchKeyword", searchKeyword);

        List<QaVO> qaList = qaService.getQaList(loggedInUser);
    	
    	model.addAttribute("qaList", qaList);
    	
    	return "/qa/qaFormList";
    }
    
    
    
    // KeyWordList 검색했을 때 목록 출력
    @RequestMapping("/qaFormKeywordList")
    public String getQaKeywordList(@RequestParam(name = "searchCondition", required = false) String searchCondition,
    						@RequestParam(name = "searchKeyword", required = false) String searchKeyword,
    						@ModelAttribute("conditionMap") Map<String, String> conditionMap,
            				Model model, HttpSession session) {
    	
    	// 세션에서 로그인된 사용자 정보 읽어오기
    	String loggedInUser = (String) session.getAttribute("userId");
        model.addAttribute("loggedInUser", loggedInUser);
        
        
        // 검색 조건 맵을 모델에 추가
        model.addAttribute("conditionMap", searchConditionMap());
        model.addAttribute("searchCondition", searchCondition);
        model.addAttribute("searchKeyword", searchKeyword);

        List<QaVO> qaList = qaService.getQaKeywordList(searchCondition, searchKeyword);
    	
    	model.addAttribute("qaList", qaList);
    	
    	return "/qa/qaFormList";
    }
   
    
    
    // 1:1 문의내역 내용 상세 보기
    @RequestMapping("/qaFormDetail")
    public String getQaFormDetail(QaVO vo, Model model, HttpSession session) {
    	
    	// 세션에서 로그인된 사용자 정보 읽어오기
    	String loggedInUser = (String) session.getAttribute("userId");
        model.addAttribute("loggedInUser", loggedInUser);
    	
    	
    	QaVO qaFormDetail = qaService.getQaFormDetail(vo);
    	
    	//Model 객체 사용해서 View로 데이터 전달
    	model.addAttribute("qaFormDetail", qaFormDetail);
    	

    	return "/qa/qaFormDetail";
    }
    
    
    
    // 1:1 문의내역 삭제 DELETE
    @RequestMapping("/deleteQaFormDetail")
	public String deleteQaFormDetail(QaVO vo, SessionStatus sessionStatus) {
		
		qaService.deleteQaFormDetail(vo);
		sessionStatus.setComplete();
		
		return "redirect:/qa/qaFormList";
	}
    
    
    // Admin 관리자모드 --------------------------------------------------------------------
    // Admin 관리자 모드에서 문의목록 LIST 조회
    @RequestMapping("/adminQaFormList")
    public String getAdminQaList(Model model, HttpSession session) {
    	
    	// 세션에서 로그인된 사용자 정보 읽어오기
    	String loggedInUser = (String) session.getAttribute("userId");
        model.addAttribute("loggedInUser", loggedInUser);
    	
    	List<QaVO> adminQaList = qaService.getAdminQaList();
    	
    	
    	model.addAttribute("adminQaList", adminQaList);
    	
    	return "/qa/adminFormList";
    }
    
    // Admin 관리자 모드에서 문의내용 CONTENT 조회
    @RequestMapping("/adminFormDetail")
    public String getAdminQaFormDetail(QaVO vo, Model model, HttpSession session) {
    	
    	// 세션에서 로그인된 사용자 정보 읽어오기
    	String loggedInUser = (String) session.getAttribute("userId");
        model.addAttribute("loggedInUser", loggedInUser);
    	
    	
    	QaVO adminFormDetail = qaService.getAdminQaFormDetail(vo);
    	
    	//Model 객체 사용해서 View로 데이터 전달
    	model.addAttribute("adminFormDetail", adminFormDetail);
    	
    	
    	return "/qa/adminFormDetail";
    }
    
    
    // Admin 관리자 모드에서 댓글 INSERT
    @PostMapping("/adminFormInsert")
    public String insertAdminComment(QaVO vo, Model model, HttpSession session) throws IllegalStateException, IOException {
    	try {
	    	QaVO qa = qaService.getQa(vo);
	    	
	        // 사용자 ID를 세션에서 가져와서 해당 사용자의 1:1 문의 목록을 가져옴
	    	String loggedInUser = (String) session.getAttribute("userId");
	        model.addAttribute("loggedInUser", loggedInUser);
	        
	        qa.setQaComment(vo.getQaComment());
	        
	        
	        // DB에 댓글 저장
	        qaService.insertAdminComment(qa);
	        
	        notiService.afterInsertQaAnwser(qa);
	        
	        return "redirect:/qa/adminFormDetail?qaId=" + vo.getQaId();
	    } catch (Exception e) {
	        e.printStackTrace(); // 예외 발생 시 콘솔에 로그 출력
	        return "redirect:/error"; // 예외 발생 시 에러 페이지로 리다이렉트
	    }
    }
    
    
    
    // Admin 관리자 모드에서 댓글 DELETE
    @RequestMapping("/deleteAdminComment")
	public String deleteAdminComment(QaVO vo, SessionStatus sessionStatus) {
		
		// DB에서 댓글 삭제
		qaService.deleteAdminComment(vo);
		
		sessionStatus.setComplete();
		
		return "redirect:/qa/adminFormList";
	}
    

    
}
	

