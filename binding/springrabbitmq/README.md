> This document contains documentation for the tracee-springrabbitmq module. Check the [TracEE main documentation](/README.md) to get started.

# tracee-springrabbitmq

This module writes and retrieves TPIC header information to/from [RabbitMQ](http://www.rabbitmq.com/) messages with the [Spring-AMQP](http://projects.spring.io/spring-amqp/) abstraction.

 * __TraceeMessagePropertiesConverter__: Inherits from `DefaultMessagePropertiesConverter and handles the TPIC information from/to messages.

## Installation

If you're use Maven for your dependency management simple add this to your pom:

```xml
<dependencies>
...
    <dependency>
        <groupId>io.tracee.binding</groupId>
        <artifactId>tracee-springrabbitmq</artifactId>
        <version>RELEASE</version> <!-- You should specify a version instead -->
    </dependency>
...
</dependencies>
```

Then set the `TraceeMessagePropertiesConverter` to Spring's `RabbitTemplate`:

```java
...
final RabbitTemplate template = new RabbitTemplate();
template.setMessagePropertiesConverter(new TraceeMessagePropertiesConverter());
...
```

