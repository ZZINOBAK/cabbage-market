<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>배추마켓</title>
<script src="https://code.jquery.com/jquery-3.4.1.js"></script>
<link rel="icon" href="/resources/pic/img/baechu.png" />
<link rel="stylesheet" href="/resources/css/main/main.css" />
<link rel="stylesheet" href="/resources/css/main/main_included_jiwon.css" />
<link rel="stylesheet" href="/resources/css/main/main_haeun.css" />

<script>
    function All(type) {
    	// 이동하고자 하는 URL 설정
        var url = '/All?type=' + type; // 실제로는 적절한 URL로 변경해야 합니다.

        // JavaScript를 사용하여 리다이렉션 수행
        window.location.href = url;
    }
</script>
</head>
<body class="hoian-kr">
	<noscript>
		<img height="1" width="1" style="display: none" alt="facebook"
			src="https://www.facebook.com/tr?id=992961397411651&ev=PageView&noscript=1" />
	</noscript>

	<!-- header -->
	<%@ include file="/WEB-INF/views/main/inc/header.jsp" %>
	
	<main>
		<section class="fleamarket-cover" style="margin-bottom:35px;">
			<div class="cover-content">
				<h1 class="cover-title">
					믿을만한<br>이웃 간 중고거래
				</h1>
				<span class="cover-description">동네 주민들과 가깝고 따뜻한 거래를<br>지금
					경험해보세요.
				</span>
				<div class="cover-image">
					<span class="fleamarket-cover-image"> 
					<img
						
						alt="믿을만한 이웃 간 중고거래"
						src="/resources/pic/img/haeuncheonjae.png">
					</span> <span class="fleamarket-cover-image-mobile"> <img
						srcset="https://d1unjqcospf8gs.cloudfront.net/assets/home/main/mobile/3x/fleamarket-1caf5c24c82acb20bd86fa8c018987be7812a1f0ce0858dcf175568bfc3cf87d.webp 3x"
						alt="믿을만한 이웃 간 중고거래"
						src="/resources/pic/img/haeuncheonjae.png">
					</span>
				</div>
			</div>
		</section>
		<c:if test="${not empty userId and fn:length(recomendPostById) > 0}">
		<section class="fleamarket-article-list">
			<!-- <h1 class="text-center article-list-title">중고거래 인기매물</h1> -->
			<div class="cards-wrap" style="width:1024px;">
			<div class="canada-haeun1" style="width:100%;">
				<div class="canada-haeun2" style="margin-left:7px;">
					<img src="/resources/pic/img/rcmd_icon.png" width="30px;" height="aute;" style="vertical-align:bottom;" alt="인기">
					이 물건 필요하세요?
				</div>
				<a class="canada-haeun3" href="/All?type=RI">
					<span class="canada-haeun4" style="color:#609966;">
					모두보기
					</span>
					<span class="canada-haeun5">
						<span class="canada-haeun6">
						 	<img src="/resources/pic/img/right_arrow.png" alt="오늘쪽 화살표">
						</span>
					</span>
				</a> 
			</div>
			</div>
			<div class="cards-wrap">
				<c:forEach var="recomendPostById" items="${recomendPostById }">
				<article class="card-top ">
					<a class="card-link " data-event-label="675477198"
						href="/post/getPost/${recomendPostById.postId }">
						<div class="card-photo ">
							<img alt="${recomendPostById.fileName }"
								src="/resources/pic/postPic/${recomendPostById.fileName }" onerror="this.onerror=null; this.src='/resources/pic/img/cabbage_icon.png'">
						</div>
						<div class="card-desc">
							<h2 class="card-title">${recomendPostById.postTitle}</h2>
							<div class="card-price "><fmt:formatNumber type="number" value="${recomendPostById.postPrice }" />원</div>
							<div class="card-region-name">${recomendPostById.userNickname }</div>
							<div class="card-counts">
								<span> 
									<img width="15" height="15" src="https://img.icons8.com/material-outlined/100/like--v1.png" alt="like--v1"/>
									${recomendPostById.postWishCount }
								</span> ∙ 
								<span> 
									<img width="15" height="15" src="https://img.icons8.com/ios-filled/50/chat.png" alt="chat"/>
									${recomendPostById.chatRoomCount }
							 	</span>
							 	<span>
								 	<c:if test="${recomendPostById.postStatus == 'RESERVE' }">
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
			</div>
		</section>
		</c:if>
		
		<section class="fleamarket-article-list">
			<!-- <h1 class="text-center article-list-title">중고거래 인기매물</h1> -->
			<div class="cards-wrap" style="width:1024px;">
			<div class="canada-haeun1" style="width:100%;">
				<div class="canada-haeun2" style="margin-left:7px;">
					<img src="/resources/pic/img/new_icon.gif" width="30px;" height="aute;" style="vertical-align:bottom;" alt="인기">
					방금 올라 왔어요!
				</div>
				<a class="canada-haeun3" href="/All?type=N">
					<span class="canada-haeun4" style="color:#609966;">
					모두보기
					</span>
					<span class="canada-haeun5">
						<span class="canada-haeun6">
						 	<img src="/resources/pic/img/right_arrow.png" alt="오늘쪽 화살표">
						</span>
					</span>
				</a> 
			</div>
			</div>
			<div class="cards-wrap">
				<c:forEach var="somethingNew" items="${somethingNew }">
				<article class="card-top ">
					<a class="card-link " data-event-label="675477198"
						href="/post/getPost/${somethingNew.postId }">
						<div class="card-photo ">
							<img alt="${somethingNew.fileName }"
								src="/resources/pic/postPic/${somethingNew.fileName }" onerror="this.onerror=null; this.src='/resources/pic/img/cabbage_icon.png'">
						</div>
						<div class="card-desc">
							<h2 class="card-title">
								${somethingNew.postTitle}
							</h2>
							<div class="card-price ">
								<fmt:formatNumber type="number" value="${somethingNew.postPrice }" />원
							</div>
							<div class="card-region-name">
								${somethingNew.userNickname }
							</div>
							<div class="card-counts">
								<span> 
									<img width="15" height="15" src="https://img.icons8.com/material-outlined/100/like--v1.png" alt="like--v1"/>
									${somethingNew.postWishCount }
								</span> ∙ 
								<span> 
									<img width="15" height="15" src="https://img.icons8.com/ios-filled/50/chat.png" alt="chat"/>
									${somethingNew.chatRoomCount }
							 	</span>
							 	<span>
								 	<c:if test="${somethingNew.postStatus == 'RESERVE' }">
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
			</div>
		</section>
		
		<section class="fleamarket-article-list">
			<!-- <h1 class="text-center article-list-title">중고거래 인기매물</h1> -->
			<div class="cards-wrap" style="width:1024px;">
			<div class="canada-haeun1" style="width:100%;">
				<div class="canada-haeun2" style="margin-left:7px;">
					<img src="/resources/pic/img/hot_icon.gif" width="30px;" height="aute;" style="vertical-align:bottom;" alt="인기">
					지금 인기 있어요!
				</div>
				<a class="canada-haeun3" href="/All?type=RV">
					<span class="canada-haeun4" style="color:#609966;">
					모두보기
					</span>
					<span class="canada-haeun5">
						<span class="canada-haeun6">
						 	<img src="/resources/pic/img/right_arrow.png" alt="오늘쪽 화살표">
						</span>
					</span>
				</a> 
			</div>
			</div>
			<div class="cards-wrap">
				<c:forEach var="recomendPostByVeiwCount" items="${recomendPostByVeiwCount }">
				<article class="card-top ">
					<a class="card-link " data-event-label="675477198"
						href="/post/getPost/${recomendPostByVeiwCount.postId }">
						<div class="card-photo ">
							<img alt="${recomendPostByVeiwCount.fileName }"
								src="/resources/pic/postPic/${recomendPostByVeiwCount.fileName }" onerror="this.onerror=null; this.src='/resources/pic/img/cabbage_icon.png'">
						</div>
						<div class="card-desc">
							<h2 class="card-title">
								${recomendPostByVeiwCount.postTitle}
							</h2>
							<div class="card-price ">
								<fmt:formatNumber type="number" value="${recomendPostByVeiwCount.postPrice }" />원
							</div>
							<div class="card-region-name">
								${recomendPostByVeiwCount.userNickname }
							</div>
							<div class="card-counts">
								<span> 
									<img width="15" height="15" src="https://img.icons8.com/material-outlined/100/like--v1.png" alt="like--v1"/>
									${recomendPostByVeiwCount.postWishCount }
								</span> ∙ 
								<span> 
									<img width="15" height="15" src="https://img.icons8.com/ios-filled/50/chat.png" alt="chat"/>
									${recomendPostByVeiwCount.chatRoomCount } 
							 	</span>
							 	<span>
								 	<c:if test="${recomendPostByVeiwCount.postStatus == 'RESERVE' }">
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
			</div>
		</section>
	</main>

	<!-- footer -->
	<%@ include file="/WEB-INF/views/main/inc/footer.jsp" %>

	<div id="fb-root" class=" fb_reset">
		<div
			style="position: absolute; top: -10000px; width: 0px; height: 0px;">
			<div></div>
		</div>
	</div>
	

<script>
// 페이지 로딩 후 실행할 함수
document.addEventListener("DOMContentLoaded", function () {
    var alrimCount = document.getElementById('alrimCount');
    console.log(alrimCount.innerText);
    if (alrimCount && parseInt(alrimCount.innerText.trim(), 10) !== 0) { 
        alrimCount.style.display = 'block';
    }
});
</script>


</body>
</html>
