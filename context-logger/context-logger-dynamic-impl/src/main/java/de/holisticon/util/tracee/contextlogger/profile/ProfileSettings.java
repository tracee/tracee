package de.holisticon.util.tracee.contextlogger.profile;

import java.io.IOException;
import java.util.Properties;

/**
 * This class mixes profile settings with system.property overrides.
 * Created by Tobias Gindler, holisticon AG on 25.03.14.
 */
public class ProfileSettings {

    private Properties profileProperties = null;

    public ProfileSettings() {
        this(Profile.getCurrentProfile());
    }

    public ProfileSettings(Profile profile) {
        try {
            this.profileProperties = profile.getProperties();
        } catch (IOException e) {
            // shouldn't occur for non CUSTOM profiles
        }

    }

    public boolean getPropertyValue (final String propertyKey) {

        if (propertyKey == null) {
            return false;
        }

        // check system property override

        // check profile properties
        String value = this.profileProperties.getProperty(propertyKey,"false");
        return Boolean.valueOf(value);

    }

}
