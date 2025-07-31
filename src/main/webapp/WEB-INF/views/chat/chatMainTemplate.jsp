<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%-- 
	
	 채팅 메인페이지 (프레임) 템플릿

	 사용자 유저VO : nowUserVO
	 채팅방VO : chatRoomVO  -- 안쓰나
	 채팅방 리스트 : chatRoomList
--%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>배추채팅</title>
<link rel="stylesheet" href="/resources/css/base.css" />
<link rel="stylesheet" href="/resources/css/chat/chat.css" />
<link rel="icon" href="/resources/pic/img/baechu.png" />
<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
<script src="/resources/js/chat/chat.js"></script>
</head>
<body id="body" class="light-theme" style="overflow: auto;">
	<div id="root" data-user_id="${nowUserVO.userId }">
		<div class="css-1mnz5cs">
			<header class="css-19lthbi">
				<div class="container">
					<!--TODO : main화면 URL 추가해야함 -->
					<a href="/"><img src="/resources/pic/Cabbage_Logo.png"
						alt="배추 로고" class="css-1nmpalf"></a>
					<button class="common-bg-hover  css-1ca43ff">
						<img class="menu-profile-img"
							src="/resources/pic/profilePic/${nowUserVO.profilePic }"
							alt="${nowUserVO.userNickname }" onerror="this.onerror=null; this.src='/resources/pic/img/cabbage_icon.png'">
						<div class="nickname-area">
							${nowUserVO.userNickname }<span class="menu-icon"><svg width="12" height="7"
									viewBox="0 0 12 7" fill="none"
									xmlns="http://www.w3.org/2000/svg">
									<path d="M1 1L6 6L11 1" stroke="currentColor"
										stroke-width="1.5" stroke-linecap="round"
										stroke-linejoin="round"></path></svg></span>
						</div>
						<!-- 오른쪽 상단 프로필 클릭시 나오는 메뉴창 -->
						<div class="menu-container">
							<div class="last-menu">
								<div tabindex="0" class="common-bg-hover menu-item logout-menu">로그아웃</div>
							</div>
						</div>
					</button>
				</div>
			</header>
			<main class="body">
				<div class="css-143d18r">
					<div></div>
					<div class="css-1plme8k">
						<nav class="sidebar">
							<a class="anchor" href="/chat"><img
								class="selected profile-image"
								src="/resources/pic/profilePic/${nowUserVO.profilePic }"
								alt="${nowUserVO.userNickname }">
								<div class="unread-count"></div></a>
						</nav>
						<nav class="css-dcpzrh">
							<div class="css-fycla4">
								<div class="nickname-area">${nowUserVO.userNickname }</div>
							</div>

							<!-- 채팅방 리스트 -->
							<ul tabindex="0" role="list" aria-label="내 채널 리스트"
								class="css-8lfz6g">
								
								
								<c:forEach var="chatRoom" items="${chatRoomList }">
									<li class="css-v2yhcd" data-chat_room_id="${chatRoom.chatRoomId }" data-message_count_by_room="${chatRoom.lastMessageCountByRoom }">
									<a class="css-y6c1l4" href="/chat/room/${chatRoom.chatRoomId }">
									<div class="profile-wrapper">
											<img class="profile-image"
												src="/resources/pic/profilePic/${chatRoom.otherUserProfilePic }"
												alt="profile">
										</div>
										<div class="css-qv4ssb">
											<div class="preview-title-wrap">
												<span class="preview-nickname">${chatRoom.otherUserNickname }</span>
												<div class="sub-text">
													<span> · </span>
													<!-- 마지막 메세지가 얼마나 전에 왔는지 (분으로 넘어온다) -->
													<span>
														<c:if test="${chatRoom.lastMessageTime < 60 }"><fmt:parseNumber integerOnly="true" value="${chatRoom.lastMessageTime }" />초 전</c:if>
														<c:if test="${chatRoom.lastMessageTime >= 60 and chatRoom.lastMessageTime < 3600 }"><fmt:parseNumber integerOnly="true" value="${chatRoom.lastMessageTime /60 }" />분 전</c:if>
														<c:if test="${chatRoom.lastMessageTime >= 3600 and chatRoom.lastMessageTime < 86400 }"><fmt:parseNumber integerOnly="true" value="${chatRoom.lastMessageTime /60 / 60  }" />시간 전</c:if>
														<c:if test="${chatRoom.lastMessageTime >= 86400 and chatRoom.lastMessageTime < 2592000 }"><fmt:parseNumber integerOnly="true" value="${chatRoom.lastMessageTime /60 / 60 / 24  }" />일 전</c:if>
														<c:if test="${chatRoom.lastMessageTime >= 2592000 and chatRoom.lastMessageTime < 31104000 }"><fmt:parseNumber integerOnly="true" value="${chatRoom.lastMessageTime /60 / 60 / 24 / 30  }" />개월 전</c:if>
														<c:if test="${chatRoom.lastMessageTime >= 31104000  }"><fmt:parseNumber integerOnly="true" value="${chatRoom.lastMessageTime /60 / 60 / 24 / 30 / 365 }" />년 전</c:if>
													</span>
												</div>
											</div>
											<div class="preview-description">
												<span class="description-text">
													<c:if test='${chatRoom.lastMessageType == "CHAT" }'>${chatRoom.lastMessage }</c:if>
													<c:if test='${chatRoom.lastMessageType == "PIC" }'>사진을 보냈습니다.</c:if>
													<c:if test='${chatRoom.lastMessageType == "DEL" }'>삭제된 메세지입니다.</c:if>
													<c:if test='${chatRoom.lastMessageType == "EMOJI" }'>이모티콘을 보냈습니다.</c:if>
													<c:if test='${chatRoom.lastMessageType == "PAY" && chatRoom.lastPayStatus == "WAITING"}'>결제요청을 보냈습니다.</c:if>
													<c:if test='${chatRoom.lastMessageType == "PAY" && chatRoom.lastPayStatus == "CANCEL"}'>결제요청을 취소했습니다.</c:if>
													<c:if test='${chatRoom.lastMessageType == "PAY" && chatRoom.lastPayStatus == "FINISH"}'>결제가 완료되었습니다.</c:if>
												</span>
											</div>
										</div> 
										<div class="unread-count-wrapper" data-count="${chatRoom.unreadMessageCount }">${chatRoom.unreadMessageCount }</div>
										<c:if test="${chatRoom.firstPostPic != null }">
											<img
											src="/resources/pic/postPic/${chatRoom.firstPostPic }"
											class="preview-image" alt="상품 썸네일">
										</c:if>
									</a>
									</li>
								</c:forEach>
								
							</ul>

						</nav>
					</div>
					
					<!-- 채팅방 섹션 -->
					<section class="css-am8mw7">
						<div class="empty-box">
							<svg width="96" height="81" viewBox="0 0 96 81" fill="none"
								xmlns="http://www.w3.org/2000/svg">
			<path
									d="M33.0004 0C15.0185 0 0 13.0729 0 29.6567C0 40.358 6.27606 49.642 15.5279 54.8364L13.8397 64.5305C13.7353 65.1299 13.928 65.7446 14.3535 66.1751L14.3573 66.179L14.3724 66.1939C14.3853 66.2066 14.4061 66.2267 14.4326 66.2506C14.4869 66.2995 14.568 66.3668 14.6744 66.435C14.9082 66.5849 15.1569 66.6709 15.3962 66.7073C15.7666 66.7637 16.0661 66.6901 16.1358 66.673L16.1413 66.6716C16.3174 66.6287 16.5003 66.558 16.6232 66.51C16.9302 66.3901 17.5014 66.1524 18.5787 65.6955C20.7218 64.7866 24.9636 62.9696 33.3799 59.3641C51.1931 59.1817 66.0008 46.1763 66.0008 29.7093C66.0008 13.1297 50.987 0 33.0004 0Z"
									fill="#DCDEE3"></path>
			<path
									d="M72.2312 29.4385C72.2312 48.2002 56.7085 62.679 37.8858 64.8408C44.0168 70.067 52.3818 73.2792 61.479 73.3633C70.2216 76.9749 74.6257 78.7941 76.8498 79.7036C77.9674 80.1606 78.5583 80.3977 78.8749 80.517C79.0036 80.5654 79.1863 80.6333 79.3599 80.6741L79.3652 80.6754C79.4339 80.6917 79.7238 80.7604 80.0821 80.7078C80.313 80.6739 80.5564 80.5935 80.7883 80.4501C80.8943 80.3846 80.9756 80.3195 81.0307 80.2717C81.0459 80.2585 81.0593 80.2464 81.0704 80.2362C81.0789 80.2284 81.0861 80.2217 81.0918 80.2163L81.1071 80.2017L81.111 80.1978C81.5557 79.764 81.7577 79.1325 81.6467 78.5179L79.9012 68.8524C89.4699 63.674 96 54.3943 96 43.6557C96 29.1206 84.0984 17.353 68.7254 14.6059C70.9682 19.0808 72.2312 24.0881 72.2312 29.4385Z"
									fill="#DCDEE3"></path></svg>
							<div class="empty-description">채팅할 상대를 선택해주세요.</div>
						</div>
					</section>
				</div>
				<div class="css-1oteowz"></div>
			</main>
			<div></div>
		</div>
	</div>
</body>
</html>