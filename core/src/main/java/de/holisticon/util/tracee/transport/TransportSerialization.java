package de.holisticon.util.tracee.transport;


import java.util.Map;

/**
 * @author Daniel Wegener (Holisticon AG)
 */
public interface TransportSerialization<T> {

    Map<String,String> parse(T serialized);
    T render(Map<String,String> backend);

}
