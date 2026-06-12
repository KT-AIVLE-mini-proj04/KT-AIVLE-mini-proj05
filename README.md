# PicStory Backend

`PicStory`는 사용자가 도서를 등록하고 관리하며, 댓글과 좋아요, 에피소드 단위 콘텐츠까지 다룰 수 있도록 만든 창작 플랫폼 백엔드 프로젝트입니다. 현재 저장소는 `Spring Boot` 기반 API 서버 구현에 집중되어 있으며, JWT 인증과 PostgreSQL 연동을 포함합니다.

첨부해주신 기존 README 초안을 바탕으로, 현재 브랜치의 실제 폴더 구조와 코드 기준 구현 범위를 반영해 문서를 정리했습니다.

## 프로젝트 개요

- 도서 등록, 조회, 수정, 삭제 기능 제공
- 회원가입 및 JWT 기반 로그인/로그아웃 지원
- 도서별 댓글 작성 및 수정/삭제 지원
- 도서 좋아요 상태 조회 및 토글 지원
- 도서별 에피소드 CRUD 및 TTS 경로 저장 지원
- 도서 표지 URL 저장 기능 제공

## 현재 구현 기능

| 구분 | 설명 | 구현 상태 |
| --- | --- | --- |
| 사용자 | 회원가입 | 구현 |
| 인증 | JWT 로그인, 인증 필터, 로그아웃, 리프레시 토큰 쿠키 처리 | 구현 |
| 도서 | 도서 CRUD, 제목 키워드 검색, 도서 수 집계, 표지 URL 저장 | 구현 |
| 댓글 | 댓글 작성, 수정, 삭제, 도서별 조회 | 구현 |
| 좋아요 | 좋아요 상태 조회, 좋아요 토글 | 구현 |
| 에피소드 | 에피소드 CRUD, 도서별 에피소드 조회, TTS 경로 저장 | 구현 |

## 기술 스택

### Backend

| 기술 | 설명 |
| --- | --- |
| Java 17 | 프로젝트 런타임 |
| Spring Boot 4.0.6 | 백엔드 애플리케이션 프레임워크 |
| Spring Web | REST API 구현 |
| Spring Security | 인증/인가 및 필터 체인 구성 |
| Spring Data JPA | ORM 및 데이터 접근 |
| Spring Validation | 요청 데이터 검증 |
| PostgreSQL | 관계형 데이터베이스 |
| JJWT 0.12.6 | JWT 발급/검증 |
| Lombok | 반복 코드 축소 |


## 의존성

`build.gradle` 기준 주요 의존성은 아래와 같습니다.

```gradle
implementation 'org.springframework.boot:spring-boot-starter'
implementation 'org.springframework.boot:spring-boot-starter-web'
implementation 'org.springframework.boot:spring-boot-starter-security'
implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
implementation 'org.springframework.boot:spring-boot-starter-validation'
implementation 'io.jsonwebtoken:jjwt-api:0.12.6'
runtimeOnly 'io.jsonwebtoken:jjwt-impl:0.12.6'
runtimeOnly 'io.jsonwebtoken:jjwt-jackson:0.12.6'
implementation 'org.postgresql:postgresql'
runtimeOnly 'org.postgresql:postgresql'
compileOnly 'org.projectlombok:lombok'
annotationProcessor 'org.projectlombok:lombok'
testImplementation 'org.springframework.boot:spring-boot-starter-test'
testImplementation 'org.springframework.security:spring-security-test'
```

## 프로젝트 구조

현재 저장소의 주요 구조는 아래와 같습니다.

```text
mini_project_05
├── build.gradle
├── settings.gradle
├── gradlew
├── gradlew.bat
├── gradle/
│   └── wrapper/
├── src/
│   ├── main/
│   │   ├── java/com/aivle/bookapp/
│   │   │   ├── config
│   │   │   ├── controller
│   │   │   ├── domain
│   │   │   ├── dto
│   │   │   ├── exception
│   │   │   ├── global/
│   │   │   │   ├── config
│   │   │   │   └── util
│   │   │   ├── repository
│   │   │   └── service
│   │   └── resources/
│   │       └── application.yaml
│   └── test/
│       └── java/com/aivle/bookapp/
```

## 주요 도메인

### Book

- `bookId`
- `usersId`
- `title`
- `author`
- `description`
- `cover`
- `createdAt`
- `updatedAt`

### Users

- `usersId`
- `loginId`
- `password`
- `name`
- `gubun`
- `email`
- `address`
- `phoneNumber`
- `createdAt`
- `updatedAt`

### Comment

- `commentsId`
- `bookId`
- `usersId`
- `content`
- `createdAt`
- `updatedAt`

### Episode

- `episodeId`
- `bookId`
- `usersId`
- `episodeTitle`
- `view`
- `episodeIndex`
- `content`
- `ttsPath`
- `createdAt`
- `updatedAt`

