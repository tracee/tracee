#!/bin/bash
#python -m maestro -f maestro.yaml stop
docker kill tracee-example-tomcat7-1
docker kill tracee-example-jbossas7-1
docker kill tracee-example-graylog-web-1
docker kill tracee-example-graylog-server-1
docker kill tracee-example-graylog-elasticsearch-1
docker kill tracee-example-graylog-mongodb-1

