$(document).ready(function () {
  $("#label1").text("영문+숫자를 조합하여 5~16자리를 입력해주세요");
  $("#pwdcheck_blank1").text("영문+숫자+특수문자 조합하여 8~16자리를 입력해주세요");
  $("#label3").text("숫자11자리를 입력해주세요");
  $("#label4").text("한글만 입력해주세요");

  let submitCheck = false;

  //ID 중복 확인
  $("#userId").on("keyup", function () {
    submitCheck = false;
    let idCheck = /^(?=.*[a-zA-Z])(?=.*[0-9])(?!.*[^a-zA-Z0-9]).{5,16}$/;

    var userId = $("#userId").val();

    if (userId == "" || userId.length == 0) {
      $("#label1").css("color", "red").text("ID를 입력해주세요.");
      submitCheck = false;
      return false;
    }

    if (!idCheck.test($("#userId").val())) {
      $("#label1").css("color", "red");
      $("#label1").text("영문+숫자를 조합하여 5~16자리를 사용해주세요");
      //$("#userId").val("");
      submitCheck = false;
      userId = false;
    } else {
      //Ajax로 전송
      $.ajax({
        url: "ConfirmId",
        data: {
          userId: userId,
        },
        type: "POST",
        dataType: "json",
        success: function (result) {
          if (result == true) {
            $("#label1").css("color", "blue").text("사용 가능한 ID 입니다.");
            submitCheck = true;
          } else {
            $("#label1").css("color", "red").text("사용 불가능한 ID 입니다.");
            submitCheck = false;
            //$("#userId").val('');
          }
        },
      }); //End Ajax
    }
  });

  //닉네임 중복 확인
  $("#userNickname").on("keyup", function () {
    submitCheck = false;

    var userNickname = $("#userNickname").val();

    if (userNickname == "" || userNickname.length == 0) {
      $("#label2").css("color", "red").text("닉네임을 입력해주세요.");
      submitCheck = false;
      return false;
    }

    //Ajax로 전송
    $.ajax({
      url: "ConfirmNick",
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
          //   				$("#userNickname").val('');
        }
      },
    }); //End Ajax
  });

  $("#userPassword").on("keyup", function () {
    let pwdCheck = /^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,16}$/;
    submitCheck = false;

    if ($("#userPassword").val() == "") {
      $("#pwdcheck_blank1").css("color", "red");
      $("#pwdcheck_blank1").text("비밀번호를 입력해주세요.");
      submitCheck = false;
      userPassword = false;
    } else if (!pwdCheck.test($("#userPassword").val())) {
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
    submitCheck = false;

    if ($("#password_check").val() == "") {
      $("#pwdcheck_blank2").css("color", "red");
      $("#pwdcheck_blank2").text("비밀번호를 입력해주세요.");
      submitCheck = false;
      password_check = false;
    } else if (userPassword == true && $("#userPassword").val() == $("#password_check").val()) {
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
    submitCheck = false;
    let phoneCheck = /^[0-9]{11}$/;

    var userPhone = $("#userPhone").val();

    if (userPhone == "" || userPhone.length == 0) {
      $("#label3").css("color", "red").text("번호를 입력해주세요");
      submitCheck = false;
      return false;
    }

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
            $("#label3").css("color", "blue").text("가입 가능한 번호입니다");
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

  $("#userName").on("keyup", function () {
    let nameCheck = /^[가-힣]{1,6}$/;
    submitCheck = false;

    if ($("#userName").val() == "") {
      $("#label4").css("color", "red");
      $("#label4").text("이름를 입력해주세요.");
      $("#label4").show();
      submitCheck = false;
      userName = false;
    } else if (!nameCheck.test($("#userName").val())) {
      $("#label4").css("color", "red");
      $("#label4").text("한글만 입력해주세요");
      $("#label4").show();
      submitCheck = false;
      userName = false;
    } else {
      $("#label4").hide();
      submitCheck = true;
      userName = true;
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

    if (hasEmptyField) {
      alert("입력한 정보를 다시 확인해주세요");
      return false; // 폼 제출 취소
    } else if (!submitCheck) {
      event.preventDefault(); // 폼 제출 막음
      alert("모든 정보를 올바르게 입력해주세요");
      return false; // 폼 제출 취소
    } else {
      alert("회원가입을 축하합니다!");
    }
  });

  window.onpageshow = function (event) {
    if (event.persisted) {
      window.location.reload();
    }
  };

  document.getElementById("cancelButton").addEventListener("click", function () {
    window.location.href = "login";
  });

  document.getElementById("logo").addEventListener("click", function () {
    window.location.href = "login";
  });

  $("#userEmail").on("blur", function () {
    submitCheck = false;
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
    $(this).attr("disabled", "disabled");
    const userEmail = $("#userEmail").val(); //사용자가 입력한 이메일 값 얻어오기
    submitCheck = false;

    //Ajax로 전송
    $.ajax({
      url: "EmailAuth",
      data: {
        userEmail: userEmail,
      },
      type: "POST",
      dataType: "json",
      success: function (result) {
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
      submitCheck = true;
    } else {
      $("#emailAuthWarn").html("인증번호가 불일치 합니다. 다시 확인해주세요!");
      $("#emailAuthWarn").css("color", "red");
      submitCheck = false;
    }
  });
});
