# Usar a imagem base do OpenJDK
FROM openjdk:17-jdk-slim

# Definir o diretório de trabalho dentro do contêiner
WORKDIR /app

# Copiar o arquivo JAR gerado para o contêiner
COPY target/*.jar app.jar

# Comando para executar a aplicação
CMD ["java", "-jar", "app.jar"]
