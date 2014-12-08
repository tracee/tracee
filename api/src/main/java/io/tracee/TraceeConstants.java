package io.tracee;

public final class TraceeConstants {

    private TraceeConstants() {
    }

	// TPIC = TracEE propagated invocation context
    public static final String HTTP_HEADER_NAME = "TPIC";
    public static final String JMS_HEADER_NAME = "TPIC";

    public static final String SESSION_ID_KEY = "traceeSessionId";
    public static final String REQUEST_ID_KEY = "traceeRequestId";

	public static final String PROFILE_HIDE_INBOUND = "HideInbound";
	public static final String PROFILE_HIDE_OUTBOUND = "HideOutbound";

}
