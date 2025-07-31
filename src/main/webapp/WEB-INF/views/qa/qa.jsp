<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" type="text/css" href="/resources/css/qa/qa.css">
<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
<title>QA 고객센터</title>
<link rel="icon" href="/resources/pic/img/baechu.png" />
</head>
<style>


.search-wrapper #search-bar {
    padding: 1.1rem 4.2rem;
    width: 100%;
    background-color: var(--seed-scale-color-gray-100);
    border-radius: 0.6rem;
    border: none;
    font-size: 2rem;
    font-weight: 400;
    letter-spacing: -2%;
    -webkit-appearance: none;
}

.search-wrapper {
  position: relative;
  z-index: 2;
}
  #search-bar {
    padding: 1.1rem 4.2rem;
    width: 100%;
    background-color: var(--seed-scale-color-gray-100);
    border-radius: 0.6rem;
    border: 1px solid;
    font-size: 2rem;
    font-weight: 400;
    letter-spacing: -2%;
    -webkit-appearance: none;

    &:focus {
       outline: none;
     }

    &::-webkit-search-cancel-button,
    &::-webkit-search-decoration {
       -webkit-appearance: none;
       display: none;
    }
  }

  #search-icon {
    position: absolute;
    top: 1.1rem;
    left: 1.2rem;
  }

  #search-clear {
    position: absolute;
    top: 1.1rem;
    right: 1.2rem;
    border: none;
    background: none;
  }

  #btn-focus-out {
    display: none;
  }
}


#search-clear {
	position: absolute;
    top: 1.1rem;
    right: 1.2rem;
}

#btn-focus-out {
	right: 0;
    top: 0;
    display: flex;
    padding: 0.9rem 0 0.9rem 1.6rem;
    background: none;
    border: none;
    font-size: 1.6rem;
    color: var(--seed-scale-color-gray-900);
      }
    }
  }
}

#background {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 1;

  @media screen and (min-width: 768px) {
    background-color: transparent;
  }

  @media screen and (max-width: 768px) {
    @media (prefers-color-scheme: dark) {
      background-color: var(--seed-scale-color-gray-50);
    }

  @media (prefers-color-scheme: light) {
    background-color: var(--seed-scale-color-gray-00);
   }
 }
}

.hidden {
  @media screen and (min-width: 768px) {
    display: none !important;
  }
}

