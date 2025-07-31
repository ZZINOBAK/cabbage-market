package com.cabbage.biz.noti.view;

import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.cabbage.biz.noti.noti.NotiService;
import com.cabbage.biz.noti.noti.NotiVO;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class NotiController {
	
	private final NotiService notiService;
	
	 
	@GetMapping(value = "/connect", produces = "text/event-stream")  
    public SseEmitter connect(HttpSession session,
    							@RequestHeader(value = "Last-Event-ID", 
    							required = false, defaultValue = "") String lastEventId) {  
        String id = (session.getAttribute("userId")).toString();
        return notiService.subscribe(id, lastEventId);  
	}  
	
	// 추가 //알림 읽으면 unread -> read
	@RequestMapping("/updateNoti")
//	public void updateNoti(NotiVO vo, HttpSession session) { 원래 이거였는데, 저 vo 사용안되는 거 같아서 뺴
	public void updateNoti(HttpSession session) {
        String id = (session.getAttribute("userId")).toString();
        
		notiService.updateNoti(id);
	}
	
	@RequestMapping("/deleteNoti")
	public void deleteNoti(@RequestParam("notiId") Long notiId, NotiVO vo) {
        vo.setNotiId(notiId);
        
		notiService.deleteNoti(vo);
	}
	
	
}
