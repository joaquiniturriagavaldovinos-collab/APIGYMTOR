# Etapa 1: Build
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copiar solo pom.xml primero (optimización de cache)
COPY Api/pom.xml .
RUN mvn dependency:go-offline -B

# Copiar código fuente
COPY Api/src ./src

# Compilar (saltar tests para build más rápido)
RUN mvn clean package -DskipTests

# Etapa 2: Runtime
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copiar JAR desde etapa de build
COPY --from=build /app/target/*.jar app.jar

# Puerto que usa Render
EXPOSE 8080

# Variables de entorno por defecto
ENV SPRING_PROFILES_ACTIVE=prod

# Ejecutar aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]