# jdk17 Image Start
FROM openjdk:17

CMD ["./gradlew", "clean", "build"]

VOLUME /tmp

# 인자설정 - JAR_File
ARG JAR_FILE=build/libs/*.jar

# jar 파일 복제
COPY ${JAR_FILE} app.jar

EXPOSE 8080

#실행 명령어
ENTRYPOINT ["java", "-jar", "app.jar"]