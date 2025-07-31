$(document).ready(function () {
  // 스페이스바 금지
  $("input[type='text']").on("keydown", function (e) {
    if (e.keyCode === 32) {
      // 32 is the keycode for spacebar
      e.preventDefault();
    }
  });

  window.onpageshow = function (event) {
    if (event.persisted) {
      window.location.reload();
    }
  };

  document.getElementById("back").addEventListener("click", function () {
    window.location.href = "login";
  });

  document.getElementById("back2").addEventListener("click", function () {
    window.location.href = "login";
  });

  $("#userEmail").on("blur", function () {
    let emailCheck = /^[A-Za-z0-9_\.\-]+@[A-Za-z0-9\-]+\.[A-za-z0-9\-]+/;
    var userEmail = $("#userEmail").val();

    if (userEmail == "" || userEmail == 0) {
      $("#emailCheck").attr("disabled", true);
      $("#label5").css("color", "red").text("이메일을 입력해주세요.");
      $("#label5").show();
      return false;
    }

    if (!emailCheck.test($("#userEmail").val())) {
      $("#emailCheck").attr("disabled", true);
      $("#label5").css("color", "red");
      $("#label5").text("이메일 양식을 지켜주세요.");
      $("#label5").show();
      userEmail = false;
    } else {
      $("#label5").hide();
      $("#emailCheck").attr("disabled", false);
    }
  });

  //인증하기 버튼을 눌렀을 때 동작
  $("#emailCheck").click(function () {
    const userEmail = $("#userEmail").val(); //사용자가 입력한 이메일 값 얻어오기
    const userId = $("userIdInpw").val();
    submitCheck = false;

    //Ajax로 전송
    $.ajax({
      url: "EmailAuth2",
      data: {
        userEmail: userEmail,
        userId: userId,
      },
      type: "POST",
      dataType: "json",
      success: function (result) {
        if (result == -1) {
          alert("아이디나 이메일이 일치하지 않습니다.");
          return;
        }

        $("#emailNum").attr("disabled", false);
        code = result;
        alert("인증 코드가 입력하신 이메일로 전송 되었습니다.");
      },
    }); //End Ajax
  });

  //인증 코드 비교
  $("#emailNum").on("keyup", function () {
    const inputCode = $("#emailNum").val(); //인증번호 입력 칸에 작성한 내용 가져오기

    if (Number(inputCode) === code) {
      $("#emailAuthWarn").html("인증번호가 일치합니다.");
      $("#emailAuthWarn").css("color", "green");
      $("#emailCheck").attr("disabled", true);
      $("#userEmail").attr("readonly", true);
      $("#pwSubmit").attr("disabled", false);
    } else {
      $("#emailAuthWarn").html("인증번호가 불일치 합니다. 다시 확인해주세요!");
      $("#emailAuthWarn").css("color", "red");
    }
  });
});
