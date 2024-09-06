FROM openjdk:17-jdk-alpine

ENV SERVICE_NAME=expense-tracker \
    GROUP_NAME=expense-tracker \

WORKDIR /app

COPY target/*.jar /app/${SERVICE_NAME}.jar

USER $GROUP_NAME

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "${SERVICE_NAME}.jar"]