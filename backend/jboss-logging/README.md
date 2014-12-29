> This document contains documentation for the `tracee-jboss-logging` backend module. Check the [TracEE main documentation](/README.md) to get started.

# tracee-jboss-logging

Backend implementation for jboss-logging 3+

## Installation / Usage

You need exactly one backend provider on your runtime classpath. Add following to your `pom.xml` to add this module to your dependency tree:

```xml
<dependencies>
...
	<dependency>
		<groupId>io.tracee.backend</groupId>
		<artifactId>tracee-jboss-logging</artifactId>
		<version>RELEASE</version> <!-- You should specify a version instead -->
		<scope>runtime</scope>
	</dependency>
...
</dependencies>
```
