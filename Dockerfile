FROM openjdk:17

WORKDIR /usr/src/app

COPY build/libs/bitcoin.insights-0.0.1-SNAPSHOT.jar .
RUN chmod 644 bitcoin.insights-0.0.1-SNAPSHOT.jar

ENV API_TOKEN=$API_TOKEN

EXPOSE 8080

CMD ["java", "-jar", "bitcoin.insights-0.0.1-SNAPSHOT.jar"]
