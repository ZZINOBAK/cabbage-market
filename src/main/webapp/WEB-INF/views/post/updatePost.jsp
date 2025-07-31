<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<c:set var="postPrice" value="${post.postPrice }" />
<fmt:formatNumber type="currency" value="${postPrice}" var="formattedPrice" />   


<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>게시글 수정</title>
<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
<link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-slider/10.6.2/css/bootstrap-slider.min.css" integrity="sha256-G3IAYJYIQvZgPksNQDbjvxd/Ca1SfCDFwu2s2lt0oGo=" crossorigin="anonymous" />
<link rel="stylesheet" href="/resources/css/post/postCreate.css">
<link rel="stylesheet" href="/resources/css/post/postCreateImage.css">
<link rel="icon" href="/resources/pic/img/baechu.png" />
<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>

<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/sweetalert2@11.4.10/dist/sweetalert2.min.css">
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11.4.10/dist/sweetalert2.min.js"></script>

<script src="/resources/js/post/postUpdate.js"></script>
</head>
<body>
	<!-- <nav class="navbar nav-global fixed-top navbar-expand-sm">
        <div class="container">
            <a class="navbar-brand" href="javascript:history.back()">
                <img src="/resources/pic/cabbage.png" width="100px" height="50px">
            </a>
        </div>
    </nav>
 -->
 
 <!-- header -->
	<%@ include file="/WEB-INF/views/main/inc/header.jsp" %>
    <div class="sell-form">
        <form id="updateForm" action="updatePost" method="post" enctype="multipart/form-data">
        	<input type="hidden" name="postId" value="${post.postId }">	<!-- 세션으로 로그인한 아이디 할당 -->
        	
            <label for="postCatId">카테고리</label>
            <select name="postCatId" id="postCatId" required>
                <option value="5">기타</option>
                <option value="1">의류</option>
                <option value="2">전자기기</option>
                <option value="3">가구</option>
                <option value="4">도서</option>
            </select>
            
            <div id="file-input-container" onclick="openFileInput()">
            <label for="uploadFile">이미지</label><br>
                <span id="file-input-text">+</span>
                <input type="file" id="uploadFile" name="uploadFile" accept="image/*" multiple onchange="previewImages(event)" id="file-input" hidden>
            </div>
            
            <div>
			    <ul id="preview-container">
		             <li id="nothingNoti">
		             	<p id="fileMsg" style="color: #ccc;">이미지는 꼭 첨부해주세요.</p>
	             	</li>
			    	<c:forEach var="postPic" items="${postPic }">
			    		<li class="preview-image">
			    			<div class="pic-container">
			    				<img src="/resources/pic/postPic/${postPic.fileName }" width="70px" height="70px">
			    				<span class="delete-icon2" onclick="deleteUploaded(this, '${postPic.fileId}')">X</span>
			    			</div>
			    		</li>
			    	</c:forEach>
			    </ul>
		    </div>
		    
		    <div class="deletePic"></div>
            <label for="postTitle">게시글 제목</label>
            <input type="text" name="postTitle" id="postTitle" value="${post.postTitle }" required>
            <p id="postTitleError" style="color: red; display: none;">공백문자는 사용할 수 없습니다.</p>
            
            <label for="postPrice">가격</label>
			<input type="text" name="postPrice" id="postPrice" maxlength="13" value="${formattedPrice}" required>
            
            <label for="postContent">상세 설명</label>
            <textarea name="postContent" id="postContent" rows="5" maxlength="1000" placeholder="최대 1000자" required>${post.postContent }</textarea>
            <p id="postContentError" style="color: red; display: none;">공백문자는 사용할 수 없습니다.</p>

			<div id="btnDiv">
            <button type="button" onclick="submitForm()" class="center">수정하기</button>
            <button onclick="javascript:history.back(); return false;" class="center">뒤로가기</button>
            </div>
        </form>
    </div>
</body>
</html>