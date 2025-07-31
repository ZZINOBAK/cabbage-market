//--------------------------------------- 전역변수 ----------------------------------------------]
var menuContainerInChatRoomState = "hidden"; // 채팅방 안에 나가기 버튼 있는 박스 보이는지 여부 ('hidden' / 'display')
var userId; // 현재 유저 아이디
var eventSource;
var countContinue = true; // 메세지에 읽음 처리할 때 상대방의 채팅이 나오면 그만하기 위한 전역변수
var myMessageCount; // 메세지 불러올 때 읽음 처리를 위한 전역변수 (채팅방마다 초기화)
//---------------------------------------- 이벤트 -----------------------------------------------
$(() => {
  // 초기화
  init();

  // 채팅창 프레임 이벤트 활성화
  frameEventOn();
});

//------------------------------------------- 함수 ---------------------------------------------
// ---- 첫 시작때 초기화 할 것들 함수
function init() {
  // 현재 로그인중인 사용자 아이디를 상수로 저장
  userId = $("#root").data("user_id");

  // SSE를 위한 EventSource 생성 (subscribe 요청 보내짐)
  if (userId != undefined) {
    eventSource = new EventSource("/chat/subscribe/" + userId);
  }

  // 첫 화면도 히스토리에 추가
  window.history.pushState({ href: document.URL }, "", document.URL);
  // 우측 상단 메뉴상자 감추기
  $(".css-1ca43ff .menu-container").css("display", "none");

  // 현재 URL로 ajax 요청보냄
  let href = $(location).attr("href");
  loadURLWithHistory(href);

  // sse 이벤트 받기 시작
  sseEventOn();

  // 1분마다 채팅방 리스트에 시간 없데이트 하는 타이머 추가
  setInterval(updateTimeInChatRoomList, 60000);

  // 안읽은 메세지가 있는지 체크 후, 왼쪽 상단에 쩜 보이든 말든
  checkIfUnreadMessageExist();
}

// ---- 채팅 패이지의 프레임 클릭 이벤트 활성화
function frameEventOn() {
  // 뒤로가기, 앞으로 가기 버튼 버튼 눌렀을 때 현재 URL에 맞게 다시 ajax
  $(window).on("popstate", function (e) {
    if (e.originalEvent.state) loadURL(e.originalEvent.state.href);
  });

  // 오른쪽 상단 내 닉네임 클릭
  clickToShowLogoutContainerOn();

  // 채팅방 리스트, 왼쪽 상단에 내 프사 클릭 (바뀌니까 부모요소로 잡음),
  $("ul.css-8lfz6g").on("click", ".css-y6c1l4", function (e) {
    e.preventDefault(); // 링크 클릭되는거 차단

    let $unreadCountDiv = $(this).find(".unread-count-wrapper");
    setUnreadCount($unreadCountDiv, "0");

    let href = $(this).attr("href");
    loadURLWithHistory(href);
  });

  $(".css-1plme8k .sidebar .anchor").on("click", function (e) {
    e.preventDefault();
    let href = $(this).attr("href");
    loadURLWithHistory(href);
  });

  // 채팅방 안에 나가기 버튼 나오는 쩜 3개 클릭 (계속 바뀌니까 부모요소로 잡음)
  clickToShowExitContainerOn();

  // 이모티콘 버튼 클릭
  emojiListBtnClickEventOn();

  // 텍스트 박스에 입력시 (계속 바뀌니까 부모요소로 잡음) (엔터키 입력 시 전송버튼 클릭)
  $("section.css-am8mw7").on("keydown", "textarea.css-10fmtiz", function (e) {
    if (e.keyCode == 13) {
      // 쉬프트 엔터는 허용
      if (!e.shiftKey) {
        e.preventDefault();
        $(".css-1useanf").trigger("click");
      }
    }
  });

  // 메세지 보내는 텍스트 칸에 변화 발생 시 글자 수 체크
  $("section.css-am8mw7").on("keyup keypress change input", "textarea.css-10fmtiz", function (e) {
    textAreaChanged();
  });

  // 사진 전송 버튼 클릭하고 이미지 선택 시 (바로 사진 전송)
  $("section.css-am8mw7").on("input", "form.css-1ckh9yi input", function () {
    let maxFileSize = 1024 * 1024 * 10; // 10MB
    // 파일 타입, 크기 체크
    let file = $(this).get(0).files[0];
    let fileType = file.type;
    let fileSize = file.size;
    if (!fileType.includes("image")) {
      clearFileInput();
      alertNotification("이미지 파일만 보낼 수 있어요.");
      return;
    } else if (fileSize > maxFileSize) {
      clearFileInput();
      alertNotification("10MB이하의 사진만 보낼 수 있어요.");
      return;
    }

    sendPic();
  });

  // 메세지 전송 버튼 클릭 시 (계속 바뀌니까 부모요소로 잡음)
  $("section.css-am8mw7").on("click", ".css-1useanf", function (e) {
    e.preventDefault();

    // 비활성화 상태면 차단
    if ($(this).hasClass("disable")) return;
    // 텍스트박스가 비어있으면 차단
    if ($("textarea.css-10fmtiz").val().trim().length == 0) return;

    sendText();
  });

  // 채팅창에 이미지 클릭하면 새탭에 띄우기
  $("section.css-am8mw7").on("click", "img.image-element", function (e) {
    e.preventDefault();
    let url = $(this).attr("src");
    window.open(url, "_blank");
  });

  // 메세지 버튼 클릭 하면
  $("section.css-am8mw7").on("click", "button.toolbox", function () {
    let $nowChatDiv = $(this).closest(".for-containment");

    let messageMenuHTML = makeMessageMenuHTML($nowChatDiv);

    $(this).before(messageMenuHTML);

    // 내용 없이 생성됐으면 클래스 없애버림 (메뉴버튼 계속 떠있는거 방지)
    if ($(".message-menu-box").children().length < 1)
      $(".message-menu-box").removeClass("message-menu-box");

    documentMouseDownEventOn();
  });

  // 복사 버튼 클릭
  $("section.css-am8mw7").on("click", ".duplicateBtn", function (e) {
    copyMessageText($(this));

    hideMessageMenu();
  });

  // 메세지 메뉴의 삭제 버튼 클릭
  $("section.css-am8mw7").on("click", ".deleteBtn", function (e) {
    let countByRoom = $(this).closest("[data-countbyroom]").attr("data-countbyroom");

    askAgainBeforeDelete(countByRoom);

    //deleteMessage(chatRoomId, countByRoom); // 삭제 요청보내기
    hideMessageMenu();
  });

  // 상대 채팅 알림 클릭하면 스크롤 바텀으로 하고, 알림 지우기
  $("section.css-am8mw7").on("click", ".partnerChatNoti", function (e) {
    scrollBottom();
    removePartnerChatNoti();
  });

  // 결제 전 가격 변동 인풋에 입력하는 이벤트
  inputOnPayEventOn();

  // 결제요청 취소 버튼 클릭 이벤트
  $("section.css-am8mw7").on("click", ".for-containment.right .payCancelBtn", function (e) {
    let countByRoom = $(this).closest(".for-containment.right").attr("data-countbyroom");
    askagainBeforeCancelPay(countByRoom);
  });

  // 결제하기 버튼 클릭
  $("section.css-am8mw7").on("click", ".for-containment.left .payBtn", function (e) {
    let $payDiv = $(this).closest(".for-containment.left");
    openPayPage($payDiv);
  });
}

function cancelPay(countByRoom) {
  let chatRoomId = getChatRoomId();
  let messageType = "PAY";
  let payStatus = "CANCEL";

  $.ajax({
    url: "/chatMessage/cancelPay",
    type: "POST",
    data: {
      chatRoomId: chatRoomId,
      countByRoom: countByRoom,
      messageType: messageType,
      payStatus: payStatus,
    },
    dataType: "text",
    success: function (data) {
      if (data == "success") {
        alertNotification("결제요청이 취소되었어요.");
      } else if (data == "fail") {
        // payStatus가 WAITING이 아님 (혹은 다른 사용자가 요청함)
        alertNotification("취소할 수 있는 상태가 아니에요.");
      }
    },
  });
}

function checkIfPartnerWithdrwal() {
  // 온도가 안뜬다는거는 탈퇴한 회원이라는 뜻
  if ($(".temperature").length < 1) {
    let $textarea = $("textarea.css-10fmtiz");
    $textarea.attr("disabled", "disabled");
    $textarea.prop("placeholder", "탈퇴한 회원에게는 메세지를 보낼 수 없어요.");
    $("input[name='uploadPic']").attr("disabled", "disabled");
    $("button.sticker-button").attr("disabled", "disabled");
  }
}

function clickToShowExitContainerOn() {
  $("section.css-am8mw7").on("click.exitBtn", ".css-1c3oejv .css-1idbtsb", function () {
    $("section.css-am8mw7").off("click.exitBtn");
    $(".css-1idbtsb .option-container").css("display", "block");
    setTimeout(function () {
      clickToCloseExitOn();
    }, 100);
  });
}

function clickToShowLogoutContainerOn() {
  // 오른쪽 상단 내 닉네임 클릭
  $(".css-1ca43ff").on("click", function (e) {
    $(".css-1ca43ff").off("click");
    $(".css-1ca43ff .menu-container").css("display", "block");
    setTimeout(function () {
      clickToCloseLogOutOn();
    }, 100);
  });
}

function showEmojiList() {
  emojiListBtnClickEventOff();
  $.ajax({
    url: "/chat/emojiList",
    type: "POST",
    dataType: "html",
    success: function (emojiListHTML) {
      $(".sticker-button").after(emojiListHTML);
      clickToCloseEmojiListOn();
    },
  });
}

function updateTimeInChatRoomList() {
  let chatRoomIdList = [];
  let $chatRoomlist = $("ul.css-8lfz6g");
  $chatRoomlist.find("[data-chat_room_id]").each(function (idx, chatRoomOnList) {
    chatRoomIdList.push($(chatRoomOnList).attr("data-chat_room_id"));
  });
  $.ajax({
    url: "/chat/updateTimeTerm",
    data: { chatRoomIdList: chatRoomIdList },
    type: "POST",
    success: function (chatRoomList) {
      chatRoomList.forEach(function (chatRoom) {
        let newLastMessageTime = chatRoom.lastMessageTime;
        let $chatRoomOnList = $chatRoomlist.find(
          "[data-chat_room_id='" + chatRoom.chatRoomId + "']"
        );
        changeTimeOnRoomInList($chatRoomOnList, newLastMessageTime);
      });
    },
  });
}

