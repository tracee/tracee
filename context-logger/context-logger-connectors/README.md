> This document contains documentation for the context-logger-connectors module. Click [here](/README.md) to get an overview that TracEE is about.

# context-logger-connectors

The tracEE context logger supports writing to the log files per default. 
But its also possible to write the context log information to other kind of targets.
This can be done via connectors provided by the submodules of this module or by enabling to to implement custom connectors.

## Available Connectors

| Module                                                 | Usage |
|-------------------------------------------------------:|:-----:|
| [context-logger-connector-api](connector-api/)         | Provides an api for developing custom connectors |
| [tracee-context-logger-http-connector](http-connector/)| Provides a connector that sends contextual log information via HTTP to another system|


## Configuring connectors
Connectors can be configured by defining system properties using the following simple configuration schema. 

    io.tracee.contextlogger.connector.<CONNECTOR NAME>.class=<CONNECTORS FULL QUALIFIED CLASS NAME>
    io.tracee.contextlogger.connector.<CONNECTOR NAME>.<CONNECTOR SPECIFIC PROPERTY NAMES>=<PROPERTY VALUE>

## Configuration example - HTTP Connector
Here is a small example that configures two Http connectors:

    io.tracee.contextlogger.connector.httpConnector1.class=io.tracee.contextlogger.connector.HttpConnector
    io.tracee.contextlogger.connector.httpConnector1.url=http://localhost:8080/target
    io.tracee.contextlogger.connector.httpConnector1.basicAuth.user=user
    io.tracee.contextlogger.connector.httpConnector1.basicAuth.password=passwd
    
    io.tracee.contextlogger.connector.httpConnector2.class=io.tracee.contextlogger.connector.HttpConnector
    io.tracee.contextlogger.connector.httpConnector2.url=http://localhost:8090/anotherTarget
        
