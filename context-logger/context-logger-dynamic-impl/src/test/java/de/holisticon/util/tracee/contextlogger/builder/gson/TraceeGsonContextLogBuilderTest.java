package de.holisticon.util.tracee.contextlogger.builder.gson;

import com.google.gson.GsonBuilder;
import de.holisticon.util.tracee.contextlogger.RegexMatcher;
import de.holisticon.util.tracee.contextlogger.data.subdata.java.JavaThrowable;
import de.holisticon.util.tracee.contextlogger.data.subdata.tracee.ObjectArrayContextProvider;
import de.holisticon.util.tracee.contextlogger.utility.RecursiveReflectionToStringStyle;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Tobias Gindler on 20.03.14.
 */
public class TraceeGsonContextLogBuilderTest {


    @Test
    public void should_create_context_log () {
        try {
            throw new NullPointerException();
        } catch (NullPointerException e) {

            TraceeGsonContextLogBuilder logBuilder = new TraceeGsonContextLogBuilder();
            Set<Class> classes = new HashSet<Class>();
            classes.add(JavaThrowable.class);

            logBuilder.setWrapperClasses(classes);

            String json = logBuilder.log(new JavaThrowable(e));

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

            Object[] oarray = new Object[3];
            oarray[0] = e;
            oarray[1] = "TATA";
            oarray[2] = new JavaThrowable(e);
            ObjectArrayContextProvider cp = new ObjectArrayContextProvider(oarray);


            String json = logBuilder.log(cp);

            MatcherAssert.assertThat(json, RegexMatcher.matches("\\{\\\"java.lang.NullPointerException\\\":\\\"NullPointerException.*\\\"java.lang.String\\\":\\\"TATA.*java.lang.NullPointerException\\]\\\"\\}"));


        }
    }
}
