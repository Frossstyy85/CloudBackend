FROM maven:3-amazoncorretto-21 as corretto-jdk

COPY ./ ./

RUN mvn -Dmaven.test.skip=true clean package

FROM amazoncorretto:21

COPY --from=corretto-jdk ./target/*.jar /app.jar
EXPOSE 8080

CMD ["java", "-jar", "/app.jar"]