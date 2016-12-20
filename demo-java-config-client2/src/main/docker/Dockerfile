FROM frolvlad/alpine-oraclejdk8
VOLUME /tmp
ADD demo-java-config-client2-1.0.0.jar app.jar
RUN sh -c 'touch /app.jar'
ENV JAVA_OPTS=""