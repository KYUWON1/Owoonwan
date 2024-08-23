FROM openjdk:17

# 빌드된 JAR 파일이 위치한 디렉토리와 .env 파일을 복사
COPY build/libs/*.jar /app/app.jar
COPY .env /app/.env

# 작업 디렉토리를 /app으로 설정
WORKDIR /app

# 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "/app/app.jar", "--spring.profiles.active=prod"]
