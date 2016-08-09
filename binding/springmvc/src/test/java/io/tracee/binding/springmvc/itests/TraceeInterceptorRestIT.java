package io.tracee.binding.springmvc.itests;

import io.tracee.Tracee;
import io.tracee.TraceeConstants;
import io.tracee.binding.springhttpclient.TraceeClientHttpRequestInterceptor;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Collections;
import java.util.UUID;

import static io.tracee.TraceeConstants.TPIC_HEADER;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class TraceeInterceptorRestIT extends WebIntegrationIT {

	private RestTemplate restTemplate = new RestTemplate();

	@Before
	public void configureTestTemplate() {
		restTemplate = new RestTemplate();
		restTemplate.setInterceptors(Collections.<ClientHttpRequestInterceptor>singletonList(new TraceeClientHttpRequestInterceptor()));
	}

	@Test
	public void jsonControllerShouldReceiveTraceeHeader() throws IOException {
		Tracee.getBackend().put(TraceeConstants.INVOCATION_ID_KEY, "myValue");
		final SillyRestBody sillyBody = restTemplate.getForEntity(ENDPOINT_URL + "nameNeedTraceeId/sven", SillyRestBody.class).getBody();
		assertThat(sillyBody.getReceivedTraceeId(), is("myValue"));
	}

	@Test
	public void jsonControllerShouldSendTraceeHeader() throws IOException {
		final ResponseEntity<SillyRestBody> entity = restTemplate.getForEntity(ENDPOINT_URL + "name/sven", SillyRestBody.class);
		assertThat(entity.getHeaders().containsKey(TraceeConstants.TPIC_HEADER), is(true));
		assertThat(entity.getHeaders().get(TraceeConstants.TPIC_HEADER).get(0), containsString(TraceeConstants.INVOCATION_ID_KEY));
	}

	@Test
	public void ensureSameValueIsReceivedAndTransmittedBack() throws IOException {
		Tracee.getBackend().put(TraceeConstants.INVOCATION_ID_KEY, "myMegaValue");

		final ResponseEntity<SillyRestBody> entity = restTemplate.getForEntity(ENDPOINT_URL + "nameNeedTraceeId/daniel", SillyRestBody.class);
		final SillyRestBody sillyBody = entity.getBody();
		assertThat(sillyBody.getReceivedTraceeId(), is("myMegaValue"));
		assertThat(entity.getHeaders().get(TraceeConstants.TPIC_HEADER).get(0), containsString(TraceeConstants.INVOCATION_ID_KEY + "=myMegaValue"));
	}

	@Test
	public void withResponseBodyAdviceNewValuesShouldReceivedAsWell() {
		Tracee.getBackend().put(TraceeConstants.INVOCATION_ID_KEY, "myMegaValue");

		final ResponseEntity<SillyRestBody> entity = restTemplate.getForEntity(ENDPOINT_URL + "nameNeedTraceeId/daniel", SillyRestBody.class);
		assertThat(entity.getHeaders().get(TraceeConstants.TPIC_HEADER).get(0), containsString("laterKey=myLaterValue"));
	}

	@RestController
	public static class SillyRestController {

		@RequestMapping("/nameNeedTraceeId/{name}")
		public SillyRestBody nameNeedTraceeId(@PathVariable String name, HttpServletRequest request) {
			if (request.getHeader(TPIC_HEADER) == null) {
				throw new AssertionError("No expected Header " + TPIC_HEADER + " in request set");
			}
			return name(name);
		}

		@RequestMapping("/name/{name}")
		public SillyRestBody name(@PathVariable String name) {
			Tracee.getBackend().put("laterKey", "myLaterValue");
			return new SillyRestBody(name, Tracee.getBackend().getInvocationId());
		}
	}

	static class SillyRestBody {

		private String name;

		private String receivedTraceeId;

		private String makeItLage;

		public SillyRestBody() {
			makeItLage = createLargeBody();
		}

		private String createLargeBody() {
			final StringBuilder sb = new StringBuilder();
			for (int i = 0; i < 100; i++) {
				sb.append(UUID.randomUUID().toString());
			}
			return sb.toString();
		}

		public SillyRestBody(String name, String receivedTraceeId) {
			this();
			this.name = name;
			this.receivedTraceeId = receivedTraceeId;
		}

		public String getName() {
			return name;
		}

		public String getMakeItLage() {
			return makeItLage;
		}

		public String getReceivedTraceeId() {
			return receivedTraceeId;
		}
	}
}
