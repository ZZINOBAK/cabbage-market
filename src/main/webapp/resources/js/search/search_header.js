// 검색어 입력창과 결과를 표시할 요소를 가져옵니다.
const hiddenFunc = document.getElementsByTagName("nav")[0];
const searchInput = document.getElementById("searchInput");
const searchResults = document.getElementById("searchResults");
const searchKeyword = document.getElementById("searchKeyword");
const searchButton = document.getElementById("searchButton");
const clearSearchInput = document.getElementById("clearSearchInput");
//=================== 검색창 입력  ================
$(() => {
  let keyword = $("#content").attr("data-keyword");
  if (keyword) searchInput.value = keyword;
  $(document).on("click", ".list-group .delBtn", function (e) {
    e.preventDefault();

    let result = $(this).prev().text();
    deleteRecentSearch(result);
  });
});
// 마우스 클릭으로 검색 결과 가져오기
searchButton.addEventListener("click", function () {
  searchKeywordResults();
});

// 엔터로 검색 결과 가져오기
searchInput.addEventListener("keyup", function (event) {
  if (event.key === "Enter") {
    event.preventDefault(); // 기본 엔터 동작 방지
    searchKeywordResults();
  }
});

// 검색어 입력창에 입력 이벤트 리스너를 추가합니다.
searchInput.addEventListener("input", function () {
  // 입력값을 가져옵니다.
  var query = searchInput.value;
  clearSearchInput.style.display = "inline-block";

  // 검색어 자동완성 결과를 업데이트하는 함수를 호출합니다.
  if (query.length > 0) {
    updateAutocompleteResults(query);
  } else {
    clearSearchInput.style.display = "none";
    searchResults.innerHTML = "";
    recentResults();
  }
});

//최근기록 리스너
searchInput.addEventListener("focus", function () {
  var query = searchInput.value;
  if (query.length < 1) {
    searchResults.innerHTML = "";
    recentResults();
    $(document).on("mousedown.keyword", function (e) {
      let $target = $(e.target);
      if (
        $target.hasClass("input-haeun1") ||
        $target.hasClass("list-group") ||
        $target.hasClass("recentKeyword") ||
        $target.hasClass("delBtn") ||
        $target.hasClass("deleteAll")
      ) {
        return;
      }

      if ($target.hasClass("list-group")) return;
      searchResults.innerHTML = "";
      $(document).off("mousedown.keyword");
    });
  }
});

//  x 닫기 버튼 클릭 이벤트
clearSearchInput.addEventListener("click", function () {
  searchInput.value = "";
  clearSearchInput.style.display = "none";
});

// 검색어 입력창에 값을 설정하는 함수 정의
function setSearchInputValue(value) {
  searchInput.value = value;
  // 검색 결과를 숨깁니다.
  searchResults.style.display = "none";
  clearSearchInput.style.display = "block";
  // 만일 검색 결과를 실행하려면 아래에 함수 추가
  searchKeywordResults();
}

// 검색 결과
function searchKeywordResults() {
  searchResults.style.display = "none";
  var keyword = searchInput.value.trim();
  var trimmedKeyword = keyword.replace(/\s+/g, " ");

  if (trimmedKeyword.length < 1) {
    alert("키워드를 입력하세요.");
    return;
  }
  location.href = "/postList?keyword=" + keyword;
}
// 000단위로 포맷팅하는 함수
function formatPrice(price) {
  return price.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",") + "원";
}

function displayKeywordResults(results) {
  searchKeyword.innerHTML = "";
  // 결과가 없을 경우 메시지 추가
  if (results.length === 0) {
    displayNoResults();
  } else {
    // 결과를 반복하며 리스트 아이템을 생성하고 추가합니다.
    results.forEach(function (result) {
      const cardElement = createCardElement(result);
      searchKeyword.appendChild(cardElement);
    });
  }
}

