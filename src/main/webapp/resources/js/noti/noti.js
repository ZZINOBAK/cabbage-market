//알림창 스크롤 설정 함수
    var alrimDiv = document.getElementById('alrim');
    var body = document.body;
    var alrimList = document.getElementById('alrimList');
//function scrollToBottom() {
//    alrimList.scrollTop = alrimList.scrollHeight;
//        console.log('scrollToBottom 호출됨');
//}

    alrimDiv.addEventListener('mouseenter', function () {
        body.classList.add('alrim-open');
    });

    alrimDiv.addEventListener('mouseleave', function () {
        body.classList.remove('alrim-open');
    });
    
    
//알림 갯수 컨트롤 
function toggleDiv(type) {
    var myPageDiv = document.getElementById(type);

    if (type === 'alrim') {
        var alrimCount = document.getElementById('alrimCount');

        if (!myPageDiv.style.display || myPageDiv.style.display === 'none') {
            var myDiv = document.getElementById('my');
            myDiv.style.display = 'none';
            myPageDiv.style.display = 'block';

        	//scrollToBottom(); // 알림창이 열릴 때 스크롤을 가장 아래로 이동

        	 // 'alrimCount' ID를 가진 요소가 존재하는지 확인
            if (alrimCount) {
                alrimCount.innerHTML = '0';
                alrimCount.style.display = 'none';
            }
            // 알림창이 열리면 읽음으로 변경
            $.ajax({
				type: "get",
				url : "updateNoti",
				success:function(data){
		        	console.log("noti.js에서 읽음으로 변경");
				}
			});
        } else {
            myPageDiv.style.display = 'none';
        }
    } else {
        if (!myPageDiv.style.display || myPageDiv.style.display === 'none') {
            var alrimDiv = document.getElementById('alrim');
            alrimDiv.style.display = 'none';
            myPageDiv.style.display = 'block';
        } else {
            myPageDiv.style.display = 'none';
        }
    }
}
    