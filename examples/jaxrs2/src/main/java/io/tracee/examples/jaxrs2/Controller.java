package io.tracee.examples.jaxrs2;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import java.util.HashMap;
import java.util.Map;

@Path("/")
public class Controller {


	@GET
	@Produces("application/json")
	public Map<String, String> touchMe(@QueryParam("hot") String hot) {
		Map<String, String> responseObject = new HashMap<String, String>();

		return responseObject;
	}

}
