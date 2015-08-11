package io.tracee.binding.springmvc.async;

import io.tracee.Tracee;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncConfigurer;
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
import java.util.concurrent.atomic.AtomicInteger;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class DelegateTpicToAsyncExecutionInterceptorIT {

	@Rule
	public ErrorCollector collector = new ErrorCollector();

	@Autowired
	private AsyncService service;

	@Autowired
	private ThreadPoolTaskExecutor executor;

	@Test
	public void shouldDelegateToClientThread() throws ExecutionException, InterruptedException {
		for (int i = 0; i < 50; i++) {
			Tracee.getBackend().put("myKey", "myVal" + i);
			service.getStringInFuture(collector);
		}

		final ThreadPoolExecutor executor = this.executor.getThreadPoolExecutor();
		while (executor.getQueue().size() > 0 || executor.getActiveCount() > 0) {
			Thread.sleep(50L);
		}
		assertThat(service.getInvocationCount(), is(50));
	}

	@Configuration
	@EnableAsync
	@ComponentScan
	static class AppConfig implements AsyncConfigurer {

		@Bean
		public PreTpicAsyncBeanPostProcessor preTpicAsyncBeanPostProcessor(AsyncTaskExecutor executor) {
			return new PreTpicAsyncBeanPostProcessor(executor);
		}

		@Bean
		public PostTpicAsyncBeanPostProcessor postTpicAsyncBeanPostProcessor(AsyncTaskExecutor executor) {
			return new PostTpicAsyncBeanPostProcessor(executor);
		}

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

		private Set<String> oldVals = Collections.newSetFromMap(new ConcurrentHashMap<String, Boolean>());

		private AtomicInteger invocationCount = new AtomicInteger(0);

		@Async
		public void getStringInFuture(ErrorCollector collector) {
			collector.checkThat(oldVals, not(hasItem(Tracee.getBackend().get("myKey"))));
			oldVals.add(Tracee.getBackend().get("myKey"));
			invocationCount.incrementAndGet();
		}

		public int getInvocationCount() {
			return invocationCount.get();
		}
	}
}