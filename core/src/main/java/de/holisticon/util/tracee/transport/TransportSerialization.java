package de.holisticon.util.tracee.transport;


import java.util.Map;

/**
 * @author Daniel Wegener (Holisticon AG)
 */
public interface TransportSerialization<T> {

	/**
	 * Parses a key value map from the given serialized form.
	 * Returns and empty map if the given serialized form cannot be deserialized.
	 */
    Map<String, String> parse(T serialized);


	/**
	 * Returns the serialized backend or <code>null</code> if the backend was empty and there
	 * is no suitable representation of <code>T</code> for empty backends.
	 */
    T render(Map<String, String> backend);

}
