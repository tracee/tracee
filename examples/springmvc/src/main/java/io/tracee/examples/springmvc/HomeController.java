package io.tracee.examples.springmvc;

import io.tracee.Tracee;
import io.tracee.examples.jaxws.client.testclient.TraceeJaxWsTestWS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Random;

@Controller
public class HomeController {

	@Resource(name = "traceeJaxWsTestWS")
	private TraceeJaxWsTestWS wsClient;

	private static final Logger LOG = LoggerFactory.getLogger(HomeController.class);

	@RequestMapping(value = "/token", method = RequestMethod.GET)
	public String index(Model model) {

		final int token = new Random().nextInt(10);
		model.addAttribute("token", token);

		LOG.info("I will now let the jaxws-service multiply {} with {}", token, token);
		final int multipliedToken = wsClient.multiply(token, token);
		LOG.info("Thank you jaxws-service. The result is {}", multipliedToken);

		Tracee.getBackend().put("withToken", Integer.toString(multipliedToken));

		return "home";
	}
}
