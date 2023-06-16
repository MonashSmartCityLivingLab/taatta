FROM maven:3-eclipse-temurin-17 AS builder
ARG VERSION="1.1-SNAPSHOT"
WORKDIR /taatta
COPY . .
RUN mvn -e package spring-boot:repackage

FROM eclipse-temurin:17 AS base
ENV TAATTA_DOCKER=1
ARG VERSION="1.1-SNAPSHOT"
WORKDIR /usr/local/taatta
RUN mkdir -p /var/log/smart-city/
# TODO: not sure if the taatta user/group is needed

FROM base AS collector
COPY --from=builder /taatta/collector/target/collector-$VERSION.jar collector.jar
RUN ln -sf /dev/stdout /var/log/smart-city/collector.log

FROM base AS athom-smart-plug
COPY --from=builder /taatta/athom-smart-plug/target/athom-smart-plug-$VERSION.jar athom-smart-plug.jar
RUN ln -sf /dev/stdout /var/log/smart-city/athom-smart-plug.log

#FROM base AS df702
#COPY --from=builder /taatta/df702/target/df702-$VERSION.jar df702.jar
#RUN ln -sf /dev/stdout /var/log/smart-city/df702.log
#
#FROM base AS ems701
#COPY --from=builder /taatta/ems701/target/ems701-$VERSION.jar ems701.jar
#RUN ln -sf /dev/stdout /var/log/smart-city/ems701.log

#FROM base AS eureka-server
#COPY --from=builder /taatta/eureka-server/target/eureka-server-$VERSION.jar eureka-server.jar
#RUN ln -sf /dev/stdout /var/log/smart-city/eureka-server.log
#
#FROM base AS notification
#COPY --from=builder /taatta/notification/target/notification-$VERSION.jar notification.jar
#RUN ln -sf /dev/stdout /var/log/smart-city/notification.log

FROM base AS pcr2
COPY --from=builder /taatta/pcr2/target/pcr2-$VERSION.jar pcr2.jar
RUN ln -sf /dev/stdout /var/log/smart-city/pcr2.log

#FROM base AS rhf1s001
#COPY --from=builder /taatta/rhf1s001/target/rhf1s001-$VERSION.jar rhf1s001.jar
#RUN ln -sf /dev/stdout /var/log/smart-city/rhf1s001.log
#
#FROM base AS tbs220
#COPY --from=builder /taatta/tbs220/target/tbs220-$VERSION.jar tbs220.jar
#RUN ln -sf /dev/stdout /var/log/smart-city/tbs220.log

#FROM base AS wqm101
#COPY --from=builder /taatta/wqm101/target/wqm101-$VERSION.jar wqm101.jar
#RUN ln -sf /dev/stdout /var/log/smart-city/wqm101.log

FROM eclipse-mosquitto:2 AS mosquitto
COPY configuration/eclipse-mosquitto/entrypoint.sh /entrypoint.sh
ENTRYPOINT ["sh", "/entrypoint.sh"]
CMD ["/usr/sbin/mosquitto","-c","/mosquitto/config/mosquitto.conf"]