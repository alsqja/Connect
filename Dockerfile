# Build 스테이지
FROM gradle:8.11.1-jdk17 AS builder

# 작업 디렉토리 설정
WORKDIR /apps

# 빌더 이미지에서 애플리케이션 빌드
COPY . /apps
RUN gradle clean build -x test --no-daemon --parallel


# 실행 스테이지
# OpenJDK 17 slim 기반 이미지 사용
FROM openjdk:17-jdk-slim

# 한국 시간대를 설정하기 위해 tzdata 설치
RUN apt-get update && apt-get install -y tzdata \
    && ln -sf /usr/share/zoneinfo/Asia/Seoul /etc/localtime \
    && dpkg-reconfigure -f noninteractive tzdata \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/*


# 이미지에 레이블 추가
LABEL type="application"

# 작업 디렉토리 설정
WORKDIR /apps

# 애플리케이션 jar 파일을 컨테이너로 복사
COPY --from=builder /apps/build/libs/*-SNAPSHOT.jar /apps/app.jar

# 애플리케이션이 사용할 포트 노출
EXPOSE 8080

# 애플리케이션을 실행하기 위한 엔트리포인트 정의
ENTRYPOINT ["java", "-jar", "/apps/app.jar", "--spring.profiles.active=prod"]