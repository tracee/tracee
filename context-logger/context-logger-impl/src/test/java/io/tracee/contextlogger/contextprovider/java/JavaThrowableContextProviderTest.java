package io.tracee.contextlogger.contextprovider.java;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link JavaThrowableContextProvider}.
 * Created by Tobias Gindler, holisticon AG on 31.03.14.
 */
public class JavaThrowableContextProviderTest {

    public static final String MESSAGE = "MESSAGE";

    private Throwable exceptionWithMessage;
    private Throwable exceptionWithoutMessage;


    @Before
    public void init() {

        try {
            throw new NullPointerException(MESSAGE);
        } catch (Throwable throwable) {
            this.exceptionWithMessage = throwable;
        }

        try {
            throw new NullPointerException();
        } catch (Throwable throwable) {
            this.exceptionWithoutMessage = throwable;
        }

    }

    @Test
    public void should_return_wrapped_type() {

        JavaThrowableContextProvider givenJavaThrowableContextProvider = new JavaThrowableContextProvider();

        Class result = givenJavaThrowableContextProvider.getWrappedType();

        MatcherAssert.assertThat(result.equals(Throwable.class), Matchers.equalTo(true));

    }

    @Test
    public void should_get_null_for_not_set_message() {
        JavaThrowableContextProvider givenJavaThrowableContextProvider = new JavaThrowableContextProvider();
        givenJavaThrowableContextProvider.setContextData(exceptionWithoutMessage);

        String result = givenJavaThrowableContextProvider.getMessage();
        MatcherAssert.assertThat(result, Matchers.nullValue());

    }

    @Test
    public void should_get_null_for_set_message() {
        JavaThrowableContextProvider givenJavaThrowableContextProvider = new JavaThrowableContextProvider();
        givenJavaThrowableContextProvider.setContextData(exceptionWithMessage);

        String result = givenJavaThrowableContextProvider.getMessage();
        MatcherAssert.assertThat(result, Matchers.notNullValue());
        MatcherAssert.assertThat(result, Matchers.equalTo(MESSAGE));

    }

    @Test
    public void should_get_stacktrace() {

        JavaThrowableContextProvider givenJavaThrowableContextProvider = new JavaThrowableContextProvider();
        givenJavaThrowableContextProvider.setContextData(exceptionWithMessage);

        String result = givenJavaThrowableContextProvider.getStacktrace();
        MatcherAssert.assertThat(result, Matchers.notNullValue());
        MatcherAssert.assertThat(result, Matchers.startsWith("java.lang.NullPointerException: MESSAGE"));

    }

}
