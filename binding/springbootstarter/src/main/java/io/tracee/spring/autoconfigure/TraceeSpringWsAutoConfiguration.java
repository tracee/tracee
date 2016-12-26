package io.tracee.spring.autoconfigure;

import io.tracee.Tracee;
import io.tracee.binding.springws.config.TraceeSpringWsConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;

/**
 * @since 2.0
 */
@Configuration
@ConditionalOnClass({Tracee.class, EnableWs.class, WsConfigurerAdapter.class})
@AutoConfigureBefore(TraceeContextAutoConfiguration.class)
@Import(TraceeSpringWsConfiguration.class)
public class TraceeSpringWsAutoConfiguration {
}
