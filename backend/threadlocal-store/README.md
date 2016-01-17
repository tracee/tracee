> This document contains documentation for the `tracee-threadlocal-store` backend module. Check the [TracEE main documentation](/README.md) to get started.

# tracee-threadlocal-store

Native backend implementation. Messages on `TraceeLogger` with loglevel WARN and ERROR are redirected to stderr. All other messages are discarded.

## Installation

You need exactly one backend provider on your runtime classpath. Add following to your `pom.xml` to add this module to your dependency tree: 

```xml
<dependencies>
...
	<dependency>
		<groupId>io.tracee.backend</groupId>
		<artifactId>tracee-threadlocal-store</artifactId>
		<version>${tracee.version}</version>
		<scope>runtime</scope>
	</dependency>
...
</dependencies>
```
