package de.holisticon.util.tracee.contextlogger.presets;

/**
 * Full Preset that enables all output.
 * Should not be used in production environment !
 * Created by Tobias Gindler, holisticon AG on 07.02.14.
 */
public class FullPreset implements PresetConfig{

    /**
     * Empty constructor does nothing.
     */
    FullPreset() {

    }

    @Override
    public boolean showCommon() {
        return true;
    }

    @Override
    public boolean showCommonSystemInfo() {
        return true;
    }

    @Override
    public boolean showCommonThreadInfo() {
        return true;
    }

    @Override
    public boolean showException() {
        return true;
    }

    @Override
    public boolean showJaxWs() {
        return true;
    }

    @Override
    public boolean showJaxWsRequest() {
        return true;
    }

    @Override
    public boolean showJaxWsResponse() {
        return true;
    }

    @Override
    public boolean showTracee() {
        return true;
    }

    @Override
    public boolean showServlet() {
        return true;
    }

    @Override
    public boolean showServletRequest() {
        return true;
    }

    @Override
    public boolean showServletResponse() {
        return true;
    }

    @Override
    public boolean showServletSession() {
        return true;
    }

    @Override
    public boolean showServletRequestAttributes() {
        return true;
    }

    @Override
    public boolean showServletRequestRemoteInfo() {
        return true;
    }

    @Override
    public boolean showServletRequestCookies() {
        return true;
    }

    @Override
    public boolean showServletRequestEnhancedInfo() {
        return true;
    }

    @Override
    public boolean showServletRequestHttpHeaders() {
        return true;
    }

    @Override
    public boolean showServletResponseHttpHeaders() {
        return true;
    }

    @Override
    public boolean showServletSessionAttributes() {
        return true;
    }
}
