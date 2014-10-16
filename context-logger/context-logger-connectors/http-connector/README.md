> This document contains documentation for the tracee-context-logger-http-connector module. Click [here](/README.md) to get an overview that TracEE is about.

# tracee-context-logger-http-connector

The TracEE tracee-context-logger-http-connector supports writing contextual data to remote systems via HTTP(async). 

## Maven artifacts
You need to add the following Dependencies to your projects pom.xml:

    <!-- Add this to enable the HTTP connector -->
    <dependency>
        <groupId>io.tracee.contextlogger</groupId>
        <artifactId>tracee-context-logger-connector-parent</artifactId>
        <version>${tracee.version}</version>
    </dependency>

## Configuration of the HTTP connector
The HTTP connector can be configured by using system properties. 

| Property                                                 | Description | Default |
|---------------------------------------------------------:|:------|:-------|
| url                       | the url to call | |
| basicAuth.user            | user used for basic authentication                | not used |
| basicAuth.password        | password used for basic authentication            | not used |
| proxy.host                | proxy hostname to use                             | not used |
| proxy.port                | proxy port to use                                 | not used |
| proxy.user                | proxy user, if proxy requires authentication      | not used |
| proxy.password            | proxy password, if proxy requires authentication  | not used |
| request.timoutInMs        | the timeout in milliseconds                       | 10000 ms |


It's possible to configure multiple http connectors by changing the name of the in the property configuration prefix. 
 
