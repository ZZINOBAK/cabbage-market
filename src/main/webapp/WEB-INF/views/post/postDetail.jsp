<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>배추마켓 - 제품 상세</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
    <link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-slider/10.6.2/css/bootstrap-slider.min.css" integrity="sha256-G3IAYJYIQvZgPksNQDbjvxd/Ca1SfCDFwu2s2lt0oGo=" crossorigin="anonymous" />
    <link rel="stylesheet" type="text/css" href="/resources/css/post/postDetail.css">
	<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
	<script type="text/javascript" src="https://cdn.iamport.kr/js/iamport.payment-1.2.0.js"></script>
	<link rel="icon" href="/resources/pic/img/baechu.png" />
	
	<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/sweetalert2@11.4.10/dist/sweetalert2.min.css">
	<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11.4.10/dist/sweetalert2.min.js"></script>
	
    <link href="https://cdn.jsdelivr.net/npm/@fancyapps/ui/dist/fancybox.css" rel="stylesheet"/>
	<script src="https://cdn.jsdelivr.net/npm/@fancyapps/ui@4.0/dist/fancybox.umd.js"></script>

	<script type="text/javascript">
	 var sellerId = "${post.sellerId}";  // sellerId 값 가져오기
     var userId = "${userId}";  // userId 값 가져오기
     
     var postStatus = '${post.postStatus}'; //게시글 상태
     
     const chatUserJson = '${chatUserJson}';
     if(chatUserJson != '')
     	var chatUser = JSON.parse(chatUserJson);
     else 
    	var chatUser = '';
     var postId = '${post.postId}';
	</script>
	<script src="/resources/js/post/postDetail.js"></script>
