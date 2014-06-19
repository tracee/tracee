package io.tracee.contextlogger.contextprovider.jaxws;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Test class for {@link JaxWsContextProvider}.
 * Created by Tobias Gindler, holisticon AG on 31.03.14.
 */
public class JaxWsContextProviderTest {

    private static final String SOAP_REQUEST = "SOAP_REQUEST";
    private static final String SOAP_RESPONSE = "SOAP_RESPONSE";

    @Test
    public void should_return_wrapped_type() {

        JaxWsContextProvider givenJaxWsContextProvider = new JaxWsContextProvider();

        Class result = givenJaxWsContextProvider.getWrappedType();

        MatcherAssert.assertThat(result.equals(JaxWsWrapper.class), Matchers.equalTo(true));

    }

    @Test
    public void should_get_null_for_request_for_null_valued_wrapper() {

        JaxWsContextProvider givenJaxWsContextProvider = new JaxWsContextProvider();

        String result = givenJaxWsContextProvider.getSoapRequest();

        MatcherAssert.assertThat(result, Matchers.nullValue());


    }

    @Test
    public void should_get_null_for_response_for_null_valued_wrapper() {

        JaxWsContextProvider givenJaxWsContextProvider = new JaxWsContextProvider();

        String result = givenJaxWsContextProvider.getSoapResponse();

        MatcherAssert.assertThat(result, Matchers.nullValue());


    }

    @Test
    public void should_get_null_for_request_for_null_valued_request() {

        JaxWsContextProvider givenJaxWsContextProvider = new JaxWsContextProvider();
        givenJaxWsContextProvider.setContextData(JaxWsWrapper.wrap(null, null));

        String result = givenJaxWsContextProvider.getSoapRequest();

        MatcherAssert.assertThat(result, Matchers.nullValue());


    }

    @Test
    public void should_get_null_for_response_for_null_valued_response() {

        JaxWsContextProvider givenJaxWsContextProvider = new JaxWsContextProvider();
        givenJaxWsContextProvider.setContextData(JaxWsWrapper.wrap(null, null));

        String result = givenJaxWsContextProvider.getSoapResponse();

        MatcherAssert.assertThat(result, Matchers.nullValue());


    }

    @Test
    public void should_get_request() {

        JaxWsContextProvider givenJaxWsContextProvider = new JaxWsContextProvider();
        givenJaxWsContextProvider.setContextData(JaxWsWrapper.wrap(SOAP_REQUEST, SOAP_RESPONSE));

        String result = givenJaxWsContextProvider.getSoapRequest();

        MatcherAssert.assertThat(result, Matchers.notNullValue());
        MatcherAssert.assertThat(result, Matchers.equalTo(SOAP_REQUEST));

    }

    @Test
    public void should_get_response() {

        JaxWsContextProvider givenJaxWsContextProvider = new JaxWsContextProvider();
        givenJaxWsContextProvider.setContextData(JaxWsWrapper.wrap(SOAP_REQUEST, SOAP_RESPONSE));

        String result = givenJaxWsContextProvider.getSoapResponse();

        MatcherAssert.assertThat(result, Matchers.notNullValue());
        MatcherAssert.assertThat(result, Matchers.equalTo(SOAP_RESPONSE));

    }

}
