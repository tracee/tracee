package de.holisticon.util.tracee.contextlogger.profile;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;

/**
 * This class mixes profile settings with system.property overrides.
 * Created by Tobias Gindler, holisticon AG on 25.03.14.
 */
public class ProfileSettings {

    private Properties profileProperties = null;
    private Map<String, Boolean> manualContextOverrides = null;

    public ProfileSettings(Profile profile, Map<String, Boolean> manualContextOverrides) {

        // if passed profile is null then use default profile
        Profile tmpProfile = profile != null ? profile : Profile.getCurrentProfile();

        try {
            this.profileProperties = tmpProfile.getProperties();
        } catch (IOException e) {
            // shouldn't occur for non CUSTOM profiles
        }

        this.manualContextOverrides = manualContextOverrides;
    }

    public final boolean getPropertyValue(final String propertyKey) {

        if (propertyKey == null) {
            return false;
        }

        // check system property override
        if (manualContextOverrides != null) {
            Boolean manualOverrideCheck = manualContextOverrides.get(propertyKey);
            if (manualOverrideCheck != null) {
                return manualOverrideCheck;
            }
        }

        // check profile properties
        if (profileProperties != null) {
            String value = this.profileProperties.getProperty(propertyKey, "false");
            return Boolean.valueOf(value);
        }

        return false;
    }

}
