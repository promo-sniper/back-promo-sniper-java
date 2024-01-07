FROM eclipse-temurin:21-jre
RUN mkdir /opt/app
COPY target/greeting-1.0-SNAPSHOT.jar /opt/app
COPY greeting.yml /opt/app
WORKDIR /opt/app
RUN java -version
CMD ["java", "-jar", "/opt/app/greeting-1.0-SNAPSHOT.jar", "server", "/opt/app/greeting.yml"]
EXPOSE 8090-8091

## READD THIS > https://github.com/fabric8io/docker-maven-plugin
## READD THIS > https://phauer.com/2015/building-dropwizard-microservice-docker-maven/