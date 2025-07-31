<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%> <%@ taglib
prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8" />
    <title>회원 정보</title>
    <link rel="icon" href="/resources/pic/img/baechu.png" />
    <script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
    <link rel="stylesheet" href="/resources/css/user/test.css" />
    <script>
      $(()=> {
        var userId = $(".sc-kqlzXE.bvAAFa").attr("data-userid");
        
        
        if($(".kHfkPy").length == 0){
        	$(".gmerZt").removeClass("gmerZt").addClass("kHfkPy");
        	salesList();
        } else {
        	purchaseList();
        }

        $.ajax({
          type: "POST",
          url: "/user/salesList",
          data: {"userId" : userId},
          dataType: "json",
          success: function (response) {
            $("#salecnt").text(response.length);
          },
          error: function (xhr, status, error) {
            console.error(error);
          },
        });

        $.ajax({
          type: "POST",
          url: "/user/wishList",
          dataType: "json",
          success: function (response) {
            $("#wishcnt").text(response.length);
          },
          error: function (xhr, status, error) {
            console.error(error);
          },
        });

        $(".sc-gVyKpa").click(function () {
          $(".sc-gVyKpa").removeClass("kHfkPy").addClass("gmerZt");
          $(this).removeClass("gmerZt").addClass("kHfkPy");
          $(".sc-gVyKpa.gmerZt2").removeClass("gmerZt2").addClass("kHfkPy");
        });

        $("#save_btn").click(function () {
          var wishKeyword = $("#name_box").val(); // 입력된 키워드 가져오기

          $.ajax({
            type: "POST",
            url: "/user/wishKeyword",
            data: { wishKeyword: wishKeyword }, // 서버로 보낼 데이터
            success: function (response) {
              alert("키워드가 등록되었습니다"); // 성공 시 alert으로 메시지 표시
              window.location.href = "/user/myInfo";
            },
            error: function (xhr, status, error) {
              console.error(error); // 에러 처리
            },
          });
          $("#modal").removeClass("show");
        });

        displayKeywordList();

        function displayKeywordList() {
        	
          $.ajax({
            type: "POST",
            url: "/user/keywordList",
            data: {"userId" : userId},
            dataType: "json",
            success: function (response) {
              let keywordList = response;

              let wishKeywordsDisplay = $("#wishKeywordsDisplay");

              keywordList.forEach(function (item) {
                let newDiv = $("<div></div>");
                newDiv.addClass("keywordItem");
                newDiv.text(item.wishKeyword);

                let checkbox = $("<input>");
                checkbox.attr("type", "checkbox");
                checkbox.attr("id", "delwkl");
                checkbox.val(item.wishKeyword);

                newDiv.prepend(checkbox);

                wishKeywordsDisplay.append(newDiv); // 만들어진 div를 wishKeywordsDisplay에 추가
              });
            },
          });
        }

        $("#editUserInfoBtn").click(function () {
          window.location.href = "/user/infoUpdate"; // 해당 URL로 이동
        });

        $("#deleteUser").click(function () {
          window.location.href = "/user/deleteUser"; // 해당 URL로 이동
        });
      });

      $(document).on("click", "#wishKeywordbtn", function (e) {
        $("#modal").addClass("show");
      });

      $(document).on("click", "#close_btn", function (e) {
        $("#modal").removeClass("show");
      });

      function purchaseList() {
        $(".sc-eEieub").hide();
        $.ajax({
          type: "POST",
          url: "/user/purchaseList",
          dataType: "json",
          success: function (response) {
            $("#buycnt").text(response.length);
            let productContainer = $(".product-container");
            productContainer.empty(); // 기존 상품 삭제
            // 서버에서 받은 데이터로 상품 목록 생성
            response.forEach(function (product) {
            	let postStatus = product.postStatus;
            	let postPrice = product.postPrice;
            	const isDisabled = product.review === "평가 완료" ? 'disabled' : ''; // 작성 완료면 비활성화
            	let productDiv = 
                    '<div class="sc-bIqbHp deAjQN">'
                        +'<a class="sc-dREXXX hsBvIx" href="/post/getPost/'+product.postId+'">'
                        
                        +'<div class="sc-iIHSe liqURL">'
                        +'<input type="button" value="'+product.review+'" class="review" data-postid="'+product.postId+'" '+isDisabled+'/>'
                        +'</div>'
                            +'<div class="sc-kcbnda cgazhg">'
                                +'<img src= "/resources/pic/postPic/'+product.fileName+'" alt="상품 이미지" />'
                                +'<div class="sc-cJOK bosAdb"></div>'
                           +'</div>'
                            +'<div class="sc-dHmInP boOsEk">'
                                +'<div class="sc-hcmgZB izvjFP">'
                                    +'<div class="sc-ejGVNB hjxvQC">'+product.postTitle+'</div>'
                                    +'<div class="sc-iiUIRa jBkujo">'
                                        +'<div class="postPrice">'+addComma(postPrice.toString())+'원</div>'
                                    +'</div>'
                                    +'<div class="sc-eLdqWK cEmwLf">'+product.postRegdate.toString()+'</div>'
                                +'</div>'
                                +'<div class="sc-hgRTRy eYqEDw">';
                                if(postStatus == "ENABLE")  productDiv += '판매중'
                                if(postStatus == "RESERVE")  productDiv += '예약중'
                                if(postStatus == "FINISH")  productDiv += '판매완료'
                                productDiv+= '</div>'
                            +'</div>'
                        +'</a>'
                    +'</div>';

              productContainer.append(productDiv); // 상품을 컨테이너에 추가
            });
            
            $('.review').on('click', function(event) {
            	event.preventDefault();
                const postId = $(this).data('postid'); // 해당 후기 버튼의 postID 가져오기
                $('#hugiModal').data('postid', postId)
                $('#hugiModal').addClass('show');      
            });      
            
         // 모달 내부의 이미지 클릭 이벤트 핸들러
            $('#hugiModal').one('click', '.ondoUp', function() {
                const postId = $('#hugiModal').data('postid'); // 모달 내 postId 가져오기
                // postId를 사용하여 Ajax 요청을 보냄
                $.ajax({
                    type: 'POST',
                    url: 'ondoUpDown',
                    data: { postId: postId, action: 'up' },
                    success: function(response) {
                    	alert("반영되었습니다");
                    	location.replace("myInfo")
                    },
                    error: function(xhr, status, error) {
                        // 오류 발생 시 동작
                        console.error(error);
                    }
                });
            });

            $('#hugiModal').one('click', '.ondoDown', function() {
                const postId = $('#hugiModal').data('postid'); // 모달 내 postId 가져오기
                // postId를 사용하여 Ajax 요청을 보냄
                $.ajax({
                    type: 'POST',
                    url: 'ondoUpDown',
                    data: { postId: postId, action: 'down' },
                    success: function(response) {
                    	alert("반영되었습니다");
                    	location.replace("myInfo")
                    },
                    error: function(xhr, status, error) {
                        // 오류 발생 시 동작
                        console.error(error);
                    }
                });
            });
            
          },
          error: function (xhr, status, error) {
            console.error(error);
          },
        });
      }

      function salesList() {
    	  let userId = $(".sc-kqlzXE.bvAAFa").attr("data-userid");
    	  let productContainer = $(".product-container");
    	  productContainer.html(""); // 기존 상품 삭제
        $(".sc-eEieub").hide();
        $.ajax({
          type: "POST",
          url: "/user/salesList",
          data: {"userId": userId},
          dataType: "json",
          success: function (response) {
            
            
            // 서버에서 받은 데이터로 상품 목록 생성
            response.forEach(function (product) {
            	let postStatus = product.postStatus;
            	let postPrice = product.postPrice;
            	let productDiv = 
                    '<div class="sc-bIqbHp deAjQN">'
                        +'<a class="sc-dREXXX hsBvIx" href="/post/getPost/'+product.postId+'">'
                            +'<div class="sc-kcbnda cgazhg">'
                                +'<img src= "/resources/pic/postPic/'+product.fileName+'" alt="상품 이미지" />'
                                +'<div class="sc-cJOK bosAdb"></div>'
                           +'</div>'
                            +'<div class="sc-dHmInP boOsEk">'
                                +'<div class="sc-hcmgZB izvjFP">'
                                    +'<div class="sc-ejGVNB hjxvQC">'+product.postTitle+'</div>'
                                    +'<div class="sc-iiUIRa jBkujo">'
                                        +'<div class="postPrice">'+addComma(postPrice.toString())+'원</div>'
                                    +'</div>'
                                    +'<div class="sc-eLdqWK cEmwLf">'+product.postRegdate.toString()+'</div>'
                                +'</div>'
                                +'<div class="sc-hgRTRy eYqEDw">';
                                if(postStatus == "ENABLE")  productDiv += '판매중'
                                if(postStatus == "RESERVE")  productDiv += '예약중'
                                if(postStatus == "FINISH")  productDiv += '판매완료'
                                productDiv+= '</div>'
                            +'</div>'
                        +'</a>'
                    +'</div>';
				
              productContainer.append(productDiv); // 상품을 컨테이너에 추가
            });
          },
          error: function (xhr, status, error) {
            console.error(error);
          },
        });
      }

      function wishList() {
        $(".sc-eEieub").show();
        $.ajax({
          type: "POST",
          url: "/user/wishList",
          dataType: "json",
          success: function (response) {
            let productContainer = $(".product-container");
            productContainer.empty(); // 기존 상품 삭제
            // 서버에서 받은 데이터로 상품 목록 생성
            response.forEach(function (product) {
            	let postStatus = product.postStatus;
            	let postPrice = product.postPrice;
            	let productDiv = 
                    '<div class="sc-bIqbHp deAjQN">'
                        +'<a class="sc-dREXXX hsBvIx" href="/post/getPost/'+product.postId+'">'
                        + ' <div class="sc-iIHSe liqURL">'
                        + '<input type="checkbox" name="RowCheck" value="'+product.postId+'">'
                        + '</div>'
                            +'<div class="sc-kcbnda cgazhg">'
                                +'<img src= "/resources/pic/postPic/'+product.fileName+'" alt="상품 이미지" />'
                                +'<div class="sc-cJOK bosAdb"></div>'
                           +'</div>'
                            +'<div class="sc-dHmInP boOsEk">'
                                +'<div class="sc-hcmgZB izvjFP">'
                                    +'<div class="sc-ejGVNB hjxvQC">'+product.postTitle+'</div>'
                                    +'<div class="sc-iiUIRa jBkujo">'
                                        +'<div class="postPrice">'+addComma(postPrice.toString())+'원</div>'
                                    +'</div>'
                                    +'<div class="sc-eLdqWK cEmwLf">'+product.postRegdate.toString()+'</div>'
                                +'</div>'
                                +'<div class="sc-hgRTRy eYqEDw">';
                                if(postStatus == "ENABLE")  productDiv += '판매중'
                                if(postStatus == "RESERVE")  productDiv += '예약중'
                                if(postStatus == "FINISH")  productDiv += '판매완료'
                                productDiv+= '</div>'
                            +'</div>'
                        +'</a>'
                    +'</div>';

              productContainer.append(productDiv); // 상품을 컨테이너에 추가
            });
          },
          error: function (xhr, status, error) {
            console.error(error);
          },
        });
      }

      function openFileUploader() {
        document.getElementById("uploadFileInput").click();
      }

      function uploadSelectedFile(event) {
        const file = event.target.files[0];
        if (!file.type.match("image.*")) {
          alert("이미지 파일을 선택해주세요.");
          return;
        }

        const formData = new FormData();
        formData.append("uploadFile", file);

        $.ajax({
          type: "POST",
          url: "/user/profileUpload",
          data: formData,
          processData: false,
          contentType: false,
          success: function (response) {
            // 성공 시 동작
            alert("프로필사진이 변경되었습니다.");
            location.reload();
          },
          error: function (xhr, status, error) {
            // 에러 시 동작
            console.error("파일 업로드 에러:", error);
          },
        });
      }

      var chkObj = document.getElementsByName("RowCheck");
      var rowCnt = chkObj.length;

      function deleteValue() {
        var url = "/user/deleteWish"; // Controller로 보내고자 하는 URL
        var valueArr = new Array();
        var list = $("input[name='RowCheck']");
        for (var i = 0; i < list.length; i++) {
          if (list[i].checked) {
            //선택되어 있으면 배열에 값을 저장함
            valueArr.push(list[i].value);
          }
        }
        if (valueArr.length == 0) {
          alert("선택된 글이 없습니다.");
        } else {
          var chk = confirm("정말 삭제하시겠습니까?");
          if (chk) {
            $.ajax({
              url: url, // 전송 URL
              type: "POST", // POST 방식
              traditional: true,
              data: {
                valueArr: valueArr, // 보내고자 하는 data 변수 설정
              },
              success: function (jdata) {
                if ((jdata = 1)) {
                  alert("관심 해제되었습니다");
                  location.replace("myInfo"); //list 로 페이지 새로고침
                } else {
                  alert("삭제 실패");
                }
              },
            });
          } else {
            // 아무 동작 없음
          }
        }
      }

      function deleteWishKeyword() {
        var url = "/user/deleteWishKeyword"; // Controller로 보내고자 하는 URL
        var valueArr = new Array();
        var list = $("input[id='delwkl']");
        for (var i = 0; i < list.length; i++) {
          if (list[i].checked) {
            // 선택되어 있으면 배열에 값을 저장함
            valueArr.push(list[i].value);
          }
        }
        if (valueArr.length == 0) {
          alert("관심 키워드를 선택해주세요");
        } else {
          var chk = confirm("정말 삭제하시겠습니까?");
          if (chk) {
            $.ajax({
              url: url, // 전송 URL
              type: "POST", // POST 방식
              traditional: true,
              data: {
                valueArr: valueArr, // 보내고자 하는 data 변수 설정
              },
              success: function (jdata) {
                if ((jdata = 1)) {
                  alert("관심 키워드가 삭제되었습니다");
                  location.replace("myInfo"); // list 로 페이지 새로고침
                } else {
                  alert("삭제 실패");
                }
              },
            });
          } else {
            // 아무 동작 없음
          }
        }
      }
      function addComma(value){
          value = value.replace(/\B(?=(\d{3})+(?!\d))/g, ",");
          return value; 
      }
      $(document).on('click', '#hclose_btn', function (e) {
    		$('#hugiModal').removeClass('show'); 
    	}); //후기모달버튼 닫기 
    </script>
  </head>
  <body>
  
  	<!-- header -->
	<%@ include file="/WEB-INF/views/main/inc/header.jsp" %>
    <div class="sc-kqlzXE bvAAFa" data-userid="${user.userId }">
      <div class="sc-jRhVzh kyOmSl">
        <div class="sc-jXQZqI jiarQh">
          <div class="sc-iGPElx ermrpj">
            <div class="sc-hzNEM dNTrAm">
              <div size="310" class="sc-chbbiW ipXCvk">
                <div class="sc-gmeYpB leXbQf">
                  <div class="sc-kZmsYB iIspdD"></div>
                </div>
                <div class="sc-kxynE eRUQZt">
                  <img
                    src="/resources/pic/profilePic/${user.userProfile}"
                    width="100"
                    height="100"
                    alt="프로필 이미지"
                    class="sc-cooIXK bORTpS"
                  />
                  <div class="sc-fcdeBU wCwuR">${user.userNickname}</div>
                  <div class="sc-iBEsjs dQLwrX">${user.userOndo}°C</div>
                  <c:if test="${nowUserId == user.userId }">
	                  <div class="sc-RcBXQ dsTcaJ">
	                    <a
	                      class="sc-bEjcJn jHSADa"
	                      href="javascript:void(0);"
	                      onclick="openFileUploader()"
	                      >프로필 변경</a
	                    >
	
	                    <input
	                      type="file"
	                      id="uploadFileInput"
	                      name="uploadFile"
	                      accept="image/*"
	                      style="display: none"
	                      onchange="uploadSelectedFile(event)"
	                    />
	                  </div>
                  </c:if>
                </div>
              </div>
            </div>
          </div>
          <div class="sc-hgHYgh kDkgVT">
            <div class="sc-eInJlc hkaFNj">
              <div class="sc-likbZx ixFzSN">
                ${user.userNickname}
                <c:if test="${nowUserId == user.userId }">
	                <button class="sc-kasBVs blcYdX" id="editUserInfoBtn">회원정보 수정</button>
	                <button class="sc-kasBVs blcYdX" id="deleteUser">회원 탈퇴</button>
                </c:if>
              </div>
            </div>
            <div class="sc-gtfDJT bheHtc">
              <div class="sc-fOICqy inwZpQ">
                ${user.userNickname}의 마켓 시작
                <div class="sc-hzDEsm cskGhu">${user.userRegdate}</div>
              </div>
            </div>
            <div class="sc-hRmvpr eMCCXV" id="wishKeywordsDisplay">
              <div id="wkl"></div>
            </div>
            <c:if test="${nowUserId == user.userId }">
	            <div class="sc-ecaExY iPyIZi">
	              <button class="sc-jKmXuR thnxK" id="wishKeywordbtn">관심 키워드 설정</button>
	              <button class="sc-jKmXuR thnxK" id="wishKeyDeletebtn" onclick="deleteWishKeyword();">
	                관심 키워드 삭제
	              </button>
	            </div>
            </c:if>
          </div>
        </div>
      </div>
      <div class="sc-OxbzP fUqZYe">
        <div class="sc-cFlXAS hJvCHs">
          <div class="sc-gpHHfC dvLjiM">
          <c:if test="${nowUserId == user.userId }">
	            <a class="sc-gVyKpa kHfkPy" href="javascript:purchaseList()"
	              >구매내역
	              <span class="sc-cpmKsF gyLEXJ" id="buycnt"></span>
	            </a>
            </c:if>

            <a class="sc-gVyKpa gmerZt" href="javascript:salesList()"
              >판매내역
              <span class="sc-cpmKsF gyLEXJ" id="salecnt"></span>
            </a>
			<c:if test="${nowUserId == user.userId }">
	            <a class="sc-gVyKpa gmerZt2" href="javascript:wishList()"
	              >관심
	              <span class="sc-cpmKsF gyLEXJ" id="wishcnt"></span>
	            </a>
            </c:if>
          </div>
        </div>
        <div class="sc-hcnlBt jfMCiR">
          <div class="sc-hkbPbT wqlOc">
            <div class="sc-kQsIoO gvsXqb"></div>
            <div class="sc-jxGEyO drYqMd">
              <div class="sc-dEfkYy ckyNBp">
                <div class="sc-cqPOvA gxRxBk">
                	<c:if test="${nowUserId == user.userId }">
                		<button class="sc-eEieub gEiEYU" onclick="deleteValue();">선택삭제</button>
                  	</c:if>
                </div>
              </div>
              <div class="sc-drlKqa hupeiQ">
                <div class="product-container">
                  <div class="sc-bIqbHp deAjQN">
                    <!-- 여기에 반복 -->
                    <a class="sc-dREXXX hsBvIx">
                      <div class="sc-iIHSe liqURL">
                        <input type="checkbox" id="delcheck" />
                      </div>
                      <div class="sc-kcbnda cgazhg">
                        <div class="sc-cJOK bosAdb"></div>
                      </div>
                      <div class="sc-dHmInP boOsEk">
                        <div class="sc-hcmgZB izvjFP">
                          <div class="sc-ejGVNB hjxvQC"></div>
                          <div class="sc-iiUIRa jBkujo">
                            <div id="postPrice"></div>
                          </div>
                          <div class="sc-eLdqWK cEmwLf"></div>
                        </div>
                        <div class="sc-hgRTRy eYqEDw"></div>
                      </div>
                    </a>
                  </div>
                  <!-- 여기에 반복  끝-->
                </div>
              </div>
            </div>
            <div class="sc-fjhmcy flxQHS"></div>
          </div>
        </div>
      </div>
    </div>
  </body>

  <!-- 모달 -->
  <div class="modal" id="modal">
    <div class="modal_body">
      <div class="m_head">
        <div class="modal_title">나의 관심 키워드</div>
        <div class="close_btn" id="close_btn">X</div>
      </div>
      <div class="m_body">
        <input
          type="text"
          class="input_box"
          id="name_box"
          maxlength="30"
          placeholder=" 관심 키워드를 입력해주세요"
        />
        <div id="modalwish"></div>
      </div>
      <div class="m_footer">
        <div class="modal_btn save" id="save_btn">확인</div>
      </div>
    </div>
  </div>
  <!-- 모달 -->
  <!-- 후기 모달 -->
<div class="hugiModal" id="hugiModal">
	<div class="hugiModal_body">
		<div class="hm_head">
			<div class="hmodal_title">평가해주세요</div>
			<div class="hclose_btn" id="hclose_btn">X</div>
		</div>
		<div class="hm_body">
			<div class="good"><img src="/resources/pic/emoji/15.png" class="ondoUp"></div>
			<div class="bad"><img src="/resources/pic/emoji/16.png" class="ondoDown"></div>
		</div>
		<div class="hm_footer">
		</div>
	</div>

</div>
<!-- 후기 모달 -->
</html>
