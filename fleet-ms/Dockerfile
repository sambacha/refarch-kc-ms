FROM us.icr.io/ibmcaseeda/kc-base-liberty
LABEL maintainer="IBM Cloud Architecture Solution Engineering team"
#Next three lines need to be there to avoid NPE on javametrics-rest, javametrics-dash, fleetms
COPY /target/liberty/wlp/usr/servers/defaultServer /config/
COPY /target/liberty/wlp/usr/shared/resources /config/resources/
COPY /src/main/liberty/config/jvmbx.options /config/jvm.options


# Adding permissions
USER root

RUN chmod g+w /config/apps

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
