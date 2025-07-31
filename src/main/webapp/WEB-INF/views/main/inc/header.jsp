<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<script src="https://code.jquery.com/jquery-3.4.1.js"></script>
<link rel="stylesheet" href="/resources/css/main/main_included_jiwon.css" />
<link rel="stylesheet" href="/resources/css/main/main_haeun.css" />
<link rel="stylesheet" href="/resources/css/main/header.css" />

<div id="gnb-root">
	<div class="light-theme">
		<div class="_1knjz490">
			<div class="_1knjz491 _1s38h9c0" style="border-bottom-style: hidden;">
				<nav class="_1knjz49j _1s38h9c0">
					<ul class="_1knjz49l _1s38h9c5">
						<li class="_1knjz49n"><a
							class="_1knjz49o _1s38h9c4 _1s38h9c2 _1knjz49p"
							href="/postList?category=1"> 의류 </a></li>
						<li class="_1knjz49n"><a
							class="_1knjz49o _1s38h9c4 _1s38h9c2 undefined"
							href="/postList?category=2"> 전자기기 </a></li>
						<li class="_1knjz49n"><a
							class="_1knjz49o _1s38h9c4 _1s38h9c2 undefined"
							href="/postList?category=3"> 가구 </a></li>
						<li class="_1knjz49n"><a
							class="_1knjz49o _1s38h9c4 _1s38h9c2 undefined"
							href="/postList?category=4"> 도서 </a></li>
						<li class="_1knjz49n"><a
							class="_1knjz49o _1s38h9c4 _1s38h9c2 undefined"
							href="/postList?category=5"> 기타 </a></li>
					</ul>
				</nav>
				<div class="_1s38h9c1 _1s38h9c0"></div>
			</div>
		</div>
	</div>
