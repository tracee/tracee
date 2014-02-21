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

# download tracee artifacts
ADD https://oss.sonatype.org/service/local/artifact/maven/redirect?r=snapshots&g=de.holisticon.util.tracee.examples&a=tracee-examples-ear&e=ear&v=LATEST /jboss-as/standalone/deployments/tracee-examples.ear


EXPOSE 8080 9990 9999

ENTRYPOINT evn && /jboss-as/bin/standalone.sh -Djboss.bind.address=0.0.0.0 -Djboss.bind.address.management=0.0.0.0