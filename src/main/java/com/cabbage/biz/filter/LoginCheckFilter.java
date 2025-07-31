package com.cabbage.biz.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.util.PatternMatchUtils;


public class LoginCheckFilter implements Filter{
	// TODO : 추가 필요
	private static final String[] whiteList = 
		{"/", "/All"
			// User
		, "/user/login", "/user/logout", "/user/findAccount", "/user/signUp", "/user/loginIn", "/user/joinUser", "/user/ConfirmId"
		, "/user/ConfirmNick", "/user/ConfirmPhone", "/user/findId", "/user/findPw", "/user/EmailAuth", "/user/EmailAuth2", "/user/info/*", "/user/salesList"
			// Search
		, "/delete-keyword", "/autocomplete", "/recent", "/get_rolling", "/postList", "/morePostList"
			// Post
		, "/post/getPost/*", "/post/getPostList"
			// Resources
		, "/resources/*"
			// Test
		, "/chat/testIndex", "/chat/testRequest"};

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest)request;
		HttpServletResponse httpResponse = (HttpServletResponse)response;
		String requestURI = httpRequest.getRequestURI();
		
		// whiteList 제외하고 다 체크
		if(isLoginCheckPath(requestURI)) {
			HttpSession session = httpRequest.getSession(false);
	        if (session == null || session.getAttribute("userId") == null) {
	          httpResponse.sendRedirect("/user/login");
	          return;
	        }
		}
		chain.doFilter(request, response);
		
	}

	@Override
	public void destroy() {
		
	}
	
	private boolean isLoginCheckPath(String requestURI) {
	    return !PatternMatchUtils.simpleMatch(whiteList, requestURI);
	  }

}
