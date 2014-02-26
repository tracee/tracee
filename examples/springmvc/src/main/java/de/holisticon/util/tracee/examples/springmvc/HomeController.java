package de.holisticon.util.tracee.examples.springmvc;

import de.holisticon.util.tracee.Tracee;
import de.holisticon.util.tracee.examples.jaxws.client.testclient.TraceeJaxWsTestWS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Random;

@Controller
public class HomeController {

	@Autowired
	TraceeJaxWsTestWS wsClient;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index(Model model) {

		final int token = new Random().nextInt(10);
		model.addAttribute("token", token);

		final int multipliedToken = wsClient.multiply(token, token);

		Tracee.getBackend().put("withToken", Integer.toString(multipliedToken));




		return "home";
	}
}