//====================자동완성 (연관 검색어)==================
// 검색어 자동완성 결과를 업데이트하는 함수를 정의합니다.
function updateAutocompleteResults(query) {
  // 새로운 XHR 객체 생성
  var xhr = new XMLHttpRequest();

  // 서버에서 JSON 데이터를 가져올 URL 설정
  var url = "/autocomplete?query=" + query;

  xhr.open("GET", url, true);

  xhr.onreadystatechange = function () {
    if (xhr.readyState === 4 && xhr.status === 200) {
      var data = JSON.parse(xhr.responseText);

      // 자동완성 결과를 업데이트
      displayAutocompleteResults(data);
    }
  };

  // 요청 보내기
  xhr.send();
}
// 최근검색 결과를 표시하는 함수 정의
function displayRecentResults(results) {
  searchResults.innerText = "";
  searchResults.style.display = "block";

  // 결과를 반복하며 리스트 아이템을 생성하고 추가합니다.
  results.forEach(function (result) {
    var listItem = document.createElement("li");
    var spanElement = document.createElement("span");
    listItem.classList.add("recentKeyword");
    spanElement.classList.add("recentKeyword");
    spanElement.style.width = "95%";
    spanElement.textContent = result;

    // 삭제 버튼 추가
    var deleteButton = document.createElement("button");
    deleteButton.className = "delBtn";
    deleteButton.textContent = "X"; // "X"로 표시
    deleteButton.addEventListener("click", function () {
      deleteRecentSearch(result);
    });

    $(document).on("click", "li.recentKeyword", function (e) {
      let $target = $(e.target);
      if ($target.hasClass("delBtn")) return;
      let result = $(this).find("span").text();
      setSearchInputValue(result);
    });

    // listItem에 마우스 이벤트 리스너 추가
    listItem.addEventListener("mouseover", function () {
      listItem.style.backgroundColor = "#eee"; // 마우스가 올라갔을 때의 색상
    });

    listItem.addEventListener("mouseout", function () {
      listItem.style.backgroundColor = ""; // 마우스가 나갔을 때의 색상 (기본값)
    });

    // 리스트 아이템에 삭제 버튼 추가
    listItem.appendChild(spanElement);
    listItem.appendChild(deleteButton);

    searchResults.appendChild(listItem);
  });

  var deleteAllliElement = document.createElement("li");
  var deleteAllSpanElement = document.createElement("span");
  if (results.length > 0) {
    deleteAllSpanElement.style.color = "red";
    deleteAllSpanElement.style.fontWeight = "bold";
    deleteAllSpanElement.textContent = "전체삭제";
    deleteAllSpanElement.classList.add("deleteAll");
  } else {
    deleteAllSpanElement.style.width = "95%";
    deleteAllSpanElement.style.height = "40px";
    deleteAllSpanElement.style.display = "flex";
    deleteAllSpanElement.style.justifyContent = "center";
    deleteAllSpanElement.style.alignItems = "center";
    deleteAllSpanElement.textContent = "최근에 검색한 키워드가 없습니다.";
  }
  deleteAllliElement.appendChild(deleteAllSpanElement);
  deleteAllliElement.addEventListener("click", function () {
    var xhr = new XMLHttpRequest();
    var url = "/deleteAllKeyword";

    xhr.open("GET", url, true);

    xhr.onreadystatechange = function () {
      if (xhr.readyState === 4 && xhr.status === 200) {
        searchResults.innerHTML = "";
        searchResults.style.display = "none";
      }
    };
    xhr.send();
  });
  searchResults.appendChild(deleteAllliElement);
}

