FROM eclipse-temurin:17-jre-alpine
COPY /target/noteapp.jar /noteapp.jar
ENTRYPOINT ["java","-jar","/noteapp.jar"]
