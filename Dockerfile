# Use OpenJDK as base image
FROM openjdk:17-jdk-slim as builder

# Set working directory
WORKDIR /app

# Copy the application JAR file into the container
COPY build/libs/demo-0.0.1-SNAPSHOT.jar /app/demo.jar

# Expose the application port (adjust if necessary)
EXPOSE 8081

# Set entry point to run the application
ENTRYPOINT ["java", "-jar", "/app/demo.jar"]
