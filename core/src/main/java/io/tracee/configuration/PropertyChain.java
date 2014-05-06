package io.tracee.configuration;

import java.util.Arrays;
import java.util.Properties;

/**
 * @author Daniel Wegener (Holisticon AG)
 */
public class PropertyChain {

	private final Iterable<Properties> propertiesChain;


	public PropertyChain(Iterable<Properties> propertiesChain) {
		this.propertiesChain = propertiesChain;
	}

	public static PropertyChain build(Properties ... properties) {
		return new PropertyChain(Arrays.asList(properties));
	}


	public String getProperty(String key) {
		for (Properties properties : propertiesChain) {
			String p = properties.getProperty(key);
			if (p != null)
				return p;
		}
		return null;
	}


}
