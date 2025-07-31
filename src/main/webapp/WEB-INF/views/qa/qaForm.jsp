<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <link rel="stylesheet" type="text/css" href="/resources/css/qa/qaForm.css">
    <script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
    <title>문의 작성</title>
    <link rel="icon" href="/resources/pic/img/baechu.png" />
</head>
<body id="wv-feedbacks-new">

    <!-- 로그인 정보 출력 -->
    <c:if test="${loggedInUser ne null}">
        <!-- 로그인한 사용자 화면에 출력하기! -->
        <p style="margin: 3em 16px 16px;">${loggedInUser}님의 소중한 의견 감사합니다.🌿</p>
    </c:if>

    <!-- 카테고리 내용 QA_CAT_CONTENT -->
    <div data-v-8b3d39b8="">
        <div data-v-8b136b14="" data-v-8b3d39b8="" class="feedback-form" style="grid-template-rows: max-content max-content minmax(188px, 1fr) max-content max-content max-content;">
            <form class="feedback-form" id="qaFormInsert" action="/qa/qaFormInsert" method="post" enctype="multipart/form-data">
            <input type="hidden" name="qaCatId" value="${categoryId }">
            <div data-v-8b136b14="" class="feedbacks-text">

                <c:set var="qaCatContent" value="${qaCatContent}"/>
                ${qaCatContent}

                <%-- <c:choose>
                    <c:when test="${categoryId==1 && qaCatContent==1}">
                        <c:set var="qaCatContent" value="${qaCatContent}"/>
                    </c:when>
                    <c:when test="${categoryId==2 && qaCatContent==2}">
                        <c:set var="qaCatContent" value="${qaCatContent}"/>
                    </c:when>
                    <c:otherwise>
                        <!-- qaCatContent에 기본값이나 처리할 값 설정 -->
                        <c:set var="qaCatContent" value="default-value"/>
                    </c:otherwise>
                </c:choose> --%>
            </div>

            <!-- 내용 입력 부분 -->
            <div data-v-8b136b14="" class="phone-container">
                <input data-v-8b136b14="" name="qaTitle" type="text" placeholder="제목을 입력해주세요 :)" class="phone"> <!----></div>
                <div data-v-38784853="" data-v-8b136b14="" class="text-area">
                    <div data-v-38784853="" class="textarea">
                        <textarea data-v-38784853="" cols="80" rows="20" name="qaContent" placeholder="여기에 내용을 적어주세요 :)" 
                        maxlength="1000" style=" border: none;  overflow: hidden;  resize: none; 
                        padding: 15px 13.1px 15px 15px; box-sizing: inherit;"></textarea>
                    </div>
                    <p data-v-38784853="" class="counter text-gray">0/1000</p>
                </div>
                <!-- 사진 업로드 부분 -->
                <div data-v-8b136b14="" class="photos">
                    <input data-v-8b136b14="" type="file" name="uploadFile" accept="image/*" multiple="multiple" style="display: none;">
                    <button form="qaForm" data-v-8b136b14="" class="photo-input">
                        <svg data-v-8b136b14="" width="20" height="17" viewBox="0 0 20 17" fill="none" xmlns="http://www.w3.org/2000/svg">
                            <path fill-rule="evenodd" clip-rule="evenodd" d="M4.2 3L5.74 0.43C5.89 0.19 6.23 0 6.49 0H13.51C13.78 0 14.11 0.19 14.26 0.43L15.8 3H18C19.1 3 20 3.89 20 5V15C20 16.11 19.11 17 18 17H2C0.9 17 0 16.11 0 15V5C0 3.9 0.89 3 2 3H4.2ZM9.0002 2.70001C8.55837 2.70001 8.2002 3.05818 8.2002 3.50001C8.2002 3.94184 8.55837 4.30001 9.0002 4.30001H11.0002C11.442 4.30001 11.8002 3.94184 11.8002 3.50001C11.8002 3.05818 11.442 2.70001 11.0002 2.70001H9.0002ZM7.3002 10C7.3002 8.50884 8.50903 7.30001 10.0002 7.30001C11.4914 7.30001 12.7002 8.50884 12.7002 10C12.7002 11.4912 11.4914 12.7 10.0002 12.7C8.50903 12.7 7.3002 11.4912 7.3002 10ZM10.0002 5.70001C7.62537 5.70001 5.7002 7.62519 5.7002 10C5.7002 12.3748 7.62537 14.3 10.0002 14.3C12.375 14.3 14.3002 12.3748 14.3002 10C14.3002 7.62519 12.375 5.70001 10.0002 5.70001Z" fill="#868B94">
                            </path>
                        </svg>
                        <p data-v-8b136b14="" class="picture-count">
                            <span data-v-8b136b14="" style="color: rgb(134, 139, 148);">0</span>/10
                        </p>
                    </button>
                </div>

                <!-- 안내사항 하단 부분 -->
                <hr data-v-8b136b14="" class="solid">
                <div data-v-31d3de98="" data-v-8b136b14="" class="notice">
                    <p data-v-31d3de98="" class="bullet-title">안내 사항</p>
                    <ul data-v-31d3de98="" class="bullet-list" style="word-break: keep-all;">
                        <li data-v-31d3de98="">고객센터 운영시간은 10:00 ~ 19:00 예요.</li>
                        <li data-v-31d3de98="">답변에는 시간이 소요됩니다. 조금만 기다려주세요 :)</li>
                        <li data-v-31d3de98="">문의 내용을 자세하게 남겨주시면 빠른 답변에 도움이 됩니다.</li>
                        <li data-v-31d3de98="">산업안전보건법에 따라 고객응대 근로자 보호조치를 하고 있으며 모든 문의는 기록으로 남습니다.</li>
                        <li data-v-31d3de98="">문의하기 버튼을 누르시면
                            <a data-v-31d3de98="" href="https://www.daangn.com/policy/cs_privacy_additional" target="_blank" style="color: inherit;">개인정보 수집 이용동의서</a>에 동의하신 것으로 간주합니다.</li>
                    </ul>
                </div>

                <!-- 문의하기 버튼 부분 -->
                <button data-v-8b136b14="" class="submit">문의하기</button>
                	<div data-v-8b136b14="" id="bottom-safe-area"></div>
                    <div data-v-8b136b14="" id="uploading" style="display: none;">
                        <svg data-v-8b136b14="" width="50" height="50" viewBox="0 0 50 50" fill="none" xmlns="http://www.w3.org/2000/svg">
                            <path fill-rule="evenodd" clip-rule="evenodd" d="M0 25C0 38.8071 11.1929 50 25 50C38.8071 50 50 38.8071 50 25C50 11.1929 38.8071 2.38419e-06 25 0C23.2741 2.38419e-06 21.875 1.39911 21.875 3.125C21.875 4.85089 23.2741 6.25 25 6.25C35.3553 6.25 43.75 14.6447 43.75 25C43.75 35.3553 35.3553 43.75 25 43.75C14.6459 43.75 6.25195 35.3573 6.25 25.0036L6.25 25C6.25 23.2741 4.85089 21.875 3.125 21.875C1.39911 21.875 0 23.2741 0 25Z" fill="var(--seed-scale-color-gray-500)">
                            </path>
                        </svg>
                    </div>
            	</form>
            </div>
        </div>

 <!-- 글자 수 카운트 부분 -->
<script>
	$(document).ready(function () {
   	 	// textarea의 내용이 변경될 때마다 글자 수를 계산하여 업데이트
    	$("textarea").on("input", function () {
       		 var text = $(this).val();
             var length = text.length;
             $(".counter").text(length + "/1000");
		});
	});

	<!-- 사진 업로드 부분 -->
    $(document).ready(function () {
    	// 파일 업로드 버튼 클릭 시 파일 입력 필드 클릭
        $(".photo-input").on("click", function () {
        	// 해당 파일 입력 필드 클릭
            $("input[type=file]").click();
		});

    	// 파일 입력 필드 변경 감지
        $("input[type=file]").on("change", function () {
        // 선택된 파일 개수 표시
        var fileCount = $(this)[0].files.length;
        $(".picture-count span").text(fileCount);

        });
	});
</script>

</body>
</html>