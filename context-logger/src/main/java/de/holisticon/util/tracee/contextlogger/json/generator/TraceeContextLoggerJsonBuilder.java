package de.holisticon.util.tracee.contextlogger.json.generator;

import com.google.gson.GsonBuilder;
import de.holisticon.util.tracee.TraceeBackend;
import de.holisticon.util.tracee.contextlogger.connector.PrintableByConnector;
import de.holisticon.util.tracee.contextlogger.json.beans.CommonCategory;
import de.holisticon.util.tracee.contextlogger.json.beans.ExceptionCategory;
import de.holisticon.util.tracee.contextlogger.json.beans.JaxWsCategory;
import de.holisticon.util.tracee.contextlogger.json.beans.ServletCategory;
import de.holisticon.util.tracee.contextlogger.json.beans.TraceeJsonEnvelope;
import de.holisticon.util.tracee.contextlogger.json.beans.WatchdogCategory;
import de.holisticon.util.tracee.contextlogger.json.beans.values.TraceeContextValue;
import de.holisticon.util.tracee.contextlogger.json.generator.datawrapper.ServletDataWrapper;
import de.holisticon.util.tracee.contextlogger.json.generator.datawrapper.WatchdogDataWrapper;
import de.holisticon.util.tracee.contextlogger.presets.Preset;

import java.util.List;

/**
 * Creator class to generate context json via a fluent api.
 * Created by Tobias Gindler, holisticon AG on 17.12.13.
 */
public class TraceeContextLoggerJsonBuilder implements PrintableByConnector{

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

    private TraceeContextLoggerJsonBuilder() {
    }

    public static TraceeContextLoggerJsonBuilder createJsonCreator() {
        return new TraceeContextLoggerJsonBuilder();
    }

    public final TraceeContextLoggerJsonBuilder addCommonCategory() {
        this.categoryCommon = CommonCategoryCreator.createCommonCategory();
        return this;
    }

    public final TraceeContextLoggerJsonBuilder addExceptionCategory(final Throwable throwable) {
        this.categoryException = ExceptionCategoryCreator.createExceptionCategory(throwable);
        return this;
    }

    public final TraceeContextLoggerJsonBuilder addJaxwsCategory(final String soapRequest, final String soapResponse) {
        this.categoryJaxws = JaxWsCategoryCreator.createJaxWsCategory(soapRequest, soapResponse);
        return this;
    }

    public final TraceeContextLoggerJsonBuilder addServletCategory(final ServletDataWrapper servletDataWrapper) {
        this.categoryServlet = ServletCategoryCreator.createServletCategory(servletDataWrapper);
        return this;
    }

    public final TraceeContextLoggerJsonBuilder addTraceeCategory(final TraceeBackend traceeBackend) {
        this.categoryTracee = TraceeCategoryCreator.createTraceeCategory(traceeBackend);
        return this;
    }

    public final TraceeContextLoggerJsonBuilder addPrefixedMessage(final String prefixedMessage) {
        this.prefixedMessage = prefixedMessage;
        return this;
    }

    public final TraceeContextLoggerJsonBuilder addWatchdogCategory(final WatchdogDataWrapper watchdogData) {
        this.watchdogCategory = WatchdogCategoryCreator.createWatchdogCategory(watchdogData);
        return this;
    }

    public final TraceeContextLoggerJsonBuilder addReportType (final TYPE type) {
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

    private String createJson(boolean withPrefix) {

        final TraceeJsonEnvelope envelope = new TraceeJsonEnvelope(reportType, Preset.getPreset().getPresetConfig().showCommon() ? categoryCommon : null, Preset
                .getPreset().getPresetConfig().showTracee() ? categoryTracee : null, Preset.getPreset().getPresetConfig().showServlet() ? categoryServlet
                : null, Preset.getPreset().getPresetConfig().showException() ? categoryException : null, Preset.getPreset().getPresetConfig().showJaxWs()
                ? categoryJaxws : null, watchdogCategory );

		return new GsonBuilder().create().toJson(envelope);
    }

}
