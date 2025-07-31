# 📦 Cabbage-Market

JSP/Servlet 기반의 중고거래 웹 애플리케이션입니다.  
사용자는 회원가입 후 상품을 등록하거나 구매할 수 있으며,  
관리자는 사용자 및 상품을 관리할 수 있습니다.

---

## 🛠 주요 기능

### 사용자
- 회원가입 / 로그인 / 로그아웃
- 상품 목록 조회
- 상품 등록 / 수정 / 삭제
- 상품 검색 및 상세 보기
- 장바구니 담기 및 주문 기능
- 실시간 알림 (SSE 기반)

### 관리자
- 회원 관리 (목록 조회, 상세 조회, 강제 탈퇴 등)
- 상품 관리 (상품 검수 및 상태 변경 등)

---

## 🧑‍💻 사용 기술

- **Backend**: Java, Servlet, JSP, JDBC, MyBatis
- **Frontend**: HTML, CSS, JavaScript, JSTL
- **Database**: Oracle
- **Build Tool**: Maven
- **Real-time**: Server-Sent Events (SSE)
- **IDE**: Spring Tool Suite (STS)
- **VCS**: Git, GitHub

---

## 🗂 프로젝트 구조

```
Cabbage_Market/
├── src/
│   └── com.market.controller     # 서블릿 컨트롤러
│   └── com.market.model.dao      # DAO
│   └── com.market.model.dto      # DTO
│   └── com.market.model.service  # 서비스
│   └── com.market.model.vo       # VO
├── WebContent/
│   └── jsp/                      # JSP 페이지
│   └── css/                      # CSS 파일
│   └── js/                       # JS 파일
│   └── images/                   # 이미지 리소스
├── .gitignore
├── pom.xml
└── README.md
```

---

## 🧑‍🏫 담당 역할

- 실시간 알림 기능 구현 (SSE 기반)
- 장바구니 및 주문 내역 연동
- 데이터 흐름 분석 및 오류 해결
- 팀원 코드 리뷰 및 문서 정리

---

## 💡 트러블슈팅 및 기술 포인트

- **SSE(Server-Sent Events)** 기반 실시간 알림 구현  
  다양한 블로그 참고 → 코드 정리 및 직접 적용  
  데이터 흐름 구조 이해 후 기능 완성

- **장바구니와 주문 내역 연동 중 타입 불일치 문제 해결**  
  전달 데이터 타입 및 구조 정리 후 로직 수정
