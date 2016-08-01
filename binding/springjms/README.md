> This document contains documentation for the `tracee-springjms` module. Check the [TracEE main documentation](/README.md) to get started.

# tracee-springjms

> This module contains all necessary classes to add the TPIC to all produced messages and read if from it in case of consuming.

 * __TraceeMessageConverter__: A `MessageConverter` that defaults to Springs `SimpleMessageConverter` but with wrapping ability.
 
## Installation

To use the `org.springframework.jms.support.converter.SimpleMessageConverter` of Spring which is the default implementation for all transformations simple configure the `TraceeMessageConverter` this way:

### Java Configuration

```java
@Bean
public MessageConverter traceeMessageConverter() {
	return new TraceeMessageConverter();
}

@Bean
public JmsTemplate jmsTemplate() {
	final JmsTemplate jmsTemplate = new JmsTemplate();
	jmsTemplate.setMessageConverter(traceeMessageConverter());
	jmsTemplate.setConnectionFactory(connectionFactory());
	return jmsTemplate;
}
...
```
If you like to use your own MessageConverter `foo.bar.FooBarConverter` use the wrapping functionality of the `TraceeMessageConverter`:

```java
public MessageConverter traceeMessageConverter() {
	return new TraceeMessageConverter(new FooBarConverter());
}
...
```

### XML Configuration

```xml
  <bean id="traceeMessageConverter" class="io.tracee.binding.springjms.TraceeMessageConverter" />
  <bean id="jmsTemplate" class="org.springframework.jms.core.JmsTemplate" p:messageConverter-ref="traceeMessageConverter"/>
```

You're able to use your own `FooBarConverter` as well, but you've to wrap it with the `TraceeMessageConverter`

```xml
  <bean id="traceeMessageConverter" class="io.tracee.binding.springjms.TraceeMessageConverter">
    <constructor-arg>
      <bean class="foo.bar.FooBarConverter" />
    </constructor-arg>
  </bean>
  <bean id="jmsTemplate" class="org.springframework.jms.core.JmsTemplate" p:messageConverter-ref="traceeMessageConverter"/>
```

That's it!

## Manual message creation / consumption

If you read / write JMS messages without a `MessageConverter` you've to read / write the TPIC to your messages by yourself. Please refer following methods:

 * Reading TPIC from a message: `JmsHelper.readTpicFromMessage(message)`
 * Write TPIC to a message: `JmsHelper.writeTpicToMessage(message)`
 * Clean the TPIC from the Thread after the processing: `Tracee.getBackend().clear()`

You should use this helper methods within your implementation of the `MessageCreator` or `MessageListener`.
