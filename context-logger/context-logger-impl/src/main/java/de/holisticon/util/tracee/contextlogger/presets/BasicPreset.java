package de.holisticon.util.tracee.contextlogger.presets;

/**
 * Default preset, enables widely used output.
 * Created by Tobias Gindler on 07.02.14.
 */
public class BasicPreset extends FullPreset{

    /**
     * Empty constructor does nothing.
     */
    BasicPreset () {
        super();
    }

    @Override
    public boolean showServletRequestCookies() {
        return false;
    }

    @Override
    public boolean showServletRequestEnhancedInfo() {
        return false;
    }

    @Override
    public boolean showServletRequestRemoteInfo() {
        return false;
    }

    @Override
    public boolean showServletRequestAttributes() {
        return false;
    }

    @Override
    public boolean showServletSession() {
        return false;
    }

    @Override
    public boolean showServletSessionAttributes() {
        return false;
    }

    @Override
    public boolean showServletRequestHttpHeaders() {
        return false;
    }

    @Override
    public boolean showServletResponseHttpHeaders() {
        return false;
    }
}
