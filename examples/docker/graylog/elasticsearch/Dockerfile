# JBoss7 with deployed tracee-examples-jaxws and tracee-examples-ejb
#
# VERSION       1.0

FROM dockerfile/java
MAINTAINER Daniel Wegener "daniel.wegener@holisticon.de"

# Install ElasticSearch.
#RUN wget -O - http://packages.elasticsearch.org/GPG-KEY-elasticsearch | apt-key add -
#RUN echo "deb http://packages.elasticsearch.org/elasticsearch/0.90.10/debian stable main" >> /etc/apt/sources.list
#RUN apt-get update
#RUN apt-get install -y elasticsearch
RUN wget https://download.elasticsearch.org/elasticsearch/elasticsearch/elasticsearch-0.90.10.deb \
 && dpkg -i elasticsearch-0.90.10.deb && rm elasticsearch-0.90.10.deb

# Prevent elasticsearch calling `ulimit`.
RUN sed -i 's/MAX_OPEN_FILES=/# MAX_OPEN_FILES=/g' /etc/init.d/elasticsearch

# Expose ports.
#   - 9200: HTTP
#   - 9300: transport
EXPOSE 9200
EXPOSE 9300

# Define an entry point.
ENTRYPOINT ["/usr/share/elasticsearch/bin/elasticsearch"]
CMD ["-f"]

ADD elasticsearch.yml /etc/elasticsearch/
ADD logging.yml /etc/elasticsearch/
