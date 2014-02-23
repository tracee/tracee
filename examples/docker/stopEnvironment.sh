#!/bin/bash
#python -m maestro -f maestro.yaml stop
docker stop tracee-example-graylog-web-1
docker stop tracee-example-graylog-server-1
docker stop tracee-example-graylog-elasticsearch-1
docker stop tracee-example-graylog-mongodb-1

