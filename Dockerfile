# ==========================================
# Stage 1: Build & Compilation Environment
# ==========================================
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# 1. Cache Maven dependencies by copying the pom first
COPY pom.xml .
RUN mvn dependency:go-offline -B

# 2. Copy the remaining source files and compile
COPY src ./src

# 3. Run production compile sequence with detailed logging
RUN mvn clean package -DskipTests --errors --batch-mode

# ==========================================
# Stage 2: Minimalist Lightweight Production Image
# ==========================================
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# Use wildcards to isolate the artifact regardless of naming shifts
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
