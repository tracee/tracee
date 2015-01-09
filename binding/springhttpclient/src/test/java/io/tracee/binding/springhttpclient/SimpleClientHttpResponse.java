package io.tracee.binding.springhttpclient;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

final class SimpleClientHttpResponse implements ClientHttpResponse {

	private final HttpStatus statusCode;
	private final String statusText;
	private final HttpHeaders httpHeaders;

	public SimpleClientHttpResponse(HttpStatus statusCode, String statusText, HttpHeaders httpHeaders) {
		this.statusCode = statusCode;
		this.statusText = statusText;
		this.httpHeaders = httpHeaders;
	}

	@Override
	public HttpStatus getStatusCode() throws IOException {
		return statusCode;
	}

	@Override
	public int getRawStatusCode() throws IOException {
		return statusCode.value();
	}

	@Override
	public String getStatusText() throws IOException {
		return statusText;
	}

	@Override
	public void close() {
	}

	@Override
	public InputStream getBody() throws IOException {
		return new ByteArrayInputStream(new byte[0]);
	}

	@Override
	public HttpHeaders getHeaders() {
		return httpHeaders;
	}
}
