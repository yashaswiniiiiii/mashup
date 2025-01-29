FROM openjdk:17-jdk-slim

WORKDIR /app

COPY build.gradle.kts settings.gradle.kts gradle/ ./
COPY gradle-wrapper/ gradle-wrapper/
COPY src/ /app/src/

RUN ./gradlew build --no-daemon

COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
