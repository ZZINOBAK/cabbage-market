<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>새로올라왔어요 ajax</title>

<script src="https://code.jquery.com/jquery-3.4.1.js"></script>

</head>
<body class="hoian-kr">
			<c:forEach var="post" items="${list }">
	    	<article class="card-top ">
	  			<a class="card-link " data-event-label="675477198" href="/post/getPost/${post.postId }">
	    			<div class="card-photo ">
	        			<img alt="미니 건조기" src="/resources/pic/postPic/${post.fileName }" onerror="this.onerror=null; this.src='/resources/img/cabbage_icon.png'">
	    			</div>
	   				<div class="card-desc">
	     				<h2 class="card-title">	
							${post.postTitle }
						</h2>
		      			<div class="card-price ">
		        			<fmt:formatNumber type="number" value="${post.postPrice }" />원
		      			</div>
					    <div class="card-region-name">
		        			${post.userNickname }
					    </div>
	      				<div class="card-counts">
					        <span>
					        	<img width="15" height="15" src="https://img.icons8.com/material-outlined/100/like--v1.png" alt="like--v1"/>
					           ${post.postWishCount }
					        </span>
					        ∙
					        <span>
					        	<img width="15" height="15" src="https://img.icons8.com/ios-filled/50/chat.png" alt="chat"/>
					            ${post.chatRoomCount }
					        </span>
					        <span>
							 	<c:if test="${post.postStatus == 'RESERVE' }">
									<span style="
									    float: right;
									    margin-right: 12px;
									    border: 1px solid #000;
									    border-radius: 10px;
									    padding: 0;
									    padding-left: 10px;
									    padding-right: 10px;
									    padding-bottom: 3px;
									    padding-top: 3px;
									    line-height: -8;
									">
									예약중
									</span>
								</c:if>
							</span>
						</div>
			   		</div>
				</a>
			</article>
			</c:forEach>
</body>
</html>