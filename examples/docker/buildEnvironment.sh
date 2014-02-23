#!/bin/bash

docker build -q -t=tracee-example-tomcat7 tomcat7 \
&& docker build -q -t=tracee-example-jbossas7 jbossas7 \
&& docker build -q -t=tracee-example-graylog-server graylog/server \
&& docker build -q -t=tracee-example-graylog-web graylog/web \
&& docker build -q -t=tracee-example-graylog-mongodb graylog/mongodb \
&& docker build -q -t=tracee-example-graylog-elasticsearch graylog/elasticsearch
