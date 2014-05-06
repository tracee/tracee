# Tomcat7 with deployed tracee-examples-webapp
#
# VERSION       1.0

FROM dockerfile/java
MAINTAINER Daniel Wegener "daniel.wegener@holisticon.de"

ENV DEBIAN_FRONTEND noninteractive

# Download & install Tomcat 7
RUN cd / && wget -q http://archive.apache.org/dist/tomcat/tomcat-7/v7.0.52/bin/apache-tomcat-7.0.52.tar.gz \
 && tar -xf apache-tomcat-7.0.52.tar.gz && rm apache-tomcat-7.0.52.tar.gz \
 && ln -s apache-tomcat-7.0.52 apache-tomcat \
 && rm -rf /apache-tomcat/webapps/examples

# Enable Tomcat remote debugging
ENV JPDA_ADDRESS 8000
ENV JPDA_TRANSPORT dt_socket
ENV CATALINA_HOME /apache-tomcat-7.0.52

# Adding additional logging support
# disabled ADDS due to docker 0.10 bug #5370

ADD http://archive.apache.org/dist/tomcat/tomcat-7/v7.0.52/bin/extras/tomcat-juli.jar $CATALINA_HOME/bin/
ADD http://archive.apache.org/dist/tomcat/tomcat-7/v7.0.52/bin/extras/tomcat-juli-adapters.jar $CATALINA_HOME/lib/

# Download slf4j and logback
ADD http://repo1.maven.org/maven2/org/slf4j/slf4j-api/1.6.6/slf4j-api-1.6.6.jar $CATALINA_HOME/lib/slf4j-api-1.6.6.jar
ADD http://repo1.maven.org/maven2/org/slf4j/jul-to-slf4j/1.6.6/jul-to-slf4j-1.6.6.jar $CATALINA_HOME/lib/
ADD http://repo1.maven.org/maven2/org/slf4j/log4j-over-slf4j/1.6.6/log4j-over-slf4j-1.6.6.jar $CATALINA_HOME/lib/
ADD http://repo1.maven.org/maven2/org/slf4j/jcl-over-slf4j/1.6.6/jcl-over-slf4j-1.6.6.jar $CATALINA_HOME/lib/
ADD http://repo1.maven.org/maven2/ch/qos/logback/logback-classic/0.9.30/logback-classic-0.9.30.jar $CATALINA_HOME/lib/
ADD http://repo1.maven.org/maven2/ch/qos/logback/logback-core/0.9.30/logback-core-0.9.30.jar $CATALINA_HOME/lib/

# Add logstash gelf appender
ADD http://repo1.maven.org/maven2/biz/paluch/logging/logstash-gelf/1.3.2/logstash-gelf-1.3.2.jar $CATALINA_HOME/lib/
ADD http://repo1.maven.org/maven2/com/googlecode/json-simple/json-simple/1.1.1/json-simple-1.1.1.jar $CATALINA_HOME/lib/

# Add jax-rs 2 api
ADD http://repo1.maven.org/maven2/javax/ws/rs/javax.ws.rs-api/2.0/javax.ws.rs-api-2.0.jar $CATALINA_HOME/lib/

# Disable jcl
RUN rm /apache-tomcat/conf/logging.properties
# Add logback configuration
ADD logback.xml $CATALINA_HOME/lib/


ENV CATALINA_OPTS -Dlogback.configurationFile=file:$CATALINA_HOME/conf/logback.xml

ADD logback.xml $CATALINA_HOME/conf/
ADD tomcat-users.xml $CATALINA_HOME/conf/

EXPOSE 8000 8080

ENTRYPOINT export CATALINA_OPTS="$CATALINA_OPTS -DGRAYLOG_UDP_ADDR=$GRAYLOG_PORT_12201_UDP_ADDR -DGRAYLOG_UDP_PORT=$GRAYLOG_PORT_12201_UDP_PORT" \
 && env \
 && ifconfig \
 && /apache-tomcat/bin/catalina.sh run

# Download and install latest tracee snapshot artifacts
ADD https://oss.sonatype.org/service/local/artifact/maven/redirect?r=snapshots&g=io.tracee.examples&a=tracee-examples-springmvc&e=war&v=LATEST $CATALINA_HOME/webapps/tracee-examples-springmvc.war
ADD https://oss.sonatype.org/service/local/artifact/maven/redirect?r=snapshots&g=io.tracee.examples&a=tracee-examples-jaxrs2&e=war&v=LATEST $CATALINA_HOME/webapps/tracee-examples-jaxrs2.war