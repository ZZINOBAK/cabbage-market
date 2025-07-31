<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<script src="https://code.jquery.com/jquery-3.6.4.min.js"></script>
<link rel="stylesheet" type="text/css" href="/resources/css/qa/qaFormList.css">
<title>문의글 목록</title>
<link rel="icon" href="/resources/pic/img/baechu.png" />
</head>
<body>
	<div id="container">
		<div title="" class="content-container"></div>
		<h1 title="" class="">
				<svg title="" width="1.8rem" height="5rem" viewBox="0 0 474 801"
					fill="none" xmlns="http://www.w3.org/2000/svg">
					<path d="" fill="#FF6F0F"></path> 
					<img src="/resources/pic/img/baechu.png" width="45" height="45" /></svg>
					<!--  <a href="redirect:/qa/qaFormList" style="text-decoration: none; color: black;">문의내역</a> -->
					문의내역
		</h1>

			<!-- SEARCH BAR 부분 -->
			<form action="/qa/qaFormKeywordList" method="get">
				<table class="border-none">
					<tr>
						<td>
						<!-- <select name="searchCondition">
								<option value="TITLE">제목</option>
								<option value="CONTENT">내용</option>
						</select>  -->
						<select name="searchCondition">
							<c:forEach var="option" items="${conditionMap }">
								<option value="${option.value }">${option.key }</option>
							</c:forEach>
						</select> 
						<input type="text" name="searchKeyword"> 
						<input type="submit" value="검색"></td>
					</tr>
				</table>
			</form>

			<!-- 1:1 문의내역 데이터 표시 영역 -->
			<section
				class="fb__communityDetail__recentView fb__communityDetail__comments">
				<h3 class="recentView__title">
					<em>1:1문의내역 최신글</em>
				</h3>


				<c:forEach var="qa" items="${qaList}">
					<ul class="recentView__wrap">
						<li class="recentView__list">
							<a href="qaFormDetail?qaId=${qa.qaId }" class="recentView__link">
								<p class="recentView__cont">${qa.qaTitle}</p> 
								<!-- 정보 박스 -->
								<div class="recentView__info">
									<!-- 작성 정보 -->
									<div class="recentView__info__write">
										<span class="write__nickname write__list">
											[${qa.qaCatName}]
										</span>
										<span class="write__date write__list">
											등록날짜 : ${qa.qaRegdate}
										</span>
									</div>

									<!-- 카운트 정보 -->
									<div class="recentView__info__count">
										<!-- <span class="count__view count__list">5</span> --> 
										<span class="count__like count__list">답변 상태: ${qa.qaStatus }</span> 
										<!-- <span class="count__comment count__list">image : ${qa.fileId }</span> -->
									</div>
								</div>
						</a>
					</ul>
				</c:forEach>
			</section>

		</div>
	</div>
<script>
function searchQa() {
    var form = $('#searchForm');
    var url = form.attr('action');
    var formData = form.serialize();

    $.ajax({
        type: 'GET',
        url: url,
        data: formData,
        success: function(data) {
            $('#recentViewSection').html(data);
        },
        error: function(xhr, status, error) {
            console.error('Ajax request failed:', status, error);
        }
    });
}
</script>	
</body>
</html>