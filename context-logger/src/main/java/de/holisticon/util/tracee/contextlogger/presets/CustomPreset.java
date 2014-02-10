package de.holisticon.util.tracee.contextlogger.presets;

import de.holisticon.util.tracee.Tracee;
import de.holisticon.util.tracee.TraceeLogger;

/**
 * Wrapper class for custom preset defined via system property.
 * If class cannot be found, then BasicPreset will be used.
 * Created by Tobias Gindler, holisticon AG on 07.02.14.
 */
public class CustomPreset implements PresetConfig{

    private static final TraceeLogger LOGGER = Tracee.getBackend().getLoggerFactory().getLogger(Preset.class);

    private final PresetConfig preset;

    CustomPreset(boolean useCustomPreset, String customPresetClassName) {

        PresetConfig tmpPreset = null;
        if (useCustomPreset && customPresetClassName != null) {
            try {
                tmpPreset = (PresetConfig) Class.forName(customPresetClassName).newInstance();
            } catch (Exception e) {
                // log warning that class cannot be found
                LOGGER.warn("couldn't create instance with classname '" + customPresetClassName + "' for custom preset.");
            }
        }

        // fallback to basic preset
        if (tmpPreset == null) {
            LOGGER.warn("CUSTOM preset was defined, but must fallback to BASIC preset because of previous errors.");
            this.preset = new BasicPreset();
        } else {
            this.preset = tmpPreset;
        }

    }

    /**
     * Gets the wrapped presetConfig instance.
     * @return the wrapped preset instance
     */
    public PresetConfig getWrappedPresetConfig () {
        return preset;
    }

    @Override
    public boolean showCommon() {
        return this.preset.showCommon();
    }

    @Override
    public boolean showCommonSystemInfo() {
        return this.preset.showCommonSystemInfo();
    }

    @Override
    public boolean showCommonThreadInfo() {
        return this.preset.showCommonThreadInfo();
    }

    @Override
    public boolean showException() {
        return this.preset.showException();
    }

    @Override
    public boolean showJaxWs() {
        return this.preset.showJaxWs();
    }

    @Override
    public boolean showJaxWsRequest() {
        return this.preset.showJaxWsRequest();
    }

    @Override
    public boolean showJaxWsResponse() {
        return this.preset.showJaxWsResponse();
    }

    @Override
    public boolean showTracee() {
        return this.preset.showTracee();
    }

    @Override
    public boolean showServlet() {
        return this.preset.showServlet();
    }

    @Override
    public boolean showServletRequest() {
        return this.preset.showServletRequest();
    }

    @Override
    public boolean showServletResponse() {
        return this.preset.showServletResponse();
    }

    @Override
    public boolean showServletSession() {
        return this.preset.showServletSession();
    }

    @Override
    public boolean showServletRequestAttributes() {
        return this.preset.showServletRequestAttributes();
    }

    @Override
    public boolean showServletRequestRemoteInfo() {
        return this.preset.showServletRequestRemoteInfo();
    }

    @Override
    public boolean showServletRequestCookies() {
        return this.preset.showServletRequestCookies();
    }

    @Override
    public boolean showServletRequestEnhancedInfo() {
        return this.preset.showServletRequestEnhancedInfo();
    }

    @Override
    public boolean showServletRequestHttpHeaders() {
        return this.preset.showServletRequestHttpHeaders();
    }

    @Override
    public boolean showServletResponseHttpHeaders() {
        return this.preset.showServletResponseHttpHeaders();
    }

    @Override
    public boolean showServletSessionAttributes() {
        return this.preset.showServletSessionAttributes();
    }
}
