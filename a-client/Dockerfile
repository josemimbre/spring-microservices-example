FROM java:8
ADD ./target/a-client-0.0.1-SNAPSHOT.jar /code/app.jar
WORKDIR /code
CMD ["/code/app.jar","--spring.profiles.active=${PROFILE}"]