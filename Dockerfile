FROM openjdk:11.0.4-jre

RUN groupadd --gid 1000 user   && useradd --uid 1000 --gid user --shell /bin/bash user

ENV SERVER_PORT="8080"
ENV SPRING_PROFILES_ACTIVE="cloud"
ENV APPLICATION_VERSION="1.0.0-SNAPSHOT"
ENV NEW_RELIC_APP_NAME=""
ENV NEW_RELIC_LICENSE_KEY ""
ENV NEW_RELIC_ACCOUNT_ID=""
ENV NEW_RELIC_DISTRIBUTED_TRACING=true

ENV SECURITY_OPTS="-Dnetworkaddress.cache.negative.ttl=0 -Dnetworkaddress.cache.ttl=0"
ENV SERVER_IP=""
ENV CLIENT_HEADERS=""
ENV ENVIRONMENT=""
ENV JAVA_TOOL_OPTIONS="-Dhttps.protocols=TLSv1.2"
ENV MAX_RAM_PERCENTAGE="-XX:MaxRAMPercentage=70"
ENV MIN_RAM_PERCENTAGE="-XX:MinRAMPercentage=70"
ENV JIGSAW_ARG="--add-opens=java.base/java.net=ALL-UNNAMED"
ENV NEW_RELIC_ARG="-javaagent:newrelic/newrelic.jar -Dnewrelic.config.distributed_tracing.enabled=$NEW_RELIC_DISTRIBUTED_TRACING"
ENV JAVA_OPTS="$JAVA_OPTS $JIGSAW_ARG $NEW_RELIC_ARG $SECURITY_OPTS $MAX_RAM_PERCENTAGE $MIN_RAM_PERCENTAGE $JAVA_TOOL_OPTIONS"


EXPOSE $SERVER_PORT

COPY /delivery/src/main/resources/newrelic/newrelic.yml app/newrelic/
COPY /build/libs/newrelic.jar app/newrelic/newrelic.jar
COPY /delivery/build/libs/delivery-$APPLICATION_VERSION.jar app/api.jar

WORKDIR /app

ENTRYPOINT exec java $JAVA_OPTS -jar api.jar