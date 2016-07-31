package io.tracee.binding.springhttpclient;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Configuration
public class SpringHttpClientTestJavaConfig {

	@Bean
	public ClientHttpRequestInterceptor traceeClientHttpRequestInterceptor() {
		return new TraceeClientHttpRequestInterceptor();
	}

	@Bean
	public RestTemplate restTemplate() {
		final RestTemplate restTemplate = new RestTemplate();
		restTemplate.setInterceptors(Collections.singletonList(traceeClientHttpRequestInterceptor()));
		return restTemplate;
	}
}
