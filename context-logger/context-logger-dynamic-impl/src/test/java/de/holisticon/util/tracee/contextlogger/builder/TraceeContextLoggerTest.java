package de.holisticon.util.tracee.contextlogger.builder;

import de.holisticon.util.tracee.contextlogger.RegexMatcher;
import de.holisticon.util.tracee.contextlogger.builder.gson.TraceeGsonContextLogBuilder;
import de.holisticon.util.tracee.contextlogger.data.subdata.java.JavaThrowable;
import de.holisticon.util.tracee.contextlogger.data.subdata.tracee.ObjectArrayContextProvider;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

/**
 * Test class for {@link de.holisticon.util.tracee.contextlogger.builder.TraceeContextLogger}.
 * Created by Tobias Gindler, holisticon AG on 21.03.14.
 */
public class TraceeContextLoggerTest {



    @Test
    public void should_create_context_log () {
        try {
            throw new NullPointerException();
        } catch (NullPointerException e) {

            String json = TraceeContextLogger.log(e);

            MatcherAssert.assertThat(json, RegexMatcher.matches("\\{\"stacktrace\":\"java.lang.NullPointerException.*\"\\}"));


        }
    }

    @Test
    public void should_create_mixed_context_log () {
        try {
            throw new NullPointerException();
        } catch (NullPointerException e) {

            TraceeGsonContextLogBuilder logBuilder = new TraceeGsonContextLogBuilder();
            Set<Class> classes = new HashSet<Class>();
            classes.add(ObjectArrayContextProvider.class);

            logBuilder.setWrapperClasses(classes);

            Object instance1 = e;
            Object instance2 = "TATA";
            Object instance3 = new JavaThrowable(e);


            String json = TraceeContextLogger.log(instance1, instance2, 212, instance3, 212.2);

            MatcherAssert.assertThat(json, RegexMatcher.matches("\\[\\{\\\"stacktrace\\\":\\\"java.lang.NullPointerException.*TATA\\\",212,\\{\\\"stacktrace\\\":\\\"java.lang.NullPointer.*\\\"\\},212.2\\]"));


        }
    }



}
