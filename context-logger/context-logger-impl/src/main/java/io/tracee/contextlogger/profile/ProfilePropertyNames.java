package io.tracee.contextlogger.profile;

/**
 * This is a class that holds all property names handled used by profiles.
 * <p/>
 * Created by Tobias Gindler, holisticon AG on 14.03.14.
 */
public final class ProfilePropertyNames {

    @SuppressWarnings("unused")
    private ProfilePropertyNames() {
        // hide constructor
    }

    // To select property
    public static final Profile DEFAULT_PROFILE = Profile.BASIC;
    public static final String PROFILE_SET_GLOBALLY_VIA_SYSTEM_PROPERTIES = "io.tracee.contextlogger.profile";
    /* file must contain property */
    public static final String PROFILE_SET_BY_FILE_IN_CLASSPATH_FILENAME = "/ProfileSelector.properties";
    public static final String PROFILE_SET_BY_FILE_IN_CLASSPATH_PROPERTY = PROFILE_SET_GLOBALLY_VIA_SYSTEM_PROPERTIES;


}
