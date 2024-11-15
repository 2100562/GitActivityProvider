FROM maven:3.9.9-amazoncorretto-21-debian@sha256:3210a7a6cc2e573590d18be064659984f0bc22c16720229224d0bc20b52522d0 AS builder
COPY . .
RUN mvn clean package
FROM amazoncorretto:21.0.4-al2023-headless@sha256:bab02f48ae7b8afdce902f55acc256fc063a6ee215b5c8f463e354778ed08fc1 AS runner
RUN mkdir -p /activityprovider/lib &&  \
    yum install -y shadow-utils && yum clean all && \
    groupadd --system ap && useradd -r -s /bin/false -g ap ap

COPY --from=builder target/*.jar /activityprovider/lib/
COPY --from=builder target/lib/*.jar /activityprovider/lib/
WORKDIR /activityprovider
RUN chown -R ap:ap /activityprovider
USER ap
ENV JAVA_OPTS ""
CMD java $JAVA_OPTS -cp .:lib/* pt.uab.meiw.aps.ActivityProvider