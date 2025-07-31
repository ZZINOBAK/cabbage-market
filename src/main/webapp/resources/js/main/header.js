// 페이지 로딩 후 실행할 함수
document.addEventListener("DOMContentLoaded", function () {
  // 숫자가 0이면 display를 none으로 설정
  var alrimCount = document.getElementById("alrimCount");
  if (alrimCount != null && alrimCount.innerText === "0") {
    alrimCount.style.display = "none";
  }
});

$(() => {
  $(".none-haeun-li.mypage").on("click", function () {
    location.href = $(this).find(".none-haeun-a").prop("href");
  });
});


//알림 갯수 컨트롤
function toggleDiv(type) {
  var myPageDiv = document.getElementById(type);

  if (type === "alrim") {
    var alrimCount = document.getElementById("alrimCount");

    if (!myPageDiv.style.display || myPageDiv.style.display === "none") {
      var myDiv = document.getElementById("my");
      myDiv.style.display = "none";
      myPageDiv.style.display = "block";

      // 'alrimCount' ID를 가진 요소가 존재하는지 확인
      if (alrimCount) {
        alrimCount.innerHTML = "0";
        alrimCount.style.display = "none";
      }
      $.ajax({
        type: "get",
        url: "/updateNoti",
        success: function (data) {},
      });
    } else {
      myPageDiv.style.display = "none";
    }
  } 
  if (type === "my") {
    if (!myPageDiv.style.display || myPageDiv.style.display === "none") {
      var alrimDiv = document.getElementById("alrim");
      alrimDiv.style.display = "none";
      myPageDiv.style.display = "block";
    } else {
      myPageDiv.style.display = "none";
    }
  }
}

function deleteAlrim() {
  const $test = $("#alrimList .none-haeun-li");

  // 각 li 요소 내의 goNotiDelete 클래스를 가진 요소를 찾아 display 속성을 변경합니다.
  $test.each((index, li) => {
    let $goNotiDelete = $(li).find("#goNotiDelete");

    if ($goNotiDelete.css("display") == "none") {
      $goNotiDelete.css("display", "block");
    } else {
      $goNotiDelete.css("display", "none");
    }
  });
}

function goNotiDelete(notiId) {
  $.ajax("/deleteNoti", {
    type: "get",
    data: { notiId: notiId },
    dataType: "text",
    success: function (data) {
      var alrimDiv = document.getElementById("alrim_" + notiId);
      alrimDiv.style.display = "none";
    },
    error: function () {
      alert("실패~~");
    },
  }); // 여기서 중괄호가 누락되었습니다.
}
