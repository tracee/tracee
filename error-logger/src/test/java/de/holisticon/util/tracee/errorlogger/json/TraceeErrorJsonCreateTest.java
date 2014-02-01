package de.holisticon.util.tracee.errorlogger.json;

import de.holisticon.util.tracee.errorlogger.TraceeErrorConstants;
import de.holisticon.util.tracee.errorlogger.json.generator.*;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Tobias Gindler, holisticon AG
 */
public class TraceeErrorJsonCreateTest {


    @Test
    public void createExceptionJsonTest() {

        final String expectedJsonPattern = "\\{\"x-tracee-exception\":\\{\"message\":\"TEST\",\"stacktrace\":\"java.lang.NullPointerException: TEST.*?\"\\}\\}";

        final NullPointerException npe = createException(new NullPointerException("TEST"));

        final String json = TraceeErrorLoggerJsonCreator.createJsonCreator().
                addExceptionCategory(npe).toString();

        final Pattern pattern = Pattern.compile(expectedJsonPattern);
        final Matcher matcher = pattern.matcher(json);
        boolean matches = matcher.matches();

        assertThat(matches, CoreMatchers.is(true));

    }

    @Ignore("pending implementation")
    @Test
    public void createCommonJsonTest() {

        final String expectedJsonPattern = "\\{\"x-tracee-common\":\\{\"system-name\":\"FE\",\"stage\":\"DEV\",\"timestamp\":\\d+,\"thread\\-name\":\".*?\",\"thread\\-id\":\\d+\\}\\}";

        System.setProperty(TraceeErrorConstants.SYSTEM_PROPERTY_NAME_SYSTEM, "FE");
        System.setProperty(TraceeErrorConstants.SYSTEM_PROPERTY_NAME_STAGE, "DEV");


        final String json = TraceeErrorLoggerJsonCreator.createJsonCreator().addCommonCategory().toString();

        System.out.println(json);

        Pattern pattern = Pattern.compile(expectedJsonPattern);
        Matcher matcher = pattern.matcher(json);
        boolean matches = matcher.matches();


    }

    @Test
    public void createJaxwsJsonTest() {

        final String expectedJsonPattern = "\\{\"x-tracee-jaxws\".*req.*res.*\\}\\}";

        System.setProperty(TraceeErrorConstants.SYSTEM_PROPERTY_NAME_SYSTEM, "FE");
        System.setProperty(TraceeErrorConstants.SYSTEM_PROPERTY_NAME_STAGE, "DEV");


        String json = TraceeErrorLoggerJsonCreator.createJsonCreator().addJaxwsCategory(
                "req", "res"
        ).toString();

        Pattern pattern = Pattern.compile(expectedJsonPattern);
        Matcher matcher = pattern.matcher(json);
        boolean matches = matcher.matches();

        System.out.println(json);

        assertThat(matches, CoreMatchers.is(true));

    }

