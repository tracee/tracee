package de.holisticon.util.tracee.errorlogger.json.beans;

import de.holisticon.util.tracee.errorlogger.json.beans.values.TraceeContextValue;
import org.codehaus.jackson.annotate.*;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.List;

/**
 * Created by Tobias Gindler, holisticon AG on 19.12.13.
 */
@JsonAutoDetect(
        getterVisibility = JsonAutoDetect.Visibility.NONE,
        isGetterVisibility = JsonAutoDetect.Visibility.NONE,
        fieldVisibility = JsonAutoDetect.Visibility.ANY
)
@JsonPropertyOrder(
        value = {
                TraceeErrorJsonEnvelope.CATEGORY_COMMON,
                TraceeErrorJsonEnvelope.CATEGORY_TRACEE,
                TraceeErrorJsonEnvelope.CATEGORY_SERVLET,
                TraceeErrorJsonEnvelope.CATEGORY_JAXWS,
                TraceeErrorJsonEnvelope.CATEGORY_EXCEPTION
        }
)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public final class TraceeErrorJsonEnvelope {

    public static final String CATEGORY_COMMON = "x-tracee-common";
    public static final String CATEGORY_TRACEE = "x-tracee-tracee";
    public static final String CATEGORY_EXCEPTION = "x-tracee-exception";
    public static final String CATEGORY_JAXWS = "x-tracee-jaxws";
    public static final String CATEGORY_SERVLET = "x-tracee-servlet";


    @JsonProperty(TraceeErrorJsonEnvelope.CATEGORY_COMMON)
    private final CommonCategory common;

    @JsonProperty(TraceeErrorJsonEnvelope.CATEGORY_TRACEE)
    private final List<TraceeContextValue> tracee;

    @JsonProperty(TraceeErrorJsonEnvelope.CATEGORY_EXCEPTION)
    private final ExceptionCategory exception;

    @JsonProperty(TraceeErrorJsonEnvelope.CATEGORY_JAXWS)
    private final JaxWsCategory jaxws;

    @JsonProperty(TraceeErrorJsonEnvelope.CATEGORY_SERVLET)
    private final ServletCategory servlet;

    @SuppressWarnings("unused")
    private TraceeErrorJsonEnvelope() {
        this(null, null, null, null, null);
    }

    public TraceeErrorJsonEnvelope(CommonCategory common,
                                   List<TraceeContextValue> tracee,
                                   ServletCategory servlet,
                                   ExceptionCategory exception,
                                   JaxWsCategory jaxws) {

        this.common = common;
        this.tracee = tracee;
        this.servlet = servlet;
        this.exception = exception;
        this.jaxws = jaxws;
    }

    public CommonCategory getCommon() {
        return common;
    }

    public ExceptionCategory getException() {
        return exception;
    }

    public JaxWsCategory getJaxws() {
        return jaxws;
    }

    public ServletCategory getServlet() {
        return servlet;
    }

    public List<TraceeContextValue> getTracee() {
        return tracee;
    }
}
