# Stage 1: Build the application using Gradle
FROM gradle:8.7-jdk21 AS builder

WORKDIR /app

# Copy Gradle build files first (to leverage Docker cache)
COPY build.gradle settings.gradle ./

# Pre-download dependencies (simulate go-offline)
RUN gradle build --no-daemon --dry-run || true

# Copy the entire project
COPY src ./src

# Build the application
RUN gradle clean build --no-daemon

# Stage 2: Run the application with a lightweight JDK image
FROM openjdk:21-jdk AS runner

WORKDIR /app

# Copy the built JAR (assumes default output dir)
COPY --from=builder /app/build/libs/*.jar ./app.jar



EXPOSE 4005

ENTRYPOINT ["java", "-jar", "app.jar"]
