
FROM maven:3.8.8-amazoncorretto-21-al2023 AS build

ARG SERVICE

WORKDIR /build

COPY . .

RUN mvn -pl "$SERVICE" -am clean package -DskipTests

FROM amazoncorretto:21.0.5

ARG SERVICE
ARG JAR_NAME

WORKDIR /app

COPY --from=build /build/$SERVICE/target/*.jar app.jar

ENV MYSQL_URL=""
ENV MYSQL_PASS=""
ENV GOOGLE_CLIENT=""
ENV GOOGLE_SECRET=""
ENV CLIENT_ID=""
ENV CLIENT_SECRET=""
ENV RABBIT_USER=""
ENV RABBIT_PASSWORD=""
ENV TOKEN_URI=""
ENV EMAIL=""
ENV EMAIL_PASSWORD=""
ENV ACCESS_KEY=""
ENV SECRET_KEY=""

ENTRYPOINT ["java", "-jar", "app.jar"]