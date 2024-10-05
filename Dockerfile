FROM alpine:latest
RUN apk add --update bash openjdk17 maven
COPY . /spring-app
WORKDIR /spring-app
RUN mvn package
EXPOSE 8080
CMD java -jar target/demo-0.0.1-SNAPSHOT.jar
