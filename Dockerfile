# Build stage
FROM eclipse-temurin:17-jdk-jammy AS build
ENV HOME=/usr/app/login-service
RUN mkdir -p $HOME
WORKDIR $HOME

# Install Maven and update certificates
RUN apt-get update && \
    apt-get install -y maven ca-certificates && \
    update-ca-certificates

# Copy pom.xml and source code
COPY pom.xml $HOME
COPY src $HOME/src

# Package the application
RUN mvn -f $HOME/pom.xml clean package -DskipTests -Dmaven.wagon.http.ssl.insecure=true -Dmaven.wagon.http.ssl.allowall=true

# Package stage
FROM eclipse-temurin:17-jre-jammy
COPY --from=build /usr/app/login-service/target/*.jar /app/login-service.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/login-service.jar"]