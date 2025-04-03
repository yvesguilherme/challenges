FROM gradle:jdk-21-and-22-alpine AS builder

COPY build.gradle settings.gradle ./
COPY src ./src

RUN gradle dependencies --no-daemon
RUN gradle build --no-daemon

FROM bellsoft/liberica-runtime-container:jdk-21_37-slim-musl

WORKDIR /app

# default folder for the jar file is /home/gradle/build/libs for the gradle image
# default folder for the jar file is /target for the maven image
COPY --from=builder /home/gradle/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]