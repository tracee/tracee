package io.tracee.examples.springaop;

import io.tracee.Tracee;
import io.tracee.contextlogger.watchdog.Watchdog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Random;

/**
 * Example Spring-MVC Controller.
 */
@Controller
public class HomeController {

    @Autowired
    private Multiplier multiplier;

    @Autowired
    private MultiplierWithClassLevelWatchdog multiplierWithClassLevelWatchdog;

    private static final Logger LOG = LoggerFactory.getLogger(HomeController.class);

    /**
     * Example MVC controller method that uses the watchdog annotation directly on itself.
     *
     * @param model the model to use
     * @return the page to invoke
     */
    @Watchdog(id = "HOME_CONTROLLER", isActive = true)
    @RequestMapping(value = "/springAopOnMvc", method = RequestMethod.GET)
    public String springAopOnMvc(Model model) {

        final int token = new Random().nextInt(10);
        model.addAttribute("tokenSpringMvc", token);

        LOG.info("I will now let multiply {} with {}", token, token);
        final int multipliedToken = multiply(token, token);
        LOG.info("The result is {}", multipliedToken);

        Tracee.getBackend().put("withToken", Integer.toString(multipliedToken));

        return "home";
    }

    /**
     * Example MVC controller method that uses the watchdog annotation at an method of an autowired bean.
     *
     * @param model the model to use
     * @return the page to invoke
     */
    @RequestMapping(value = "/springAopOnBean", method = RequestMethod.GET)
    public String springAopOnBean(Model model) {

        final int token = new Random().nextInt(10);
        model.addAttribute("token", token);

        LOG.info("I will now let multiply {} with {}", token, token);
        final int multipliedToken = multiplier.multiply(token, token);
        LOG.info("The result is {}", multipliedToken);

        Tracee.getBackend().put("withToken", Integer.toString(multipliedToken));

        return "home";
    }

    /**
     * Example MVC controller method that uses the watchdog annotation at the type of an autowired bean.
     *
     * @param model the model to use
     * @return the page to invoke
     */
    @RequestMapping(value = "/springAopOnBeanWithClassLevelWatchdog", method = RequestMethod.GET)
    public String springAopOnBeanClassLevelWatchdog(Model model) {

        final int token = new Random().nextInt(10);
        model.addAttribute("token", token);

        LOG.info("I will now let multiply {} with {}", token, token);
        final int multipliedToken = multiplierWithClassLevelWatchdog.multiply(token, token);
        LOG.info("The result is {}", multipliedToken);

        Tracee.getBackend().put("withToken", Integer.toString(multipliedToken));

        return "home";
    }


    /**
     * Using a watchdog annotation only works if the method is invoked via a spring bean proxy. Therefore this will not work if the method is used directly via this.multiply().
     * To make this work you have to invoke the method by using the current proxy. You can do this by invoking ((HomeController) AopContext.currentProxy()).multiply()
     *
     * @param a first parameter
     * @param b second parameter
     * @return result
     */
    // !!! NOT WORKING if called directly instead of using a proxy !!!
    @Watchdog(id = "HOME_CONTROLLER_MULTIPLY", isActive = true)
    public int multiply(int a, int b) {
        if (a < 5) {
            throw new IllegalArgumentException(" argument a=" + a + " triggered exception");
        }

        return a * b;
    }
}
