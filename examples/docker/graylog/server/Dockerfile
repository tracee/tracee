# JBoss7 with deployed tracee-examples-jaxws and tracee-examples-ejb
#
# VERSION       1.0

FROM dockerfile/java
MAINTAINER Daniel Wegener "daniel.wegener@holisticon.de"

ENV DEBIAN_FRONTEND noninteractive

# Download and install Greylog
RUN cd / && wget -q https://github.com/Graylog2/graylog2-server/releases/download/0.20.1/graylog2-server-0.20.1.tgz \
&& tar xvfz graylog2-server-0.20.1.tgz && rm graylog2-server-0.20.1.tgz \
&& ln -s graylog2-server-0.20.1 graylog2-server

ADD graylog2.conf /etc/

EXPOSE 12900
EXPOSE 12201/udp

ENTRYPOINT export HOST_ADDR=`hostname --ip-address` \
  && sed -i "s/MONGODB_PORT/$MONGO_PORT_27017_TCP_PORT/g" "/etc/graylog2.conf" \
  && sed -i "s/MONGODB_HOST/$MONGO_PORT_27017_TCP_ADDR/g" "/etc/graylog2.conf" \
  && sed -i "s/HOST_ADDR/$HOST_ADDR/g" "/etc/graylog2.conf" \
  && sed -i "s/ELASTICSEARCH_HOST/$ELASTICSEARCH_PORT_9300_TCP_ADDR/g" "/etc/graylog2.conf" \
  && sed -i "s/ELASTICSEARCH_PORT/$ELASTICSEARCH_PORT_9300_TCP_PORT/g" "/etc/graylog2.conf" \
  && env && ifconfig && cd /graylog2-server/ \
  && java -jar graylog2-server.jar --debug
