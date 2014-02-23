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
RUN cd / && wget -q https://github.com/Graylog2/graylog2-web-interface/releases/download/0.20.0/graylog2-web-interface-0.20.0.tgz
RUN cd / && tar xvfz graylog2-web-interface-0.20.0.tgz && rm graylog2-web-interface-0.20.0.tgz
RUN cd / && ln -s graylog2-web-interface-0.20.0 graylog2-web-interface

ADD graylog2-web-interface.conf /graylog2-web-interface/conf/

EXPOSE 9000

ENTRYPOINT env \
 && cd /graylog2-web-interface/ \
 && sed -i "s/GRAYLOG_PORT_12900_TCP_ADDR/$GRAYLOG_PORT_12900_TCP_ADDR/g" "/graylog2-web-interface/conf/graylog2-web-interface.conf" \
 && sed -i "s/GRAYLOG_PORT_12900_TCP_PORT/$GRAYLOG_PORT_12900_TCP_PORT/g" "/graylog2-web-interface/conf/graylog2-web-interface.conf" \
 && ./bin/graylog2-web-interface -v




