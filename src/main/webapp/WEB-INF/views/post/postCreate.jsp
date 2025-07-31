<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>판매 글 등록</title>
<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
<link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-slider/10.6.2/css/bootstrap-slider.min.css" integrity="sha256-G3IAYJYIQvZgPksNQDbjvxd/Ca1SfCDFwu2s2lt0oGo=" crossorigin="anonymous" />
<link rel="stylesheet" href="/resources/css/post/postCreate.css">
<link rel="stylesheet" href="/resources/css/post/postCreateImage.css">
<link rel="icon" href="/resources/pic/img/baechu.png" />
<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
<script src="/resources/js/post/postCreate.js"></script>

</head>
<body>
	<div id="loading-overlay">
         <div id="loading-spinner"></div>
      </div>
    
    <!-- header -->
	<%@ include file="/WEB-INF/views/main/inc/header.jsp" %>

    <div class="sell-form">
        <form action="insertPost" method="post" enctype="multipart/form-data" onsubmit="disableSubmitButton()">
        	<input type="hidden" name="sellerId" value=${userId }>	<!-- 세션으로 로그인한 아이디 할당 -->
        	
            <label for="postCatId">카테고리</label>
            <select name="postCatId" id="postCatId" required>
                <option value="5">기타</option>
                <option value="1">의류</option>
                <option value="2">전자기기</option>
                <option value="3">가구</option>
                <option value="4">도서</option>
            </select>
            
            <div id="file-input-container" onclick="openFileInput()">
            <label>이미지</label><br>
                <span id="file-input-text">+</span>
                <input type="file" id="uploadFile" name="uploadFile" accept="image/*" multiple onchange="previewImages(event)" id="file-input" hidden required="required">
            </div>
            
             <div>
	             <ul id="preview-container">
		             <li id="nothingNoti">
		             	<p id="fileMsg" style="color: #ccc;">이미지는 꼭 첨부해주세요.</p>
	             	</li>
	             </ul>
	          </div>
		    
            <label for="postTitle">게시글 제목</label>
            <input type="text" name="postTitle" maxlength="300" id="postTitle" required>
            <p id="postTitleError" style="color: red; display: none;">공백문자는 사용할 수 없습니다.</p>
            
            <label for="postPrice">가격</label>
            <input type="text" name="postPrice" id="postPrice" maxlength="13" required>
            
            <label for="postContent">상세 설명</label>
            <textarea name="postContent" id="postContent" rows="5" maxlength="1000" placeholder="최대 1000자" required></textarea>
            <p id="postContentError" style="color: red; display: none;">공백문자는 사용할 수 없습니다.</p>

			<div id="btnDiv">
            <button type="submit" class="center">등록하기</button>
            <button onclick="javascript:history.back(); return false;" class="center">뒤로가기</button>
            </div>
        </form>
    </div>
</body>
</html>