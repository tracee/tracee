#!/bin/bash

# Start Elasticsearch
docker run -d -p 9200:9200 -p 9300:9300 -h="tracee-example-graylog-elasticsearch-1" --name="tracee-example-graylog-elasticsearch-1" tracee-example-graylog-elasticsearch
# Start MongoDB
docker run -d -p 27017:27017 -p 28017:28017 -h="tracee-example-graylog-mongodb-1" --name="tracee-example-graylog-mongodb-1" tracee-example-graylog-mongodb
# Wait until MongoDB is up
while ! nc -vz localhost 27017; do sleep 1; done
while ! nc -vz localhost 28017; do sleep 1; done
sleep 5s
# Start graylog2-server
docker run -d -p 12900:12900 -p 12201:12201/udp -link tracee-example-graylog-mongodb-1:mongo -link tracee-example-graylog-elasticsearch-1:elasticsearch -h="tracee-example-graylog-server-1" --name="tracee-example-graylog-server-1" tracee-example-graylog-server
curl -XPOST http://admin:yummie@127.0.0.1:12900/system/inputs -d @graylog2-server-inputs.json -H "Content-Type:application/json"
while [ $? -ne 0 ]; do
	sleep 2
    curl -XPOST http://admin:yummie@127.0.0.1:12900/system/inputs -d @graylog2-server-inputs.json -H "Content-Type:application/json"
done
# Start graylog2-server-web
docker run -d -p 9000:9000 -link tracee-example-graylog-server-1:graylog -h="tracee-example-graylog-web-1" --name="tracee-example-graylog-web-1" tracee-example-graylog-web
# Start jbossas7
docker run -d -p 8081:8080 -p 9990:9990 -link tracee-example-graylog-server-1:graylog -h="tracee-example-jbossas7-1" --name="tracee-example-jbossas7-1" tracee-example-jbossas7
# Start tomcat7
docker run -d -p 8080:8080 -link tracee-example-graylog-server-1:graylog -link tracee-example-jbossas7-1:jbossas7 -h="tracee-example-tomcat7-1" --name="tracee-example-tomcat7-1" tracee-example-tomcat7