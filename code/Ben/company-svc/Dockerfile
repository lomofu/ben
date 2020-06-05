FROM java:8-jdk-alpine

ENV  APP /company-svc

COPY target/company.jar /usr/app/app.jar

WORKDIR /usr/app

ENTRYPOINT exec java $JAVA_OPTS -jar app.jar