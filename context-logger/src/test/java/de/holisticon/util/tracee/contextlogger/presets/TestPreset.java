package de.holisticon.util.tracee.contextlogger.presets;

/**
 * Created by Tobias Gindler, holisticon AG on 07.02.14.
 */
public class TestPreset implements PresetConfig{

    @Override
    public boolean showCommon() {
        return false;
    }

    @Override
    public boolean showCommonSystemInfo() {
        return false;
    }

    @Override
    public boolean showCommonThreadInfo() {
        return false;
    }

    @Override
    public boolean showException() {
        return false;
    }

    @Override
    public boolean showJaxWs() {
        return false;
    }

    @Override
    public boolean showJaxWsRequest() {
        return false;
    }

    @Override
    public boolean showJaxWsResponse() {
        return false;
    }

    @Override
    public boolean showTracee() {
        return false;
    }

    @Override
    public boolean showServlet() {
        return false;
    }

    @Override
    public boolean showServletRequest() {
        return false;
    }

    @Override
    public boolean showServletResponse() {
        return false;
    }

    @Override
    public boolean showServletSession() {
        return false;
    }

    @Override
    public boolean showServletRequestAttributes() {
        return false;
    }



    @Override
    public boolean showServletRequestRemoteInfo() {
        return false;
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
    public boolean showServletRequestHttpHeaders() {
        return false;
    }

    @Override
    public boolean showServletResponseHttpHeaders() {
        return false;
    }

    @Override
    public boolean showServletSessionAttributes() {
        return false;
    }
}
