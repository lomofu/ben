FROM java:8-jdk-alpine

ENV  APP /bot-svc

COPY target/bot.jar /usr/app/app.jar

WORKDIR /usr/app

ENTRYPOINT exec java $JAVA_OPTS -jar app.jar