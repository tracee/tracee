#!/bin/bash

echo "building tracee-example-tomcat7 image" \
&& docker build -q -t=tracee-example-tomcat7 tomcat7 \
&& echo "building tracee-example-jbossas7 image" \
&& docker build -q -t=tracee-example-jbossas7 jbossas7 \
&& echo "building tracee-example-graylog-server image" \
&& docker build -q -t=tracee-example-graylog-server graylog/server \
&& echo "building tracee-example-graylog-web image" \
&& docker build -q -t=tracee-example-graylog-web graylog/web \
&& echo "building tracee-example-graylog-mongodb image" \
&& docker build -q -t=tracee-example-graylog-mongodb graylog/mongodb \
&& echo "building tracee-example-graylog-elasticsearch image" \
&& docker build -q -t=tracee-example-graylog-elasticsearch graylog/elasticsearch
