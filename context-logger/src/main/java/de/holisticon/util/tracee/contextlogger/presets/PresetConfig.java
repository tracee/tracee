package de.holisticon.util.tracee.contextlogger.presets;

/**
 * Created by Tobias Gindler, holisticon AG on 07.02.14.
 */
public interface PresetConfig {

    // common category
    boolean showCommon();
    boolean showCommonSystemInfo();
    boolean showCommonThreadInfo();

    // exception category
    boolean showException();

    // jaxws category
    boolean showJaxWs();
    boolean showJaxWsRequest();
    boolean showJaxWsResponse();

    // tracee category
    boolean showTracee();


    // servlet categoy
    boolean showServlet();
    boolean showServletRequest();
    boolean showServletResponse();
    boolean showServletSession();
    boolean showServletRequestAttributes();

    // servlet sub-subcategories
    // request
    boolean showServletRequestRemoteInfo();
    boolean showServletRequestCookies();
    boolean showServletRequestEnhancedInfo();
    boolean showServletRequestHttpHeaders();

    // response
    boolean showServletResponseHttpHeaders();

    // session
    boolean showServletSessionAttributes();

}
