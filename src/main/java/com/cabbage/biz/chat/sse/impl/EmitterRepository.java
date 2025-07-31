package com.cabbage.biz.chat.sse.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import net.jodah.expiringmap.ExpirationPolicy;
import net.jodah.expiringmap.ExpiringMap;

/*
 * emitter id 는 userId_currentTimeMilis
 * event id 는 userId_currentTimeMilis_eventName
 */

@Component
public class EmitterRepository {
	// SseEmitter 객체들이 저장될 맵
	// expiringmap 라이브러리 이용 (ConcurrentHashMap 기반, 최대사이즈, 데이터 만료 시간 설정 가능) (캐시 대용) (안써도될듯?)
	// <id (userId_currentTimeMillis) , SseEmitter>
	private final Map<String, SseEmitter> emitterMap = new ConcurrentHashMap<String, SseEmitter>();
	
	// 이벤트 데이터 객체들이 저장될 맵 (thread-safe 하기위해 ConcurrentHashMap 사용)
	// 데이터 유실 방지를 위해 최근데 전송된 데이터들을 저장해놓는 공간
	// < userId_currentTime_eventName, Object(이벤트 객체) >
	// 캐시 저장소 공간 확보를 위해 시간제로 저장
	private final Map<String, Object> eventCacheMap = ExpiringMap.builder()
																 .expirationPolicy(ExpirationPolicy.CREATED)
																 .maxSize(10000)
																 .expiration(5, TimeUnit.MINUTES)
																 .build();
	
	
// ====================================== emitterMap 메소드 =============================================
	
	// 새로운 SseEmitter를 emitterMap에 저장한다
	// 그리고 SseEmitter객체 리턴
	public SseEmitter saveEmitter(String id, SseEmitter emitter) {
		emitterMap.put(id, emitter);
		return emitter;
	}
	
	// SseEmitter의 key값(id)을 받아서 emitterMap에서 삭제한다
	public void deleteEmitterById(String id) {
		emitterMap.remove(id);
	}
	
	// userId가 같은 emitter를 모두 찾아서 리턴
	// key(id) 의 앞부분이 userId와 같으면 Set에 추가해서 리턴한다
	public Map<String, SseEmitter> findAllEmitterByUserId(String userId) {
		Map<String, SseEmitter> emitters = new HashMap<String, SseEmitter>();
		
		for(String key : emitterMap.keySet()) {
			if(key.startsWith(userId))
				emitters.put(key, emitterMap.get(key));
		}
		
		
		return emitters;
	}
	
	// ================================= eventCacheMap 메소드 ===========================================
	
	// key와 데이터를 받아서 eventCacheMap에 저장한다
	public void saveEventCache(String key, Object data) {
		eventCacheMap.put(key, data);
	}
	
	// 이벤트 캐시에서 유저아이디로 시작하는 캐시값들 찾는 매소드
	public Map<String, Object> findAllEventCacheStartWithId(String userId) {
		Map<String, Object> eventCaches = new HashMap<String, Object>();
		
		for(String key : eventCacheMap.keySet()) {
			if(key.startsWith(userId))
				eventCaches.put(key, eventCacheMap.get(key));
		}
		
		return eventCaches;
	}
	
	// userId로 시작하는 key를 가진 event를 모두 삭제한다
	public void deleteAllEventStartWithId(String userId) {
		for(String key : eventCacheMap.keySet()) {
			if(key.startsWith(userId))
				eventCacheMap.remove(key);
		}
	}
	
}
