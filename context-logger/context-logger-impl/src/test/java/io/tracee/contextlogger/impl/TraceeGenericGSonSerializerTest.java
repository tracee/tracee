package io.tracee.contextlogger.impl;

import io.tracee.contextlogger.api.TraceeContextProvider;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;

/**
 * Created by Tobias Gindler, holisticon AG on 14.03.14.
 */
public class TraceeGenericGSonSerializerTest {

    @Test
    public void abs() throws IOException {
        Enumeration<URL> urls = TraceeContextProvider.class.getClassLoader().getResources("io.tracee.contextlogger.api");
        while (urls.hasMoreElements()) {
            URL nextElement = urls.nextElement();
            System.out.println(nextElement.toString());
        }
        System.out.println("");
    }


}
