> This document contains documentation for TracEE examples. Click [here](/README.md) to get an overview that TracEE is about.

# tracee-examples

This folder contains docker descriptors for tracee-examples. It starts a preconfigured tomcat7 and jbossas7 and
deploys tracee-examples-webapp and tracee-examples-ear into these containers.

!![overview](docker.png?raw=true)

### Building the environment


install docker and run `./buildEnvironment.sh`

### Start the enviroment

run `./startEnvironment.sh`

After starting all services, you may browse the following web endpoints:

 * [http://localhost:8080/](http://localhost:8080/) - tomcat7
 * [http://localhost:8081/](http://localhost:8081/) - jbossas7
 * [http://localhost:9990/](http://localhost:9990/) - jbossas7 - management interface
 * [http://localhost:9000/](http://localhost:9000/) - graylog-web

_username and password is always admin:yummie_

