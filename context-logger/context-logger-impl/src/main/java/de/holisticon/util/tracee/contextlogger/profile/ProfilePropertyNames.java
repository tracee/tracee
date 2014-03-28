package de.holisticon.util.tracee.contextlogger.profile;

/**
 * This is a class that holds all property names handled used by profiles.
 *
 * Created by Tobias Gindler, holisticon AG on 14.03.14.
 */
public final class ProfilePropertyNames {

    @SuppressWarnings("unused")
    private ProfilePropertyNames() {
        // hide constructor
    }

    // To select property
    public static final Profile DEFAULT_PROFILE = Profile.BASIC;
    public static final String PROFILE_SET_GLOBALLY_VIA_SYSTEM_PROPERTIES = "de.holisticon.util.tracee.contextlogger.profile";
    /** file must contain property */
    public static final String PROFILE_SET_BY_FILE_IN_CLASSPATH_FILENAME = "/ProfileSelector.properties";
    public static final String PROFILE_SET_BY_FILE_IN_CLASSPATH_PROPERTY = PROFILE_SET_GLOBALLY_VIA_SYSTEM_PROPERTIES;

    // Implicit:
    // Tracee - only dynamic context
    public static final String TRACEE = "tracee";

    // Common
    public static final String COMMON_TIMESTAMP = "common.timestamp";
    public static final String COMMON_STAGE = "common.stage";
    public static final String COMMON_SYSTEM_NAME = "common.systemName";
    public static final String COMMON_THREAD_NAME = "common.threadName";
    public static final String COMMON_THREAD_ID = "common.threadId";



    // Explicit:
    // servlet
    public static final String SERVLET_REQUEST_PARAMETERS = "servlet.servletRequest.parameters";
    public static final String SERVLET_REQUEST_URL = "servlet.servletRequest.url";
    public static final String SERVLET_REQUEST_HTTP_METHOD = "servlet.servletRequest.httpMethod";
    public static final String SERVLET_REQUEST_HTTP_HEADER = "servlet.servletRequest.httpHeaders";
    public static final String SERVLET_REQUEST_ATTRIBUTES = "servlet.servletRequest.attributes";
    public static final String SERVLET_REQUEST_COOKIES = "servlet.servletRequest.cookies";

    public static final String SERVLET_REQUEST_SCHEME = "servlet.servletRequest.scheme";
    public static final String SERVLET_REQUEST_IS_SECURE = "servlet.servletRequest.isSecure";
    public static final String SERVLET_REQUEST_CONTENT_TYPE = "servlet.servletRequest.contentType";
    public static final String SERVLET_REQUEST_CONTENT_LENGTH = "servlet.servletRequest.contentLength";
    public static final String SERVLET_REQUEST_LOCALE = "servlet.servletRequest.locale";


    // servlet - remote info
    public static final String SERVLET_REQUEST_REMOTE_ADDRESS = "servlet.servletRequest.remoteAddress";
    public static final String SERVLET_REQUEST_REMOTE_HOST = "servlet.servletRequest.remoteHost";
    public static final String SERVLET_REQUEST_REMOTE_PORT = "servlet.servletRequest.remotePort";

    public static final String SERVLET_RESPONSE_HTTP_STATUS_CODE = "servlet.servletResponse.httpStatusCode";
    public static final String SERVLET_RESPONSE_HTTP_HEADER = "servlet.servletResponse.httpHeader";

    public static final String SERVLET_SESSION = "servlet.session";

    // servlet - cookie
    public static final String COOKIE_NAME = "cookie.name";
    public static final String COOKIE_VALUE = "cookie.value";
    public static final String COOKIE_DOMAIN = "cookie.domain";
    public static final String COOKIE_PATH = "cookie.path";
    public static final String COOKIE_SECURE = "cookie.secure";
    public static final String COOKIE_MAXAGE = "cookie.maxage";

    // Exception
    public static final String EXCEPTION_MESSAGE = "exception.message";
    public static final String EXCEPTION_STACKTRACE = "exception.stacktrace";


    // watchdog
    public static final String WATCHDOG_ID = "watchdog.id";
    public static final String WATCHDOG_ASPECTJ_CONTEXT = "watchdog.aspectj.context";

    // ASPECTJ - ProceedingJoinPoint
    public static final String ASPECTJ_PROCEEDING_JOIN_POINT_CLASS = "aspectj.proceedingJoinPoint.class";
    public static final String ASPECTJ_PROCEEDING_JOIN_POINT_METHOD = "aspectj.proceedingJoinPoint.method";
    public static final String ASPECTJ_PROCEEDING_JOIN_POINT_PARAMETERS = "aspectj.proceedingJoinPoint.parameters";
    public static final String ASPECTJ_PROCEEDING_JOIN_POINT_DESERIALIZED_INSTANCE = "aspectj.proceedingJoinPoint.deserialized-instance";

    // ejb
    public static final String EJB_INVOCATION_CONTEXT_PARAMETERS = "ejb.invocationContext.parameters";
    public static final String EJB_INVOCATION_CONTEXT_METHOD_NAME = "ejb.invocationContext.methodName";
    public static final String EJB_INVOCATION_CONTEXT_TARGET_INSTANCE = "ejb.invocationContext.targetInstance";
    public static final String EJB_INVOCATION_CONTEXT_DATA = "ejb.invocationContext.contextData";

    // jaxws
    public static final String JAXWS_SOAP_REQUEST = "jaxws.soapRequest";
    public static final String JAXWS_SOAP_RESPONSE = "jaxws.soapResponse";


}
