<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>회원탈퇴</title>
<link rel="icon" href="/resources/pic/img/baechu.png" />
<link
	href="https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@400;500;700&display=swap"
	rel="stylesheet">
<script src="https://kit.fontawesome.com/53a8c415f1.js"
	crossorigin="anonymous">
</script>
<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>   
<script>
$(document).ready(function() {
	
	$("#stay_btn").click(function() {
		window.location.href = 'myInfo';
	});

	$('li a').click(function(event) {
    	event.preventDefault();
    	$('#modal').addClass('show');

	});
	
	$(document).on('click', '#leave_btn', function (e) {
	    
	    $.ajax({
	        type: 'POST', // 혹은 GET 방식에 맞게 설정
	        url: 'leaveUser', // 요청을 보낼 URL
	        success: function(response) {
	            alert("탈퇴 되었습니다");
	            window.location.href = 'login'; // 로그인 페이지로 이동하는 예시
	        },
	        error: function(xhr, status, error) {
	            console.error('서버 요청 실패:', error);
	            // 실패했을 때의 동작 구현
	        }
	    });
	});
});

</script>
<link rel="stylesheet" type="text/css" href="/resources/css/user/delete.css">
</head>

<body>
	<div class="wrap">
		<div class="find">
			<div class="imgback">
				<img class="back"
					src="/resources/pic/img/arrow-left-solid.svg" id="back">
			</div>
			<div class="findinfo">
				<h2>회원탈퇴</h2>
				<h4>회원탈퇴 사유를 골라주세요.</h4>
				<ul>
					<li><a href="">너무 많이 이용해요</a></li>
					<li><a href="">사고 싶은 물품이 없어요</a></li>
					<li><a href="">물품이 안팔려요</a></li>
					<li><a href="">비매너 사용자를 만났어요</a></li>
					<li><a href="">억울하게 이용이 제한됐어요</a></li>
					<li><a href="">알림이 너무 많이 와요</a></li>
					<li><a href="">기타</a></li>
				</ul>
			</div>
		</div>
	</div>
</body>

<!-- 모달 -->
<div class="modal" id="modal">
	<div class="modal_body">
		<div class="m_head">
			<div class="modal_title">정말로 떠나실건가요?</div>
		</div>
		<div class="m_body">
			<img alt="울어" src="/resources/pic/emoji/6.png">
		</div>
		<div class="m_footer">
			<div class="modal_btn leave" id="leave_btn">떠나기</div>
			<div class="modal_btn save" id="stay_btn">머무르기</div>
		</div>
	</div>
</div>

</html>
