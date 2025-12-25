# Fase de build
FROM maven:3.9.2-eclipse-temurin-17 AS build
WORKDIR /app

# Copia todos os arquivos do projeto
COPY . .

# Build do JAR sem testes
RUN mvn clean package -DskipTests

# Fase de runtime
FROM eclipse-temurin:17-jre
WORKDIR /app

# Copia o JAR buildado
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

# Inicia a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]