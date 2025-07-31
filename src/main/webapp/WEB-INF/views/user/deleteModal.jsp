<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
<script>
$(function(){
	  $("#confirm").click(function(){
	      modalClose(); //모달 닫기 함수 호출
	      
	      //컨펌 이벤트 처리
	  });
	  $("#modal-open").click(function(){        
	      $("#popup").css('display','flex').hide().fadeIn();
	      //팝업을 flex속성으로 바꿔준 후 hide()로 숨기고 다시 fadeIn()으로 효과
	  });
	  $("#close").click(function(){
	      modalClose(); //모달 닫기 함수 호출
	  });
	  function modalClose(){
	      $("#popup").fadeOut(); //페이드아웃 효과
	  }
	});
</script>
<link rel="stylesheet" type="text/css" href="/resources/css/user/modal.css">
</head>

<body>

	<div class="container">
		//컨테이너
		<div class="popup-wrap" id="popup">
			//모달을 감쌀 박스
			<div class="popup">
				//실질적 모달팝업
				<div class="popup-head">
					//로고 영역 <span class="head-title">MuziMuzi</span>
				</div>
				<div class="popup-body">
					//컨텐츠 영역
					<div class="body-content">
						<div class="body-titlebox">
							<h1>Confirm Modal</h1>
						</div>
						<div class="body-contentbox">
							<p>모달 내용칸</p>
						</div>
					</div>
				</div>
				<div class="popup-foot">
					//푸터 버튼 영역 <span class="pop-btn confirm" id="confirm">확인</span> <span
						class="pop-btn close" id="close">창 닫기</span>
				</div>
			</div>
		</div>
	</div>

</body>
</html>