<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>아이디/비밀번호 찾기</title>
<link rel="icon" href="/resources/pic/img/baechu.png" />
<link href="https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@400;500;700&display=swap" rel="stylesheet">
<script src="https://kit.fontawesome.com/53a8c415f1.js" crossorigin="anonymous"></script>
<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>   
<script>
	  	<% if (request.getAttribute("message") != null) { %>
	        alert("<%= request.getAttribute("message") %>");
	    <% } %>	    
</script>
<script type="text/javascript" src="/resources/js/user/findAccount.js"></script>
<link rel="stylesheet" type="text/css" href="/resources/css/user/find.css">

</head>
<body>
	<div class="wrap">
		<div class="find">
			<form id="findIdForm" action="findId" method="post">
				<img class="back" src="/resources/pic/img/arrow-left-solid.svg" id="back">
				<h2>아이디 찾기</h2>
				<div class="findinfo">
					<h4>이름</h4>
					<input type="text" name="userName" placeholder="이름"
						autocomplete="off" maxlength="6">
				</div>
				<div class="findinfo">
					<h4>전화번호</h4>
					<input type="text" name="userPhone" placeholder="전화번호"
						autocomplete="off" maxlength="11">
				</div>

				<div class="submit">
					<input type="submit" value="아이디 찾기">
				</div>
			</form>
		</div>

		<div class="find">
			<form id="findPwForm" action="findPw" method="post">
				<img class="back" src="/resources/pic/img/arrow-left-solid.svg" id="back2">
				<h2>비밀번호 찾기</h2>
				<div class="findinfo">
					<h4>아이디</h4>
					<input type="text" name="userId" id="userIdInpw" placeholder="ID"
						autocomplete="off" maxlength="16">
				</div>

				<div class="findinfo">
					<h4>이메일</h4>
					<input type="text" placeholder="이메일을 입력해주세요" name="userEmail" id="userEmail" maxlength="30" autocomplete="off">
			
					<label id="label5"></label> 
					<input type="button" id="emailCheck" value="인증번호 받기" disabled="disabled"> 
					<input type="text" placeholder="인증번호 6자리를 입력해주세요" name="emailNum" id="emailNum"
					maxlength="6" disabled="disabled">
			
					<span id="emailAuthWarn"></span>
				</div>
				
				<div class="submit">
					<input type="submit" value="비밀번호 찾기" disabled="disabled" id="pwSubmit">
				</div>
			</form>
		</div>
	</div>


</body>

</html>



