$(document).ready(function () {
  $(".changeStatus").click(function () {
    $(".statusChange").slideToggle();
  });
});
$(document).ready(function () {
  $(".upDel").click(function () {
    $(".div_upDel").slideToggle();
  });
});

//alert창 디자인
const Toast = Swal.mixin({
  toast: true,
  position: "center-center",
  showConfirmButton: false,
  timer: 900,
  timerProgressBar: true,
});

/* 게시글 상태에 따른 표시 */
document.addEventListener("DOMContentLoaded", function () {
  var statusElement = document.getElementById("postStatus");

  if (postStatus === "RESERVE") {
    statusElement.innerText = "예약 중";
    statusElement.style.color = "#609966";
  } else if (postStatus === "FINISH") {
    statusElement.innerText = "거래완료";
    statusElement.style.color = "gray";
  } else {
    statusElement.innerText = "";
  }
});

document.addEventListener("DOMContentLoaded", function () {
  var reserveButton = document.getElementById("reserveButton");
  var finishButton = document.getElementById("finishButton");
  var enableButton = document.getElementById("enableButton");

  if (reserveButton != null && finishButton != null && enableButton != null)
    if (postStatus === "RESERVE") {
      reserveButton.disabled = true;
    } else if (postStatus === "FINISH") {
      finishButton.disabled = true;
    } else {
      enableButton.disabled = true;
    }
});
//게시글을 작성한 사람에 따라 다르게 보이게
document.addEventListener("DOMContentLoaded", function () {
  if (postStatus === "FINISH") {
    $(".sellerAction").hide();
  } else if (sellerId === userId) {
    $(".sellerAction").hide();
  } else {
    $(".sellerAction2").hide();
  }
});

// 찜 등록
function addWishList(userId, postId) {
  // 로딩 상태를 표시할 엘리먼트
  var loadingOverlay = $("#loading-overlay");

  // 로딩 상태를 표시
  loadingOverlay.css("display", "block");

  if ("userId" == "") {
    Swal.fire({
      title: "",
      text: "로그인 후 이용가능합니다.",
      icon: "warning",

      confirmButtonColor: "#9DC08B", // confrim 버튼 색깔 지정
      cancelButtonColor: "#d33", // cancel 버튼 색깔 지정
      confirmButtonText: "확인", // confirm 버튼 텍스트 지정
    });
  } else {
    var form = {
      userId: userId,
      postId: postId,
    };

    $.ajax({
      type: "POST",
      url: "/post/addWishList",
      contentType: "application/json; charset=utf-8",
      data: JSON.stringify(form),
      success: function (result) {
        Toast.fire({
          icon: "success",
          title: "찜 목록에 추가하였습니다.",
        }).then(function () {
          location.reload();
        });
      },
      error: function (e) {
        alert("오류 발생");
      },
    });
  }
}

/* 찜 해제 */
function deleteWishList(userId, postId) {
  // 로딩 상태를 표시할 엘리먼트
  var loadingOverlay = $("#loading-overlay");

  // 로딩 상태를 표시
  loadingOverlay.show();

  var form = {
    userId: userId,
    postId: postId,
  };

  $.ajax({
    type: "POST",
    url: "/post/deleteWishList",
    contentType: "application/json; charset=utf-8",
    data: JSON.stringify(form),
    success: function (result) {
      Toast.fire({
        icon: "error",
        title: "찜 목록에서 삭제되었습니다.",
      }).then(function () {
        location.reload();
      });
    },
    error: function (e) {
      alert("오류 발생");
    },
  });
}

//이미지 페이징
document.addEventListener("DOMContentLoaded", function () {
  var images = document.getElementsByClassName("pic");
  if (images.length > 0) {
    images[0].style.display = "block";

    var currentIndex = 0;

    //다음 사진 표시
    window.showNext = function () {
      images[currentIndex].style.display = "none";
      currentIndex = (currentIndex + 1) % images.length;
      images[currentIndex].style.display = "block";
    };
    //이전 사진 표시
    window.showPrev = function () {
      images[currentIndex].style.display = "none";
      currentIndex = (currentIndex - 1 + images.length) % images.length;
      images[currentIndex].style.display = "block";
    };
  }
});

