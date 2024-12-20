FROM openjdk:17

WORKDIR /usr/src/app

COPY build/libs/bitcoin-insights-0.0.1-SNAPSHOT.jar .
RUN chmod 644 bitcoin-insights-0.0.1-SNAPSHOT.jar

EXPOSE 8000

CMD ["java", "-jar", "-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE}", "-Dvaadin.productionMode=true", "bitcoin-insights-0.0.1-SNAPSHOT.jar"]
