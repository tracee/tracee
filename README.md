# tracee   0.1-SNAPSHOT

[![Build Status](https://secure.travis-ci.org/holisticon/tracee.png)](https://travis-ci.org/holisticon/tracee)

## Introduction

Debugging distributed enterprise applications is difficult.

...

You may already aggregate all your machine logs in a single logging database (using logstash, elasticsearch or others) but it is still
complicated to find all log entries that belong to a certain interaction with the system.

*TracEE* is a framework that tries to ease this kind of interaction diagnosis by passing contextual information through your system and
makes them visible in your logs. Therefore if contains adapters or interceptors for the most relevant ee technologies:

* servlet 2.5
* jax-ws
* jax-rs
* jms

The following logging frameworks are supported as backends

* slf4j
* log4j
* jboss-logging

This project is sill in early experimental alpha stage and the whole api may change during further development.

## Getting started
*Just some lines of description in prose and code that gets the user up and running with this library.*


The following context information are visible out of the box:
- for each new incoming



## Contribution
- (2013) Daniel Wegener (Holisticon AG)
- (2013) Tobias Gindler (Holisticon AG)

### Setup a development environment
tracee is built using Maven (at least version 3.0.4).
A simple import of the pom in your IDE should get you up and running:

``mvn clean install``

### Requirements
The likelihood of a pull request being used rises with the following properties:

- You have used a feature branch.
- You have included a test that demonstrates the functionality added or fixed.
- You adhered to the [code conventions](http://www.oracle.com/technetwork/java/javase/documentation/codeconvtoc-136057.html).

## TODO
- Add connectors to error logging servlet filter and jaxws service handler to send error jsons to external systems.
- Documentation


# servlet use cases
-

## Slides
[to be completed](docs/slides/index.html)

## Sponsoring
This project is sponsored and supported by [holisticon AG](http://www.holisticon.de/)

## License
This project is released under the revised [BSD License](LICENSE).