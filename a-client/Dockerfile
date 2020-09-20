FROM gradle:6.6.1-jdk14 as builder
RUN gradle --version && java -version
WORKDIR /code
COPY build.gradle settings.gradle /code/
RUN gradle clean build --no-daemon > /dev/null 2>&1 || true
COPY ./ /code/
RUN gradle clean build --no-daemon

FROM openjdk:14-alpine
COPY --from=builder /code/build/libs/a-client-0.0.1-SNAPSHOT.jar /app/app.jar
WORKDIR /app
CMD ["java","-jar", "app.jar","--spring.profiles.active=${PROFILE}"]