</div>
<div class="div-haeun1">
	<div class="div-haeun2">
		<div class="div-haeun3">
			<a class="_1knjz492" href="/"> <span> <svg width="65"
						height="36" viewBox="0 0 65 36" fill="none"
						xmlns="http://www.w3.org/2000/svg">
							<image href="/resources/pic/img/cabbage_market_logo.png"
							width="65" height="34" />
						</svg>
			</span>
			</a>
			<div class="a-haeun1">
				<!-- 				<div class="_1s38h9c1 _1s38h9c0">
 -->
				<ul id="rollingNotice" class="rolling">
				</ul>
				<ul id="showingNotice" class="showing">
				</ul>
			</div>
			<div class="div-haeun4">
				<span class="div-haeun5"> <input class="input-haeun1"
					type="text" id="searchInput" placeholder="물품이나 동네를 검색해보세요"
					autocomplete="off">
					<div class="input-group-append">
						<span class="input-group-text" id="clearSearchInput"
							style="cursor: pointer; display: none; height: 38px">&times;</span>
					</div>
					<ul class="list-group" id="searchResults"></ul> <a class="a-haeun2">
						<img class="img-haeun2" id="searchButton" src="/resources/pic/img/cabbage_icon.png"
						width="16" height="17" alt="검색버튼 아이콘">
				</a>
				</span>
			</div>
			<div class="div-haeun6">
				<!-- <span> -->
				<c:if test="${not empty userId}">
							<div class="none-haeun" id="alrim" style="display:none;">
								
								<ul class="none-haeun-ul" id="alrimList" style="max-height:430px; overflow-y:auto;">
								<li class="firstLi">
									<div style="
									    margin: 0px;
									    margin-left: 17px;
									    margin-right: 17px;
									    height: 36px;
									    margin-top: 14px;
									    display: flex;
									    justify-content: space-between;
									    align-items: center;
									">
								
									    <span>
									        알림
									    </span>
									    <span>
									    <button id="alrimDeleteBtn" onclick="deleteAlrim()" >
									        <img class="img-haeun4" src="/resources/pic/img/bin_icon.png" width="23" height="25" alt="알림 삭제 버튼 이미지">
									    </button>
									    </span>
									</div>	
									</li>
									<c:if test="${empty alrim2}">
										<li>
											알림이 없습니다.
										</li>
									</c:if>
									<c:forEach var="alrim2" items="${alrim2 }">
									
									<li class="none-haeun-li" id="alrim_${alrim2.notiId }">
										<a class="none-haeun-a" href=${alrim2.notiUrl } style="font-size: 18px;">
										
											<div class="notification" >
											 	<div class="notiInfo notiContainer">
											 	
												 	<c:if test="${alrim2.notiType == '키워드' || alrim2.notiType == '가격 변동' }">
												 		<div class="notiPicContainer">
															<img class="notiPic" src="/resources/pic/postPic/${alrim2.fileName }" onerror="this.onerror=null; this.src='/resources/pic/img/cabbage_icon.png'" 
																	alt="알림 이미지">
														</div>
													</c:if>
													<c:if test="${alrim2.notiType == '문의글 답변'}">
												 		<div class="notiPicContainer">
															<img class="notiPic" src="/resources/pic/img/munigeul_icon.png" onerror="this.onerror=null; this.src='/resources/pic/img/cabbage_icon.png'" 
																	alt="알림 이미지">
														</div>
													</c:if>
										 			<c:if test="${alrim2.notiType == '온도 변동'}">
												 		<div class="notiPicContainer">
															<img class="notiPic" src="/resources/pic/img/ondo_icon.jpg" onerror="this.onerror=null; this.src='/resources/pic/img/cabbage_icon.png'" 
																	alt="알림 이미지">
														</div>
													</c:if>
										 			<div class="titleContainer">
										 				<span class="title-small" > [${alrim2.notiType } 알림] </span>
										 				<div class="notiContentContainer">
										 					<span  class="notiContent-small"> ${alrim2.notiContent } </span>
										 				</div>
										 			</div>
													
										 		</div>
										 	</div>
										
											<%-- <c:if test="${alrim2.notiType == '키워드' || alrim2.notiType == '가격 할인' }">
											
											<img class="" src="/resources/pic/postPic/${alrim2.fileName }" 
													onerror="this.onerror=null; this.src='/resources/pic/img/cabbage_icon.png'" width="60" height="60" alt="알림 삭제 버튼 이미지">
											</c:if>
											<div class="alrimContainer" style="display:inline-block;">
											<b>[${alrim2.notiType } 알림]</b>
												<span class="alrimContent">${alrim2.notiContent }</span>
											</div> --%>
											
									    </a>
									    <button onclick="goNotiDelete('${alrim2.notiId}')" id="goNotiDelete" style="display:none;">
									    	X
									    </button>
									</li>		
									
									</c:forEach>
											
								</ul>
							</div>
							<button type="button" class="button-haeun1" onclick="toggleDiv('alrim')" >
									<img class="img-haeun4" src="/resources/pic/img/noti_icon.png" width="23" height="25" alt="마이페이지 버튼 이미지">
									<div id="someElement" data-count="${alrim }">
							            <span class="alrim alrimCount" id="alrimCount">  
							                ${alrim}
							            </span>
									</div>
							</button>
							</c:if>
				<c:if test="${empty userId}">
					<a type="button" href="/user/login"
						class="button-haeun1">
						<img class="img-haeun4" src="/resources/pic/img/user_icon.png"
							width="23" height="25" alt="마이페이지 버튼 이미지">
					</a>

				</c:if>
				<c:if test="${not empty userId}">
					

					<button type="button" onclick="toggleDiv('my')"
						class="button-haeun1">
						<img class="img-haeun4" src="/resources/pic/img/user_icon.png"
							width="23" height="25" alt="마이페이지 버튼 이미지">
						<div class="none-haeun" id="my">
						<!-- style="z-index: 1000; position:fixed; padding-top:120px;" -->
						<ul class="none-haeun-ul">
							<li class="none-haeun-li mypage"><a class="none-haeun-a" href="/user/myInfo">
									마이페이지 </a></li>
							<li style="margin-top: 4px;"><a class="none-haeun-a"
								onclick="logout()" href="/user/logout"> 로그아웃 </a></li>
						</ul>
					</div>
					</button>

				</c:if>

				<!-- </span> -->
				<a class="button-haeun1" href="/chat">
					<img class="img-haeun3" src="/resources/pic/img/cabbage_icon.png"
						alt="배추톡 버튼 이미지"> 배추톡
						<%-- <div id="someElement" data-count="${unreadChatCount}">
						<span class="alrim" id="alrimCount" >${unreadChatCount}</span>
					</div> --%>
					<c:if test="${unreadChatCount > 0}">
					    <div id="someElement" data-count="${unreadChatCount}">
					        <span class="alrim" id="alrimCount">${unreadChatCount}</span>
					    </div>
					</c:if>
				</a>
				<!-- <a class="a-haeun3" href="">
						<img class="img-hauen4" src="/resources/pic/img/cabbage_icon.png" width="23" height="25" alt="마이페이지 버튼 이미지">
						"MY"
					</a> -->
				<a class="button-haeun1" href="/post/create">
					<img class="img-haeun3" src="/resources/pic/img/cabbage_icon.png"
						alt="마이페이지 버튼 이미지"> 글쓰기
				</a>
			</div>
		</div>
	</div>
</div>
<c:if test="${not empty userId}">
<script src="/resources/js/noti/notisse.js"></script>	

</c:if>
<!-- <script src="/resources/js/noti/noti.js"></script>	-->

<script src="/resources/js/main/header.js" type="text/javascript"></script>
<script src="/resources/js/search/search_header.js"></script>
