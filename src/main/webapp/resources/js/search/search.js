// 검색어 입력창과 결과를 표시할 요소를 가져옵니다.

// 000단위로 포맷팅하는 함수
function formatPrice(price) {
  return price.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",") + "원";
}

const cards = document.querySelectorAll(".card-top");

cards.forEach((card) => {
  const priceElement = card.querySelector(".card-price");
  const price = parseFloat(priceElement.textContent.replace(/[^0-9]/g, "")); // 문자열에서 숫자만 추출
  const formattedPrice = price.toLocaleString();
  priceElement.textContent = `${formattedPrice}원`;
});

// 리스트 불러오기 함수()
function getList(curPage, param, isCategory) {
  var data;
  if (isCategory) {
    data = { curPage: curPage, keyword: param };
  } else {
    data = { curPage: curPage, category: param };
  }
  $.ajax("morePostList", {
    type: "get",
    data: data,
    success: function (data) {
      $(".cards-wrap").append(data);
      $("#load").hide();
      isLoading = false;
    },
  });
}

var paging = 8;
var totalP = document.getElementById("totalCField").value / paging;
var searchParams = new URLSearchParams(window.location.search);
var category = searchParams.get("category");
var keyword = searchParams.get("keyword");
var curPage = 1; //페이지 초기값
var isLoading = false; //현재 페이지가 로딩중인지 여부
var param = keyword !== null ? keyword : category;
var isCategory = keyword !== null ? true : false;

$(window).on("scroll", function () {
  var scrollTop = $(window).scrollTop();
  var windowsHeight = $(window).height();
  var documentHeight = $(document).height();

  var isBottom = Math.ceil(scrollTop + windowsHeight) >= documentHeight;
  if (isBottom) {
    if (curPage >= totalP) {
      return false;
    } else {
      isLoading = true;
      $("#load").show();
      curPage++;
      getList(curPage, param, isCategory);
    }
  }
});
