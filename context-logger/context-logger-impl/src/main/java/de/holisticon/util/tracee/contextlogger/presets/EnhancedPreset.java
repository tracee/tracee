package de.holisticon.util.tracee.contextlogger.presets;

/**
 * Created by Tobias Gindler (Holisticon AG).
 */
public class EnhancedPreset extends FullPreset {

    /**
     * Empty constructor does nothing.
     */
    EnhancedPreset() {
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



}
