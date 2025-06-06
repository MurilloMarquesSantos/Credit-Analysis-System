FROM maven:3.8.8-amazoncorretto-21-al2023 AS build

WORKDIR /build

COPY . .

RUN mvn clean package -DskipTests

FROM amazoncorretto:21.0.5

WORKDIR /app

COPY --from=build ./build/target/*.jar ./notificationservice.jar

EXPOSE 8084

ENV RABBIT_USER=""
ENV RABBIT_PASSWORD=""
ENV EMAIL=""
ENV EMAIL_PASSWORD=""

ENTRYPOINT ["java", "-jar", "notificationservice.jar"]