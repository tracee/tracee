package io.tracee.binding.spring.context.async;

import io.tracee.TraceeBackend;
import io.tracee.binding.spring.context.async.config.TraceeAsyncConfiguration;
import io.tracee.binding.spring.context.config.TraceeContextConfiguration;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = DelegateTpicToAsyncExecutionInterceptorIT.AppConfig.class)
public class DelegateTpicToAsyncExecutionInterceptorIT {

	@Rule
	public ErrorCollector collector = new ErrorCollector();

	@Autowired
	private AsyncService service;

	@Autowired
	private ThreadPoolTaskExecutor executor;

	@Autowired
	private TraceeBackend backend;

	@Test
	public void shouldDelegateToClientThread() throws ExecutionException, InterruptedException {
		for (int i = 0; i < 50; i++) {
			backend.put("myKey", "myVal" + i);
			service.getStringInFuture(collector);
		}
		backend.clear();

		final ThreadPoolExecutor executor = this.executor.getThreadPoolExecutor();
		while (executor.getQueue().size() > 0 || executor.getActiveCount() > 0) {
			Thread.sleep(500L);
		}
		executor.shutdown();
		executor.awaitTermination(1, TimeUnit.MINUTES);
		assertThat(backend.copyToMap().entrySet(), is(empty()));
	}

	@Configuration
	@Import({TraceeContextConfiguration.class, TraceeAsyncConfiguration.class})
	@EnableAsync
	static class AppConfig extends AsyncConfigurerSupport {

		@Bean
		public AsyncService asyncService() {
			return new AsyncService();
		}

		@Bean
		@Override
		public Executor getAsyncExecutor() {
			ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
			executor.setCorePoolSize(5);
			executor.setMaxPoolSize(5);
			executor.setQueueCapacity(50);
			return executor;
		}
	}

	public static class AsyncService {

		@Autowired
		private TraceeBackend traceeBackend;

		private Set<String> oldVals = Collections.newSetFromMap(new ConcurrentHashMap<String, Boolean>());

		private AtomicInteger invocationCount = new AtomicInteger(0);

		@Async
		public void getStringInFuture(ErrorCollector collector) {
			final String backendValue = traceeBackend.get("myKey");
			collector.checkThat(backendValue, notNullValue());
			collector.checkThat(oldVals, not(hasItem(backendValue)));
			oldVals.add(backendValue);
			invocationCount.incrementAndGet();
		}

		public int getInvocationCount() {
			return invocationCount.get();
		}
	}
}
