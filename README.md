# 🎄 크리스마스 커플 웹사이트

여자친구를 위한 크리스마스 기념 웹사이트입니다.

## 기능

- 💕 커플 정보 관리
- 💝 추억 기록
- 💌 크리스마스 메시지
- 📸 사진 갤러리

## 기술 스택

- Java 17
- Spring Boot 4.0.1
- MySQL
- Thymeleaf
- JavaScript

## 설정 방법

1. MySQL 데이터베이스 생성
 
   CREATE DATABASE christmas_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   2. 설정 파일 복사
   
   cp src/main/resources/application.yaml.example src/main/resources/application.yaml
   cp src/main/resources/application-local.yaml.example src/main/resources/application-local.yaml
   3. `application.yaml`에서 DB 비밀번호 설정
   - `password: your_password` 부분을 실제 MySQL 비밀번호로 변경

4. 실행
   
   ./gradlew bootRun
   ## 배포

ngrok으로 간단히 로컬에서 돌렸습니다 ㅎㅎ
