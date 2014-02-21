#!/bin/bash
#python -m maestro -f maestro.yaml start
docker run -d -p 27017:27017 -p 28017:28017 --name=tracee-example-graylog-mongodb-1 tracee-examples-graylog-mongodb
docker run -d -p 9200:9200 -p 9300:9300 --name=tracee-example-graylog-elasticsearch-1 tracee-examples-graylog-elasticsearch
docker run -d -p 12900:12900 -link tracee-example-graylog-mongodb-1:mongo -link tracee-example-graylog-elasticsearch-1:elasticsearch --name=tracee-example-graylog-server-1 tracee-examples-graylog-server