// 자동완성 결과를 표시하는 함수 정의
function displayAutocompleteResults(results) {
  searchResults.innerText = "";
  // 결과가 있을 경우, 엘리먼트를 보이게 만듭니다.
  if (results.length > 0) {
    searchResults.style.display = "block";
  } else {
    // 결과가 없을 경우, 엘리먼트를 숨깁니다.
    searchResults.style.display = "none";
  }

  // 결과를 반복하며 리스트 아이템을 생성하고 추가합니다.
  results.forEach(function (result) {
    var listItem = document.createElement("li");
    var spanElement = document.createElement("span");
    listItem.classList.add("recentKeyword");
    spanElement.classList.add("recentKeyword");
    spanElement.style.width = "95%";
    spanElement.textContent = result;

    $(document).on("click", "li.recentKeyword", function (e) {
      let $target = $(e.target);
      if ($target.hasClass("delBtn")) return;
      let result = $(this).find("span").text();
      setSearchInputValue(result);
    });

    // listItem에 마우스 이벤트 리스너 추가
    listItem.addEventListener("mouseover", function () {
      listItem.style.backgroundColor = "#eee"; // 마우스가 올라갔을 때의 색상
    });

    listItem.addEventListener("mouseout", function () {
      listItem.style.backgroundColor = ""; // 마우스가 나갔을 때의 색상 (기본값)
    });

    // 리스트 아이템에 삭제 버튼 추가
    listItem.appendChild(spanElement);

    searchResults.appendChild(listItem);
  });
}

// ===================최근 검색어 목록======================
function recentResults(query) {
  // 새로운 XHR 객체 생성
  var xhr = new XMLHttpRequest();

  // 서버에서 JSON 데이터를 가져올 URL 설정
  var url = "/recent";

  xhr.open("GET", url, true);

  xhr.onreadystatechange = function () {
    if (xhr.readyState === 4 && xhr.status === 200) {
      var data = JSON.parse(xhr.responseText);

      // 자동완성 결과를 업데이트
      displayRecentResults(data);
    }
  };

  // 요청 보내기
  xhr.send();
}
// 최근 검색 기록 삭제 함수
function deleteRecentSearch(searchKeyword) {
  // 새로운 XHR 객체 생성
  var xhr = new XMLHttpRequest();

  // 서버에서 JSON 데이터를 가져올 URL 설정
  var url = "/delete-keyword?keyword=" + searchKeyword;

  xhr.open("GET", url, true);

  xhr.onreadystatechange = function () {
    if (xhr.readyState === 4 && xhr.status === 200) {
      //다시 리스트 띄우기
      recentResults();
    }
  };

  // 요청 보내기
  xhr.send();
}

// ===============인기 검색어 롤링 관련 펑션===========================
const rollingNotice = document.getElementById("rollingNotice");
const showingNotice = document.getElementById("showingNotice");

// 서버에서 롤링 텍스트 데이터를 가져와서 업데이트
function updateRollingTexts() {
  // 새로운 XHR 객체 생성
  var xhr = new XMLHttpRequest();

  // 서버에서 롤링 텍스트 데이터를 가져올 URL 설정
  var url = "/get_rolling";

  xhr.open("GET", url, true);

  xhr.onreadystatechange = function () {
    if (xhr.readyState === 4 && xhr.status === 200) {
      var data = JSON.parse(xhr.responseText);

      // 롤링 텍스트 업데이트
      displayRollingTexts(data);
    }
  };

  // 요청 보내기
  xhr.send();
}

// 롤링 텍스트를 표시하는 함수 정의
function displayRollingTexts(texts) {
  // 기존의 롤링 텍스트 엘리먼트 삭제
  rollingNotice.innerHTML = "";
  showingNotice.innerHTML = "";

  // 서버에서 받아온 롤링 텍스트로 엘리먼트 생성 후 추가
  texts.forEach(function (text, idx) {
    let rollingTextHTML =
      '<li><a href="/postList?keyword=' + text + '">' + (idx + 1) + ". " + text + "</a></li>";

    $("#rollingNotice").append(rollingTextHTML);
    $("#showingNotice").append(rollingTextHTML);
  });
  // 스크롤 시작
  startScroll();
}

//
function noticeScroll() {
  $("#rollingNotice li:first").slideUp(function () {
    $(this).appendTo($("#rollingNotice")).slideDown();
  });
}

let scrollInterval;

function startScroll() {
  scrollInterval = setInterval(noticeScroll, 2000); // 원하는 간격(ms)으로 설정
}

function stopScroll() {
  clearInterval(scrollInterval);
}

updateRollingTexts();
