FROM openjdk:11

LABEL name=psaarine

WORKDIR /app

RUN apt-get update

RUN apt-get install maven -y

COPY . .

RUN mvn test