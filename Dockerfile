# Giai đoạn build
FROM maven:3.8.4-openjdk-17-slim AS build
WORKDIR /app

COPY pom.xml .
COPY src ./src

# Chạy maven clean package
RUN mvn clean package -DskipTests

# Giai đoạn chạy
FROM openjdk:17-jdk-slim-buster

LABEL authors="pdt" \
      version="1.0" \
      description="Spring Boot Application Docker Image"

WORKDIR /app

# Copy file JAR từ giai đoạn build
COPY --from=build /app/target/project-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

# Add healthcheck
HEALTHCHECK --interval=30s --timeout=3s \
    CMD curl -f http://localhost:8080/actuator/health || exit 1

# Chạy ứng dụng
CMD ["java", "-jar", "app.jar"]