$(document).ready(function () {
  $("#pwdcheck_blank1").text("영문+숫자+특수문자 조합하여 8~16자리를 입력해주세요");
  $("#label3").text("숫자11자리를 입력해주세요");

  let submitCheck = true;

  //닉네임 중복 확인
  $("#userNickname").on("keyup", function () {
    var userNickname = $("#userNickname").val();

    //Ajax로 전송
    $.ajax({
      url: "nickUpdate",
      data: {
        userNickname: userNickname,
      },
      type: "POST",
      dataType: "json",
      success: function (result) {
        if (result == true) {
          $("#label2").css("color", "blue").text("사용 가능한 닉네임 입니다.");
          submitCheck = true;
        } else {
          $("#label2").css("color", "red").text("사용 불가능한 닉네임 입니다.");
          submitCheck = false;
        }
      },
    }); //End Ajax
  });

  $("#userPassword").on("keyup", function () {
    let pwdCheck = /^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,16}$/;

    if (!pwdCheck.test($("#userPassword").val())) {
      $("#pwdcheck_blank1").css("color", "red");
      $("#pwdcheck_blank1").text("영문+숫자+특수문자 조합하여 8~16자리를 사용해주세요");
      //$("#userPassword").val("");
      submitCheck = false;
      userPassword = false;
    } else {
      $("#pwdcheck_blank1").css("color", "blue");
      $("#pwdcheck_blank1").text("사용 가능한 비밀번호 입니다.");
      submitCheck = true;
      userPassword = true;
    }
  });

  /////비밀번호 확인//////
  $("#password_check").on("keyup", function () {
    if (userPassword == true && $("#userPassword").val() == $("#password_check").val()) {
      $("#pwdcheck_blank2").css("color", "blue");
      $("#pwdcheck_blank2").text("비밀번호가 일치합니다");
      submitCheck = true;
      password_check = true;
    } else {
      $("#pwdcheck_blank2").css("color", "red");
      $("#pwdcheck_blank2").text("비밀번호를 다시 확인해주세요");
      //$("#password_check").val("");
      submitCheck = false;
      password_check = false;
    }
  });

  //핸드폰 중복 확인
  $("#userPhone").on("keyup", function () {
    let phoneCheck = /^[0-9]{11}$/;

    var userPhone = $("#userPhone").val();

    if (!phoneCheck.test($("#userPhone").val())) {
      $("#label3").css("color", "red");
      $("#label3").text("숫자11자리를 입력해주세요");
      //$("#userPhone").val("");
      submitCheck = false;
      userPhone = false;
    } else {
      //Ajax로 전송
      $.ajax({
        url: "ConfirmPhone",
        data: {
          userPhone: userPhone,
        },
        type: "POST",
        dataType: "json",
        success: function (result) {
          if (result == true) {
            $("#label3").css("color", "blue").text("변경 가능한 번호입니다");
            submitCheck = true;
          } else {
            $("#label3").css("color", "red").text("이미 등록된 번호입니다");
            submitCheck = false;
            //$("#userPhone").val('');
          }
        },
      }); //End Ajax
    }
  });

  // 스페이스바 금지
  $("input[type='text'], input[type='password']").on("keydown", function (e) {
    if (e.keyCode === 32) {
      // 32 is the keycode for spacebar
      e.preventDefault();
    }
  });

  // 가입하기 버튼 클릭 시 공백 여부 확인
  $("form").submit(function (event) {
    let hasEmptyField = false;

    $("input[type='text'], input[type='password']").each(function () {
      if ($(this).val().trim() === "") {
        hasEmptyField = true;
        return false; // 종료
      }
    });

    if (!submitCheck) {
      event.preventDefault(); // 폼 제출 막음
      alert("모든 정보를 올바르게 입력해주세요");
      return false; // 폼 제출 취소
    } else {
      alert("수정되었습니다");
    }
  });

  window.onpageshow = function (event) {
    if (event.persisted) {
      window.location.reload();
    }
  };

  document.getElementById("cancelButton").addEventListener("click", function () {
    window.location.href = "myInfo";
  });

  document.getElementById("logo").addEventListener("click", function () {
    window.location.href = "myInfo";
  });
});