//결제 창
function directPayPage(postId) {
  var popup = window.open(
    "/post/directPayPage?postId=" + postId,
    "DirectPay",
    "width=800, height=700"
  );
  if (popup) {
    popup.focus();
  } else {
    Swal.fire({
      title: "",
      text: "팝업 차단을 확인해주세요.",
      icon: "warning",

      confirmButtonColor: "#9DC08B", // confrim 버튼 색깔 지정
      cancelButtonColor: "#d33", // cancel 버튼 색깔 지정
      confirmButtonText: "확인", // confirm 버튼 텍스트 지정
    });
  }
}

//게시글 논리적 삭제 처리
function deletePost(postId) {
  if (userId != sellerId) {
    Swal.fire({
      title: "오류",
      text: "잘못된 접근입니다.",
      icon: "error",

      confirmButtonColor: "#9DC08B", // confrim 버튼 색깔 지정
      cancelButtonColor: "#d33", // cancel 버튼 색깔 지정
      confirmButtonText: "확인", // confirm 버튼 텍스트 지정
    });
  } else {
    Swal.fire({
      title: "",
      text: "정말로 삭제 하시겠습니까?",
      icon: "warning",

      showCancelButton: true, // cancel버튼 보이기. 기본은 원래 없음
      confirmButtonColor: "#9DC08B", // confrim 버튼 색깔 지정
      cancelButtonColor: "#d33", // cancel 버튼 색깔 지정
      confirmButtonText: "삭제", // confirm 버튼 텍스트 지정
      cancelButtonText: "취소", // cancel 버튼 텍스트 지정
    }).then((result) => {
      if (result.isConfirmed) {
        var formData = { postId: postId };

        $.ajax({
          url: "/post/deletePost",
          type: "post",
          data: formData,
          success: function (data) {
            Toast.fire({
              icon: "error",
              title: "게시글 삭제 완료",
            }).then(function () {
              window.location.href = "/post/getPostList";
            });
          },
          error: function (jqXHR, textStatus, errorThrown) {
            alert("실패" + " textStatus : " + textStatus + " errorThrown : " + errorThrown);
          },
        });
      }
    });
  }
}
//게시글 예약중 전환
function reservePost(postId) {
  if (userId != sellerId) {
    Swal.fire({
      title: "오류",
      text: "잘못된 접근입니다.",
      icon: "error",

      confirmButtonColor: "#9DC08B", // confrim 버튼 색깔 지정
      cancelButtonColor: "#d33", // cancel 버튼 색깔 지정
      confirmButtonText: "확인", // confirm 버튼 텍스트 지정
    });
  } else {
    Swal.fire({
      title: "",
      text: "'예약중'으로 전환하시겠습니까?",
      icon: "question",

      showCancelButton: true, // cancel버튼 보이기. 기본은 원래 없음
      confirmButtonColor: "#9DC08B", // confrim 버튼 색깔 지정
      cancelButtonColor: "#d33", // cancel 버튼 색깔 지정
      confirmButtonText: "변경", // confirm 버튼 텍스트 지정
      cancelButtonText: "취소", // cancel 버튼 텍스트 지정
    }).then((result) => {
      if (result.isConfirmed) {
        var formData = { postId: postId };

        $.ajax({
          url: "/post/reservePost",
          type: "post",
          data: formData,
          success: function (data) {
            //window.location.href = "/post/getPostList";
            location.reload();
          },
          error: function (jqXHR, textStatus, errorThrown) {
            alert("실패" + " textStatus : " + textStatus + " errorThrown : " + errorThrown);
          },
        });
      }
    });
  }
}
//게시글 거래완료 전환
function finishPost(postId) {
  let chatUser = "";
  let options = "";
  if (chatUserJson != "") chatUser = JSON.parse(chatUserJson);
  if (chatUser != "")
    options = chatUser.map(
      (user) => `<option value="${user.userNickname}">${user.userNickname}</option>`
    );

  if (userId != sellerId) {
    Swal.fire({
      title: "오류",
      text: "잘못된 접근입니다.",
      icon: "error",

      confirmButtonColor: "#9DC08B", // confrim 버튼 색깔 지정
      cancelButtonColor: "#d33", // cancel 버튼 색깔 지정
      confirmButtonText: "확인", // confirm 버튼 텍스트 지정
    });
  } else {
    Swal.fire({
      title: "",
      text: "'거래완료'로 전환하시겠습니까?",
      icon: "question",

      showCancelButton: true, // cancel버튼 보이기. 기본은 원래 없음
      confirmButtonColor: "#9DC08B", // confrim 버튼 색깔 지정
      cancelButtonColor: "#d33", // cancel 버튼 색깔 지정
      confirmButtonText: "변경", // confirm 버튼 텍스트 지정
      cancelButtonText: "취소", // cancel 버튼 텍스트 지정
    }).then((result) => {
      if (result.isConfirmed) {
        var formData = { postId: postId };

        $.ajax({
          url: "/post/finishPost",
          type: "post",
          data: formData,
          success: function (data) {
            Swal.fire({
              title: "구매자 선택",
              html: '<select id="buyerName" class="swal2-input">' + options + "</select>",
              confirmButtonText: "선택",
              showCancelButton: true,
              cancelButtonText: "선택안함",
              confirmButtonColor: "#9DC08B",
            }).then((result) => {
              if (result.isConfirmed) {
                const buyerName = Swal.getPopup().querySelector("#buyerName").value;

                $.ajax({
                  url: "/post/insertBuyer",
                  type: "post",
                  data: { userNickname: buyerName, postId: postId },
                  success: function (data) {
                    if (data == 1) {
                      alert("성공");
                      location.reload();
                    } else {
                      alert("실패 - 관리자에게 문의하세요");
                      location.reload();
                    }
                  },
                  error: function (textStatus) {
                    alert("실패");
                    //location.reload();
                  },
                });
              } else {
                location.reload();
              }
            });
          },
          error: function (jqXHR, textStatus, errorThrown) {
            alert("실패" + " textStatus : " + textStatus + " errorThrown : " + errorThrown);
          },
        });
      }
    });
  }
}
//게시글 거래중 전환
function enablePost(postId) {
  if (userId != sellerId) {
    Swal.fire({
      title: "오류",
      text: "잘못된 접근입니다.",
      icon: "error",

      confirmButtonColor: "#9DC08B", // confrim 버튼 색깔 지정
      cancelButtonColor: "#d33", // cancel 버튼 색깔 지정
      confirmButtonText: "확인", // confirm 버튼 텍스트 지정
    });
  } else {
    Swal.fire({
      title: "",
      text: "'판매중'으로 전환하시겠습니까?",
      icon: "question",

      showCancelButton: true, // cancel버튼 보이기. 기본은 원래 없음
      confirmButtonColor: "#9DC08B", // confrim 버튼 색깔 지정
      cancelButtonColor: "#d33", // cancel 버튼 색깔 지정
      confirmButtonText: "변경", // confirm 버튼 텍스트 지정
      cancelButtonText: "취소", // cancel 버튼 텍스트 지정
    }).then((result) => {
      if (result.isConfirmed) {
        var formData = { postId: postId };

        $.ajax({
          url: "/post/enablePost",
          type: "post",
          data: formData,
          success: function (data) {
            //window.location.href = "/post/getPostList";
            location.reload();
          },
          error: function (jqXHR, textStatus, errorThrown) {
            alert("실패" + " textStatus : " + textStatus + " errorThrown : " + errorThrown);
          },
        });
      }
    });
  }
}
