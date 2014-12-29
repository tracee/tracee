> This document contains documentation for the `tracee-slf4j` backend module. Check the [TracEE main documentation](/README.md) to get started.

# tracee-slf4j

Backend implementation for [slf4j (1.6.6+)](http://www.slf4j.org/)

## Installation

You need exactly one backend provider on your runtime classpath. Add following to your `pom.xml` to add this module to your dependency tree:

```xml
<dependencies>
...
	<dependency>
		<groupId>io.tracee.backend</groupId>
		<artifactId>tracee-slf4j</artifactId>
		<version>RELEASE</version> <!-- You should specify a version instead -->
		<scope>runtime</scope>
	</dependency>
...
</dependencies>
```
