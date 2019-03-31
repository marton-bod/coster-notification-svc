FROM adoptopenjdk/openjdk11:alpine-slim
ADD /notification-service-*.jar /notification-service.jar
ARG JAVA_ARGS
ENTRYPOINT ["sh", "-c", "java $JAVA_ARGS -jar notification-service.jar"]
