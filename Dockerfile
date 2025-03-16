FROM openjdk:17-slim
WORKDIR /app

RUN apt-get update && \
    apt-get install -y maven && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

COPY pom.xml .
COPY src ./src

RUN mvn package -DskipTests

RUN find target -name "*.jar" | xargs -I{} cp {} app.jar

RUN groupadd -r pokemon && useradd -r -g pokemon pokemon

RUN chown pokemon:pokemon app.jar

USER pokemon:pokemon

EXPOSE 5000
ENTRYPOINT ["java", "-jar", "app.jar"]