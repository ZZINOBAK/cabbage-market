<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link href="https://cdn.jsdelivr.net/npm/@fancyapps/ui/dist/fancybox.css" rel="stylesheet"/>
   <script src="https://cdn.jsdelivr.net/npm/@fancyapps/ui@4.0/dist/fancybox.umd.js"></script>
<link rel="stylesheet" type="text/css" href="/resources/css/qa/adminFormDetail.css">
<title>관리자모드 댓글 입력/삭제</title>
<link rel="icon" href="/resources/pic/img/baechu.png" />
</head>
<body>
	<div id="container">

		<div title="" class="content-container"></div>
		<h1 title="" class="">
				<svg title="" width="1.8rem" height="3rem" viewBox="0 0 474 801"
					fill="none" xmlns="http://www.w3.org/2000/svg">
					<path d="" fill="#FF6F0F"></path> 
					<img src="/resources/pic/img/admin_baechu.png" width="70" height="70" /></svg>
					쉿!🤫 여기는 관리자 페이지
		</h1>
		<br>

		<div id="document" class="idx-type">
			<div class="panel">
				<div id="container" class="bg-color">
					<!-- 헤더 영역 -->
					<form action="adminFormInsert" method="post">
						<header class="fb__communityDetail__header">

							<c:set var="admin" value="${adminFormDetail }" />
							<!-- 타이틀 -->
							<p>[${admin.qaCatName }]</p>
							<p class="header__title js__communityDetail__title">
								${admin.qaTitle }</p>

							<!-- 글 정보 -->
							<div class="header__info">
								<!-- 유저 이미지 -->
								<!-- <figure class="header__info__thumb">
								<a
									href="/community/userProfile?code=1757faca2bf9b200b3d7825fac53108c&amp;pageMode=userProfile"
									class="fb__communityDetail__userImageLink"> <img
									alt="유저이미지" class="profile-image loadimage"
									src="https://eytzjvrxjleq11337293.cdn.ntruss.com/data/mall_data/images/user_profile/1757faca2bf9b200b3d7825fac53108c/after_aa8cb8d20fd61677980fff04708bf642.jpg"
									style="">
								</a>
							</figure> -->
							</div>
						</header>

						<!-- 내용 영역 -->
						<div class="fb__communityDetail__contents">
							<p class="contents__text default">${admin.qaContent }</p>
						</div>
						
						
										<!-- 이미지 영역 -->
					<div class="fb__communityDetail__comments js__communityDetail__comments">
					<p>이미지를 확대해보세요 🔎 </p>
					<c:choose>
					    <c:when test="${not empty admin.fileName}">
					        <p>
					            <a href="/resources/pic/qaPic/${admin.fileName}" data-fancybox="gallery">
					                <img alt="" src="/resources/pic/qaPic/${admin.fileName}" width="50px" height="50px">
					            </a>
					        </p>
					    </c:when>
					    <c:otherwise>
					        <p>첨부된 이미지가 없습니다.</p>
					    </c:otherwise>
					</c:choose>
						
						
						
						<div class="fb__communityDetail__comments js__communityDetail__comments">
								<!-- 댓글이 있을 때 -->
					            <c:if test="${not empty admin.qaComment}">
					                <h3 style="padding: 0.1rem 0.9rem 0rem;">${admin.qaComment}</h3>
					            </c:if>
					
					            <!-- 댓글이 없을 때 -->
					            <c:if test="${empty admin.qaComment}">
					                <form action="adminCommentAction" method="post">
					                    <p>
					                        <textarea rows="10" cols="50" name="qaComment" placeholder="댓글을 입력하세요" style="font-size: 20px"></textarea>
					                    </p>
					                    <p>
					                        <input type="hidden" name="qaId" value="${admin.qaId}">
					                        <input type="hidden" name="userId" value="${loggedInUser}">
					                        <input type="submit" value="등록">
					                    </p>
					                </form>
					            </c:if>
								</div>
								
					</form>
					

					<p>
						<a href="adminQaFormList" class="button-link">목록</a>
						<a href="deleteAdminComment?qaId=${admin.qaId }" class="button-link">삭제</a>	
					</p>
				</div>
			</div>
		</div>
	</div>

</body>
</html>