function hideMessageMenu() {
  $("div.message-menu-box").remove();
}

function askagainBeforeCancelPay(countByRoom) {
  let checkModalHTML = makeCancelPayModalHTML(countByRoom);
  $(".css-143d18r").children().eq(0).html(checkModalHTML);
  modalClickEventOn("CANCEL_PAY");
}

function askAgainBeforePayRequest(postPrice) {
  let checkModalHTML = makePayRequestModalHTML(postPrice);
  $(".css-143d18r").children().eq(0).html(checkModalHTML);
  modalClickEventOn("PAY");
}

function askAgainBeforeExit(lastCountByRoom) {
  let checkModalHTML = makeExitCheckModalHTML(lastCountByRoom);
  $(".css-143d18r").children().eq(0).html(checkModalHTML);
  modalClickEventOn("EXIT");
}

function askAgainBeforeDelete(countByRoom) {
  let checkModalHTML = makeDeleteCheckModalHTML(countByRoom);
  $(".css-143d18r").children().eq(0).html(checkModalHTML);
  modalClickEventOn("DEL");
}

function modalClickEventOn(type) {
  $("div.css-uthtbs").on("click", function (e) {
    let $target = $(e.target);
    // 배경이나 취소버튼 클릭하면 모달 닫기
    if ($target.hasClass("css-uthtbs") || $target.hasClass("cancel")) {
      closeModal();
    } else if ($target.hasClass("confirm")) {
      if (type == "DEL") {
        let chatRoomId = getChatRoomId();
        let countByRoom = $target.attr("data-delete_count_by_room");
        deleteMessage(chatRoomId, countByRoom);
      } else if (type == "EXIT") {
        // TODO 나가기 일때 로직 구성
        let lastCountByRoom = $target.attr("data-last_count_by_room");
        exitChatRoom(lastCountByRoom);
      } else if (type == "PAY") {
        // send(pay)
        let finalPriceInput = $("input#finalPrice").val();
        sendPayRequest(finalPriceInput);
      } else if (type == "CANCEL_PAY") {
        let countByRoom = $target.attr("data-count_by_room");
        cancelPay(countByRoom);
      }
      closeModal();
    } else {
      return;
    }
    modalClickEventOff();
  });
}

function sendPayRequest(finalPriceInput) {
  let chatRoomId = getChatRoomId();
  let senderId = userId;
  let messageContent = "PAY_CHAT";
  let messageType = "PAY";
  let payStatus = "WAITING";
  $.ajax({
    url: "/chatMessage/send",
    type: "POST",
    data: {
      chatRoomId: chatRoomId,
      senderId: senderId,
      messageContent: messageContent,
      messageType: messageType,
      finalPriceInput: finalPriceInput,
      payStatus: payStatus,
    },
    success: function () {
      // 할거 있으면 추가
    },
  });
}

function exitChatRoom(lastCountByRoom) {
  let chatRoomId = getChatRoomId();
  $.ajax({
    url: "/chat/exit",
    data: { chatRoomId: chatRoomId, exitPoint: lastCountByRoom },
    type: "POST",
    dataType: "text",
    success: function (data) {
      if (data == "success") {
        location.href = "/chat";
      }
    },
  });
}

function clickToCloseExitOn() {
  $(document).on("click.exit", function (e) {
    let $target = $(e.target);
    // 나가기 박스 클릭한거면 안닫음
    if ($target.hasClass("option-container")) {
      e.preventDefault();
      return;
      // 나가기 클릭한거면 나가기
    } else if ($target.hasClass("exit")) {
      e.preventDefault();
      $(document).off("click.exit");
      let lastCountByRoom = $(".css-13cllyv")
        .find("[data-countbyroom]:last-of-type")
        .attr("data-countbyroom");
      askAgainBeforeExit(lastCountByRoom);
      // 결제 요청 클릭한거면
    } else if ($target.hasClass("payRequest")) {
      e.preventDefault();
      $(document).off("click.exit");
      let postPrice = $(".reserved-price").text();
      askAgainBeforePayRequest(postPrice);
    } else if ($target.hasClass("css-1idbtsb")) {
      e.preventDefault();
    }
    $(document).off("click.exit");
    removeExit();
  });
}

function clickToCloseLogOutOn() {
  $(document).on("click.logout", function (e) {
    let $target = $(e.target);
    // 로그아웃 박스 클릭한거면 안닫음
    if ($target.hasClass("last-menu")) {
      return;
      // 로그아웃 클릭한거면 로그아웃하고 이벤트 off
    } else if ($target.hasClass("logout-menu")) {
      e.preventDefault();
      $(document).off("click.logout");
      // 로그아웃
      location.href = "/user/logout";
    } else if ($target.hasClass("css-1ca43ff")) {
      e.preventDefault();
    }
    $(document).off("click.logout");
    removeLogout();
  });
}

function removeExit() {
  $(".option-container").css("display", "none");
  clickToShowExitContainerOn();
}

function removeLogout() {
  $(".menu-container").css("display", "none");
  clickToShowLogoutContainerOn();
}

function emojiListBtnClickEventOn() {
  $("section.css-am8mw7").on("click.stickerBtn", ".sticker-button", function () {
    showEmojiList();
  });
}

function clickToCloseEmojiListOn() {
  $(document).on("click", function (e) {
    let $target = $(e.target);
    // 이모지 박스 클릭한거면 안닫음
    if ($target.hasClass("css-1ilp7b6")) {
      e.preventDefault();
      return;
      // 이모지 클릭한거면 보내고 이벤트 off
    } else if ($target.hasClass("sticker-img")) {
      e.preventDefault();
      $(document).off("click");
      sendEmoji($target);
      removeEmojiList();
      return;
    } else {
      $(document).off("click");
      removeEmojiList();
    }
  });
}

function removeEmojiList() {
  $("ul.css-1ilp7b6").remove();
  emojiListBtnClickEventOn();
}

function emojiListBtnClickEventOff() {
  $("section.css-am8mw7").off("click.stickerBtn");
}

function modalClickEventOff() {
  $("div.css-uthtbs").off("click");
}

function closeModal() {
  $("div.css-uthtbs").remove();
}

function deleteMessage(chatRoomId, countByRoom) {
  $.ajax({
    url: "/chatMessage/delete",
    type: "POST",
    data: { chatRoomId: chatRoomId, countByRoom: countByRoom },
    dataType: "text",
    success: function (data) {
      if (data == "timeOut") {
        // TODO : 15분이 지나서 삭제할수 없다는 알림 띄우기
        alertNotification("전송 후 5분이 지난 메세지는 삭제할 수 없어요.");
      } else if (data == "notMine") {
        // TODO : 자신의 메세지가 아닌 것은 지울 수 없다는 알림 띄우기
        alertNotification("상대방의 메세지는 삭제할 수 없어요.");
      } else {
        alertNotification("메세지를 삭제했어요.");
      }
    },
  });
}

function copyMessageText($clickedBtn) {
  let textToCopy = $clickedBtn.closest(".message-wrapper").find(".message-box").text();
  let tempTextarea = $("<textarea id='temp'>");
  $("body").append(tempTextarea);
  tempTextarea.val(textToCopy).select();
  document.execCommand("copy");
  tempTextarea.remove();

  alertNotification("메시지를 복사했어요.");
}

function scrollEventToRemovePartnerChatNotiOn() {
  $(".css-13cllyv").on("scroll.partnerChatNoti", function () {
    let $chatContainer = $(this);

    let isScrollBottom =
      Math.ceil($chatContainer.scrollTop() + $chatContainer.innerHeight()) >=
      $chatContainer.get(0).scrollHeight;

    if (isScrollBottom) {
      removePartnerChatNoti();
    }
  });
}

function removePartnerChatNoti() {
  $(".partnerChatNoti").remove();
  $(".css-13cllyv").off("scroll.partnerChatNoti");
}

// 스크롤 떠있을 때 상대가 채팅보내면 알림
function alertPartnerChatNoti() {
  let partnerChatNoti = makePartnerChatNotiHTML();
  $(".css-voabwl").append(partnerChatNoti);
  scrollEventToRemovePartnerChatNotiOn();
}

// 내용을 포함하는 알림창을 띄웠다가 없앰
function alertNotification(content) {
  let notiHTML = makeNotiHTML(content);
  $("section.css-am8mw7").after(notiHTML);
  setTimeout(function () {
    $(".css-1e7vfs6").addClass("fade-out");
    setTimeout(function () {
      $(".css-1e7vfs6").remove();
    }, 500);
  }, 2000);
}

// 화면 클릭하면 메세지 메뉴창 사라짐
function documentMouseDownEventOn() {
  $(document).on("mousedown", function (e) {
    let $target = $(e.target);

    // 메세지 메뉴 누른거면 그쪽 이벤트에서 처리하고 삭제
    if ($target.hasClass("message-menu-item")) {
      documentMouseDownEventOff();
      return;
    } else {
      hideMessageMenu();
      //$("button.toolbox").css("display", "");
      documentMouseDownEventOff();
    }
  });
}

function documentMouseDownEventOff() {
  $(document).off("mousedown");
}

// 채팅방 리스트에 안읽은 메세지 개수 설정 (그리고 안읽은 채팅 유무에 대해 왼쪽 상단 프사에 쩜 표시)
function setUnreadCount($unreadCountDiv, value) {
  $unreadCountDiv.attr("data-count", value);
  $unreadCountDiv.text(value);

  checkIfUnreadMessageExist();
}

function checkIfUnreadMessageExist() {
  let isExist = false;
  $("ul.css-8lfz6g .unread-count-wrapper").each(function () {
    if ($(this).attr("data-count") > 0) {
      isExist = true;
      return;
    }
  });

  // 안읽은 메세지 없으면 쩜 숨기기
  if (isExist) $(".sidebar .unread-count").css("display", "");
  else $(".sidebar .unread-count").css("display", "none");
}

// ---- 스크롤로 채팅 불러오는 이벤트 활성화
// $(this).prop("scrollHeight") * 0.2
function scrollEventToLoadChatListOn() {
  $(".css-13cllyv").on("scroll.loadChat", function () {
    if ($(this).scrollTop() <= 200) {
      // 스크롤 막기 (ajax 로드 완료하고 풀어줌)
      disableScrolling();
      scrollEventToLoadChatListOff();
      // 현재 불러온 마지막 카운트를 찾아서 리스트 요청
      let lastCount = $(this).find("[data-countByRoom]").eq(0).attr("data-countByRoom");
      getChatMessageList(lastCount);
    }
  });
}