</head>
<body>
	<div id="loading-overlay">
         <div id="loading-spinner"></div>
      </div>
	<!-- header -->
	<%@ include file="/WEB-INF/views/main/inc/header.jsp" %>

    <div class="container container-sm container-detail">
    
    
	<div class="picDiv">
			    <c:if test="${not empty postPic}">
			    	<button class="arrow-btnL" onclick="showPrev()">&nbsp;</button>
			    </c:if>
			<div class="pic-container">
			    <c:if test="${empty postPic }">
			    	<h4>게시글에 등록된 사진이 없습니다.</h4>
				</c:if>
			    <c:forEach var="postPic" items="${postPic }">
			    <a href="/resources/pic/postPic/${postPic.fileName }" data-fancybox="gallery">
				   <img class="pic" src="/resources/pic/postPic/${postPic.fileName }" alt="제품 사진" width="90%" height="90%" style="display:none;">
				</a>   
			    </c:forEach>
		    </div>
			    <c:if test="${not empty postPic}">
			    	<button class="arrow-btnR" onclick="showNext()"></button>
				</c:if>
	</div>
	
		<section id="article-profile">
			<a id="article-profile-link" href="/user/info/${post.sellerId }">
				<h3 class="hide">프로필</h3>
				<div class="space-between">
					<div style="display: flex;">
						<div id="article-profile-image">
							<c:choose>
								<c:when test="${not empty user.fileName }">
									<img src="/resources/pic/profilePic/${user.fileName }" width="90px" height="90px">
								</c:when>
								<c:otherwise>
									<img src="/resources/pic/profilePic/profile_default.png" width="90px" height="90px">
								</c:otherwise>
							</c:choose>
						</div>
						<div id="article-profile-left">
							<div id="nickname"><br>  ${user.userNickname }</div>
						</div>
					</div>
					<div id="article-profile-right">
						<dl id="temperature-wrap">
							<dt>매너온도</dt>
							<dd class="text-color-05 ">
								${user.userOndo } <span>°C</span>
							</dd>
						</dl>
					</div>
				</div>
			</a>
		</section>
		
		<h3 class="product-title"><div id="postStatus"></div> ${post.postTitle }</h3>
		<c:set var="formattedPrice" value="${post.postPrice}" />
		<fmt:formatNumber var="formattedPrice" value="${formattedPrice}" type="number" />
        <h4 class="product-price" style="font-weight:bold;"><c:out value="${formattedPrice}" />원</h4> 
		<ul class="navbar-nav ml-auto">
                <li class="nav-item">
                <div class="sellerAction">
                    <a href="/chat/request/${post.postId }" class="btn">채팅하기</a>
                    <c:if test="${userId != null }">
	                     <c:choose>
				            <%-- wishPost가 null이면 찜 목록에 없는 것으로 판단 --%>
				            <c:when test="${not empty wishPost}">
				                <button type="button" class="btn" id="deleteWish"  onclick="deleteWishList('${userId}', '${post.postId}')"><img src="/resources/pic/postPic/wish/wishHeart.png" width="22px" height="22px"></button>
				            </c:when>
				            <c:otherwise>
				                <button type="button" class="btn"  id="addWish" onclick="addWishList('${userId}', '${post.postId}')"><img src="/resources/pic/postPic/wish/defaultHeart.png" width="22px" height="22px"></button>
				            </c:otherwise>
				        </c:choose>
			        </c:if>
			    </div>
			        <hr>
			        <c:if test="${userId == post.sellerId }">
					    <div class="sellerAction2" style="display: inline-block;">
					        <span class="upDel">수정/삭제 ▼ </span>
						        <div class="div_upDel">
						        <a href="/post/updatePostPage?postId=${post.postId }" class="btn">수정하기</a><br>
						        <button class="btn" onclick="deletePost('${post.postId}')">삭제하기</button>
						        </div>
					    </div>
					    <div class="sellerAction2" style="display: inline-block;">
					        <span class="changeStatus">상태변경 ▼ </span>
						        <div class="statusChange">
						        <button class="btn" id="enableButton" onclick="enablePost('${post.postId}')" >판매중</button>
						        <button class="btn" id="reserveButton" onclick="reservePost('${post.postId}')">예약중</button><br>
						        <button class="btn" id="finishButton" onclick="finishPost('${post.postId}')" >거래완료</button><br>
						        </div>
						</div>      
				  </c:if>
                </li>
        </ul>
        <ul class="list-product-information">
            <li class="list-item category">카테고리 <span>${post.postCatName }</span></li>
            <li class="list-item date">상품 등록 일자 <span><time datetime='2019-08-20T08:30:00Z'>${post.postRegdate }</time></span></li>
        </ul>
        <div class="description">
            <pre class="postContent">${post.postContent }</pre>
        </div>

		<p id="article-counts">관심 ${countWish} ∙ 조회 ${post.postViews }</p>

	<!-- 판매자의 다른 상품 -->
	<section id="article-detail-hot-more">
		<h3>판매자 '${user.userNickname }'님의 다른 상품</h3>
		<section class="cards-wrap">
		<c:forEach var="anotherPost" items="${anotherPost }">
			<article class="card ">
				<a class="card-link ga-click" data-event-label="671808084"
					data-event-category="show_article_from"
					data-event-action="hot_region" href="/post/getPost/${anotherPost.postId }">
					<div class="card-photo ">
						<img alt="대표 사진" src="/resources/pic/postPic/${anotherPost.fileName }" onerror="this.src='/resources/pic/postPic/wish/defaultPost.png'" width="100px" height="177px">
					</div>
					<div class="card-desc">
						<h2 class="card-title">${anotherPost.postTitle }</h2>
						
						<c:set var="formatPrice" value="${anotherPost.postPrice}" />
						<fmt:formatNumber var="formatPrice" value="${formatPrice}" type="number" />
						<div class="card-price "><c:out value="${formatPrice}" />원</div>
						
						<div class="card-counts">
							<span> 조회 ${anotherPost.postViews } </span>
						</div>
					</div>
				</a>
			</article>
		</c:forEach>
		<c:if test="${empty anotherPost }">
		 	<h4> 판매자의 다른 상품이 없습니다. </h4>
		</c:if>
		</section>
	</section>
	</div>

	<!-- 푸터 -->
    <footer>
        <div class="container">
            <a href="index.html">2023 배추 마켓</a>
            <h6>Copyright © Danggeun Market Inc. All rights reserved.</h6>
        </div>
    </footer>

    <!-- <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.4.1/jquery.min.js" integrity="sha256-CSXorXvZcTkaix6Yvo6HppcZGetbYMGWSFlBw8HfCJo=" crossorigin="anonymous"></script> 
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js" integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1" crossorigin="anonymous"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js" integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM" crossorigin="anonymous"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-slider/10.6.2/bootstrap-slider.js" integrity="sha256-59/apVFrosMLFX2dHZLGvb3nPpu7e0Yx1rsDr1dTRrk=" crossorigin="anonymous"></script> -->
</body>
</html>