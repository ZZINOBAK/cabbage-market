<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>로그인</title>
<link rel="icon" href="/resources/pic/img/baechu.png" />
<link href="https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@400;500;700&display=swap" rel="stylesheet">
<script 
src="https://kit.fontawesome.com/53a8c415f1.js" crossorigin="anonymous">
</script>
<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>  
	
<script>
<% if (request.getAttribute("message") != null) { %>
	alert("<%= request.getAttribute("message") %>");
<% } %>

$(document).ready(function() {
	$("#back").click(function() {
	    window.history.back();
	});	
})



</script>
<link rel="stylesheet" type="text/css" href="/resources/css/user/login.css">
</head>
<body>
	<div class="wrap">

		<div class="login">
			<form action="loginIn" method="post">
				<img class="back" src="/resources/pic/img/arrow-left-solid.svg" id="back">
				<h2>로그인</h2>
				<div class="login_sns">
					<li><a href=""><i class="fab fa-instagram"></i></a></li>
					<li><a href=""><i class="fab fa-facebook-f"></i></a></li>
					<li><a href=""><i class="fab fa-twitter"></i></a></li>
				</div>
				<div class="login_id">
					<h4>아이디</h4>
					<input type="text" name="userId" placeholder="ID"
						autocomplete="off">
				</div>
				<div class="login_pw">
					<h4>비밀번호</h4>
					<input type="password" name="userPassword" placeholder="Password"
						autocomplete="off">
				</div>
				<div class="login_etc">
					<div class="sign_up">
						<a href="signUp">회원가입</a>
					</div>
					<div class="forgot_pw">
						<a href="findAccount">계정 찾기</a>
					</div>
				</div>
				<div class="submit">
					<input type="submit" value="로그인">
				</div>
			</form>
		</div>
	</div>
</body>

</html>
	
	
	
