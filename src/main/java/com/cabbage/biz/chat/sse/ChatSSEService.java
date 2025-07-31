package com.cabbage.biz.chat.sse;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.cabbage.biz.chat.user.UserVO;

public interface ChatSSEService {
	
	// Sse 만료시간 (10분) 10L * 60 * 1000;
	public static final Long DEFAULT_TIMEOUT = 10L * 60 * 1000;
	public static final String EVENT_NAME_CONNECTED = "connect";
	public static final String EVENT_NAME_CHAT_MESSAGE = "chatMessage";
	public static final String EVENT_NAME_CHAT_READ = "read";
	
	SseEmitter subscribe(String userId, String lastEventId);
	void sendEvent(UserVO user, Object chatMessage, String eventName);
}
