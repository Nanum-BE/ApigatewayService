FROM openjdk:17-ea-11-jdk-slim
VOLUME /tmp
#123
COPY apigateway-service-0.0.1-SNAPSHOT.jar ApigatewayService.jar
ENTRYPOINT ["java", "-jar","ApigatewayService.jar"]
