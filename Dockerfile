# Usa uma imagem leve do Java 21
FROM eclipse-temurin:21-jdk-alpine

# Define o diretorio de trabalho dentro do container
WORKDIR /app

# Copia o arquivo JAR gerado pelo Maven para dentro do container
# O asterisco garante que pegue o jar independente da versão (0.0.1, etc)
COPY target/*.jar app.jar

# Expoe a porta (apenas documentacao, o docker-compose que faz o mapeamento real)
EXPOSE 8080

# Comando para rodar a aplicacao
ENTRYPOINT ["java", "-jar", "app.jar"]