// 스크롤 이벤트 끄기
function scrollEventToLoadChatListOff() {
  $(".css-13cllyv").off("scroll.loadChat");
}

// 스크롤 막기 (계속 고정된 스크롤로 위치하게 한다)
function disableScrolling() {
  let nowScrollTop = $(".css-13cllyv").scrollTop();
  $(".css-13cllyv").on("scroll.disable", function () {
    $(".css-13cllyv").scrollTop(nowScrollTop);
  });
}

// 스크롤 활성화
function enableScrolling() {
  $(".css-13cllyv").off("scroll.disable");
}

// ---- 채팅방에 연결하고 현재 보이는 채팅방 버튼 seleceted 처리
function loadURL(href) {
  // ajax 요청 하기 전에 스크롤로 메세지 로드하는 이벤트 종료시킴
  scrollEventToLoadChatListOff();

  $.ajax({
    url: href,
    type: "POST",
    dataType: "html",
    success: (data) => {
      $("section.css-am8mw7").html(data);
      getChatMessageList();
      afterLoadURL(href);
    },
  });
}

// ---- 직접 눌러서 채팅방 불러올 시 채팅방 불러오고, 히스토리 추가
function loadURLWithHistory(href) {
  loadURL(href);
  // 브라우저 URI 변경 (history 추가)
  window.history.pushState({ href: href }, "", href);
}

// ---- ajax로 채팅방 가져오고 초기화 해야할 것들 함수
function afterLoadURL(href) {
  if (href.includes("http") && !href.endsWith("/chat")) {
    let arr = href.split("/");
    href = "/" + arr[3] + "/" + arr[4] + "/" + arr[5];
  }
  // 채팅방 selected 설정
  setNowChatRoomToSelected();

  // 채팅방 나가기 메뉴박스 감추기 (상태 변수도 hidden으로 초기화)
  $(".css-1idbtsb .option-container").css("display", "none");
  menuContainerInChatRoomState = "hidden";

  countContinue = true; // 메세지에 읽음 처리할 때 상대방의 채팅이 나오면 그만하기 위한 전역변수
  myMessageCount = 0; // 메세지 불러올 때 읽음 처리를 위한 전역변수 (채팅방마다 초기화)

  // 채팅방 불러 왔으니까 해당 채팅방에 다 읽음처리 (/chat 요청일 때 빼고)
  if (!href.endsWith("/chat")) readOldMessages(getChatRoomId(), userId);

  // 상대가 탈퇴한 상태면 입력창 비활성화
  checkIfPartnerWithdrwal();
}

// ---- 텍스트박스에 입력하면 글자수 변화, 버튼 (비)활성화
function textAreaChanged() {
  $textArea = $("textarea.css-10fmtiz");
  let length = $textArea.val().length;
  let trimLength = $textArea.val().trim().length;

  $("span.text-length").text(length + "/1000");
  if (trimLength > 0 && trimLength <= 1000) {
    $("button.css-1useanf").removeClass("disable");
  } else {
    $("button.css-1useanf").addClass("disable");
  }
}

// ---- SSE로 이벤트 발생 했을 때 캐치하는 것들 모음
function sseEventOn() {
  // chatMessage라는 이름의 이벤트 발생
  // 메세지 도착
  $(eventSource).on("chatMessage", function (e) {
    //======================  모든 상황에서 실행 =========================
    e = e.originalEvent; // jquery 객체에서 DOM 객체로 변경

    let $chatContainer = $(".css-13cllyv");

    const data = JSON.parse(e.data); // data가 chatMessage다

    let chatRoomId = data.chatRoomId;
    let messageType = data.messageType;
    let payStatus = data.payStatus;
    let $chatRoomOnList = $("[data-chat_room_id='" + chatRoomId + "'");

    // 현재 리스트에 방금 채팅 온 채팅방이 없으면, ajax로 데이터 요청하고 리스트에 만들기
    if ($chatRoomOnList.length == 0) {
      addNewChatRoomInList(chatRoomId, data);
    }

    // 현재 리스트 채팅방에 몇분전 텍스트 변경 (메세지 삭제가 아닐 때만)
    if (messageType != "DEL" && payStatus != "CANCEL" && payStatus != "FINISH") {
      setTimeTermOnRoomInList($chatRoomOnList, data);
    }

    // 현재 리스트 채팅방에 countByRoom 데이터, 최근 메세지 내용 변경, 안읽은 메세지 1 증가 (현재 보다 크면)
    // (새로운 메세지가 왔더가 가장 최근의 메세지가 삭제됐을 때만 실행)
    let countByRoom = data.countByRoom;
    let lastCountByRoom = $chatRoomOnList.attr("data-message_count_by_room");

    if (countByRoom >= lastCountByRoom) {
      // countByRoom 변경
      $chatRoomOnList.attr("data-message_count_by_room", countByRoom);

      // 현재 리스트 채팅방에서 최근 메세지 내용 변경
      switch (messageType) {
        case "CHAT":
          $chatRoomOnList.find(".preview-description .description-text").text(data.messageContent);
          break;
        case "PIC":
          $chatRoomOnList.find(".preview-description .description-text").text("사진을 보냈습니다.");
          break;
        case "DEL":
          $chatRoomOnList
            .find(".preview-description .description-text")
            .text("삭제된 메세지입니다.");
          break;
        case "EMOJI":
          $chatRoomOnList
            .find(".preview-description .description-text")
            .text("이모티콘을 보냈습니다.");
          break;
        case "PAY":
          changeLastMessageWhenPay($chatRoomOnList, payStatus);
      }

      if (messageType != "DEL" && payStatus != "CANCEL" && payStatus != "FINISH") {
        // 현재 리스트 채팅방에서 안읽은 메세지 1 증가 (지금 활성화된 채팅방이면 말고)
        if (!$chatRoomOnList.find("a.css-y6c1l4").hasClass("selected")) {
          let $unreadCountDiv = $chatRoomOnList.find(".unread-count-wrapper");
          let beforeCount = Number($unreadCountDiv.attr("data-count"));
          setUnreadCount($unreadCountDiv, beforeCount + 1);
        }
        // 채팅방 리스트에서 지금 채팅온 방 맨 위로 올리기
        $("ul.css-8lfz6g").prepend($chatRoomOnList);
      }
    }

    // 현재 채팅방에 온 메세지가 아니라면 채팅방에 추가하지 않음
    let nowChatRoomId = getChatRoomId();

    if (chatRoomId != nowChatRoomId) {
      return;
    }
    //======================  메세지가 들어온 채팅방에 들어와 있으면 실행 =========================

    // 메세지 삭제 이벤트면 해당 메세지만 삭제로 변경하고 끝
    if (messageType == "DEL") {
      let $deletedChatDiv = $(".for-containment[data-countbyroom='" + countByRoom + "']");
      changeChatToDEL($deletedChatDiv);
      return;
      // 결제 정보가 변경된 경우도 해당 메세지만 변경하고 끝
    } else if (payStatus == "CANCEL" || payStatus == "FINISH") {
      let $canceledPayChatDiv = $(".for-containment[data-countbyroom='" + countByRoom + "']");
      changePayChatToCancel($canceledPayChatDiv, payStatus);
      return;
    }

    // 현재 스크롤이 바닥에 있으면 로드하고 스크롤 다시 바닥으로 내려줄거
    let isScrollBottom;
    if (
      Math.ceil($chatContainer.scrollTop() + $chatContainer.innerHeight()) >=
      $chatContainer.get(0).scrollHeight
    ) {
      isScrollBottom = true;
    } else {
      isScrollBottom = false;
    }

    // 메세지 만들어서 추가
    let messageHTML = makeMessageHTML(data);
    $chatContainer.append(messageHTML);
    changeByState("NEW");

    // 스크롤이 바닥이거나, 내가 메세지를 보낸거면 바닥으로 다시 보냄(유지)
    let $newChat = $chatContainer.children().eq(-1);
    if (isScrollBottom || $newChat.hasClass("right")) {
      scrollBottom();
    }

    // 스크롤이 떠있고, 상대 채팅이면 밑에 알림 생성
    if (!isScrollBottom && $newChat.hasClass("left")) {
      alertPartnerChatNoti();
    }

    // 현재 유저, 현재 채팅방에 있는 안읽은 메세지 개수 0으로 업데이트
    readOldMessages(chatRoomId, userId);
  });

  // 상대가 채팅을 방금 읽음
  $(eventSource).on("read", function (e) {
    e = e.originalEvent;

    let chatRoom = JSON.parse(e.data);
    let chatRoomId = chatRoom.chatRoomId;
    let nowChatRoomId = getChatRoomId();

    // 읽음 이벤트가 발생한 채팅방과 현재 채팅방이 다르면 리턴
    if (chatRoomId != nowChatRoomId) {
      return;
    }

    // 현재 채팅방에 가장 마지막 채팅에 읽음 추가
    let readHTML = makeReadHTML();
    let $lastChat = $(".css-13cllyv").children().eq(-1);

    // 마지막 채팅이 내 채팅일 때만 읽음 붙임
    if ($lastChat.hasClass("right")) {
      $(".css-13cllyv").find(".unread-text").remove();
      changeByState("NEW");
      // TODO : 읽음 있을 때 처음 불러올 떄 시간 두번나옴
      $lastChat.find("div.css-lty8rs").prepend(readHTML);
    }
  });

  // subscribe 성공
  $(eventSource).on("connect", function (e) {
    e = e.originalEvent; // jquery 객체에서 DOM 객체로 변경
  });
}

function changeLastMessageWhenPay($chatRoomOnList, payStatus) {
  switch (payStatus) {
    case "WAITING":
      $chatRoomOnList.find(".preview-description .description-text").text("결제요청을 보냈습니다.");
      break;
    case "CANCEL":
      $chatRoomOnList
        .find(".preview-description .description-text")
        .text("결제요청을 취소했습니다.");
      break;
    case "FINISH":
      $chatRoomOnList.find(".preview-description .description-text").text("결제가 완료되었습니다.");
      break;
  }
}

function setNowChatRoomToSelected() {
  let nowChatRoomId = getChatRoomId();
  $(".css-y6c1l4").removeClass("selected");
  $("li.css-v2yhcd[data-chat_room_id='" + nowChatRoomId + "']")
    .find(".css-y6c1l4")
    .addClass("selected");
}