### BookLike

- `id`
- `user`
- `book`
- `createdAt`
- `updatedAt`

## 보안 및 인증

- `Spring Security` 기반 무상태 인증 구조 사용
- `POST /auth/login`은 컨트롤러가 아니라 `JwtLoginFilter`에서 처리
- 로그인 성공 시 액세스 토큰은 응답 바디로 반환
- 리프레시 토큰은 `HttpOnly` 쿠키로 저장
- `JwtAuthenticationFilter`, `JwtRefreshFilter`를 통해 인증 및 토큰 재발급 흐름 처리
- `POST /auth/logout`으로 저장된 리프레시 토큰 제거
- CORS 허용 origin은 현재 `http://localhost:5173`으로 설정

## API 요약

### 사용자 및 인증

| Method | Endpoint | 설명 |
| --- | --- | --- |
| `POST` | `/users` | 회원가입 |
| `POST` | `/auth/login` | 로그인 |
| `POST` | `/auth/logout` | 로그아웃 |

로그인 요청 예시:

```json
{
  "loginId": "user01",
  "password": "password123"
}
```

### 도서

| Method | Endpoint | 설명 |
| --- | --- | --- |
| `POST` | `/books` | 도서 등록 |
| `GET` | `/books` | 도서 목록 조회 |
| `GET` | `/books?keyword={keyword}` | 제목 검색 |
| `GET` | `/books/count` | 도서 수 집계 조회 |
| `GET` | `/books/{id}` | 도서 상세 조회 |
| `PATCH` | `/books/{id}` | 도서 수정 |
| `DELETE` | `/books/{id}` | 도서 삭제 |
| `PATCH` | `/books/{id}/cover` | 도서 표지 URL 저장 |

### 댓글

| Method | Endpoint | 설명 |
| --- | --- | --- |
| `POST` | `/comment` | 댓글 작성 |
| `PATCH` | `/comment/{id}` | 댓글 수정 |
| `DELETE` | `/comment/{id}` | 댓글 삭제 |
| `GET` | `/comment?bookId={bookId}` | 도서별 댓글 조회 |
| `GET` | `/comment?bookId={bookId}&page={page}` | 도서별 댓글 페이지 조회 |

댓글 API는 `Authorization: Bearer {accessToken}` 헤더를 사용합니다.

### 좋아요

| Method | Endpoint | 설명 |
| --- | --- | --- |
| `GET` | `/books/{bookId}/likes` | 좋아요 상태 및 개수 조회 |
| `POST` | `/books/{bookId}/likes` | 좋아요 토글 |

### 에피소드

| Method | Endpoint | 설명 |
| --- | --- | --- |
| `GET` | `/episodes/{id}` | 에피소드 단건 조회 |
| `GET` | `/episodes?bookId={bookId}` | 도서별 에피소드 목록 조회 |
| `POST` | `/episodes` | 에피소드 생성 |
| `PATCH` | `/episodes/{id}` | 에피소드 수정 |
| `DELETE` | `/episodes/{id}` | 에피소드 삭제 |
| `POST` | `/episodes/{id}/tts` | 에피소드 TTS 경로 저장 |

## 실행 방법

### 1. 사전 준비

- Java 17
- PostgreSQL
- Gradle Wrapper

### 2. 설정

현재 `src/main/resources/application.yaml`에는 기본 구조가 이미 정의되어 있으며, 실행 전 빈 값으로 남아 있는 DB/JWT 민감 정보만 채우면 됩니다.

현재 파일 구조를 기준으로, 아래처럼 예시 값을 채워 사용할 수 있습니다.

```yaml
spring:
  application:
    name: bookapp

  datasource:
    url: jdbc:postgresql://localhost:5432/bookapp
    username: postgres
    password: postgres1234
    driver-class-name: org.postgresql.Driver
    
  jpa:
    hibernate:
      ddl-auto: validate
    properties:
      hibernate:
        format_sql: true
    show-sql: false

jwt:
  secret: 0123456789abcdef0123456789abcdef0123456789abcdef0123456789abcdef
  access-token-expiration-ms: 3600000
  refresh-token-expiration-ms: 2073600000
```

실행 전에는 예시 값을 실제 환경값으로 바꿔야 합니다.

- `spring.datasource.url`
- `spring.datasource.username`
- `spring.datasource.password`
- `spring.datasource.driver-class-name`
- `jwt.secret`

참고:

- `spring.jpa.hibernate.ddl-auto: validate`로 설정되어 있어, 실행 시점에 DB 스키마가 엔티티와 일치해야 합니다.
- Hikari 커넥션 풀은 `maximum-pool-size: 1`로 제한되어 있습니다.

### 3. 애플리케이션 실행

```bash
./gradlew bootRun
```
