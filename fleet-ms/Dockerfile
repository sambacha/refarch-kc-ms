FROM websphere-liberty:19.0.0.3-microProfile2
LABEL maintainer="IBM Cloud Architecture Solution Engineering at IBM Cloud"
# This is the common image for websphere liberty to run only local

USER root

RUN apt-get update -q -y && apt-get dist-upgrade -q -y


ARG LICENSE_JAR_URL
ENV KAFKA_BROKERS=""
ENV KAFKA_APIKEY=""
ENV POSTGRESQL_URL=""
ENV POSTGRESQL_USER=""
ENV POSTGRESQL_PWD=""

# Next three lines need to be there to avoid NPE on javametrics-rest, javametrics-dash, fleetms
COPY /target/liberty/wlp/usr/servers/defaultServer /config/
COPY /target/liberty/wlp/usr/shared/resources /config/resources/
COPY /src/main/liberty/config/jvmbx.options /config/jvm.options

USER 1001
# Install required features if not present, install APM Data Collector
RUN installUtility install  defaultServer apmDataCollector-7.4 openapi-3.0 --acceptLicense
# Upgrade to production license if URL to JAR provided

RUN \
  if [ $LICENSE_JAR_URL ]; then \
    wget $LICENSE_JAR_URL -O /tmp/license.jar \
    && java -jar /tmp/license.jar -acceptLicense /opt/ibm \
    && rm /tmp/license.jar; \
  fi
