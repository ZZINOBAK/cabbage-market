<%@ page contentType="text/html; charset=UTF-8"
		 pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>배추마켓</title>
	<link rel="stylesheet" href="/resources/css/search/body.css" />
	<link rel="stylesheet" href="/resources/css/main/showAll.css" />
	<link rel="stylesheet" href="/resources/css/main/main_included_jiwon.css" />
	<link rel="stylesheet" href="/resources/css/main/main_haeun.css" />
	<link rel="icon" href="/resources/pic/img/baechu.png" />


	<style>
		.search-input {
			display: inline-block; /* inline-block으로 설정 */
			width: 300px;
			box-sizing: border-box;
			padding: 10px;
			margin: 0;
		}

		#clearSearchInput {
			position: absolute; /* 또는 position: relative; 또는 다른 위치 지정 속성 사용 */
			right: 36px; /* 원하는 위치로 조절 */
			top: 45%; /* 세로 중앙 정렬을 위한 조절 */
			transform: translateY(-50%); /* 세로 중앙 정렬을 위한 조절 */
			cursor: pointer;
			display: none; /* 초기에는 숨김 상태로 설정 */
			font-size: x-large;
		}

		#clearSearchInput:hover {
			color: red; /* x에 호버될 때 색상 변경 등 추가 스타일 적용 가능 */
		}
		nav {
			position: relative;
			z-index: 1; /* #searchResults 위로 올라오도록 설정 */
		}
		#searchResults {
			position: absolute;
			top: 100%; /* 아래에 표시 */
			z-index: 2;
			display: none;
			width: 400px;
			max-height: 500px; /* 레이어의 최대 높이 설정 */
			overflow-y: auto; /* 넘치는 경우 스크롤 표시 */
			border: 1px solid #ccc;
			border-radius: 4px;
			background-color: #fff;
			right: 50px;
		}

		#searchResults li {
			padding: 8px;
			border-bottom: 1px solid #ccc;
			cursor: pointer;
		}

		#searchResults li:last-child {
			border-bottom: none;
		}
		li {
			display: flex;
			justify-content: space-between;
			align-items: center;
		}

		/*button {*/
		/*	margin-left: 5px; !* 삭제 버튼과 텍스트 사이의 간격 조절 *!*/
		/*}*/



		.input-group #input-group-text {
			margin-left: 5px; /* 입력 상자와 '×' 사이에 간격 추가 */
			cursor: pointer;
			height: 38px;
		}

		/* 검색 결과 리스트 스타일 */
		.list-group {
			position: absolute;
			top: 100%; /* 검색창 아래에 위치 */
			left: 0;
			z-index: 1000; /* 다른 요소들 위에 표시 */
			width: 100%; /* 가로 폭을 100%로 설정하거나 필요에 따라 조절하세요 */
			margin: 0;
			padding: 0;
			display: none; /* 초기에는 숨겨둠 */
			border: 1px solid #ccc;
			background-color: #fff;
		}
		.rolling {margin:0;padding:0;list-style:none;overflow:hidden;height:30px;}
		.rolling li {margin:0px;padding:0px;height:30px;}


		.card-top {
			width: calc(25% - 20px); /* 20px은 마진 등을 고려한 여백 값으로 조절하세요 */
			margin: 10px; /* 여백 조절 */
			box-sizing: border-box; /* 상자 크기를 여백과 패딩을 포함하여 계산하도록 설정 */
		}

		#result .result-container .empty-result {
			padding: 34px 0 24px 0;
			text-align: center;
		}
	</style>
</head>
<body class="hoian-kr">
<%--<jsp:include page="/WEB-INF/views/header.jsp"/>--%>

<noscript>
	<img height="1" width="1" style="display:none" alt="facebook" src="https://www.facebook.com/tr?id=992961397411651&ev=PageView&noscript=1" />
</noscript>

<!-- header -->
<%@ include file="/WEB-INF/views/main/inc/header.jsp" %>

<section id="content" data-keyword="${keyword }">
	<h1 class="head-title" id="hot-articles-head-title">
		<!--카테고리 선택하면 그걸로 출력되게 수정  -->
	</h1>
	<section class="cards-wrap" id="searchKeyword">
		<c:choose>
			<c:when test="${!empty posts}">
				<c:forEach var="categoryPost" items="${posts}">
					<article class="card-top ">
					<a class="card-link " data-event-label="675477198"
						href="/post/getPost/${categoryPost.postId }">
						<div class="card-photo ">
							<img alt="${categoryPost.fileName }"
								src="/resources/pic/postPic/${categoryPost.fileName }" onerror="this.onerror=null; this.src='/resources/pic/img/cabbage_icon.png'">
						</div>
						<div class="card-desc">
							<h2 class="card-title">${categoryPost.postTitle}</h2>
							<div class="card-price "><fmt:formatNumber type="number" value="${categoryPost.postPrice }" />원</div>
							<div class="card-region-name">${categoryPost.userNickname }</div>
							<div class="card-counts">
								<span> 
									<img width="15" height="15" src="https://img.icons8.com/material-outlined/100/like--v1.png" alt="like--v1"/>
									${categoryPost.postWishCount }
								</span> ∙ 
								<span> 
									<img width="15" height="15" src="https://img.icons8.com/ios-filled/50/chat.png" alt="chat"/>
									${categoryPost.chatRoomCount }
							 	</span>
							 	<span>
							 	<c:if test="${categoryPost.postStatus == 'RESERVE' }">
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
			</c:when>
			<c:otherwise>
				<article style="width: 100%;">
					<div id="NoSearchKeyword" style="display: flex; align-items: center; justify-content: center;">
						<section class="empty-result" style="display: flex; flex-direction: row; align-items: center;">
							<img alt="No Results Image" src="https://ogq-sticker-global-cdn-z01.afreecatv.com/sticker/1799d283caee3b9/6.png">
							<p class="empty-result-message" style="font-size: 24px;">
								<span class="o-keyword">"<%out.print(request.getParameter("keyword"));%>"</span>에 대한 결과가 없어요.
							</p>
						</section>
					</div>
				</article>
			</c:otherwise>
		</c:choose>
	</section>
</section>

	<!-- footer -->
	<%@ include file="/WEB-INF/views/main/inc/footer.jsp" %>

<input type="hidden" value="${totalC }" id="totalCField">
<div id="fb-root" class=" fb_reset">
	<div style="position: absolute; top: -10000px; width: 0px; height: 0px;">
		<div>
		</div>
	</div>
</div>

<script src="https://code.jquery.com/jquery-3.4.1.js"></script>
<script src="/resources/js/search/search.js"></script>

</body>
</html>