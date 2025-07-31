<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" type="text/css" href="/resources/css/qa/adminFormList.css">
<title>관리자 페이지</title>
<link rel="icon" href="/resources/pic/img/baechu.png" />
<style>

</style>
</head>
<body>
	<div id="container">
		<div title id="faq-wrapper" style="font-size: 40px;">
			<br>
			<div title="" class="content-container"></div>
			<div title=""></div>
			<svg title="" width="1.8rem" height="0rem" viewBox="0 0 474 801" fill="none"
					xmlns="http://www.w3.org/2000/svg">
			<path d="" fill="#FF6F0F"></path> 
			<img src="/resources/pic/img/admin_baechu.png" width="80" height="80" />
			</svg>
			<h1 title="" class="">쉿!🤫</h1>
			<h1 title="">여기는 관리자 페이지</h1>
			<br>
		</div>
			

				<!-- <p><a href="lo">뒤로가기 버튼</a></p> -->
				<%-- <c:set var="loggedInUser" value="${sessionScope.loggedInUser}" />
				<!-- 로그인 정보 출력 -->
				<c:if test="${loggedInUser ne null}">
					<p style="margin: 3em 16px 16px;">${loggedInUser.userId}님의문의내역🔍</p>
				</c:if> --%>



				<form action="adminQaFormList" method="get">
					<table class="border-none">
						<tr>
							<td><select name="searchCondition">
									<option value="TITLE">제목</option>
									<option value="CONTENT">내용</option>
							</select> <%-- <select name="searchCondition">
					<c:forEach var="option" items="${conditionMap }">
						<option value="${option.value }">${option.key }</option>
					</c:forEach>
					</select> --%> <input type="text" name="searchKeyword"> <input
								type="submit" value="검색"></td>
						</tr>
					</table>
				</form>

				<!-- 데이터 표시 영역 -->
				<section
					class="fb__communityDetail__recentView fb__communityDetail__comments">
					<h3 class="recentView__title">
						<em>문의내역 최신글</em>
					</h3>


					<c:forEach var="qa" items="${adminQaList}">
						<ul class="recentView__wrap">
							<li class="recentView__list">
								<a href="adminFormDetail?qaId=${qa.qaId }" class="recentView__link">
									<p class="recentView__cont">${qa.qaTitle}</p> 
									<!-- 정보 박스 -->
									<div class="recentView__info">
										<!-- 작성 정보 -->
										<div class="recentView__info__write">
											<span class="write__nickname write__list">
												[${qa.qaCatName}]
											</span>
											<span class="write__date write__list" style="font-weight:bold;">
												회원ID : ${qa.userId}
											</span>
											<span class="write__date write__list">
												등록날짜 : ${qa.qaRegdate}
											</span>
										</div>

										<!-- 답변 상태 -->
										<div class="recentView__info__count">
											<span class="count__like count__list">${qa.qaStatus }</span> 
											<!-- <span class="count__comment count__list">이미지파일 : ${qa.fileId }</span> -->
										</div>
									</div>
							</a>
						</ul>
					</c:forEach>
				</section>
	</div>			
</body>
</html>

