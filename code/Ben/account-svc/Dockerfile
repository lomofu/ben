FROM java:8-jdk-alpine

ENV  APP /account-svc

COPY target/account.jar /usr/app/app.jar

WORKDIR /usr/app

ENTRYPOINT exec java $JAVA_OPTS -jar app.jar