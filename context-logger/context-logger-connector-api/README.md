> This document contains documentation for the context-logger-connector-api module. Click [here](/README.md) to get an overview that TracEE is about.

# context-logger-connector-api
This module defines the api used to create custom connectors. The connector must implement the Connector interface and reside in the classpath to be used.
The configuration will be passed to the connector via the init method of the Connector interface.
