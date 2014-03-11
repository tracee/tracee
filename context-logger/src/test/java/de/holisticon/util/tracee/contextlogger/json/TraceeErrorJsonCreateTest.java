package de.holisticon.util.tracee.contextlogger.json;

import com.google.gson.Gson;
import de.holisticon.util.tracee.contextlogger.TraceeContextLoggerConstants;
import de.holisticon.util.tracee.contextlogger.json.beans.CommonCategory;
import de.holisticon.util.tracee.contextlogger.json.beans.TraceeJsonEnvelope;
import de.holisticon.util.tracee.contextlogger.json.generator.TraceeContextLoggerJsonBuilder;
import de.holisticon.util.tracee.contextlogger.json.generator.datawrapper.ServletDataWrapper;
import de.holisticon.util.tracee.contextlogger.presets.Preset;
import org.junit.Ignore;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Tobias Gindler, holisticon AG.
 */
public class TraceeErrorJsonCreateTest {

    @Test
    public void createExceptionJsonTest() {

        final String expectedJsonPattern = "\\{\"x-tracee-exception\":\\{\"message\":\"TEST\",\"stacktrace\":\"java.lang.NullPointerException: TEST.*?\"\\}\\}";

        final NullPointerException npe = createException(new NullPointerException("TEST"));

        final String json = TraceeContextLoggerJsonBuilder.createJsonCreator().addExceptionCategory(npe).toString();

        final Pattern pattern = Pattern.compile(expectedJsonPattern);
        final Matcher matcher = pattern.matcher(json);
        final boolean matches = matcher.matches();

        assertThat(matches, is(true));
    }

    @Test
    public void createCommonJsonTest() {
        System.setProperty(TraceeContextLoggerConstants.SYSTEM_PROPERTY_NAME_SYSTEM, "FE");
        System.setProperty(TraceeContextLoggerConstants.SYSTEM_PROPERTY_NAME_STAGE, "DEV");

        final String json = TraceeContextLoggerJsonBuilder.createJsonCreator().addCommonCategory().toString();

		final CommonCategory jsonObj = new Gson().fromJson(json, TraceeJsonEnvelope.class).getCommon();
		
		assertThat(jsonObj.getSystemName(), is("FE"));
		assertThat(jsonObj.getStage(), is("DEV"));
		assertThat(jsonObj.getThreadId(), is(greaterThan(0L)));
		assertThat(jsonObj.getThreadName(), is(notNullValue()));
		assertThat(jsonObj.getTimestamp(), is(notNullValue()));
    }

    @Test
    public void createJaxwsJsonTest() {

        final String expectedJsonPattern = "\\{\"x-tracee-jaxws\".*req.*res.*\\}\\}";

        System.setProperty(TraceeContextLoggerConstants.SYSTEM_PROPERTY_NAME_SYSTEM, "FE");
        System.setProperty(TraceeContextLoggerConstants.SYSTEM_PROPERTY_NAME_STAGE, "DEV");

        final String json = TraceeContextLoggerJsonBuilder.createJsonCreator().addJaxwsCategory("req", "res").toString();

        final Pattern pattern = Pattern.compile(expectedJsonPattern);
        final Matcher matcher = pattern.matcher(json);
        final boolean matches = matcher.matches();

        assertThat(matches, is(true));
    }

    @Test
    public void createServletJsonTest() {

        System.setProperty(TraceeContextLoggerConstants.SYSTEM_PROPERTY_CONTEXT_LOGGER_PRESET, Preset.FULL.name());
        Preset.reload();

        final String expectedHttpMethod = "POST";
        final String expectedUser = "TEST";
        final String expectedURL = "http://www.test.de:4573/abc/def";
        final String expectedHttpParameterName1 = "P1";
        final String expectedHttpParameterValue1 = "V1";
        final String expectedHttpParameterName2 = "P2";
        final String expectedHttpParameterValue2 = "V2";
        final String expectedRequestAttributeName = "RA1";
        final String expectedRequestAttributeValue = "V1";
        final String[] expectedHttpParameterValues1 = {expectedHttpParameterValue1, expectedHttpParameterValue2};
        final String[] expectedHttpParameterValues2 = {expectedHttpParameterValue2};

        final String expectedRemoteAddress = "1.1.1.1";
        final String expectedRemoteHost = "test.holisticon.de";
        final Integer expectedRemotePort = 1000;

        final String expectedJson = "{\"x-tracee-servlet\":{\"request\":{\"url\":\"http://www.test.de:4573/abc/def\",\"http-method\":\"POST\",\"http-parameters\":[{\"name\":\"P1\",\"value\":\"V1\"},{\"name\":\"P1\",\"value\":\"V2\"},{\"name\":\"P2\",\"value\":\"V2\"}],\"http-request-headers\":[{\"name\":\"P1\",\"value\":\"V1\"},{\"name\":\"P2\",\"value\":\"V2\"}],\"remote-info\":{\"http-remote-address\":\"1.1.1.1\",\"http-remote-host\":\"test.holisticon.de\",\"http-remote-port\":1000},\"enhanced-request-info\":{\"is-secure\":false,\"content-length\":0,\"locale\":\"de_DE\"}},\"response\":{\"http-status-code\":0,\"http-response-headers\":[]},\"session\":{\"session-exists\":false},\"request-attributes\":[{\"name\":\"RA1\",\"value\":\"V1\"}]}}";

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

        final Enumeration<String> expectedParameterNames = toEnumeration(Arrays.asList(expectedHttpParameterName1, expectedHttpParameterName2).iterator());

        final Enumeration<String> expectedHeaderNames = toEnumeration(Arrays.asList(expectedHttpParameterName1, expectedHttpParameterName2).iterator());

        final Enumeration<String> expectedRequestAttributeNames = toEnumeration(Arrays.asList(expectedRequestAttributeName).iterator());

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
        when(httpServletRequestMock.getLocale()).thenReturn(Locale.GERMANY);

        when(httpServletRequestMock.getAttributeNames()).thenReturn(expectedRequestAttributeNames);
        when(httpServletRequestMock.getAttribute(expectedRequestAttributeName)).thenReturn(expectedRequestAttributeValue);

        final String json = TraceeContextLoggerJsonBuilder.createJsonCreator()
                .addServletCategory(ServletDataWrapper.wrap(httpServletRequestMock, httpServletResponsetMock)).toString();

		//assertThat(new Gson().toJsonTree(json), is(new Gson().toJsonTree(expectedJson)));
        assertThat(json, is(expectedJson));

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


	/**
	 * Why do we care about the ordering?
	 */
	@Ignore
    @Test
    public void checkOrderTest() {

        final String expectedJsonPattern = ".*x-tracee-common.*x-tracee-jaxws.*x-tracee-exception.*";
        final NullPointerException npe = createException(new NullPointerException("TEST"));

        final String json = TraceeContextLoggerJsonBuilder.createJsonCreator().addCommonCategory().addExceptionCategory(npe).addJaxwsCategory("req", "res")
                .toString();

        final Pattern pattern = Pattern.compile(expectedJsonPattern);
        final Matcher matcher = pattern.matcher(json);
        final boolean matches = matcher.matches();

        assertThat(matches, is(true));
    }

	@SuppressWarnings("unchecked")
    private <T extends Throwable> T createException(final T exception) {
        try {
            throw exception;
        }
        catch (final Throwable f) {
            return (T) f;
        }
    }
}