function setTimeTermOnRoomInList($chatRoomOnList, chatMessage) {
  let newLastMessageTime = getmessageTimeTerm(chatMessage); // 몇 초전에 도착 메세지인지
  changeTimeOnRoomInList($chatRoomOnList, newLastMessageTime);
}

function changeTimeOnRoomInList($chatRoomOnList, newLastMessageTime) {
  if (newLastMessageTime < 60) {
    $chatRoomOnList.find(".sub-text span:eq(-1)").text(newLastMessageTime + "초 전");
  } else if (newLastMessageTime < 60 * 60) {
    $chatRoomOnList
      .find(".sub-text span:eq(-1)")
      .text(Math.floor(newLastMessageTime / 60) + "분 전");
  } else if (newLastMessageTime < 60 * 60 * 24) {
    $chatRoomOnList
      .find(".sub-text span:eq(-1)")
      .text(Math.floor(newLastMessageTime / 60 / 60) + "시간 전");
  } else if (newLastMessageTime < 60 * 60 * 24 * 30) {
    $chatRoomOnList
      .find(".sub-text span:eq(-1)")
      .text(Math.floor(newLastMessageTime / 60 / 60 / 24) + "일 전");
  } else if (newLastMessageTime < 60 * 60 * 24 * 30 * 12) {
    $chatRoomOnList
      .find(".sub-text span:eq(-1)")
      .text(Math.floor(newLastMessageTime / 60 / 60 / 24 / 30) + "달 전");
  } else {
    $chatRoomOnList
      .find(".sub-text span:eq(-1)")
      .text(Math.floor(newLastMessageTime / 60 / 60 / 24 / 30 / 12) + "년 전");
  }
}

function addNewChatRoomInList(chatRoomId, chatMessage) {
  $.ajax({
    url: "/chat/roomInList/" + chatRoomId,
    type: "POST",
    dataType: "json",
    success: function (data) {
      let chatRoom = data;
      let chatRoomInListHTML = makeChatRoomInListHTML(chatRoom);
      $("ul.css-8lfz6g").prepend(chatRoomInListHTML);

      // 현재 들어와있는 채팅방이면 셀렉트 처리
      setNowChatRoomToSelected();

      // 이것도 클라이언트 시간이랑 비교하기 위해 추가
      let $chatRoomOnList = $("ul.css-8lfz6g").find("li[data-chat_room_id='" + chatRoomId + "']");
      setTimeTermOnRoomInList($chatRoomOnList, chatMessage);
    },
  });
}

// 현재 채팅방에서 현재 유저 안읽은 메세지 개수 0으로 초기화
function readOldMessages(chatRoomId) {
  $.ajax({
    url: "/chatMessage/read",
    data: { chatRoomId: chatRoomId },
    type: "POST",
    success: function () {
      let $unreadCountDiv = $("li[data-chat_room_id='" + chatRoomId + "']").find(
        ".unread-count-wrapper"
      );
      setUnreadCount($unreadCountDiv, "0");
    },
  });
}

// 메세지의 시간과 현재 시간을 비교해서 몇 '초' 전에 도착한 메세지인지 구한다
function getmessageTimeTerm(chatMessage) {
  let time = chatMessage.messageTime;
  let chatDate = new Date(time); // UTC 에서 현재 시간으로 바뀐다
  let nowDate = new Date(); // 현재 클라이언트 시간

  let term = Math.floor((nowDate.getTime() - chatDate.getTime()) / 1000); // 밀리세컨에서 세컨드로 변경 후 소수점 버림
  return term;
}

function scrollBottom() {
  // 스크롤 맨 밑으로 (빈 화면일 때 빼고)
  if ($("div.css-13cllyv").length > 0) {
    let scrollHeight = $("div.css-13cllyv").get(0).scrollHeight;
    $("div.css-13cllyv").scrollTop(scrollHeight);
  }
}

function getChatRoomId() {
  let arr = $(location).attr("href").split("/");
  let chatRoomId = arr[arr.length - 1];

  return chatRoomId;
}

function clearFileInput() {
  $("input[name='uploadPic']").val("");
}

function inputOnPayEventOn() {
  $(document).on(
    {
      keyup: function () {
        formatCurrency($(this));
      },
    },
    "input[data-type='currency']"
  );
}

function formatNumber(n) {
  // format number 1000000 to 1,234,567
  return n.replace(/\D/g, "").replace(/\B(?=(\d{3})+(?!\d))/g, ",");
}

function formatCurrency(input, blur) {
  // appends $ to value, validates decimal side
  // and puts cursor back in right position.

  // get input value
  let input_val = input.val();

  // don't validate empty input
  if (input_val === "") {
    return;
  }

  // original length
  let original_len = input_val.length;

  // initial caret position
  let caret_pos = input.prop("selectionStart");

  // check for decimal
  if (input_val.indexOf(".") >= 0) {
    // get position of first decimal
    // this prevents multiple decimals from
    // being entered
    var decimal_pos = input_val.indexOf(".");

    // split number by decimal point
    var left_side = input_val.substring(0, decimal_pos);
    var right_side = input_val.substring(decimal_pos);

    // add commas to left side of number
    left_side = formatNumber(left_side);

    // validate right side
    right_side = formatNumber(right_side);

    // On blur make sure 2 numbers after decimal
    if (blur === "blur") {
      right_side += "00";
    }

    // Limit decimal to only 2 digits
    right_side = right_side.substring(0, 2);

    // join number by .
    input_val = "₩" + left_side + "." + right_side;
  } else {
    // no decimal entered
    // add commas to number
    // remove all non-digits
    input_val = formatNumber(input_val);
    input_val = "₩" + input_val;

    // final formatting
    if (blur === "blur") {
      input_val += ".00";
    }
  }

  // send updated string to input
  input.val(input_val);

  // put caret back in the right position
  let updated_len = input_val.length;
  caret_pos = updated_len - original_len + caret_pos;
  input[0].setSelectionRange(caret_pos, caret_pos);
}

function openPayPage($payDiv) {
  let chatRoomId = getChatRoomId();
  let countByRoom = $payDiv.attr("data-countbyroom");
  let postId;
  let finalPrice;

  $.ajax({
    url: "/chatMessage/dataForPayment",
    type: "POST",
    data: { chatRoomId: chatRoomId, countByRoom: countByRoom },
    dataType: "json",
    success: function (chatMessage) {
      // 찾아진 데이터가 없으면 리턴
      if (chatMessage.chatRoomId != chatRoomId) {
        alertNotification("결제 요청의 대상이 아닙니다.");
        return;
      }
      postId = chatMessage.postId;
      finalPrice = chatMessage.finalPrice;

      // ajax로 데이터 가져오고, success 시 실행
      let $tempForm = $("<form>", {
        id: "tempForm",
        action: "/post/directPayPage",
        method: "POST",
        target: "_blank",
      });

      // form이랑 같이 넘길 데이터
      let $data1 = $("<input>", { name: "chatRoomId", value: chatRoomId });
      let $data2 = $("<input>", { name: "countByRoom", value: countByRoom });
      let $data3 = $("<input>", { name: "postId", value: postId });
      let $data4 = $("<input>", { name: "finalPrice", value: finalPrice });

      // form에 추가
      $tempForm.append($data1);
      $tempForm.append($data2);
      $tempForm.append($data3);
      $tempForm.append($data4);

      // form을 만들고 submit (새창으로)
      $(document.body).append($tempForm);
      $("#tempForm").submit();

      // form 삭제
      $("#tempForm").remove();
    },
  });
}

//===================================== 채팅 메세지 관련 함수 ========================================

// ---- 이모지 보내기 (img 태그 받아옴)
function sendEmoji($clickedEmoji) {
  let chatRoomId = getChatRoomId();
  let emojiId = $clickedEmoji.parent().attr("data-emoji_id");
  let form = $("form.css-1ckh9yi")[0];
  let formData = new FormData(form);

  formData.append("chatRoomId", chatRoomId);
  formData.append("senderId", userId);
  formData.append("messageContent", "EMOJI_CHAT");
  formData.append("messageType", "EMOJI");
  formData.append("emojiId", emojiId);

  $.ajax({
    url: "/chatMessage/send",
    type: "POST",
    data: formData,
    contentType: false,
    processData: false,
    success: function () {
      // 할거없네
    },
  });
}

// ---- 사진 보내기
function sendPic() {
  let chatRoomId = getChatRoomId();
  let form = $("form.css-1ckh9yi")[0];
  let formData = new FormData(form);

  formData.append("chatRoomId", chatRoomId);
  formData.append("senderId", userId);
  formData.append("messageContent", "PIC_CHAT");
  formData.append("messageType", "PIC");

  $.ajax({
    url: "/chatMessage/send",
    type: "POST",
    data: formData,
    contentType: false,
    processData: false,
    success: function () {
      clearFileInput();
    },
  });
}

// ---- 텍스트 보내기
function sendText() {
  let chatRoomId = getChatRoomId();
  let form = $("form.css-1ckh9yi")[0];
  let formData = new FormData(form);
  let $textArea = $("textarea.css-10fmtiz");

  formData.append("chatRoomId", chatRoomId);
  formData.append("senderId", userId);
  formData.append("messageContent", $textArea.val());
  formData.append("messageType", "CHAT");

  $.ajax({
    url: "/chatMessage/send",
    type: "POST",
    data: formData,
    contentType: false,
    processData: false,
    success: function () {
      $textArea.val("");
      textAreaChanged();
    },
  });
}

