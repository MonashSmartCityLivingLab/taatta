FROM maven:3-eclipse-temurin-17 AS builder
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
RUN mvn -e package spring-boot:repackage

FROM eclipse-temurin:17 AS base
ENV TAATTA_DOCKER=1
ARG VERSION="2.0.0"
WORKDIR /usr/local/taatta
RUN mkdir -p /var/log/smart-city/

FROM base AS athom-presence-sensor
RUN ln -sf /dev/stdout /var/log/smart-city/athom-presence-sensor.log
COPY --from=builder /taatta/athom-presence-sensor/target/athom-presence-sensor-$VERSION.jar athom-presence-sensor.jar

FROM base AS athom-smart-plug
RUN ln -sf /dev/stdout /var/log/smart-city/athom-smart-plug.log
COPY --from=builder /taatta/athom-smart-plug/target/athom-smart-plug-$VERSION.jar athom-smart-plug.jar

FROM base AS collector
RUN ln -sf /dev/stdout /var/log/smart-city/collector.log
COPY --from=builder /taatta/collector/target/collector-$VERSION.jar collector.jar

FROM base AS df702
RUN ln -sf /dev/stdout /var/log/smart-city/df702.log
COPY --from=builder /taatta/df702/target/df702-$VERSION.jar df702.jar

FROM base AS ems701
RUN ln -sf /dev/stdout /var/log/smart-city/ems701.log
COPY --from=builder /taatta/ems701/target/ems701-$VERSION.jar ems701.jar

FROM base AS eureka-server
RUN ln -sf /dev/stdout /var/log/smart-city/eureka-server.log
COPY --from=builder /taatta/eureka-server/target/eureka-server-$VERSION.jar eureka-server.jar

FROM base AS notification
RUN ln -sf /dev/stdout /var/log/smart-city/notification.log
COPY --from=builder /taatta/notification/target/notification-$VERSION.jar notification.jar

FROM base AS pcr2
RUN ln -sf /dev/stdout /var/log/smart-city/pcr2.log
COPY --from=builder /taatta/pcr2/target/pcr2-$VERSION.jar pcr2.jar

FROM base AS rhf1s001
RUN ln -sf /dev/stdout /var/log/smart-city/rhf1s001.log
COPY --from=builder /taatta/rhf1s001/target/rhf1s001-$VERSION.jar rhf1s001.jar

FROM base AS tbs220
RUN ln -sf /dev/stdout /var/log/smart-city/tbs220.log
COPY --from=builder /taatta/tbs220/target/tbs220-$VERSION.jar tbs220.jar

FROM base AS wqm101
RUN ln -sf /dev/stdout /var/log/smart-city/wqm101.log
COPY --from=builder /taatta/wqm101/target/wqm101-$VERSION.jar wqm101.jar

# mosquitto
FROM eclipse-mosquitto:2 AS mosquitto
COPY configuration/eclipse-mosquitto/entrypoint.sh /entrypoint.sh
ENTRYPOINT ["sh", "/entrypoint.sh"]
CMD ["/usr/sbin/mosquitto","-c","/mosquitto/config/mosquitto.conf"]