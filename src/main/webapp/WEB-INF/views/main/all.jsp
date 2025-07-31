<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>배추마켓</title>
<script src="https://code.jquery.com/jquery-3.4.1.js"></script>
<link rel="icon" href="/resources/pic/img/baechu.png" />
<link rel="stylesheet" href="/resources/css/main/showAll.css" />
<link rel="stylesheet" href="/resources/css/main/main_included_jiwon.css" />
<link rel="stylesheet" href="/resources/css/main/main_haeun.css" />

</head>
<body class="hoian-kr">
	<noscript>
		<img height="1" width="1" style="display:none" alt="facebook" src="https://www.facebook.com/tr?id=992961397411651&ev=PageView&noscript=1" />
	</noscript>
	
	<!-- header -->
	<%@ include file="/WEB-INF/views/main/inc/header.jsp" %>
	
	<section id="content">
  		<h1 class="head-title" id="hot-articles-head-title">
  			<c:if test="${type == 'RV' }">
  			<img src="/resources/pic/img/hot_icon.gif" width="40px;" height="aute;" style="vertical-align:sub;" alt="인기" data-type="${type }">
	        &nbsp;지금 인기 있어요!&nbsp;
	        <img src="/resources/pic/img/hot_icon.gif" width="40px;" height="aute;" style="vertical-align:sub;" alt="인기" >
  			</c:if>
  			<c:if test="${type == 'RI' }">
  			<img src="/resources/pic/img/rcmd_icon.png" width="40px;" height="aute;" style="vertical-align:sub;" alt="인기" data-type="${type }">
	        &nbsp;이 물건 필요하세요?&nbsp;
	        <img src="/resources/pic/img/rcmd_icon.png" width="40px;" height="aute;" style="vertical-align:sub;" alt="인기">
	        </c:if>
	        <c:if test="${type == 'N' }">
  			<img src="/resources/pic/img/new_icon.gif" width="40px;" height="aute;" style="vertical-align:sub;" alt="인기" data-type="${type }">
	        &nbsp;방금 올라 왔어요!&nbsp;
	        <img src="/resources/pic/img/new_icon.gif" width="40px;" height="aute;" style="vertical-align:sub;" alt="인기">
	        </c:if>
  		</h1>
		<section class="cards-wrap">
			<c:forEach var="post" items="${list }">
	    	<article class="card-top ">
	  			<a class="card-link " data-event-label="675477198" href="/post/getPost/${post.postId }">
	    			<div class="card-photo ">
	        			<img src="/resources/pic/postPic/${post.fileName }" onerror="this.onerror=null; this.src='/resources/pic/img/cabbage_icon.png'">
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
		</section>
	</section>
	
	<!-- footer -->
	<%@ include file="/WEB-INF/views/main/inc/footer.jsp" %>

	<div id="fb-root" class=" fb_reset">
		<div style="position: absolute; top: -10000px; width: 0px; height: 0px;">
			<div>
			</div>
		</div>
	</div>
	
<script>
	var curPage = 1; //페이지 초기값
	var isLoading = false; //현재 페이지가 로딩중인지 여부
	 
	$(window).on("scroll", function() {
		var scrollTop = $(window).scrollTop(); // 위로 스크롤된 길이
		var windowsHeight = $(window).height(); //웹브라우저의 창의 높이
		var documentHeight = $(document).height(); // 문서 전체의 높이
		var isBottom = scrollTop + windowsHeight + 10 >= documentHeight; //바닥에 갔는지 여부
		
		if (isBottom) {
			//만일 현재 마지막 페이지라면
			if (curPage >= ${totalP}) {
				return false; //함수종료
			} else {
				isLoading = true; //위에서 종료되지 않으면 로딩상태를 true로 변경
				$("#load").show(); //로딩바 표시
				curPage++; //현재페이지 1증가
				getList(curPage); //추가로 받을 리스트 ajax처리
			}
		}
	});
	 
	// 리스트 불러오기 함수
	function getList(curPage) {
		let type = $("#hot-articles-head-title > img").eq(0).attr("data-type");
		$.ajax({
			type: "POST",
			url : "All",
			data : { "curPage" : curPage, "type" : type},
			success:function(data){
	        	//서버에서 전송된 데이터를 list-wrap에 추가하기
				$(".cards-wrap").append(data); 
				$("#load").hide(); //로딩바 숨기기
				isLoading = false; // 로딩여부를 false로 변경
			}
		});
	}
	
</script>
	
</body>
</html>