// ---- 페이지를 받아서 현재 채팅방의 메세지 리스트를 가져온다 (출력까지 함)
function getChatMessageList(lastCount) {
  let arr = $(location).attr("href").split("/");
  let chatRoomId = arr[arr.length - 1];

  // URI가 /chat 인 경우는 아무것도 안함
  if (chatRoomId == "chat") return;

  let url = "/chatMessage/getList/" + chatRoomId;
  if (!lastCount) {
    lastCount = "";
  } else {
    url += "/" + lastCount;
  }

  // 맨 위에 있는 날짜 표시 지우고 시작함 (마지막에 맨 위에 다시 추가함)
  $(".css-13cllyv").children().eq(0).remove();

  $.ajax({
    url: url,
    type: "POST",
    dataType: "json",
    success: function (chatMessageList) {
      // 메세지 타입 : messageId, chatRoomId, senderId, messageContent , messageTime, messageType
      // 메세지 컨테이너 : $(".css-13cllyv")

      // 채팅 리스트를 순회하며 컨테이너에 엘리먼트를 추가한다
      chatMessageList.forEach((chatMessage) => {
        let messageHTML = makeMessageHTML(chatMessage);
        let partnerUnreadCount = chatMessage.partnerUnreadCount; // 상대가 안읽은 메세지의 개수

        $(".css-13cllyv").prepend(messageHTML);

        // 상대의 안읽은 메세지 개수로 어디까지 읽었는지 찾고 읽음표시 추가
        let $nowMessageDiv = $(".css-13cllyv").children().eq(0);
        // 내가 보낸 메세지만 카운트한다
        if (countContinue) {
          if ($nowMessageDiv.hasClass("right")) {
            // 상대가 안읽은 메세지의 개수랑 지금 카운트랑 같으면 읽음표시 추가
            if (myMessageCount == partnerUnreadCount) {
              let readHTML = makeReadHTML();
              $nowMessageDiv.find("div.css-lty8rs").prepend(readHTML);
            }
            myMessageCount++;

            // 상대 채팅이 나온순간 그만하기 (의미없음)
          } else if ($nowMessageDiv.hasClass("left")) {
            countContinue = false;
          }
        }

        changeByState("OLD");
      });

      // 다 불러오고 맨위에 날짜 무조건 추가 (다시 불러올때 지우고 시작함)
      addDateChat($(".css-13cllyv").children().eq(0));

      // 스크롤로 채팅 내용 불러오는 이벤트 활성화 (20개 이상의 데이터를 가져왔을 때만 (아니면 다 가져왔다는 뜻))
      if (chatMessageList.length >= 20) scrollEventToLoadChatListOn();

      // 스크롤 (처음 불러올 때만 바텀으로)
      if (lastCount == "") {
        scrollBottom();
      }

      // 스크롤 활성화 하기
      enableScrolling();
    },
    error: function () {
      // 다 불러오고 맨위에 날짜 무조건 추가 (다시 불러올때 지우고 시작함)
      addDateChat($(".css-13cllyv").children().eq(0));
      // 스크롤 활성화 하기
      enableScrolling();
    },
  });
}

//------------------------------------- 상태관련 함수 -------------------------------------
// ---- 상태에 따라 변경사항 변경하는 메인 함수
// 새로운 채팅에 대한건지 예전 채팅에 대한건지 (OLD, NEW)
function changeByState(type) {
  // 컨테이너에서 위에서 첫번째 채팅이랑 그 다음 채팅의 상태를 체크해서 변화주기
  // 날짜가 바꼈다거나, 같은 분에 보낸 채팅이거나 등등
  let $chatContainer = $(".css-13cllyv");

  let $newChat;
  let $oldChat;
  if (type == "OLD") {
    // 메세지의 최상위 div를 찾음
    $newChat = $chatContainer.children().eq(0);
    $oldChat = $chatContainer.children().eq(1);
  } else if (type == "NEW") {
    $newChat = $chatContainer.children().eq(-1);
    $oldChat = $chatContainer.children().eq(-2);
  }
  // 첫 메세지를 보내면 위에 날짜 추가 (내가 보낸 메세지일 때만 추가)
  if ($oldChat.length < 1) {
    if (type == "NEW" && $newChat.hasClass("right")) addDateChat($newChat);
    return;
  }

  let $newChatDateDiv = $newChat.find(".message-date");
  let $oldChatDateDiv = $oldChat.find(".message-date");

  // 같은 사람이 보낸 메세지라면 (방향이 같음)
  if ($newChat.attr("class") == $oldChat.attr("class")) {
    if ($newChatDateDiv.text() == $oldChatDateDiv.text()) {
      // 같은 시간(분)에 보낸거면
      sameTimeChange($newChat, $oldChat, type);
    }
  }
  // 날짜가 바뀌면
  if (!$newChat.hasClass("day-divider") && !$oldChat.hasClass("day-divider")) {
    if ($newChatDateDiv.attr("data-date") != $oldChatDateDiv.attr("data-date")) {
      if (type == "OLD") {
        addDateChat($oldChat);
      } else if (type == "NEW") {
        addDateChat($newChat);
      }
    }
  }

  // 상대 채팅 도착하면 모든 읽음 표시 삭제
  if (type == "NEW") {
    if ($chatContainer.children().eq(-1).hasClass("left")) {
      $chatContainer.find(".unread-text").remove();
    }
  }

  // 리스트 불러올 때 최근 보낸 메세지까지 같이 불러와지는거 방지
  if (type == "OLD") {
    if ($newChat.attr("data-countbyroom") == $oldChat.attr("data-countbyroom")) {
      $newChat.remove();
    }
  }
}

// ---- 같은 사람이 같은 시간에 보냈을 때 변경사항 반영하는 함수 (프사, 시간)
function sameTimeChange($newChat, $oldChat, type) {
  if ($newChat.hasClass("day-divider") || $oldChat.hasClass("day-divider")) {
    return;
  }
  if (type == "OLD") {
    $newChat.find(".message-date").css("visibility", "hidden");
    $oldChat.find(".css-17oljzs").css("visibility", "hidden");
  } else if (type == "NEW") {
    // 새로운 채팅일 때만 읽음 있으면 건너뜀
    if ($oldChat.find(".unread-text").length > 0 || $newChat.find(".unread-text").length > 0)
      return;
    $oldChat.find(".message-date").css("visibility", "hidden");
    $newChat.find(".css-17oljzs").css("visibility", "hidden");
  }
}

// ---- 날짜가 바뀌면 날짜 추가
function addDateChat($dateChangedChat) {
  let timeHTML = makeDateHTML($dateChangedChat);
  $dateChangedChat.before(timeHTML);
}
// ---- 내가 보낸 메세지인지 확인후 boolean 리턴
function isMyMessage(chatMessage) {
  return chatMessage.senderId == userId;
}

// UTC 시간을 현지 시간으로 바꾸고, 0000년 0월 0일/오전 0:00 으로 바꿔줌
function makeLocalDateString(time) {
  let date = new Date(time); // UTC 에서 현재 시간으로 바뀐다
  let rawHours = Number(date.getHours());
  let a;
  let hours;
  let minutes = ("0" + date.getMinutes()).slice(-2);
  if (rawHours >= 0 && rawHours < 12) {
    a = "오전";
    hours = rawHours;
  } else {
    a = "오후";
    hours = rawHours - 12;
  }
  if (hours == 0) hours = 12;

  let dateString =
    date.getFullYear() +
    "년 " +
    (date.getMonth() + 1) +
    "월 " +
    date.getDate() +
    "일/" +
    a +
    " " +
    hours +
    ":" +
    minutes;
  return dateString;
}

// 현재 채팅이 삭제 가능한지 리턴해준다 (15분이 지났는지 확인)
function checkIfCanBeDeleted($chatDiv) {
  let $thisDateDiv = $chatDiv.find(".message-date");
  let thisDateArr = $thisDateDiv.attr("data-date").split(" "); // 0000년, 0월, 00일
  let thisTimeArr = $thisDateDiv.text().split(" "); // 오전, 0:00

  let thisYear = thisDateArr[0].slice(0, -1);
  let thisMonth = Number(thisDateArr[1].slice(0, -1)) - 1;
  let thisDate = thisDateArr[2].slice(0, -1);

  let thisHour = Number(thisTimeArr[1].split(":")[0]);
  if (thisTimeArr[0] == "오전") {
    if (thisHour == 12) thisHour = 0;
    else thisHour = thisHour;
  } else if (thisTimeArr[0] == "오후") {
    if (thisHour == 12) thisHour = thisHour;
    else thisHour = thisHour + 12;
  }

  let thisMinute = thisTimeArr[1].split(":")[1];

  let messageDate = new Date(thisYear, thisMonth, thisDate, thisHour, thisMinute);
  let localDate = new Date();

  // 밀리세컨에서 분으로 변경 후 소수점 버림
  let term = Math.floor((localDate.getTime() - messageDate.getTime()) / 1000 / 60);

  if (term < 5) return true;
  else return false;
}
function changeChatToDEL($deletedChatDiv) {
  let delContentHTML = makeDelContentHTML();

  $deletedChatDiv.find(".message-wrapper").html(delContentHTML);
  //$deletedChatDiv.find("button.toolbox").remove();
}

function changePayChatToCancel($canceledPayChatDiv, payStatus) {
  let payContentHTML;
  switch (payStatus) {
    case "CANCEL":
      payContentHTML = makePayCANCELContentHTML();
      break;
    case "FINISH":
      payContentHTML = makePayFINISHContentHTML();
      break;
  }
  $canceledPayChatDiv.find(".message-wrapper").html(payContentHTML);
}
//------------------------------------- HTML 생성 함수 -------------------------------------
function makeMessageHTML(chatMessage) {
  let chatMessageType = chatMessage.messageType;
  // UTC 시간대로 넘어온거를 현지 시간으로 바꾸고, 출력할 형식에 맞는 문자열로 바꿔줌
  let localDateString = makeLocalDateString(chatMessage.messageTime);
  let resultHTML;

  switch (chatMessageType) {
    case "CHAT":
      resultHTML = makeChatHTML(chatMessage, localDateString);
      break;
    case "PIC":
      resultHTML = makePicHTML(chatMessage, localDateString);
      break;
    case "DEL":
      resultHTML = makeDelHTML(chatMessage, localDateString);
      break;
    case "EMOJI":
      resultHTML = makeEmojiHTML(chatMessage, localDateString);
      break;
    case "PAY":
      resultHTML = makePayHTML(chatMessage, localDateString);
      break;
  }

  return resultHTML;
}

function makePayHTML(chatMessage, localDateString) {
  let resultHTML;
  let payStatus = chatMessage.payStatus;
  switch (payStatus) {
    case "WAITING":
      if (isMyMessage(chatMessage)) {
        // 내가 보낸 메세면
        resultHTML = makeMyPayBeforeHTML(chatMessage, localDateString);
      } else {
        // 상대가 보낸 메세지면
        resultHTML = makePartnerPayBeforeHTML(chatMessage, localDateString);
      }
      break;
    case "CANCEL":
      if (isMyMessage(chatMessage)) {
        // 내가 보낸 메세면
        resultHTML = makeMyPayCANCELHTML(chatMessage, localDateString);
      } else {
        // 상대가 보낸 메세지면
        resultHTML = makePartnerPayCANCELHTML(chatMessage, localDateString);
      }
      break;
    case "FINISH":
      if (isMyMessage(chatMessage)) {
        // 내가 보낸 메세면
        resultHTML = makeMyPayFINISHHTML(chatMessage, localDateString);
      } else {
        // 상대가 보낸 메세지면
        resultHTML = makePartnerPayFINISHHTML(chatMessage, localDateString);
      }
      break;
  }
  return resultHTML;
}

