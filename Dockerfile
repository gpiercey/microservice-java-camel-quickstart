FROM openjdk:20-jdk-slim as builder

RUN apt-get update \
 && apt-get install -y --no-install-recommends maven

WORKDIR /build
COPY . .

RUN mvn clean compile package \
    && mkdir -p app \
    && cp target/micro*.jar app/microservice.jar \
    && cp src/main/resources/* app/ \
    && (cd app && tar cf - *) | gzip -9 > /microservice.tar.gz

FROM openjdk:20-jdk-slim

WORKDIR /app
COPY --from=builder /microservice.tar.gz .

RUN apt-get update \
 && apt-get upgrade -y \
 && apt-get install -y --no-install-recommends tzdata less \
 && ln -snf /usr/share/zoneinfo/Etc/UTC /etc/localtime && echo "Etc/UTC" > /etc/timezone \
 && apt-get autoclean -y \
 && apt-get autoremove -y \
 && apt-get clean -y \
 && rm -rf /var/lib/apt/lists/* \
 && tar xzf microservice.tar.gz \
 && rm -f microservice.tar.gz

EXPOSE 8080
CMD [ "java", "-jar", "/app/microservice.jar" ]