#feedback-wrapper .cta {
  display: block;
  padding: 1rem 1.6rem;
  font-size: 1.4rem;
  border-radius: 0.6rem;
  background-color: var(--seed-scale-color-gray-100);
  border: none;
  color: var(--seed-scale-color-gray-900);
  font-weight: 700;
  width: fit-content;
  height: fit-content;
  margin-top: 1.6rem;
}
</style>
<body id="qa-index">
	
    
	<div title id="faq-wrapper">
	<a title="" href="/" id="web-title">
			<path d="" fill="#FF6F0F"></path> 
			<img src="/resources/pic/img/baechu.png" width="45" height="45" />
	<div title>고객센터</div>
	</a>
    
    <br>
    <div title="" class="content-container"></div>
	    <div title=""></div>
		    <h1 title="" class="">안녕하세요,</h1>
		    <h1 title="">무엇을 도와드릴까요? 🤔</h1>
		    <br>
		<div title="" title="">
	<!-- SEARCH BAR 부분 -->
	<div searchBar="" class="search-wrapper ">
	<div searchBar="" class="input-area-wrapper">
    	<form searchBar="" action="/qa/faqKeywordList" method="get">
		    <!-- 돋보기 아이콘 추가 -->
		    <svg searchBar="" width="18" height="18" viewBox="0 0 18 18" fill="none" xmlns="http://www.w3.org/2000/svg" id="search-icon">
		    <path fill-rule="evenodd" clip-rule="evenodd" d="M7.75065 0.833008C3.93068 0.833008 0.833984 3.92971 0.833984 7.74967C0.833984 11.5696 3.93068 14.6663 7.75065 14.6663C9.42056 14.6663 10.9522 14.0746 12.1474 13.0893L16.0292 16.9711C16.2896 17.2314 16.7117 17.2314 16.9721 16.9711C17.2324 16.7107 17.2324 16.2886 16.9721 16.0283L13.0902 12.1465C14.0755 10.9513 14.6673 9.41958 14.6673 7.74967C14.6673 3.92971 11.5706 0.833008 7.75065 0.833008ZM2.16732 7.74967C2.16732 4.66608 4.66706 2.16634 7.75065 2.16634C10.8342 2.16634 13.334 4.66608 13.334 7.74967C13.334 10.8333 10.8342 13.333 7.75065 13.333C4.66706 13.333 2.16732 10.8333 2.16732 7.74967Z" fill="#868B94"></path>
		    </svg>
		    <!-- 검색바 입력 필드 -->
		    <input searchBar="" id="search-bar" type="search" name="searchKeyword" placeholder="궁금한 것을 검색해보세요.">
  		</form>
 	</div>
 	</div></div>
 	
	<br><br>
	
	<h1 style="font-size:23px;">자주 묻는 질문</h1>
	<br>
	<!-- FAQ 리스트 -->
	<div id="faqLists">
	<c:forEach var="faq" items="${faqList }">
		<input type="radio" name="accordion" value="${faq.faqTitle }" id="radioFAQ" style="display:none;">
		<button class="collapsible">${faq.faqTitle }</button>
		<div class="content" >
		  <p style="font-weight: 700; font-style: italic; font-size: 17px;">🔎 ${faq.faqContent }</p>
		</div>
	</c:forEach>
	</div>
	
	
    
	<div title="" id="feedback-wrapper" style="margin-bottom: -4rem;" >
	<div feedback="" id="feedback-wrapper" style="margin-bottom: -4rem; display: flex; gap: 33rem;">
		<h2 title="">도움이 필요하신가요?</h2>
		<div qna="" id="qna" style="display: flex; gap: 30px;">
			<a title="" href="/qa/qaCategory" class="cta">문의하기</a>
			<a title="" href="/qa/qaFormList" class="cta">문의내역</a>
		</div>
	</div></div>
	<ul data-v-4b2e992c="" data-v-8cce96fc="" id="infos-wrapper" class="">
	<li data-v-4b2e992c="">
	<a data-v-4b2e992c="" href="https://www.daangn.com/policy/terms">이용약관</a></li>
	<li data-v-4b2e992c="">
	<a data-v-4b2e992c="" href="https://privacy.daangn.com/">개인정보처리방침</a></li>
	<li data-v-4b2e992c=""><a data-v-4b2e992c="" href="/wv/faqs/slug/youth_protection_policy">청소년 보호정책</a></li> 
	<li data-v-4b2e992c=""><a data-v-4b2e992c="" href="/wv/faqs?company-info=true"> 회사정보</a></li> 
	<li data-v-4b2e992c="" id="copyright">© 배추마켓</li></ul>
	</div>
<script type="text/javascript">
$(".collapsible").click(function(){
	$("#radioFAQ").click();
	$("#radioFAQ").next().click(function(){
		//alert('dkjdk');
		$("#radioFAQ").hide().click(function() {
		$(".content p").next().slideToggle(1000);
		});
	})
});


var coll = document.getElementsByClassName("collapsible");
var i;

for (i = 0; i < coll.length; i++) {
  coll[i].addEventListener("click", function() {
    this.classList.toggle("active");
    var content = this.nextElementSibling;
    if (content.style.maxHeight){
      content.style.maxHeight = null;
    } else {
      content.style.maxHeight = content.scrollHeight + "px";
    } 
  });
}

// searchBar 부분
   $(document).ready(function () {
        $('#search-bar').on('input', function () {
            var searchTerm = $(this).val().toLowerCase();

            $('.faq-item').each(function () {
                var faqTitle = $(this).find('.collapsible').text().toLowerCase();

                if (faqTitle.includes(searchTerm)) {
                    $(this).show();
                } else {
                    $(this).hide();
                }
            });
        });
    });


</script>	

</body>
</html>