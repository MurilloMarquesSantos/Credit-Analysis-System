FROM maven:3.8.8-amazoncorretto-21-al2023 AS build

WORKDIR /build

COPY . .

RUN mvn clean package -DskipTests

FROM amazoncorretto:21.0.5

WORKDIR /app

COPY --from=build ./build/target/*.jar ./userservice.jar

EXPOSE 8080

ENV MYSQL_PASS=""
ENV GOOGLE_CLIENT=""
ENV GOOGLE_SECRET=""
ENV CLIENT_ID=""
ENV CLIENT_SECRET=""
ENV RABBIT_USER=""
ENV RABBIT_PASSWORD=""
ENV TOKEN_URI=""

ENTRYPOINT ["java", "-jar", "userservice.jar"]