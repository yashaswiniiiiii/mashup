# Use a base image with Java and Gradle
FROM openjdk:17-jdk-slim as build

# Set working directory inside the container
WORKDIR /app

# Copy the Gradle files
COPY build.gradle.kts settings.gradle.kts gradle/ ./
COPY gradle-wrapper/ gradle-wrapper/

# Copy the source code into the container
COPY src/ /app/src/

# Install Gradle and build the application
RUN ./gradlew build --no-daemon

# Create the final image
FROM openjdk:17-jdk-slim

# Set the working directory inside the final image
WORKDIR /app

# Copy the built JAR from the build stage
COPY --from=build /app/build/libs/*.jar app.jar

# Expose the port that your Spring Boot application will run on
EXPOSE 8080

# Define the entrypoint for the container
ENTRYPOINT ["java", "-jar", "app.jar"]
