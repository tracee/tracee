package io.tracee.spring.autoconfigure;


import io.tracee.binding.spring.context.async.config.TraceeAsyncConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.task.AsyncTaskExecutor;

/**
 * @since 2.0
 */
@Configuration
@ConditionalOnBean(AsyncTaskExecutor.class)
@AutoConfigureBefore(TraceeContextAutoConfiguration.class)
@Import(TraceeAsyncConfiguration.class)
class TraceeSpringAsyncAutoConfiguration { }
