FROM frolvlad/alpine-oraclejdk8
VOLUME /tmp
ADD demo-java-config-client1-1.0.0.jar app.jar
RUN sh -c 'touch /app.jar'
ENV JAVA_OPTS=""