# builder image
FROM adoptopenjdk/openjdk11-openj9:jdk-11.0.1.13-alpine-slim as builder

COPY . /app/src
WORKDIR /app/src

# when this bug is fixed, we can generate the docs. otherwise we have the static files included manually
# https://github.com/micronaut-projects/micronaut-openapi/issues/108
#RUN JAVA_TOOL_OPTIONS=-Dmicronaut.openapi.views.spec=redoc.enabled=true,rapidoc.enabled=true,swagger-ui.enabled=true,swagger-ui.theme=flattop \
#    ./gradlew --no-daemon build -x test --info --stacktrace

RUN ./gradlew --no-daemon build -x test --stacktrace

# runtime image
FROM adoptopenjdk/openjdk11-openj9:jdk-11.0.1.13-alpine-slim
COPY --from=builder /app/src/build/libs/quintessential-tasklist-zeebe-*-all.jar quintessential-tasklist-zeebe.jar
EXPOSE 8080
CMD java -Dcom.sun.management.jmxremote -noverify ${JAVA_OPTS} -jar quintessential-tasklist-zeebe.jar