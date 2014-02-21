#!/bin/bash

docker build -q -t=tracee-examples-tomcat7 tomcat7 \
&& docker build -q -t=tracee-examples-jbossas7 jbossas7 \
&& docker build -q -t=tracee-examples-graylog-server graylog/server \
&& docker build -q -t=tracee-examples-graylog-web graylog/web \
&& docker build -q -t=tracee-examples-graylog-mongodb graylog/mongodb
&& docker build -q -t=tracee-examples-graylog-elasticsearch graylog/elasticsearch
