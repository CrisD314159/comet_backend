FROM amazoncorretto:17-alpine-jdk
LABEL authors="cristiandavidvargasloaiza"
COPY target/comet-0.0.1-SNAPSHOT.jar comet-1.0.0.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/comet-1.0.0.jar"]