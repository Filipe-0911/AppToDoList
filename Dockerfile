# Usar uma imagem base mais leve com JDK 17 e configuração de fuso horário
FROM dvmarques/openjdk-17-jdk-alpine-with-timezone

# Definir o diretório de trabalho
WORKDIR /app

# Copiar o arquivo JAR para dentro do contêiner
COPY ./target/App-0.0.1-SNAPSHOT.jar /app/App-0.0.1-SNAPSHOT.jar

# Expor a porta 8080
EXPOSE 8080

# Definir o comando para iniciar a aplicação Java
CMD ["java", "-jar", "App-0.0.1-SNAPSHOT.jar"]
