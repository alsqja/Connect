<div align="center">

<!-- logo -->
<img src="https://capsule-render.vercel.app/api?type=waving&height=200&text=Connect&fontAlign=50&fontAlignY=40&color=0:0099ff,100:0066cc&fontColor=FFFFFF&stroke=FFFFFF&strokeWidth=2" width="400"/>

</div> 

## 📝 프로젝트 소개

### 🎯connect 프로젝트
#### ~를 백엔드로 구현하는 프로젝트

## 🍨 Team 97s
|                                      Backend & Frontend                                       |                                      Backend                                       |
|:----------------------------------------------------------------------------------:|:----------------------------------------------------------------------------------:|
| ![](https://cdn-static.zep.us/static/assets/baked-avartar-images/8-56-26-332.png)  | ![](https://cdn-static.zep.us/static/assets/baked-avartar-images/10-58-53-336.png) |
|                        [김민범](https://github.com/alsqja)                         |                        [이경섭](https://github.com/gyungsubLee)                         |
|                             유저, 매칭, CI/CD, 일정                              |                                   채팅, 알림                                    |
| ![](https://cdn-static.zep.us/static/assets/baked-avartar-images/2-279-20-325.png) | ![](https://cdn-static.zep.us/static/assets/baked-avartar-images/10-72-41-563.png) |
|                        [조현지](https://github.com/chohyuun)                         |                        [장은영](https://github.com/eunyounging)                         |
|                             결제, 포인트, 쿠폰                           |                                 카테고리/서브 카테고리, 배너, 신고                               |
<br />

## ⚙ 기술 스택
### Back-end
<div>
<img src="https://github.com/yewon-Noh/readme-template/blob/main/skills/Java.png?raw=true" width="80">
<img src="https://github.com/yewon-Noh/readme-template/blob/main/skills/SpringBoot.png?raw=true" width="80">
<img src="https://github.com/yewon-Noh/readme-template/blob/main/skills/SpringSecurity.png?raw=true" width="80">
<img src="https://github.com/yewon-Noh/readme-template/blob/main/skills/SpringDataJPA.png?raw=true" width="80">
<img src="https://github.com/yewon-Noh/readme-template/blob/main/skills/Mysql.png?raw=true" width="80">

- **IDE** : IntelliJ  
- **JDK** : openjdk version '17.0.2'
- **Framework** : springframework.boot version '3.4.1', Spring Data JPA
  - 선택 이유 : 

</div>

### Infra
<div>
<img src="https://github.com/yewon-Noh/readme-template/blob/main/skills/AWSEC2.png?raw=true" width="80">
</div>

### Tools
<div>
<img src="https://github.com/yewon-Noh/readme-template/blob/main/skills/Github.png?raw=true" width="80">
<img src="https://github.com/yewon-Noh/readme-template/blob/main/skills/Notion.png?raw=true" width="80">
<img src="https://github.com/yewon-Noh/readme-template/blob/main/skills/Docker.png?raw=true?raw=true" width="80">

- **Tool** : Figma, DBdiagram, Slack, Github & git, Postman, Docker
- **Build Tool** : Gradle
</div>

<br />

## 프로젝트 구조

```plaintext
📦 connect-project
├── 📂 src
│   ├── 📂 main
│   │   ├── 📂 java
│   │   │   ├── 📂 
│   │   │   ├── 📂 
│   │   │   ├── 📂 
│   │   │   ├── 📂 global   # 공통 코드 (ex. config, filter)
│   │   │   ├── 📂 
│   │   │   ├── 📂 
│   │   │   ├── 📂 
│   │   │   ├── 📂 
│   │   │   └── 📂 
│   └── 📂 test
├── 📄 .gitignore
├── 📄 Dockerfile
└── 📄 README.md
```

## 🛠️ 프로젝트 아키텍쳐
<details>
<summary>와이어 프레임</summary>

<img src="">

👉🏻 [와이어 프레임 바로보기](https://www.figma.com/design/iMqa9R5iK9aNcW81Xyl2Bi/01-%EC%B5%9C%EC%A2%85-%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8-%EC%99%80%EC%9D%B4%EC%96%B4-%ED%94%84%EB%A0%88%EC%9E%84?node-id=0-1&p=f&t=7h2GcHjh7sLqGqeo-0)
</details>

<details>
<summary>DBdiagram</summary>
<img src="">

👉🏻 [DBdiagram 바로보기](https://dbdiagram.io/d/connect-677631885406798ef7139d06)

</details>

<br />

## 🪧 커밋 컨벤션
<details>
<summary>커밋 컨벤션 펼치기</summary>

- feat : 기능 추가

- fix : 기능 수정
  
- hotfix : 기능 급하게 수정
  
- test : 테스트 코드 작성
  
- refactor : 리팩토링
  
- docs : 문서 작업
  
- style : 코드 스타일 등 로직 변경 외 처리

- PR 은 이슈당 하나 씩
  
- 브랜치 기능별로 분리 (feature/login, feature/signup)

- 모두 approve 되면 merge

- 브랜치 규칙
  - main
  - develop
  - feature/기능
  - fix/기능
  - refactor/반영한 부분
</details>

## 👔 코드 컨벤션
<details>
<summary>코드 컨벤션 펼치기</summary>

- 개행, 띄어쓰기
  - formatter 사용
  - return 앞에 한 줄 띄우기

- 클래스 명
  - PascalCase 사용 (ex : UserAccount)

- 변수 명
  - camelCase 사용 (ex : firstName)

- 패키지 구조 : 도메인 형 (ex : domain / global)

- constructor 사용

- Error message -> Enum 관리

- import * 규칙

- service interface 없이 class 로 바로 생성

- Lombok
  - AllArgsConstructor, Setter 사용 금지
  - 기본 생성자 - protected 선언 
</details>

## 🗂️ APIs
작성한 API는 아래에서 확인할 수 있습니다.

👉🏻 [API 바로보기](https://www.notion.so/teamsparta/API-7d191d644a674fbe971141dd2e02c782)

## 🔧구현 기능
<details>
<summary>구현 기능 펼치기</summary>

### 🧑‍🧑‍🧒 user

- 회원가입 & 회원 탈퇴
- 로그인 & 로그아웃
- 리프레시 토큰 발급
- 프로필 수정 & 조회
- 비밀번호 확인
- 관리자 유저 수정
- 관리자 유저 전체 조회

### 🪜 카테고리/서브 카테고리

- 카테고리 생성 & 수정 & 삭제
- 서브 카테고리 생성 & 수정 & 삭제
- 카테고리, 서브 카테고리 전체 조회

### 🎬 배너 

- 배너 생성 & 수정
- 배너 전체 조회 & 단건 조회

### ✅ 포인트

- 포인트 생성 & 수정
- 포인트 내역 조회

### 📇 쿠폰

- 쿠폰 생성 & 수정
- 쿠폰 발급
- 유저 쿠폰 확인

### 💬 신고

- 관리자
  - 신고 내역 조회
  - 신고 삭제
- 사용자
  - 신고 하기
  - 신고 취소
  - 본인 신고 내역 확인
  - 해당 유저 신고 당한 내역 확인

### 💬 채팅

- 채팅 보내기 & 받기
- 채팅방 목록 확인
- 채팅방 입장

### 🗓️ 일정

- 일정 등록 & 수정 & 삭제
- 일정 전체 조회 & 단일 조회
- 월별 일정 조회

### 💸 결제

- 결제 등록
- 결제 취소
- 결제 내역

### 👩‍❤️‍👨매칭

- 일정 매칭 생성
- 매칭 신청
- 매칭 수정
- 매칭 전체 조회
- 단일 일정 매칭 조회
</details>


<br />

