FROM maven:3.8.8-amazoncorretto-21-al2023 AS build

WORKDIR /build

COPY . .

RUN mvn clean package -DskipTests

FROM amazoncorretto:21.0.5

WORKDIR /app

COPY --from=build ./build/target/*.jar ./creditservice.jar

EXPOSE 8082

ENV RABBIT_USER=""
ENV RABBIT_PASSWORD=""

ENTRYPOINT ["java", "-jar", "creditservice.jar"]