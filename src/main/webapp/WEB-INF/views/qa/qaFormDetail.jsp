<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link href="https://cdn.jsdelivr.net/npm/@fancyapps/ui/dist/fancybox.css" rel="stylesheet"/>
   <script src="https://cdn.jsdelivr.net/npm/@fancyapps/ui@4.0/dist/fancybox.umd.js"></script>
<link rel="stylesheet" type="text/css" href="/resources/css/qa/qaFormDetail.css">
<title>1:1 문의내역</title>
<link rel="icon" href="/resources/pic/img/baechu.png" />
</head>
<body>
	<div id="container">

		<div title="" class="content-container"></div>
			<h1 title="" class="">
					<svg title="" width="1.8rem" height="3rem" viewBox="0 0 474 801"
					fill="none" xmlns="http://www.w3.org/2000/svg">
					<path d="" fill="#FF6F0F"></path> 
					<img src="/resources/pic/img/baechu.png" width="45" height="45" /></svg>
					문의내역
			</h1>
		<br>
		
		<div id="document" class="idx-type">
			<div class="panel">
				<div id="container" class="bg-color">
					<!-- 헤더 영역 -->
					<form action="getQaFormDetail" method="get">
					<header class="fb__communityDetail__header">
						<c:set var="qaFormDetail" value="${qaFormDetail}" />
						
						<!-- 타이틀 -->
						<p>[${qaFormDetail.qaCatName }]</p>
						<p class="header__title js__communityDetail__title">
							${qaFormDetail.qaTitle }
						</p>
	
						<!-- 글 정보 -->
						<div class="header__info">
						</div>
					</header>
					
					<!-- 내용 영역 -->
					<div class="fb__communityDetail__contents">
					<p class="contents__text default">
				        ${qaFormDetail.qaContent }
					</p>
					</div>
					
					
					<!-- 이미지 영역 -->
					<div class="fb__communityDetail__comments js__communityDetail__comments">
					<p>이미지를 확대해보세요 🔎 </p>
					<c:choose>
					    <c:when test="${not empty qaFormDetail.fileName}">
					        <p>
					            <a href="/resources/pic/qaPic/${qaFormDetail.fileName}" data-fancybox="gallery">
					                <img alt="" src="/resources/pic/qaPic/${qaFormDetail.fileName}" width="50px" height="50px">
					            </a>
					        </p>
					    </c:when>
					    <c:otherwise>
					        <p>첨부된 이미지가 없습니다.</p>
					    </c:otherwise>
					</c:choose>
					

				

					<!-- 관리자 댓글 표시 -->
					<div class="commentContainerWrapper">
						<img src="/resources/pic/img/adminPic.png" width="70" height="70" />
					    <c:choose>
					        <c:when test="${not empty qaFormDetail.qaComment}">
					            <p class="commentContainer">
					                ${qaFormDetail.qaComment }
					            </p>
					        </c:when>
					        <c:otherwise>
					            <p class="commentContainer">
					                답변은 조금만 기다려주세요!
					            </p>
					        </c:otherwise>
					    </c:choose>
					</div>
					<p>
					    <a href="qaFormList" class="button-link">목록</a>
					    <a href="deleteQaFormDetail?qaId=${qaFormDetail.qaId }" class="button-link">삭제</a>	
					</p>
			</div>
		</div>
	</div>
<script>
	
	function clearCommentForm() {
        document.querySelector('textarea[name="qaComment"]').value = ''; // 댓글 입력란 초기화
    }
</script>
    
    
    
</script>	
</body>
</html>