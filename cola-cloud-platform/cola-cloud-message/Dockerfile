FROM java:8-jre
MAINTAINER Alexander Lukyanchikov <sqshq@sqshq.com>

ADD ./target/message-service.jar /app/
CMD ["java", "-Xmx200m", "-jar", "/app/message-service.jar"]

EXPOSE 8000