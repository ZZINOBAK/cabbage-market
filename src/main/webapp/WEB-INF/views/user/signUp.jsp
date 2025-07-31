<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>배추마켓 회원가입</title>
<link rel="icon" href="/resources/pic/img/baechu.png" />
<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
<script type="text/javascript" src="/resources/js/user/signUp.js"></script>
<link rel="stylesheet" type="text/css" href="/resources/css/user/signUp.css">
</head>
<body>

	<div class="member">
		<!-- 1. 로고 -->
		<img class="logo" src="/resources/pic/Cabbage_Logo.png" id="logo">

		<form action="joinUser" method="post" accept-charset="UTF-8">

			<!-- 2. 필드 -->
			<div class="field">
				<b>아이디</b> <span class="placehold-text"> <input type="text"
					placeholder="아이디를 입력해주세요" name="userId" id="userId" type="text"
					autofocus></span> <label id="label1"></label>
			</div>
			<div class="field">
				<b>비밀번호</b> <input class="userpw" type="password"
					placeholder="비밀번호를 입력해주세요" name="userPassword" id="userPassword"
					type="password" maxlength="16"> <label id="pwdcheck_blank1"></label>
			</div>
			<div class="field">
				<b>비밀번호 재확인</b> <input class="userpw-confirm" type="password"
					placeholder="비밀번호를 입력해주세요(8~16)" id="password_check"
					type="password" maxlength="16"> <label id="pwdcheck_blank2"></label>
			</div>
			<div class="field">
				<b>이름</b> <input type="text" placeholder="이름을 입력해주세요"
					name="userName" id="userName" maxlength="6"> <label
					id="label4"></label>
			</div>

			<div class="field">
				<b>닉네임</b> <input type="text" placeholder="닉네임을 입력해주세요"
					name="userNickname" id="userNickname" maxlength="16"> <label
					id="label2"></label>
			</div>

			<div class="field">
				<b>이메일</b> <input type="text" placeholder="이메일을 입력해주세요"
					name="userEmail" id="userEmail" maxlength="30"> <label
					id="label5"></label> 
					<input type="button" id="emailCheck" value="인증번호 받기" disabled="disabled"> 
					<input type="text"
					placeholder="인증번호 6자리를 입력해주세요" name="emailNum" id="emailNum"
					maxlength="6" disabled="disabled"> <span id="emailAuthWarn"></span>
			</div>

			<div class="field tel-number">
				<b>휴대전화</b>
				<div>
					<input type="text" placeholder="전화번호를 입력주세요" name="userPhone"
						id="userPhone" maxlength="11"> <label id="label3"></label>
				</div>
			</div>

			<!-- 6. 가입하기 버튼 -->
			<input type="submit" value="가입하기">
			<input type="button" id="cancelButton" value="취소하기">
		</form>
	</div>

</body>
</html>