FROM openjdk:11

LABEL name=psaarine

WORKDIR /app

RUN apt-get update

RUN apt-get install maven -y

COPY . .

EXPOSE 8080

RUN mvn test

RUN mvn clean install

ENTRYPOINT ["java", "-jar", "/app/target/wolt-backend-task.jar"]