    @Ignore("to be fixed")
    @Test
    public void createServletJsonTest() {

        final String expectedHttpMethod = "POST";
        final String expectedUser = "TEST";
        final String expectedURL = "http://www.test.de:4573/abc/def";
        final String expectedHttpParameterName1 = "P1";
        final String expectedHttpParameterValue1 = "V1";
        final String expectedHttpParameterName2 = "P2";
        final String expectedHttpParameterValue2 = "V2";
        final String[] expectedHttpParameterValues1 = {expectedHttpParameterValue1, expectedHttpParameterValue2};
        final String[] expectedHttpParameterValues2 = {expectedHttpParameterValue2};
        final String expectedRemoteAddress = "1.1.1.1";
        final String expectedRemoteHost = "test.holisticon.de";
        final Integer expectedRemotePort = 1000;


        final String expectedJson = "{\"x-tracee-servlet\":{\"url\":\"http://www.test.de:4573/abc/def\","
                + "\"http-method\":\"POST\",\"http-parameters\":[{\"name\":\"P1\",\"value\":\"V1\"},"
                + "{\"name\":\"P1\",\"value\":\"V2\"},{\"name\":\"P2\",\"value\":\"V2\"}],"
                + "\"http-headers\":[{\"name\":\"P1\",\"value\":\"V1\"},{\"name\":\"P2\","
                + "\"value\":\"V2\"}],\"http-remote-address\":\"1.1.1.1\","
                + "\"http-remote-host\":\"test.holisticon.de\",\"http-remote-port\":1000,\"user\":\"TEST\"}}";


        final HttpServletResponse httpServletResponsetMock = mock(HttpServletResponse.class);

        final HttpServletRequest httpServletRequestMock = mock(HttpServletRequest.class);
        when(httpServletRequestMock.getUserPrincipal()).thenReturn(new Principal() {
            @Override
            public String getName() {
                return expectedUser;
            }
        });
        when(httpServletRequestMock.getMethod()).thenReturn(expectedHttpMethod);
        when(httpServletRequestMock.getRequestURL()).thenReturn(new StringBuffer(expectedURL));


        final Enumeration<String> expectedParameterNames = toEnumeration(
                Arrays.asList(expectedHttpParameterName1, expectedHttpParameterName2).iterator());

        final Enumeration<String> expectedHeaderNames = toEnumeration(
                Arrays.asList(expectedHttpParameterName1, expectedHttpParameterName2).iterator());

        when(httpServletRequestMock.getParameterNames()).thenReturn(expectedParameterNames);
        when(httpServletRequestMock.getHeaderNames()).thenReturn(expectedHeaderNames);

        given(httpServletRequestMock.getHeader(expectedHttpParameterName1)).willReturn(expectedHttpParameterValue1);
        given(httpServletRequestMock.getHeader(expectedHttpParameterName2)).willReturn(expectedHttpParameterValue2);

        given(httpServletRequestMock.getParameter(expectedHttpParameterName1)).willReturn(expectedHttpParameterValue1);
        given(httpServletRequestMock.getParameter(expectedHttpParameterName2)).willReturn(expectedHttpParameterValue2);

        given(httpServletRequestMock.getParameterValues(expectedHttpParameterName1)).willReturn(expectedHttpParameterValues1);
        given(httpServletRequestMock.getParameterValues(expectedHttpParameterName2)).willReturn(expectedHttpParameterValues2);

        when(httpServletRequestMock.getRemoteAddr()).thenReturn(expectedRemoteAddress);
        when(httpServletRequestMock.getRemoteHost()).thenReturn(expectedRemoteHost);
        when(httpServletRequestMock.getRemotePort()).thenReturn(expectedRemotePort);

        final String json = TraceeErrorLoggerJsonCreator.createJsonCreator().addServletCategory(
                httpServletRequestMock, httpServletResponsetMock
        ).toString();


        assertThat(json, CoreMatchers.is(expectedJson));

    }

    private <T> Enumeration<T> toEnumeration(final Iterator<T> iterator) {

        return new Enumeration<T>() {
            @Override
            public boolean hasMoreElements() {
                return iterator.hasNext();
            }

            @Override
            public T nextElement() {
                return iterator.next();
            }
        };
    }

    @Test
    public void checkOrderTest() {

        final String expectedJsonPattern = ".*x-tracee-common.*x-tracee-jaxws.*x-tracee-exception.*";
        NullPointerException npe = createException(new NullPointerException("TEST"));


        String json = TraceeErrorLoggerJsonCreator.createJsonCreator()
                .addCommonCategory()
                .addExceptionCategory(npe)
                .addJaxwsCategory(
                        "req", "res"
                ).toString();

        Pattern pattern = Pattern.compile(expectedJsonPattern);
        Matcher matcher = pattern.matcher(json);
        boolean matches = matcher.matches();

        System.out.println(json);

        assertThat(matches, CoreMatchers.is(true));

    }

    private <T> T createException(T exception) {
        T tmp = null;
        try {
            throw (Throwable) exception;
        } catch (Throwable f) {
            tmp = (T) f;
        }
        return tmp;
    }

}
