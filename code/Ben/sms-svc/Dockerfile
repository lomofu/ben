FROM java:8-jdk-alpine

ENV  APP /sms-svc

COPY target/sms.jar /usr/app/app.jar

WORKDIR /usr/app

ENTRYPOINT exec java $JAVA_OPTS -jar app.jar