function makeDelHTML(chatMessage, localDateString) {
  let resultHTML;
  if (isMyMessage(chatMessage)) {
    // 내가 보낸 메세면
    resultHTML = makeMyDelHTML(chatMessage, localDateString);
  } else {
    // 상대가 보낸 메세지면
    resultHTML = makePartnerDelHTML(chatMessage, localDateString);
  }
  return resultHTML;
}

function makeDateHTML($dateChangedChat) {
  let resultHTML =
    '<div class="day-divider">' +
    '<div class="date-text">' +
    $dateChangedChat.find(".message-date").attr("data-date") +
    "</div>" +
    "</div>";
  return resultHTML;
}

function makeEmojiHTML(chatMessage, localDateString) {
  let resultHTML;

  if (isMyMessage(chatMessage)) {
    // 내가 보낸 메세면
    resultHTML = makeMyEmojiHTML(chatMessage, localDateString);
  } else {
    // 상대가 보낸 메세지면
    resultHTML = makePartnerEmojiHTML(chatMessage, localDateString);
  }

  return resultHTML;
}

function makeChatHTML(chatMessage, localDateString) {
  // type이 CHAT인 경우 텍스트가 들어있는 HTML을 만든다)
  let resultHTML;

  if (isMyMessage(chatMessage)) {
    // 내가 보낸 메세면
    resultHTML = makeMyChatHTML(chatMessage, localDateString);
  } else {
    // 상대가 보낸 메세지면
    resultHTML = makePartnerChatHTML(chatMessage, localDateString);
  }

  return resultHTML;
}

function makePicHTML(chatMessage, localDateString) {
  let resultHTML;

  if (isMyMessage(chatMessage)) {
    // 내가 보낸 메세면
    resultHTML = makeMyPicHTML(chatMessage, localDateString);
  } else {
    // 상대가 보낸 메세지면
    resultHTML = makePartnerPicHTML(chatMessage, localDateString);
  }

  return resultHTML;
}

function makeMyDelHTML(chatMessage, localDateString) {
  let resultHTML =
    '<div class="for-containment right" data-countByRoom="' +
    chatMessage.countByRoom +
    '">' +
    '<div class="for-containment   css-uc14ng">' +
    '<div class="sender css-lty8rs">' +
    '<div class="message-date" data-date="' +
    localDateString.split("/")[0] +
    '">' +
    localDateString.split("/")[1] +
    "</div>" +
    "</div>" +
    '<div class="message-wrapper">' +
    "<div>" +
    '<p class="message-box deleted-message"><span class="info-icon"><svg width="18" height="18" viewBox="0 0 16 16" fill="none" xmlns="http://www.w3.org/2000/svg"><path d="M7.99968 4.73486C8.30024 4.73486 8.54389 4.97851 8.54389 5.27908V8.3403C8.54389 8.64086 8.30024 8.88452 7.99968 8.88452C7.69911 8.88452 7.45546 8.64086 7.45546 8.3403V5.27908C7.45546 4.97851 7.69911 4.73486 7.99968 4.73486Z" fill="currentColor"></path><path d="M8.67981 10.7212C8.67981 11.0969 8.37525 11.4015 7.99954 11.4015C7.62384 11.4015 7.31927 11.0969 7.31927 10.7212C7.31927 10.3455 7.62384 10.041 7.99954 10.041C8.37525 10.041 8.67981 10.3455 8.67981 10.7212Z" fill="currentColor"></path><path fill-rule="evenodd" clip-rule="evenodd" d="M1.33301 8.00016C1.33301 4.31826 4.31778 1.3335 7.99968 1.3335C11.6816 1.3335 14.6663 4.31826 14.6663 8.00016C14.6663 11.6821 11.6816 14.6668 7.99968 14.6668C4.31778 14.6668 1.33301 11.6821 1.33301 8.00016ZM7.99968 2.42193C4.9189 2.42193 2.42144 4.91939 2.42144 8.00016C2.42144 11.0809 4.9189 13.5784 7.99968 13.5784C11.0804 13.5784 13.5779 11.0809 13.5779 8.00016C13.5779 4.91939 11.0804 2.42193 7.99968 2.42193Z" fill="currentColor"></path></svg></span>삭제한 메시지</p>' +
    "</div>" +
    "</div>" +
    "</div>" +
    "</div>";

  return resultHTML;
}

function makePartnerDelHTML(chatMessage, localDateString) {
  let partnerProfilePic = $(".css-1c3oejv .chat-header-image").attr("src");
  let resultHTML =
    '<div class="for-containment left" data-countByRoom="' +
    chatMessage.countByRoom +
    '">' +
    '<div class="  css-7rih9z">' +
    '<div class=" css-17oljzs">' +
    '<img class="profile-image" src="' +
    partnerProfilePic +
    '" alt="상대 프사">' +
    "</div>" +
    '<div class="message-wrapper">' +
    "<div>" +
    '<p class="message-box deleted-message"><span class="info-icon"><svg width="18" height="18" viewBox="0 0 16 16" fill="none" xmlns="http://www.w3.org/2000/svg"><path d="M7.99968 4.73486C8.30024 4.73486 8.54389 4.97851 8.54389 5.27908V8.3403C8.54389 8.64086 8.30024 8.88452 7.99968 8.88452C7.69911 8.88452 7.45546 8.64086 7.45546 8.3403V5.27908C7.45546 4.97851 7.69911 4.73486 7.99968 4.73486Z" fill="currentColor"></path><path d="M8.67981 10.7212C8.67981 11.0969 8.37525 11.4015 7.99954 11.4015C7.62384 11.4015 7.31927 11.0969 7.31927 10.7212C7.31927 10.3455 7.62384 10.041 7.99954 10.041C8.37525 10.041 8.67981 10.3455 8.67981 10.7212Z" fill="currentColor"></path><path fill-rule="evenodd" clip-rule="evenodd" d="M1.33301 8.00016C1.33301 4.31826 4.31778 1.3335 7.99968 1.3335C11.6816 1.3335 14.6663 4.31826 14.6663 8.00016C14.6663 11.6821 11.6816 14.6668 7.99968 14.6668C4.31778 14.6668 1.33301 11.6821 1.33301 8.00016ZM7.99968 2.42193C4.9189 2.42193 2.42144 4.91939 2.42144 8.00016C2.42144 11.0809 4.9189 13.5784 7.99968 13.5784C11.0804 13.5784 13.5779 11.0809 13.5779 8.00016C13.5779 4.91939 11.0804 2.42193 7.99968 2.42193Z" fill="currentColor"></path></svg></span>삭제한 메시지</p>' +
    "</div>" +
    "</div>" +
    '<div class="css-lty8rs">' +
    '<div class="message-date" data-date="' +
    localDateString.split("/")[0] +
    '">' +
    localDateString.split("/")[1] +
    "</div>" +
    "</div>" +
    "</div>" +
    "</div>";

  return resultHTML;
}

function makeMyEmojiHTML(chatMessage, localDateString) {
  let resultHTML =
    '<div class="for-containment right" data-countByRoom="' +
    chatMessage.countByRoom +
    '">' +
    '<div class="for-containment   css-uc14ng">' +
    '<div class="sender css-lty8rs">' +
    '<div class="message-date" data-date="' +
    localDateString.split("/")[0] +
    '">' +
    localDateString.split("/")[1] +
    "</div>" +
    "</div>" +
    '<div class="message-wrapper">' +
    "<div>" +
    '<img src="/resources/pic/emoji/' +
    chatMessage.emojiFileName +
    '" alt="sticker" class="css-1918wba">' +
    "</div>" +
    '<button type="button" class="toolbox"><img src="/resources/pic/icon/more_horiz.svg" alt="more"></button>' +
    "</div>" +
    "</div>" +
    "</div>";

  return resultHTML;
}
function makePartnerEmojiHTML(chatMessage, localDateString) {
  let partnerProfilePic = $(".css-1c3oejv .chat-header-image").attr("src");
  let resultHTML =
    '<div class="for-containment left" data-countByRoom="' +
    chatMessage.countByRoom +
    '">' +
    '<div class="  css-7rih9z">' +
    '<div class=" css-17oljzs">' +
    '<img class="profile-image" src="' +
    partnerProfilePic +
    '" alt="상대 프사">' +
    "</div>" +
    '<div class="message-wrapper">' +
    "<div>" +
    '<img src="/resources/pic/emoji/' +
    chatMessage.emojiFileName +
    '" alt="sticker" class="css-1918wba">' +
    "</div>" +
    "</div>" +
    '<div class="css-lty8rs">' +
    '<div class="message-date" data-date="' +
    localDateString.split("/")[0] +
    '">' +
    localDateString.split("/")[1] +
    "</div>" +
    "</div>" +
    "</div>" +
    "</div>";

  return resultHTML;
}

function makeMyChatHTML(chatMessage, localDateString) {
  let resultHTML =
    '<div class="for-containment right" data-countByRoom="' +
    chatMessage.countByRoom +
    '">' +
    '<div class="for-containment   css-uc14ng">' +
    '<div class="sender css-lty8rs">' +
    '<div class="message-date" data-date="' +
    localDateString.split("/")[0] +
    '">' +
    localDateString.split("/")[1] +
    "</div>" +
    "</div>" +
    '<div class="message-wrapper">' +
    "<div>" +
    '<p class="message-box">' +
    chatMessage.messageContent +
    "</p>" +
    "</div>" +
    '<button type="button" class="toolbox"><img src="/resources/pic/icon/more_horiz.svg" alt="more"></button>' +
    "</div>" +
    "</div>" +
    "</div>";

  return resultHTML;
}

