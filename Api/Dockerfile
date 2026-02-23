# -------- Etapa 1: Build --------
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copiar pom primero (mejor cache)
COPY Api/pom.xml .
RUN mvn dependency:go-offline -B

# Copiar código
COPY Api/src ./src

# Compilar
RUN mvn clean package -DskipTests

# -------- Etapa 2: Runtime --------
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENV SPRING_PROFILES_ACTIVE=prod

ENTRYPOINT ["java", "-jar", "app.jar"]