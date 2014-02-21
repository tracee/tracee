package de.holisticon.util.tracee.contextlogger.json.generator;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

import de.holisticon.util.tracee.contextlogger.connector.PrintableByConnector;
import de.holisticon.util.tracee.contextlogger.json.beans.*;
import de.holisticon.util.tracee.contextlogger.json.generator.datawrapper.WatchdogDataWrapper;
import org.codehaus.jackson.map.ObjectMapper;

import de.holisticon.util.tracee.TraceeBackend;
import de.holisticon.util.tracee.contextlogger.json.beans.values.TraceeContextValue;
import de.holisticon.util.tracee.contextlogger.json.generator.datawrapper.ServletDataWrapper;
import de.holisticon.util.tracee.contextlogger.presets.Preset;

/**
 * Creator class to generate context json via a fluent api.
 * Created by Tobias Gindler, holisticon AG on 17.12.13.
 */
public class TraceeContextLoggerJsonCreator implements PrintableByConnector{

    public enum TYPE {
        ENDPOINT,
        WATCHDOG
    }


    private CommonCategory categoryCommon = null;
    private ServletCategory categoryServlet = null;
    private ExceptionCategory categoryException = null;
    private JaxWsCategory categoryJaxws = null;
    private List<TraceeContextValue> categoryTracee = null;
    private String prefixedMessage = null;
    private WatchdogCategory watchdogCategory = null;
    private String reportType = null;

    private TraceeContextLoggerJsonCreator() {
    }

    public static TraceeContextLoggerJsonCreator createJsonCreator() {
        return new TraceeContextLoggerJsonCreator();
    }

    public final TraceeContextLoggerJsonCreator addCommonCategory() {
        this.categoryCommon = CommonCategoryCreator.createCommonCategory();
        return this;
    }

    public final TraceeContextLoggerJsonCreator addExceptionCategory(final Throwable throwable) {
        this.categoryException = ExceptionCategoryCreator.createExceptionCategory(throwable);
        return this;
    }

    public final TraceeContextLoggerJsonCreator addJaxwsCategory(final String soapRequest, final String soapResponse) {
        this.categoryJaxws = JaxWsCategoryCreator.createJaxWsCategory(soapRequest, soapResponse);
        return this;
    }

    public final TraceeContextLoggerJsonCreator addServletCategory(final ServletDataWrapper servletDataWrapper) {
        this.categoryServlet = ServletCategoryCreator.createServletCategory(servletDataWrapper);
        return this;
    }

    public final TraceeContextLoggerJsonCreator addTraceeCategory(final TraceeBackend traceeBackend) {
        this.categoryTracee = TraceeCategoryCreator.createTraceeCategory(traceeBackend);
        return this;
    }

    public final TraceeContextLoggerJsonCreator addPrefixedMessage(final String prefixedMessage) {
        this.prefixedMessage = prefixedMessage;
        return this;
    }

    public final TraceeContextLoggerJsonCreator addWatchdogCategory(final WatchdogDataWrapper watchdogData) {
        this.watchdogCategory = WatchdogCategoryCreator.createWatchdogCategory(watchdogData);
        return this;
    }

    public final TraceeContextLoggerJsonCreator addReportType (final TYPE type) {
        this.reportType = type != null ? type.name() : null;
        return this;
    }

    @Override
    public String getPrefix() {
        return this.prefixedMessage;
    }

    @Override
    public final String toString() {
        return this.createJson(false);
    }

    public final String toStringWithPrefix() {
        return this.createJson(true);
    }

    private String createJson(boolean withPrefix) {

        final TraceeJsonEnvelope envelope = new TraceeJsonEnvelope(reportType, Preset.getPreset().getPresetConfig().showCommon() ? categoryCommon : null, Preset
                .getPreset().getPresetConfig().showTracee() ? categoryTracee : null, Preset.getPreset().getPresetConfig().showServlet() ? categoryServlet
                : null, Preset.getPreset().getPresetConfig().showException() ? categoryException : null, Preset.getPreset().getPresetConfig().showJaxWs()
                ? categoryJaxws : null, watchdogCategory );

        final StringWriter stringWriter = new StringWriter();
        if (withPrefix && this.prefixedMessage != null) {
            stringWriter.append(this.prefixedMessage).append(" - ");
        }

        try {

            final ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(stringWriter, envelope);

            return stringWriter.toString();

        }
        catch (final IOException e) {
            throw new RuntimeException("Couldn't create JSON for error output", e);
        }

    }

}