function makePartnerChatHTML(chatMessage, localDateString) {
  let partnerProfilePic = $(".css-1c3oejv .chat-header-image").attr("src");
  let resultHTML =
    '<div class="for-containment left" data-countByRoom="' +
    chatMessage.countByRoom +
    '">' +
    '<div class="  css-7rih9z">' +
    '<div class=" css-17oljzs">' +
    '<img class="profile-image" src="' +
    partnerProfilePic +
    '" alt="상대 프사">' +
    "</div>" +
    '<div class="message-wrapper">' +
    "<div>" +
    '<p class="message-box">' +
    chatMessage.messageContent +
    "</p>" +
    "</div>" +
    '<button type="button" class="toolbox"><img src="/resources/pic/icon/more_horiz.svg" alt="more"></button>' +
    "</div>" +
    '<div class="css-lty8rs">' +
    '<div class="message-date" data-date="' +
    localDateString.split("/")[0] +
    '">' +
    localDateString.split("/")[1] +
    "</div>" +
    "</div>" +
    "</div>" +
    "</div>";

  return resultHTML;
}

function makeMyPicHTML(chatMessage, localDateString) {
  let resultHTML =
    '<div class="for-containment right" data-countByRoom="' +
    chatMessage.countByRoom +
    '">' +
    '<div class="for-containment   css-uc14ng">' +
    '<div class="sender css-lty8rs">' +
    '<div class="message-date" data-date="' +
    localDateString.split("/")[0] +
    '">' +
    localDateString.split("/")[1] +
    "</div>" +
    "</div>" +
    '<div class="message-wrapper">' +
    '<div class="css-hskpmw">' +
    '<img src="/resources/pic/chatPic/' +
    chatMessage.picFileName +
    '"' +
    'class="image-element common-bg-hover extension-png" alt="내가 보낸 사진">' +
    '<div class="hover-effect"></div>' +
    "</div>" +
    '<button type="button" class="toolbox"><img src="/resources/pic/icon/more_horiz.svg" alt="more"></button>' +
    "</div>" +
    "</div>" +
    "</div>";
  return resultHTML;
}

function makePartnerPicHTML(chatMessage, localDateString) {
  let partnerProfilePic = $(".css-1c3oejv .chat-header-image").attr("src");
  let resultHTML =
    '<div class="for-containment left" data-countByRoom="' +
    chatMessage.countByRoom +
    '">' +
    '<div class="  css-7rih9z">' +
    '<div class=" css-17oljzs">' +
    '<img class="profile-image"' +
    'src="' +
    partnerProfilePic +
    '"' +
    'alt="상대 프사">' +
    "</div>" +
    '<div class="message-wrapper">' +
    '<div class="css-hskpmw">' +
    '<img src="/resources/pic/chatPic/' +
    chatMessage.picFileName +
    '"' +
    'class="image-element common-bg-hover extension-png" alt="상대가 보낸 사진">' +
    '<div class="hover-effect"></div>' +
    "</div>" +
    "</div>" +
    '<div class="css-lty8rs">' +
    '<div class="message-date" data-date="' +
    localDateString.split("/")[0] +
    '">' +
    localDateString.split("/")[1] +
    "</div>" +
    "</div>" +
    "</div>" +
    "</div>";
  return resultHTML;
}

function makeReadHTML() {
  let resultHTML = '<span class="unread-text">읽음</span>';
  return resultHTML;
}

function makeMessageMenuHTML($chatDiv) {
  let resultHTML = "";
  let canBeDeleted = checkIfCanBeDeleted($chatDiv); // true / false

  resultHTML += '<div class="message-menu-box">';

  // 내 메세지고, 5분 안지났으면 삭제 버튼 포함해서 만들기
  if (canBeDeleted && $chatDiv.parent().hasClass("right")) {
    resultHTML += '<div class="message-menu-item deleteBtn">삭제</div>';
  }
  // 문자가 있을 때만 만들기
  if ($chatDiv.find("p").length > 0) {
    resultHTML += '<div class="message-menu-item duplicateBtn">복사</div>';
  }
  resultHTML += "</div>";

  return resultHTML;
}

function makeNotiHTML(content) {
  resultHTML = '<div class="css-1e7vfs6"><div>' + content + "</div></div>";
  return resultHTML;
}

function makePartnerChatNotiHTML() {
  let content = "새로운 채팅이 도착했어요.";
  resultHTML = '<div class="partnerChatNoti"><div>' + content + "</div></div>";
  return resultHTML;
}

function makeDelContentHTML() {
  let resultHTML =
    '<p class="message-box deleted-message"><span class="info-icon"><svg width="18" height="18" viewBox="0 0 16 16" fill="none" xmlns="http://www.w3.org/2000/svg"><path d="M7.99968 4.73486C8.30024 4.73486 8.54389 4.97851 8.54389 5.27908V8.3403C8.54389 8.64086 8.30024 8.88452 7.99968 8.88452C7.69911 8.88452 7.45546 8.64086 7.45546 8.3403V5.27908C7.45546 4.97851 7.69911 4.73486 7.99968 4.73486Z" fill="currentColor"></path><path d="M8.67981 10.7212C8.67981 11.0969 8.37525 11.4015 7.99954 11.4015C7.62384 11.4015 7.31927 11.0969 7.31927 10.7212C7.31927 10.3455 7.62384 10.041 7.99954 10.041C8.37525 10.041 8.67981 10.3455 8.67981 10.7212Z" fill="currentColor"></path><path fill-rule="evenodd" clip-rule="evenodd" d="M1.33301 8.00016C1.33301 4.31826 4.31778 1.3335 7.99968 1.3335C11.6816 1.3335 14.6663 4.31826 14.6663 8.00016C14.6663 11.6821 11.6816 14.6668 7.99968 14.6668C4.31778 14.6668 1.33301 11.6821 1.33301 8.00016ZM7.99968 2.42193C4.9189 2.42193 2.42144 4.91939 2.42144 8.00016C2.42144 11.0809 4.9189 13.5784 7.99968 13.5784C11.0804 13.5784 13.5779 11.0809 13.5779 8.00016C13.5779 4.91939 11.0804 2.42193 7.99968 2.42193Z" fill="currentColor"></path></svg></span>삭제한 메시지</p>';
  return resultHTML;
}

function makeDeleteCheckModalHTML(countByRoom) {
  let resultHtml =
    '<div class="css-uthtbs">' +
    '<div class="css-1l4wgsq">' +
    '<div class="css-vq6gu1">' +
    "<div>" +
    '<div class="title">메시지를 삭제할까요?</div>' +
    '<div class="description">모든 사람에게 안 보이게 돼요.</div>' +
    "</div>" +
    '<div class="button-wrapper">' +
    '<button type="button" class="common-bg-hover css-zrzqm7 cancel">취소</button>' +
    '<button type="button" class="confirm css-zrzqm7" data-delete_count_by_room="' +
    countByRoom +
    '">삭제</button>' +
    "</div></div></div></div>";

  return resultHtml;
}

function makeExitCheckModalHTML(countByRoom) {
  let resultHtml =
    '<div class="css-uthtbs">' +
    '<div class="css-1l4wgsq">' +
    '<div class="css-vq6gu1">' +
    "<div>" +
    '<div class="title">채팅방 나가기</div>' +
    '<div class="description">채팅방을 나가면 채팅 내역을 더이상 확인할 수 없어요. 정말 나가시겠어요?</div>' +
    "</div>" +
    '<div class="button-wrapper">' +
    '<button type="button" class="common-bg-hover css-zrzqm7 cancel">취소</button>' +
    '<button type="button" class="confirm css-zrzqm7" data-last_count_by_room="' +
    countByRoom +
    '">확인</button>' +
    "</div></div></div></div>";

  return resultHtml;
}

function makePayRequestModalHTML(postPrice) {
  let resultHtml =
    '<div class="css-uthtbs">' +
    '<div class="css-1l4wgsq">' +
    '<div class="css-vq6gu1">' +
    "<div>" +
    '<div class="title">결제 요청하기</div>' +
    '<div class="description">최종 결정된 가격을 입력해주세요.<br />' +
    '<input type="text" name="currency-field" id="finalPrice" value="' +
    "₩" +
    formatNumber(postPrice) +
    '" data-type="currency" placeholder="가격을 입력해주세요.">' +
    "</div>" +
    "</div>" +
    '<div class="button-wrapper">' +
    '<button type="button" class="common-bg-hover css-zrzqm7 cancel">취소</button>' +
    '<button type="button" class="confirm css-zrzqm7" >확인</button>' +
    "</div></div></div></div>";

  return resultHtml;
}

function makeCancelPayModalHTML(countByRoom) {
  let resultHtml =
    '<div class="css-uthtbs">' +
    '<div class="css-1l4wgsq">' +
    '<div class="css-vq6gu1">' +
    "<div>" +
    '<div class="title">결제요청 취소</div>' +
    '<div class="description">상대방이 아직 결제하지 않았어요. 결제요청을 취소하시겠습니까?</div>' +
    "</div>" +
    '<div class="button-wrapper">' +
    '<button type="button" class="common-bg-hover css-zrzqm7 cancel">취소</button>' +
    '<button type="button" class="confirm css-zrzqm7" data-count_by_room="' +
    countByRoom +
    '">확인</button>' +
    "</div></div></div></div>";

  return resultHtml;
}

function makeChatRoomInListHTML(chatRoom) {
  let lastMessageTime = chatRoom.lastMessageTime;
  let lastMessageType = chatRoom.lastMessageType;
  let resultHTML =
    '<li class="css-v2yhcd" data-chat_room_id="' +
    chatRoom.chatRoomId +
    '" data-message_count_by_room="' +
    chatRoom.lastMessageCountByRoom +
    '"><a class="   css-y6c1l4"' +
    'href="/chat/room/' +
    chatRoom.chatRoomId +
    '">' +
    '<div class="profile-wrapper">' +
    '<img class="profile-image"' +
    'src="/resources/pic/profilePic/' +
    chatRoom.otherUserProfilePic +
    '"' +
    'alt="profile">' +
    "</div>" +
    '<div class="css-qv4ssb">' +
    '<div class="preview-title-wrap">' +
    '<span class="preview-nickname">' +
    chatRoom.otherUserNickname +
    "</span>" +
    '<div class="sub-text">' +
    "<span> · </span>" +
    "<span>";

  // 초랑 분 단위까지만 표시해도 되겠지
  if (lastMessageTime < 60) {
    resultHTML += lastMessageTime + "초 전";
  } else {
    resultHTML += lastMessageTime / 60 + "분 전";
  }
  resultHTML =
    resultHTML +
    "</span>" +
    "</div>" +
    "</div>" +
    '<div class="preview-description">' +
    '<span class="description-text">' +
    "<!-- lastMessageType 에 따라 값 넣기 -->";
  if (lastMessageType == "CHAT") resultHTML += chatRoom.lastMessage;
  else if (lastMessageType == "PIC") resultHTML += "사진을 보냈습니다.";
  else if (lastMessageType == "DEL") resultHTML += "삭제된 메세지입니다.";
  resultHTML =
    resultHTML +
    "</span>" +
    "</div>" +
    "</div> " +
    '<div class="unread-count-wrapper" data-count="' +
    chatRoom.unreadMessageCount +
    '">' +
    chatRoom.unreadMessageCount +
    "</div>" +
    "<img " +
    'src="/resources/pic/postPic/' +
    chatRoom.firstPostPic +
    '"' +
    'class="preview-image" alt="상품 썸네일"></a></li>';

  return resultHTML;
}

