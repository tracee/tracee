package de.holisticon.util.tracee.contextlogger.profile;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Test class for {@link de.holisticon.util.tracee.contextlogger.profile.ProfileSettings}
 * Created by Tobias Gindler, holisticon AG on 25.03.14.
 */
public class ProfileSettingsTest {


    @Test
    public void should_return_false_for_servlet_session_in_basic_profile () {


        ProfileSettings profileSettings = new ProfileSettings(Profile.BASIC);
        boolean shouldBeFalse = profileSettings.getPropertyValue(ProfilePropertyNames.SERVLET_SESSION);

        MatcherAssert.assertThat(shouldBeFalse, Matchers.equalTo(false));


    }

    @Test
    public void should_return_true_for_servlet_request_parameters_in_basic_profile () {


        ProfileSettings profileSettings = new ProfileSettings(Profile.BASIC);
        boolean shouldBeFalse = profileSettings.getPropertyValue(ProfilePropertyNames.SERVLET_REQUEST_PARAMETERS);

        MatcherAssert.assertThat(shouldBeFalse, Matchers.equalTo(true));


    }

    @Test
    public void should_return_false_for_unknown_property_key_in_basic_profile () {


        ProfileSettings profileSettings = new ProfileSettings(Profile.BASIC);
        boolean shouldBeFalse = profileSettings.getPropertyValue("UNKNOWN");

        MatcherAssert.assertThat(shouldBeFalse, Matchers.equalTo(false));


    }

    @Test
    public void should_return_false_for_null_valued_property_key_in_basic_profile () {


        ProfileSettings profileSettings = new ProfileSettings(Profile.BASIC);
        boolean shouldBeFalse = profileSettings.getPropertyValue(null);

        MatcherAssert.assertThat(shouldBeFalse, Matchers.equalTo(false));


    }

}
