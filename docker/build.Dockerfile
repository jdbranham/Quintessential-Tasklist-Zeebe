# builder image
FROM adoptopenjdk/openjdk11-openj9:jdk-11.0.1.13-alpine-slim as builder

COPY . /app/src
WORKDIR /app/src

RUN ./gradlew build -x test --info --stacktrace

# runtime image
FROM adoptopenjdk/openjdk11-openj9:jdk-11.0.1.13-alpine-slim
COPY --from=builder /app/src/build/libs/quintessential-tasklist-zeebe-*-all.jar quintessential-tasklist-zeebe.jar
EXPOSE 8080
CMD java -Dcom.sun.management.jmxremote -noverify ${JAVA_OPTS} -jar quintessential-tasklist-zeebe.jar