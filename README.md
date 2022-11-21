# microservice-java-camel-quickstart

This is a starter template for new microservices. It offers a fully functional microservice using Java using Apache Camel with automated tests, Dockerfile and a simple Helm chart for Kubernetes configurations.

## Build & Run
You can build and run the application using maven on the command line or use these details to configure a run configuration in IntelliJ.
```shell
mvn clean compile exec:java
```

## Build & Package
This build produces a fat jar in the target folder.

```shell
mvn clean compile package
docker build -t starter:latest .
```

## Property Encryption
This application has the ability to decrypt properties that have been encrypted using Jasypt and tagged with ENC(...) in the application.properties file.

At runtime it will look for an environment variable called CRYPTO_KEY for the passcode. If the variable is not defined it will happily continue to run but will be unable to decrypt anything.

To enable this functionality, first generate a strong password and update your deployment.yaml in your helm chart or use docker to inject the password into your container.

```shell
export CRYPTO_KEY=K4RYpFYAn3DmXftAUn6vxFGtH33U9xzZ
```

You can use the Jasypt CLI to encrypt strings using your strong key:<br/>
http://www.jasypt.org/cli.html

For reference, the above example will look something like this in your properties file, and once it is read it will appear in memory as the string 'blah'.

```properties
some.secret.property = ENC(AgTglrHkQ5AUZK92fASdEg==)
```

## Test Locally

```shell
docker build --no-cache --squash -t microservice:latest .
docker run --rm -it microservice:latest /bin/bash

docker run --rm -it -p 8080:8080 microservice:latest
curl http://localhost:8080/api/health
curl http://localhost:8080/api/samples
curl http://localhost:8080/api/sample/123
. . .
```
