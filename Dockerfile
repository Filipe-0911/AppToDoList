# Imagem base com Maven + JDK 17
FROM maven:3.9.2-eclipse-temurin-17

# Diretório de trabalho dentro do container
WORKDIR /app

# Copia todos os arquivos do projeto para dentro do container
COPY . .

# Instala dependências e gera o JAR
RUN mvn clean package -DskipTests

# Expõe a porta do Spring Boot
EXPOSE 8080

# Comando para rodar a aplicação
CMD ["java", "-jar", "target/App-0.0.1-SNAPSHOT.jar"]
