# Build aşaması
FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /app
COPY . .
RUN chmod +x mvnw
RUN ./mvnw clean package -DskipTests
 
# Run aşaması
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
 
# Gerekli araçları yükle
RUN apk add --no-cache busybox-extras curl bash
 
COPY --from=build /app/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
