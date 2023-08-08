FROM maven:3-eclipse-temurin-17 AS base-builder
ENV HOME=/taatta
WORKDIR $HOME

# cache dependencies
ADD pom.xml $HOME

ADD athom-presence-sensor/pom.xml $HOME/athom-presence-sensor/pom.xml
ADD athom-smart-plug/pom.xml $HOME/athom-smart-plug/pom.xml
ADD collector/pom.xml $HOME/collector/pom.xml
ADD df702/pom.xml $HOME/df702/pom.xml
ADD ems701/pom.xml $HOME/ems701/pom.xml
ADD eureka-server/pom.xml $HOME/eureka-server/pom.xml
ADD notification/pom.xml $HOME/notification/pom.xml
ADD pcr2/pom.xml $HOME/pcr2/pom.xml
ADD rhf1s001/pom.xml $HOME/rhf1s001/pom.xml
ADD tbs220/pom.xml $HOME/tbs220/pom.xml
ADD wqm101/pom.xml $HOME/wqm101/pom.xml

RUN mvn dependency:resolve

COPY . .

FROM eclipse-temurin:17 AS base
ENV TAATTA_DOCKER=1
ARG VERSION="2.0.0"
WORKDIR /usr/local/taatta
RUN mkdir -p /var/log/smart-city/

FROM base-builder AS athom-presence-sensor-builder
RUN mvn --projects athom-presence-sensor -e package spring-boot:repackage

FROM base AS athom-presence-sensor
RUN ln -sf /dev/stdout /var/log/smart-city/athom-presence-sensor.log
COPY --from=athom-presence-sensor-builder /taatta/athom-presence-sensor/target/athom-presence-sensor-$VERSION.jar athom-presence-sensor.jar


FROM base-builder AS athom-smart-plug-builder
RUN mvn --projects athom-smart-plug -e package spring-boot:repackage

FROM base AS athom-smart-plug
RUN ln -sf /dev/stdout /var/log/smart-city/athom-smart-plug.log
COPY --from=athom-smart-plug-builder /taatta/athom-smart-plug/target/athom-smart-plug-$VERSION.jar athom-smart-plug.jar


FROM base-builder AS collector-builder
RUN mvn --projects collector -e package spring-boot:repackage

FROM base AS collector
RUN ln -sf /dev/stdout /var/log/smart-city/collector.log
COPY --from=collector-builder /taatta/collector/target/collector-$VERSION.jar collector.jar


FROM base-builder AS df702-builder
RUN mvn --projects df702 -e package spring-boot:repackage

FROM base AS df702
RUN ln -sf /dev/stdout /var/log/smart-city/df702.log
COPY --from=df702-builder /taatta/df702/target/df702-$VERSION.jar df702.jar


FROM base-builder AS pcr2-builder
RUN mvn --projects pcr2 -e package spring-boot:repackage

FROM base AS pcr2
RUN ln -sf /dev/stdout /var/log/smart-city/pcr2.log
COPY --from=pcr2-builder /taatta/pcr2/target/pcr2-$VERSION.jar pcr2.jar


FROM base-builder AS rhf1s001-builder
RUN mvn --projects rhf1s001 -e package spring-boot:repackage

FROM base AS rhf1s001
RUN ln -sf /dev/stdout /var/log/smart-city/rhf1s001.log
COPY --from=rhf1s001-builder /taatta/rhf1s001/target/rhf1s001-$VERSION.jar rhf1s001.jar


FROM base-builder AS tbs220-builder
RUN mvn --projects tbs220 -e package spring-boot:repackage

FROM base AS tbs220
RUN ln -sf /dev/stdout /var/log/smart-city/tbs220.log
COPY --from=tbs220-builder /taatta/tbs220/target/tbs220-$VERSION.jar tbs220.jar


FROM base-builder AS wqm101-builder
RUN mvn --projects wqm101 -e package spring-boot:repackage

FROM base AS wqm101
RUN ln -sf /dev/stdout /var/log/smart-city/wqm101.log
COPY --from=wqm101-builder /taatta/wqm101/target/wqm101-$VERSION.jar wqm101.jar


# mosquitto
FROM eclipse-mosquitto:2 AS mosquitto
COPY configuration/eclipse-mosquitto/entrypoint.sh /entrypoint.sh
ENTRYPOINT ["sh", "/entrypoint.sh"]
CMD ["/usr/sbin/mosquitto","-c","/mosquitto/config/mosquitto.conf"]