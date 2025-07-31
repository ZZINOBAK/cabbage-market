<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%-- 
	 채팅방을 요청하면 채팅 내용을 같이 작성해서 보내준다
	 (채팅 내용만 요청하면 내용만 보내줌) (내용부분만 뽑아서 템플릿 만들 예정)

	 사용자 유저VO : nowUserVO
	 현대 채팅 상대방 유저VO : partnerUserVO
	 현재 채팅 포스트VO : postVO
--%>
<!DOCTYPE html>
<div class="chat-normal-room" >
	<div class="css-voabwl">
	
		<!-- 채팅방 위쪽 상대 프로필 컨테이너 -->
		<div class="css-1c3oejv">
			<!-- 채팅 대상 프로필 -->
			<div class="chat-header-profile">
				<img class="chat-header-image"
					src="/resources/pic/profilePic/${partnerUserVO.profilePic }"
					alt="${partnerUserVO.userNickname }">
					
				<div class="main-title">
					<span>${partnerUserVO.userNickname }</span>
					<c:if test="${partnerUserVO.userStatus != 'WITHDRWAL' }">
						<span class="temperature">${partnerUserVO.userOndo }°C</span>
					</c:if>
				</div>
			</div>


			<!-- 채팅 대상 프로필 옆에 있는 쩜 3개 (채팅방 나가기 버튼) -->
			<div class="css-1idbtsb">
				<div class="more-button-wrapper common-bg-hover">
					<svg width="4" height="16" viewBox="0 0 4 16" fill="none"
						xmlns="http://www.w3.org/2000/svg">
												<path fill-rule="evenodd" clip-rule="evenodd"
							d="M2.0002 3.19998C2.7152 3.19998 3.3002 2.61498 3.3002 1.89998C3.3002 1.18498 2.7152 0.599976 2.0002 0.599976C1.2852 0.599976 0.700195 1.18498 0.700195 1.89998C0.700195 2.61498 1.2852 3.19998 2.0002 3.19998Z"
							fill="currentColor"></path>
												<path fill-rule="evenodd" clip-rule="evenodd"
							d="M2.0002 6.70001C1.2852 6.70001 0.700195 7.28501 0.700195 8.00001C0.700195 8.71501 1.2852 9.30001 2.0002 9.30001C2.7152 9.30001 3.3002 8.71501 3.3002 8.00001C3.3002 7.28501 2.7152 6.70001 2.0002 6.70001Z"
							fill="currentColor"></path>
												<path fill-rule="evenodd" clip-rule="evenodd"
							d="M2.0002 12.8C1.2852 12.8 0.700195 13.385 0.700195 14.1C0.700195 14.815 1.2852 15.4 2.0002 15.4C2.7152 15.4 3.3002 14.815 3.3002 14.1C3.3002 13.385 2.7152 12.8 2.0002 12.8Z"
							fill="currentColor"></path></svg>
				</div>
				<!-- 채팅 상대 옆에 있는 메뉴박스 -->
				<div class="option-container">
					<c:if test="${postVO.sellerId == nowUserVO.userId && partnerUserVO.userStatus != 'WITHDRWAL' && postVO.postStatus == 'ENABLE'}">
						<div class="option-item common-bg-hover payRequest">결제요청 보내기</div>
					</c:if>
					
					<div class="option-item common-bg-hover exit">채팅방 나가기</div>
				</div>
			</div>
		</div>
		
		<!-- 현재 채팅방의 게시글 정보 -->
		<a href="/post/getPost/${postVO.postId }" target="_blank"
			rel="noreferrer" class="css-16mjcje">
			<div class="reserved-wrapper common-bg-hover">
				<c:if test="${postVO.postPic != null and postVO.postTitle != null and postVO.postPrice != null }">
					<img class="article-image"
						src="/resources/pic/postPic/${postVO.postPic }"
						alt="${postVO.postTitle }">
					<div class="reserved-main">
						<div>${postVO.postTitle }</div>
						<div class="reserved-price"><fmt:formatNumber type="number" maxFractionDigits="3" value="${postVO.postPrice}" />원</div>
					</div>
				</c:if>
				<div>
					<span class="reserve-button closed">
						<c:if test='${postVO.postStatus == "ENABLE" }'>판매중</c:if>
						<c:if test='${postVO.postStatus == "RESERVE" }'>예약중</c:if>
						<c:if test='${postVO.postStatus == "FINISH" }'>거래완료</c:if>
						<c:if test='${postVO.postStatus == "DELETE" }'>삭제됨</c:if>
					</span>
				</div>
			</div>
		</a>
		
		<!-- -------------------------------------------- 채팅 내용 있을 곳 ---------------------------------------------------- -->
		<!-- javascript에서 추가 -->
		<div tabindex="0" role="region" aria-label="메시지 리스트" class="css-13cllyv"></div>
		
	</div>
	
	<!-- 메세지 작성영역 -->
	<form class="css-1ckh9yi" enctype="multipart/form-data">
		<textarea placeholder="메시지를 입력해주세요" class="css-10fmtiz"></textarea>
		<div class="chatform-option-area">
			<div class="chatform-submenu">
				<label class="option-wrapper"> <span class="option-tooltip">사진</span>
					<svg width="20" height="20" viewBox="0 0 20 20" fill="none" xmlns="http://www.w3.org/2000/svg"><rect width="20" height="20" rx="3"></rect><path d="M6 9C7.10457 9 8 8.10457 8 7C8 5.89543 7.10457 5 6 5C4.89543 5 4 5.89543 4 7C4 8.10457 4.89543 9 6 9Z" fill="white"></path><path d="M3 16L6.5 12L10 16" fill="white"></path><path d="M7 16L12 10L17 16" fill="white"></path></svg> 
					<input type="file" accept="image/*" name="uploadPic">
				</label> <label class="option-wrapper"><span class="option-tooltip">이모티콘</span>
				<div class="option-wrapper css-1f5m7zv">
						<button class="sticker-button" type="button">
							sticker
							<svg width="20" height="20" viewBox="0 0 20 20" fill="none"
								xmlns="http://www.w3.org/2000/svg">
								<path fill-rule="evenodd" clip-rule="evenodd"
									d="M10 20C15.5228 20 20 15.5228 20 10C20 4.47715 15.5228 0 10 0C4.47715 0 0 4.47715 0 10C0 15.5228 4.47715 20 10 20ZM7.55556 8.2222C7.55556 9.08131 6.85912 9.77776 6.00001 9.77776C5.1409 9.77776 4.44445 9.08131 4.44445 8.2222C4.44445 7.36309 5.1409 6.66665 6.00001 6.66665C6.85912 6.66665 7.55556 7.36309 7.55556 8.2222ZM14.0002 9.77776C14.8593 9.77776 15.5558 9.08131 15.5558 8.2222C15.5558 7.36309 14.8593 6.66665 14.0002 6.66665C13.1411 6.66665 12.4447 7.36309 12.4447 8.2222C12.4447 9.08131 13.1411 9.77776 14.0002 9.77776ZM6.29774 11.9236C6.13284 11.5137 5.66687 11.3151 5.25697 11.48C4.84706 11.6449 4.64845 12.1109 4.81336 12.5208C5.28799 13.7006 6.18929 14.5207 7.14118 15.0388C8.08738 15.5537 9.13514 15.8 9.99999 15.8C11.8597 15.8 14.2492 14.8376 15.186 12.5223C15.3518 12.1127 15.1541 11.6463 14.7445 11.4806C14.3349 11.3149 13.8686 11.5126 13.7028 11.9221C13.0841 13.4512 11.4292 14.2 9.99999 14.2C9.40929 14.2 8.62371 14.024 7.90603 13.6334C7.19403 13.2459 6.60088 12.6771 6.29774 11.9236Z"></path></svg>
						</button>
						
					</div></label>
			</div>
			<button class="disable css-1useanf" aria-disabled="true">전송</button>
		</div>
		<span class="text-length">0/1000</span>
	</form>
	
</div>