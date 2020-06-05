FROM java:8-jdk-alpine

ENV  APP /gateway

COPY target/gateway.jar /usr/app/app.jar

WORKDIR /usr/app

ENTRYPOINT exec java $JAVA_OPTS -jar app.jar