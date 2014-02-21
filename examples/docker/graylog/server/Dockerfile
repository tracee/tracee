# JBoss7 with deployed tracee-examples-jaxws and tracee-examples-ejb
#
# VERSION       1.0

FROM dockerfile/java
MAINTAINER Daniel Wegener "daniel.wegener@holisticon.de"

ENV DEBIAN_FRONTEND noninteractive

# Installing maestro-ng helpers
RUN apt-get -y install python python-pip git
RUN pip install git+git://github.com/signalfuse/maestro-ng

# Download & install Greylog
RUN cd / && wget -q https://github.com/Graylog2/graylog2-server/releases/download/0.20.0/graylog2-server-0.20.0.tgz \
&& tar xvfz graylog2-server-0.20.0.tgz && rm graylog2-server-0.20.0.tgz \
&& ln -s graylog2-server-0.20.0 graylog2-server

ADD graylog2.conf /etc/

EXPOSE 12900

ENTRYPOINT env \
  sed -i "s/MONGODB_PORT/$MONGO_PORT_27017_TCP_PORT/g" "/etc/graylog2.conf" \
  && sed -i "s/MONGODB_HOST/$MONGO_PORT_27017_TCP_ADDR/g" "/etc/graylog2.conf" \
  && sed -i "s/HOSTNAME/$ELASTICSEARCH_PORT_9300_TCP_ADDR/g" "/etc/graylog2.conf" \
  && sed -i "s/ELASTICSEARCH_HOST/$ELASTICSEARCH_PORT_9200_TCP_ADDR/g" "/etc/graylog2.conf" \
  && sed -i "s/ELASTICSEARCH_PORT/$ELASTICSEARCH_PORT_9200_TCP_PORT/g" "/etc/graylog2.conf" \
  && env && ifconfig && cd /graylog2-server/ \
  && /bin/bash
  #&& java -jar graylog2-server.jar --debug

