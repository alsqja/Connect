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
  - 선택 이유 :  
- **JDK** : openjdk version '17.0.2'
- **Framework** : springframework.boot version '3.4.1', Spring Data JPA

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

- **Tool** : Figma, ERD Cloud, Slack, Github & git, Postman, Docker
- **Build Tool** : Gradle
</div>

<br />

## 프로젝트 구조

```plaintext
📦 trello-project
├── 📂 src
│   ├── 📂 main
│   │   ├── 📂 java
│   │   │   ├── 📂 board
│   │   │   ├── 📂 card
│   │   │   ├── 📂 comment
│   │   │   ├── 📂 global    # 공통 코드 (ex. config, filter)
│   │   │   ├── 📂 list
│   │   │   ├── 📂 member
│   │   │   ├── 📂 notice
│   │   │   ├── 📂 user
│   │   │   └── 📂 workspace
│   └── 📂 test
├── 📄 .gitignore
├── 📄 Dockerfile
└── 📄 README.md
```

## 🛠️ 프로젝트 아키텍쳐
<details>
<summary>와이어 프레임</summary>

<img src="https://github.com/user-attachments/assets/6d3bd3ce-87de-4de2-8999-7281e7dc2921">

👉🏻 [와이어 프레임 바로보기](https://www.figma.com/design/yojLnL4papWqljeFPi95jT/Untitled?node-id=0-1&t=RZNg2zPztvl5EarL-1)
</details>

<details>
<summary>ERD</summary>
<img src="https://github.com/user-attachments/assets/b21cafae-b9f2-40e3-ba01-786651657840">

👉🏻 [ERD 바로보기](https://www.erdcloud.com/d/z8s3jowhc7E8ALxH7)

</details>

<br />

## 🪧 커밋 컨벤션
| 작업 타입| 작업 내용|
|------|-|
|✨ update|해당 파일에 새로운 기능이 생김|
|🎉 feat|없던 파일을 생성함, 초기 세팅|
|🐛 bugfix|버그 수정|
|♻️ refactor|코드 리팩토링|
|🩹 fix|코드 수정|
|🚚 move|파일 옮김/정리|
|🔥 del|기능/파일을 삭제|
|🍻 test|테스트 코드를 작성|
|🙈 gitfix|gitignore 수정|
|🔨script|package.json 변경(npm 설치 등)|
|📝 docs|문서 추가

## 👔 코드 컨벤션
<details>
<summary>코드 컨벤션 펼치기</summary>

- 주석
  - java doc 사용

- 클래스 명
  - PascalCase 사용 (ex : UserAccount)

- 변수 명
  - camelCase 사용 (ex : firstName)

- 패키지 구조 : 도메인 형
  - 도메인 밑에 패키지 없이 작성
  - Dto만 분리

- 생성자 → 생성자 패턴

- lombok Setter 사용 금지

- service interface 없이 class 로 바로 생성
</details>

## 🗂️ APIs
작성한 API는 아래에서 확인할 수 있습니다.

👉🏻 [API 바로보기](https://teamsparta.notion.site/2-1582dc3ef51481ee80ead2738eea31f3)

## 🔧구현 기능
<details>
<summary>구현 기능 펼치기</summary>

### 🧑‍🧑‍🧒 user

- 회원가입 & 회원 탈퇴
- 로그인 & 로그아웃

### 🪜 워크 스페이스

- 워크 스페이스 생성 & 수정 & 삭제
- 워크 스페이스 전체 조회 & 단건 조회
- 워크 스페이스 멤버 초대 & 초대 수락
- 워크 스페이스 멤버 목록 조회
- 워크 스페이스 유저 권한 수정

### 🎬 보드 

- 보드 생성 & 수정 & 삭제
- 보드 단건 조회

### ✅ 리스트

- 리스트 생성 & 수정 & 삭제
- 리스트 순서 변경

### 📇 카드

- 카드 생성 & 수정 & 삭제
- 카드 단건 조회
- 카드 다건 조회 (검색)

### 💬 댓글

- 댓글 생성 & 수정 & 삭제
- 댓글 전체 조회

### ⏰ 알림

- 특정 동작 수행시 슬랙으로 알림 발송
</details>


<br />

