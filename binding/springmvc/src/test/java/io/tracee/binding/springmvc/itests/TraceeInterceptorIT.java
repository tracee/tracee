package io.tracee.binding.springmvc.itests;

import io.tracee.Tracee;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static io.tracee.TraceeConstants.TPIC_HEADER;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

public class TraceeInterceptorIT extends WebIntegrationIT{

	@Test
	public void shouldDelegateContextToServerAndBack() throws IOException {
		final Header traceeResponseHeader = get("addToTpic", "in+Client=yes").getFirstHeader(TPIC_HEADER);

		assertThat(traceeResponseHeader, notNullValue());
		assertThat(traceeResponseHeader.getValue(), containsString("inInterceptor=y+e+s"));
		assertThat(traceeResponseHeader.getValue(), containsString("in+Client=yes"));
	}

	@Test
	public void shouldDelegateContextToServerAndReceiveItInCaseOfException() throws IOException {
		final Header traceeResponseHeader = get("throwException", "in+Client=yes").getFirstHeader(TPIC_HEADER);

		assertThat(traceeResponseHeader, notNullValue());
		assertThat(traceeResponseHeader.getValue(), containsString("inInterceptor=y+e+s"));
		assertThat(traceeResponseHeader.getValue(), containsString("in+Client=yes"));
	}

	@Test
	public void whenResponseIsCommitedWeShouldReceiveInitValues() throws IOException {
		final Header traceeResponseHeader = get("closeResponse", "initialVal=0815").getFirstHeader(TPIC_HEADER);

		assertThat(traceeResponseHeader, notNullValue());
		assertThat(traceeResponseHeader.getValue(), containsString("initialVal=0815"));
		assertThat(traceeResponseHeader.getValue(), not(containsString("initialVal=laterValue321")));
	}

	private HttpResponse get(String remoteRessource, String traceeHeaderValue) throws IOException {
		final HttpClient client = new DefaultHttpClient();
		final HttpGet httpGet = new HttpGet(ENDPOINT_URL + remoteRessource);
		if (traceeHeaderValue != null) {
			httpGet.setHeader(TPIC_HEADER, traceeHeaderValue);
			httpGet.setHeader("Accept", MediaType.APPLICATION_JSON_VALUE);
		}
		return client.execute(httpGet);
	}

	@Controller
	public static class SillyController {

		@RequestMapping("/addToTpic")
		@ResponseStatus(HttpStatus.NO_CONTENT)
		public void handleGet(HttpServletRequest request) {
			if (request.getHeader(TPIC_HEADER) == null) {
				throw new AssertionError("No expected Header " + TPIC_HEADER + " in request set");
			}
			Tracee.getBackend().put("inInterceptor", "y e s");
		}

		@RequestMapping("/throwException")
		@ResponseStatus(HttpStatus.BAD_REQUEST)
		public void throwException(HttpServletRequest request) {
			handleGet(request);
			throw new RuntimeException("Mööp!");
		}

		@RequestMapping("/closeResponse")
		@ResponseStatus(HttpStatus.NO_CONTENT)
		public void closeResponse(HttpServletResponse response) throws IOException {
			response.flushBuffer();
			Tracee.getBackend().put("initialVal", "laterValue321");
		}
	}
}
