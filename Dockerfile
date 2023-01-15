FROM eclipse-temurin:17-alpine
VOLUME /tmp
ADD target/route-finder-api-*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
EXPOSE 8082
