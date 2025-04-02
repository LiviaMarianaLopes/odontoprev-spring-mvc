FROM openjdk:21-jdk-slim

WORKDIR /usr/app

COPY . .

RUN chmod +x gradlew
RUN ./gradlew build -x test

RUN useradd -ms /bin/bash appuser
USER appuser

EXPOSE 8080
CMD ["java", "-jar", "build/libs/odontoprev-java-mvc-0.0.1-SNAPSHOT.jar"]