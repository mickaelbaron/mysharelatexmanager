# Build env
FROM maven:3-jdk-11 AS build-java-stage
LABEL maintainer="Mickael BARON (baron.mickael@gmail.com)"

WORKDIR /mysharelatexmanager
COPY mysharelatexmanager-api mysharelatexmanager-api
COPY mysharelatexmanager-server mysharelatexmanager-server
COPY pom.xml .
RUN mvn -f pom.xml clean package -Dmaven.test.skip=true

# Run env
FROM openjdk:11-jre-slim
LABEL maintainer="Mickael BARON (baron.mickael@gmail.com)"
COPY --from=build-java-stage /mysharelatexmanager/mysharelatexmanager-server/target/classes /mysharelatexmanager/classes
COPY --from=build-java-stage /mysharelatexmanager/mysharelatexmanager-server/target/dependency/*.jar /mysharelatexmanager/dependency/
EXPOSE 9991
ENTRYPOINT ["java", "-cp", "/mysharelatexmanager/classes:/mysharelatexmanager/dependency/*", "com.kumuluz.ee.EeApplication"]
