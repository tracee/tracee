# JBoss7 with deployed tracee-examples-jaxws and tracee-examples-ejb
#
# VERSION       1.0

FROM dockerfile/java
MAINTAINER Daniel Wegener "daniel.wegener@holisticon.de"

ENV DEBIAN_FRONTEND noninteractive

# Download and install JBoss 7
RUN cd / &&  wget -q http://download.jboss.org/jbossas/7.1/jboss-as-7.1.1.Final/jboss-as-7.1.1.Final.tar.gz \
  &&  tar -xf jboss-as-7.1.1.Final.tar.gz && rm jboss-as-7.1.1.Final.tar.gz \
  &&  ln -s jboss-as-7.1.1.Final jboss-as

ENV JBOSS_HOME /jboss-as-7.1.1.Final

# Download logstash-gelf-logging as jboss7 module (see https://github.com/mp911de/logstash-gelf#jboss-7-configuration)
ADD http://repo1.maven.org/maven2/biz/paluch/logging/logstash-gelf/1.3.2/logstash-gelf-1.3.2-logging-module.zip /
RUN unzip /logstash-gelf-1.3.2-logging-module.zip -d / \
  && mv /logstash-gelf-1.3.2/* /jboss-as/modules \
  && rm -rf /logstash-gelf-1.3.2*


ADD standalone-full.xml $JBOSS_HOME/standalone/configuration/

# Add management user to jboss7
RUN $JBOSS_HOME/bin/add-user.sh admin yummie --silent=true

EXPOSE 8080 9990 9999

ENTRYPOINT env \
  && sed -i "s/GRAYLOG_PORT_12201_UDP_PORT/$GRAYLOG_PORT_12201_UDP_PORT/g" "/jboss-as/standalone/configuration/standalone-full.xml" \
  && sed -i "s/GRAYLOG_PORT_12201_UDP_ADDR/$GRAYLOG_PORT_12201_UDP_ADDR/g" "/jboss-as/standalone/configuration/standalone-full.xml" \
  && $JBOSS_HOME/bin/standalone.sh --server-config=standalone-full.xml -Djboss.bind.address=0.0.0.0 -Djboss.bind.address.management=0.0.0.0

# Download and install latest tracee snapshot artifacts
ADD https://oss.sonatype.org/service/local/artifact/maven/redirect?r=snapshots&g=de.holisticon.util.tracee.examples&a=tracee-examples-ear&e=ear&v=LATEST $JBOSS_HOME/standalone/deployments/tracee-examples.ear