function makePayFINISHContentHTML() {
  let resultHTML =
    '<div class="css-87inxw">' +
    '<div class="temp-message-wrap">' +
    '<div class="thumbnail-wrap">' +
    '<div class="content-wrapper">' +
    '<div class="content-title">결제완료!</div>' +
    '<div class="content-text">' +
    '<div class="css-1o46l5b">' +
    "<span>결제가 완료되었어요.</span>" +
    "</div>" +
    "</div>" +
    "</div>" +
    "</div>" +
    "<div>" +
    '<div class="temp-button payFinishedBtn">완료됨</div>' +
    "</div>" +
    "</div>" +
    "</div>";

  return resultHTML;
}

function makePayCANCELContentHTML() {
  let resultHTML =
    '<div class="css-87inxw">' +
    '<div class="temp-message-wrap">' +
    '<div class="thumbnail-wrap">' +
    '<div class="content-wrapper">' +
    '<div class="content-title">결제요청 취소됨.</div>' +
    '<div class="content-text">' +
    '<div class="css-1o46l5b">' +
    "<span>취소된 결제요청이에요.</span>" +
    "</div>" +
    "</div>" +
    "</div>" +
    "</div>" +
    "<div>" +
    '<div class="temp-button payCanceledBtn">취소됨</div>' +
    "</div>" +
    "</div>" +
    "</div>";

  return resultHTML;
}

function makeMyPayBeforeHTML(chatMessage, localDateString) {
  let resultHTML =
    '<div class="for-containment right" data-countByRoom="' +
    chatMessage.countByRoom +
    '">' +
    '<div class="for-containment   css-uc14ng">' +
    '<div class="sender css-lty8rs">' +
    '<div class="message-date" data-date="' +
    localDateString.split("/")[0] +
    '">' +
    localDateString.split("/")[1] +
    "</div>" +
    "</div>" +
    '<div class="message-wrapper">' +
    '<div class="css-87inxw">' +
    '<div class="temp-message-wrap">' +
    '<div class="thumbnail-wrap">' +
    '<div class="content-wrapper">' +
    '<div class="content-title">' +
    formatNumber(chatMessage.finalPrice.toString()) +
    "원</div>" +
    '<div class="content-title">결제 요청을 보냈어요.</div>' +
    '<div class="content-text">' +
    '<div class="css-1o46l5b">' +
    "<span>상대방이 결제를 마칠 때 까지 기다려주세요.</span>" +
    "</div>" +
    "</div>" +
    "</div>" +
    "</div>" +
    "<div>" +
    '<div class="temp-button payCancelBtn">취소하려면 클릭하세요.</div>' +
    "</div>" +
    "</div>" +
    "</div>" +
    "</div>" +
    "</div>" +
    "</div>";

  return resultHTML;
}
function makePartnerPayBeforeHTML(chatMessage, localDateString) {
  let partnerProfilePic = $(".css-1c3oejv .chat-header-image").attr("src");
  let resultHTML =
    '<div class="for-containment left" data-countByRoom="' +
    chatMessage.countByRoom +
    '">' +
    '<div class="  css-7rih9z">' +
    '<div class=" css-17oljzs">' +
    '<img class="profile-image" src="' +
    partnerProfilePic +
    '" alt="상대 프사">' +
    "</div>" +
    '<div class="message-wrapper">' +
    '<div class="css-87inxw">' +
    '<div class="temp-message-wrap">' +
    '<div class="thumbnail-wrap">' +
    '<div class="content-wrapper">' +
    '<div class="content-title">' +
    formatNumber(chatMessage.finalPrice.toString()) +
    "원</div>" +
    '<div class="content-title">결제 요청을 받았어요.</div>' +
    '<div class="content-text">' +
    '<div class="css-1o46l5b">' +
    "<span>아래 버튼을 눌러 결제를 진행해주세요.</span>" +
    "</div>" +
    "</div>" +
    "</div>" +
    "</div>" +
    "<div>" +
    '<div class="temp-button payBtn">결제하려면 클릭하세요.</div>' +
    "</div>" +
    "</div>" +
    "</div>" +
    "</div>" +
    '<div class="css-lty8rs">' +
    '<div class="message-date" data-date="' +
    localDateString.split("/")[0] +
    '">' +
    localDateString.split("/")[1] +
    "</div>" +
    "</div>" +
    "</div>" +
    "</div>";

  return resultHTML;
}

function makeMyPayCANCELHTML(chatMessage, localDateString) {
  let resultHTML =
    '<div class="for-containment right" data-countByRoom="' +
    chatMessage.countByRoom +
    '">' +
    '<div class="for-containment   css-uc14ng">' +
    '<div class="sender css-lty8rs">' +
    '<div class="message-date" data-date="' +
    localDateString.split("/")[0] +
    '">' +
    localDateString.split("/")[1] +
    "</div>" +
    "</div>" +
    '<div class="message-wrapper">' +
    '<div class="css-87inxw">' +
    '<div class="temp-message-wrap">' +
    '<div class="thumbnail-wrap">' +
    '<div class="content-wrapper">' +
    '<div class="content-title">결제요청 취소됨.</div>' +
    '<div class="content-text">' +
    '<div class="css-1o46l5b">' +
    "<span>취소된 결제요청이에요.</span>" +
    "</div>" +
    "</div>" +
    "</div>" +
    "</div>" +
    "<div>" +
    '<div class="temp-button payCanceledBtn">취소됨</div>' +
    "</div>" +
    "</div>" +
    "</div>" +
    "</div>" +
    "</div>" +
    "</div>";

  return resultHTML;
}
function makePartnerPayCANCELHTML(chatMessage, localDateString) {
  let partnerProfilePic = $(".css-1c3oejv .chat-header-image").attr("src");
  let resultHTML =
    '<div class="for-containment left" data-countByRoom="' +
    chatMessage.countByRoom +
    '">' +
    '<div class="  css-7rih9z">' +
    '<div class=" css-17oljzs">' +
    '<img class="profile-image" src="' +
    partnerProfilePic +
    '" alt="상대 프사">' +
    "</div>" +
    '<div class="message-wrapper">' +
    '<div class="css-87inxw">' +
    '<div class="temp-message-wrap">' +
    '<div class="thumbnail-wrap">' +
    '<div class="content-wrapper">' +
    '<div class="content-title">결제요청 취소됨.</div>' +
    '<div class="content-text">' +
    '<div class="css-1o46l5b">' +
    "<span>취소된 결제요청이에요.</span>" +
    "</div>" +
    "</div>" +
    "</div>" +
    "</div>" +
    "<div>" +
    '<div class="temp-button payCanceledBtn">취소됨</div>' +
    "</div>" +
    "</div>" +
    "</div>" +
    "</div>" +
    '<div class="css-lty8rs">' +
    '<div class="message-date" data-date="' +
    localDateString.split("/")[0] +
    '">' +
    localDateString.split("/")[1] +
    "</div>" +
    "</div>" +
    "</div>" +
    "</div>";

  return resultHTML;
}

function makeMyPayFINISHHTML(chatMessage, localDateString) {
  let resultHTML =
    '<div class="for-containment right" data-countByRoom="' +
    chatMessage.countByRoom +
    '">' +
    '<div class="for-containment   css-uc14ng">' +
    '<div class="sender css-lty8rs">' +
    '<div class="message-date" data-date="' +
    localDateString.split("/")[0] +
    '">' +
    localDateString.split("/")[1] +
    "</div>" +
    "</div>" +
    '<div class="message-wrapper">' +
    '<div class="css-87inxw">' +
    '<div class="temp-message-wrap">' +
    '<div class="thumbnail-wrap">' +
    '<div class="content-wrapper">' +
    '<div class="content-title">결제 완료!</div>' +
    '<div class="content-text">' +
    '<div class="css-1o46l5b">' +
    "<span>결제가 완료되었어요.</span>" +
    "</div>" +
    "</div>" +
    "</div>" +
    "</div>" +
    "<div>" +
    '<div class="temp-button payFinishedBtn">완료됨</div>' +
    "</div>" +
    "</div>" +
    "</div>";
  "</div>" + "</div>" + "</div>";

  return resultHTML;
}
function makePartnerPayFINISHHTML(chatMessage, localDateString) {
  let partnerProfilePic = $(".css-1c3oejv .chat-header-image").attr("src");
  let resultHTML =
    '<div class="for-containment left" data-countByRoom="' +
    chatMessage.countByRoom +
    '">' +
    '<div class="  css-7rih9z">' +
    '<div class=" css-17oljzs">' +
    '<img class="profile-image" src="' +
    partnerProfilePic +
    '" alt="상대 프사">' +
    "</div>" +
    '<div class="message-wrapper">' +
    '<div class="css-87inxw">' +
    '<div class="temp-message-wrap">' +
    '<div class="thumbnail-wrap">' +
    '<div class="content-wrapper">' +
    '<div class="content-title">' +
    formatNumber(chatMessage.finalPrice.toString()) +
    "원</div>" +
    '<div class="content-title">결제 완료!</div>' +
    '<div class="content-text">' +
    '<div class="css-1o46l5b">' +
    "<span>결제가 완료되었어요.</span>" +
    "</div>" +
    "</div>" +
    "</div>" +
    "</div>" +
    "<div>" +
    '<div class="temp-button payFinishedBtn">완료됨</div>' +
    "</div>" +
    "</div>" +
    "</div>" +
    "</div>" +
    '<div class="css-lty8rs">' +
    '<div class="message-date" data-date="' +
    localDateString.split("/")[0] +
    '">' +
    localDateString.split("/")[1] +
    "</div>" +
    "</div>" +
    "</div>" +
    "</div>";

  return resultHTML;
}
