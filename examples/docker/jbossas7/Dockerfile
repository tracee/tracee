# JBoss7 with deployed tracee-examples-jaxws and tracee-examples-ejb
#
# VERSION       1.0

FROM dockerfile/java
MAINTAINER Daniel Wegener "daniel.wegener@holisticon.de"

ENV DEBIAN_FRONTEND noninteractive

# Installing maestro-ng helpers
RUN apt-get -y install python python-pip git
RUN pip install git+git://github.com/signalfuse/maestro-ng

# Download & install JBoss 7
RUN cd / &&  wget -q http://download.jboss.org/jbossas/7.1/jboss-as-7.1.1.Final/jboss-as-7.1.1.Final.tar.gz \
  &&  tar -xf jboss-as-7.1.1.Final.tar.gz && rm jboss-as-7.1.1.Final.tar.gz \
  &&  ln -s jboss-as-7.1.1.Final jboss-as

# download logstash-gelf-logging module
ADD http://repo1.maven.org/maven2/biz/paluch/logging/logstash-gelf/1.2.6/logstash-gelf-1.2.6-logging-module.zip /
RUN unzip /logstash-gelf-1.2.6-logging-module.zip -d / \
  && mv /logstash-gelf-1.2.6/* /jboss-as/modules \
  && rm -rf /logstash-gelf-1.2.6*


ADD standalone.xml /jboss-as/standalone/configuration/

EXPOSE 8080 9990 9999

ENTRYPOINT env \
  && sed -i "s/GRAYLOG_PORT_12201_UDP_PORT/$GRAYLOG_PORT_12201_UDP_PORT/g" "/jboss-as/standalone/configuration/standalone.xml" \
  && sed -i "s/GRAYLOG_PORT_12201_UDP_ADDR/$GRAYLOG_PORT_12201_UDP_ADDR/g" "/jboss-as/standalone/configuration/standalone.xml" \
  && /jboss-as/bin/standalone.sh -Djboss.bind.address=0.0.0.0 -Djboss.bind.address.management=0.0.0.0

# download tracee artifacts
ADD https://oss.sonatype.org/service/local/artifact/maven/redirect?r=snapshots&g=de.holisticon.util.tracee.examples&a=tracee-examples-ear&e=ear&v=LATEST /jboss-as/standalone/deployments/tracee-examples.ear