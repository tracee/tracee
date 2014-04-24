# JBoss7 with deployed tracee-examples-jaxws and tracee-examples-ejb
#
# VERSION       1.0

FROM dockerfile/java
MAINTAINER Daniel Wegener "daniel.wegener@holisticon.de"

ENV DEBIAN_FRONTEND noninteractive

# Download and install Greylog
RUN cd / && wget -q https://github.com/Graylog2/graylog2-web-interface/releases/download/0.20.1/graylog2-web-interface-0.20.1.tgz \
 && tar xvfz graylog2-web-interface-0.20.1.tgz && rm graylog2-web-interface-0.20.1.tgz \
 && ln -s graylog2-web-interface-0.20.1 graylog2-web-interface

ENV GRAYLOG2_WEB_HOME /graylog2-web-interface-0.20.1
ADD graylog2-web-interface.conf $GRAYLOG2_WEB_HOME/conf/

EXPOSE 9000

# Reduce initial heap space from 1024m default.
ENV JAVA_OPTS -Xms256m

ENTRYPOINT env \
 && cd /graylog2-web-interface/ \
 && sed -i "s/GRAYLOG_PORT_12900_TCP_ADDR/$GRAYLOG_PORT_12900_TCP_ADDR/g" "/graylog2-web-interface/conf/graylog2-web-interface.conf" \
 && sed -i "s/GRAYLOG_PORT_12900_TCP_PORT/$GRAYLOG_PORT_12900_TCP_PORT/g" "/graylog2-web-interface/conf/graylog2-web-interface.conf" \
 && ./bin/graylog2-web-interface -v
