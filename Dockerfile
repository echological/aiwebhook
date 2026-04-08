FROM eclipse-temurin:25-jre
WORKDIR /app

COPY target/*-runner.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java","-Dquarkus.http.host=0.0.0.0","-jar","/app/app.jar"]