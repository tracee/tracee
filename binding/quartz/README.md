> This document contains documentation for the tracee-quartz module. Check the [TracEE main documentation](/README.md) to get started.

# tracee-quartz

This module contains a [Quartz](http://quartz-scheduler.org/) `JobListener` to extract the TPIC and generate a TracEE invocation identifier before the job starts. This ID is send to other services if a entire binding is attached to the communication stack. 

* __TraceeJobListener__: Generates a invocation Id and clears the backend after the job has finished or aborted in an ungraceful way.
* __TraceeContextInjector__: This class injects the current TPIC into the `JobDataMap` at the time when a dynamic generated job is scheduled.
 
## Installation

Use this module with Quartz 2.1 or above. Add this module as dependency. For Maven:

```xml
<dependencies>
    ...
    <dependency>
        <groupId>io.tracee.binding</groupId>
        <artifactId>tracee-quartz</artifactId>
        <version>RELEASE</version>
    </dependency>
    ...
</dependencies>
```

Add the `TraceeJobListener` like other job listeners:

### With Quartz-Properties

Quartz-Jobs are typically configured by configuration file. (See the [configuration documentation](http://quartz-scheduler.org/generated/2.2.1/html/qs-all/#page/Quartz_Scheduler_Documentation_Set%2Fco-ovr_about_quartz_configuration.html%23) for further instructions.).

Add an additional job listener to your configuration:

```
org.quartz.jobListener.TraceeJobListener.class = io.tracee.binding.quartz.TraceeJobListener
```

### Programmatic with Java

```java
...
scheduler.getListenerManager().addJobListener(new TraceeJobListener());
...
```

### Delegate TPIC information to dynamic created jobs
It could be the case that you use quartz to continue your invocation processing in asynchronous manner. It could be necessary for you to delegate the invocationId or other TPIC information to the generated job. You could achieve this by using the `TraceeContextInjector` at dynamic generated `Trigger`,`JobDetails` or `JobDataMap`. The 
`TraceeJobListener` will pick the TPIC and set it to the `TraceeBackend` of the job itself.

```java
...
new TraceeContextInjector().injectContext(trigger);
...
```
