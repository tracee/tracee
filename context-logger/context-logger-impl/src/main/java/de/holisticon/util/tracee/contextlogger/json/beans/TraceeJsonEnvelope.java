package de.holisticon.util.tracee.contextlogger.json.beans;

import com.google.gson.annotations.SerializedName;
import de.holisticon.util.tracee.contextlogger.json.beans.values.TraceeContextValue;

import java.util.List;

/**
 * Envelope for Json data.
 * Created by Tobias Gindler, holisticon AG on 19.12.13.
 */
public final class TraceeJsonEnvelope {

    public static final String REPORT_TYPE = "x-tracee-report-type";
    public static final String CATEGORY_COMMON = "x-tracee-common";
    public static final String CATEGORY_TRACEE = "x-tracee-tracee";
    public static final String CATEGORY_EXCEPTION = "x-tracee-exception";
    public static final String CATEGORY_JAXWS = "x-tracee-jaxws";
    public static final String CATEGORY_SERVLET = "x-tracee-servlet";
    public static final String CATEGORY_WATCHDOG = "x-tracee-watchdog";


    @SerializedName(TraceeJsonEnvelope.REPORT_TYPE)
    private final String reportType;

    @SerializedName(TraceeJsonEnvelope.CATEGORY_COMMON)
    private final CommonCategory common;

    @SerializedName(TraceeJsonEnvelope.CATEGORY_TRACEE)
    private final List<TraceeContextValue> tracee;

    @SerializedName(TraceeJsonEnvelope.CATEGORY_EXCEPTION)
    private final ExceptionCategory exception;

    @SerializedName(TraceeJsonEnvelope.CATEGORY_JAXWS)
    private final JaxWsCategory jaxws;

    @SerializedName(TraceeJsonEnvelope.CATEGORY_SERVLET)
    private final ServletCategory servlet;

    @SerializedName(TraceeJsonEnvelope.CATEGORY_WATCHDOG)
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
