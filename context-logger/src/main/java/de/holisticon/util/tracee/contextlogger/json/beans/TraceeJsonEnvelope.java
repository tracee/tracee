package de.holisticon.util.tracee.contextlogger.json.beans;

import de.holisticon.util.tracee.contextlogger.json.beans.values.TraceeContextValue;
import org.codehaus.jackson.annotate.*;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.List;

/**
 * Envelope for Json data.
 * Created by Tobias Gindler, holisticon AG on 19.12.13.
 */
@JsonAutoDetect(
        getterVisibility = JsonAutoDetect.Visibility.NONE,
        isGetterVisibility = JsonAutoDetect.Visibility.NONE,
        fieldVisibility = JsonAutoDetect.Visibility.ANY
)
@JsonPropertyOrder(
        value = {
                TraceeJsonEnvelope.REPORT_TYPE,
                TraceeJsonEnvelope.CATEGORY_COMMON,
                TraceeJsonEnvelope.CATEGORY_TRACEE,
                TraceeJsonEnvelope.CATEGORY_WATCHDOG,
                TraceeJsonEnvelope.CATEGORY_SERVLET,
                TraceeJsonEnvelope.CATEGORY_JAXWS,
                TraceeJsonEnvelope.CATEGORY_EXCEPTION
        }
)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public final class TraceeJsonEnvelope {

    public static final String REPORT_TYPE = "x-tracee-report-type";
    public static final String CATEGORY_COMMON = "x-tracee-common";
    public static final String CATEGORY_TRACEE = "x-tracee-tracee";
    public static final String CATEGORY_EXCEPTION = "x-tracee-exception";
    public static final String CATEGORY_JAXWS = "x-tracee-jaxws";
    public static final String CATEGORY_SERVLET = "x-tracee-servlet";
    public static final String CATEGORY_WATCHDOG = "x-tracee-watchdog";


    @JsonProperty(TraceeJsonEnvelope.REPORT_TYPE)
    private final String reportType;

    @JsonProperty(TraceeJsonEnvelope.CATEGORY_COMMON)
    private final CommonCategory common;

    @JsonProperty(TraceeJsonEnvelope.CATEGORY_TRACEE)
    private final List<TraceeContextValue> tracee;

    @JsonProperty(TraceeJsonEnvelope.CATEGORY_EXCEPTION)
    private final ExceptionCategory exception;

    @JsonProperty(TraceeJsonEnvelope.CATEGORY_JAXWS)
    private final JaxWsCategory jaxws;

    @JsonProperty(TraceeJsonEnvelope.CATEGORY_SERVLET)
    private final ServletCategory servlet;

    @JsonProperty(TraceeJsonEnvelope.CATEGORY_WATCHDOG)
    private final WatchdogCategory watchdog;

    @SuppressWarnings("unused")
    private TraceeJsonEnvelope() {
        this(null, null, null, null, null, null, null);
    }

    public TraceeJsonEnvelope(String reportType,
                              CommonCategory common,
                              List<TraceeContextValue> tracee,
                              ServletCategory servlet,
                              ExceptionCategory exception,
                              JaxWsCategory jaxws,
                              WatchdogCategory watchdog) {
        this.reportType = reportType;
        this.common = common;
        this.tracee = tracee;
        this.servlet = servlet;
        this.exception = exception;
        this.jaxws = jaxws;
        this.watchdog = watchdog;
    }

    @SuppressWarnings("unused")
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

    public String getReportType() {
        return reportType;
    }
}
