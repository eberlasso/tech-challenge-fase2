FROM maven:3.9.11-eclipse-temurin-25 AS build
WORKDIR /app

COPY pom.xml ./

# Baixa dependências em camada separada (cache)
RUN mvn -B dependency:go-offline

COPY src ./src

# Compila, executa testes unitários + integração e verifica cobertura JaCoCo
RUN mvn -B clean verify

# Exibe relatório de cobertura no log do build
RUN echo "===== RELATÓRIO DE COBERTURA JACOCO ====" && \
    cat /app/target/site/jacoco/index.html | grep -o 'Total[^%]*%' | head -5 || \
    echo "Relatório HTML gerado em: /app/target/site/jacoco/index.html"

FROM eclipse-temurin:25-jre
WORKDIR /app

COPY --from=build /app/target/restaurant-management-1.0.0-SNAPSHOT.jar /app/app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
