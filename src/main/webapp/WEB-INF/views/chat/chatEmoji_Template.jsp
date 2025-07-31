<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
	List<Map>으로 받자
	<emojiId, emojiFileName>
 --%>
<ul class="css-1ilp7b6">
	<c:forEach var="emoji" items="${emojis }">
		<li class="common-bg-hover css-1x1qfn4" data-emoji_id="${emoji.EMOJI_ID }">
			<img class="sticker-img" src="/resources/pic/emoji/${emoji.FILE_NAME }" alt="sticker">
		</li>
	</c:forEach>
</ul>