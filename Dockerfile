## READD THIS > https://github.com/fabric8io/docker-maven-plugin
## READD THIS > https://phauer.com/2015/building-dropwizard-microservice-docker-maven/
## https://medium.com/@fernandoevangelista_28291/criando-e-enviando-imagem-docker-com-java-e-maven-4fa3c70dba0f
FROM maven:3.9.6-eclipse-temurin-21 as builder

ENV MAVEN_OPTS="-XX:+TieredCompilation -XX:TieredStopAtLevel=1"

COPY src /usr/src/app/src
COPY pom.xml /usr/src/app
COPY config.yml /usr/src/app

RUN mvn -f /usr/src/app/pom.xml clean package

#FROM gcr.io/distroless/java17-debian12
FROM eclipse-temurin:21-jre
LABEL repository=promo-sniper
LABEL tag=latest
LABEL author="Anor Neto"

COPY --from=builder /usr/src/app/target/promo-sniper-1.0-SNAPSHOT.jar /usr/app/promo-sniper-1.0-SNAPSHOT.jar
COPY --from=builder /usr/src/app/config.yml /usr/app/config.yml
WORKDIR /usr/app


ENTRYPOINT ["java", "-jar", "/usr/app/promo-sniper-1.0-SNAPSHOT.jar", "server", "/usr/app/config.yml"]
EXPOSE 8080
