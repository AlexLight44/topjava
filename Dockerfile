FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /src
COPY pom.xml .
COPY src ./src
RUN mvn -B -DskipTests package

FROM tomcat:9.0-jdk21-temurin

ENV TOPJAVA_ROOT=/opt/topjava \
    CATALINA_OPTS="-Dspring.profiles.active=vds,datajpa -Dfile.encoding=UTF-8"

RUN rm -rf /usr/local/tomcat/webapps/* \
    && mkdir -p /opt/topjava/config/messages /opt/topjava/log

COPY config/messages/ /opt/topjava/config/messages/
COPY docker/db.properties /opt/topjava/config/db.properties
COPY --from=build /src/target/topjava.war /usr/local/tomcat/webapps/topjava.war

EXPOSE 8080
