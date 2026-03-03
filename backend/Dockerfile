# ── Stage 1: Build ──────────────────────────────────────────────────────────
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Copy pom.xml first (caches dependency layer)
COPY pom.xml .
RUN mvn dependency:go-offline -q

# Copy source and build
COPY src ./src
RUN mvn clean package -DskipTests -q

# ── Stage 2: Run ─────────────────────────────────────────────────────────────
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

COPY --from=build /app/target/inventory-management-system-1.0.0.jar app.jar

# Render/Railway inject PORT automatically
EXPOSE 8080

ENTRYPOINT ["java", "-Xmx256m", "-Xms128m", "-jar", "app.jar"]
