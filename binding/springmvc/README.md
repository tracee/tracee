> This document contains documentation for the tracee-springmvc module. Click [here](/README.md) to get an overview what TracEE is about.

# tracee-springmvc

This tracee module reads the incoming TracEE context for requests to your Spring MVC controllers and generates requestIds if needed. It adds the TPIC header to your responses as well. - If you don't like to expose the TPIC in your responses (maybe you want to hide it from your users), you should take an individual TracEE configuration.
This module requires Spring 3.1.0 or above.

## Installation

To your Spring dependencies add `tracee-springmvc`. That's all! - For example in maven-style projects add to the `pom.xml`:

```xml
<dependencies>
...
    <dependency>
	<groupId>io.tracee.inbound</groupId>
	<artifactId>tracee-springmvc</artifactId>
        <version>${tracee.version}</version> <!-- You should specify a version instead -->
    </dependency>
...
</dependencies>
```

Then you're able to use our `TraceeInterceptor` in two ways:

With Spring JavaConfig:
```java
@Configuration
@EnableWebMvc
public class YourApplicationConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new io.tracee.springmvc.TraceeInterceptor());
    }

}
```

With XML configuration:

```xml
<mvc:interceptors>
	<bean id="traceeInterceptor" class="io.tracee.springmvc.TraceeInterceptor">
		<property name="profileName" value="default"/> <!-- The profile configuration is optional -->
	</bean>
</mvc:interceptors>
...
```
