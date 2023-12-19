FROM openjdk:17

WORKDIR /usr/src/app

COPY build/libs/bitcoin-insights-0.0.1-SNAPSHOT.jar .
RUN chmod 644 bitcoin-insights-0.0.1-SNAPSHOT.jar

ENV API_TOKEN=$API_TOKEN
ENV RPC_USER=$RPC_USER
ENV RPC_PASSWORD=$RPC_PASSWORD
ENV RPC_ADDRESS=$RPC_ADDRESS

EXPOSE 8000

CMD ["java", "-jar", "bitcoin-insights-0.0.1-SNAPSHOT.jar"]
