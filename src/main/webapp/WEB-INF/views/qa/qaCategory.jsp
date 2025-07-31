<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" type="text/css" href="/resources/css/qa/qaCategory.css">
<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
<title>QA Category</title>
<link rel="icon" href="/resources/pic/img/baechu.png" />
<script>
</script>
</head>

<body id="wv-feedbacks-new">
<form action="/qa/qaForm" method="post">
    <c:if test="${loggedInUser ne null}">
        <p style="margin: 3em 16px 16px;">${loggedInUser}님 안녕하세요! 🤚</p>
    </c:if>
    
    
    
    <div data-v-8b3d39b8="">
    <div data-v-aed0d24c="" data-v-8b3d39b8="">
    <h3 data-v-aed0d24c="" class="category-title ">
	😊 문의하기 전, 카테고리를 선택해주세요! 😊
    </h3>
    </div>
    </div>
    
 	<!-- 카테고리 리스트 -->
    <div data-v-8b3d39b8="">
	    <div data-v-aed0d24c="" data-v-8b3d39b8="">
	        <ul data-v-aed0d24c="" class="feedbacks-category-list">
	            <c:forEach var="category" items="${qaCategoryList}">
	                <li data-v-aed0d24c="" class="feedbacks-category-item">
	                    <a data-v-aed0d24c="" href="/qa/qaForm?categoryId=${category.qaCatId}">
	                        ${category.qaCatName}
	                    </a>
	                </li>
	            </c:forEach>
	        </ul>
	    </div>
	</div>
    
    <!-- Hidden Input으로 선택한 카테고리 ID 전달 -->
    <input type="hidden" id="categoryId" name="categoryId" value="${qaCatContent}">

</form>
        
</body>
</html>