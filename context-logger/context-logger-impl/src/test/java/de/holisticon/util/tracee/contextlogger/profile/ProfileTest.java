package de.holisticon.util.tracee.contextlogger.profile;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.internal.MockitoCore;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Properties;

/**
 * Test class for {@link de.holisticon.util.tracee.contextlogger.profile.Profile}.
 * Created by Tobias Gindler, holisticon AG on 14.03.14.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(Profile.class)
public class ProfileTest {

    public static final String PROFILE_SELECTOR_PROPERTIES = "ProfileSelector.properties";

    public static final String TEST_PROPERTY_FILENAME = "/test.property";
    public static final String TEST_PROPERTY_PROP1_KEY = "test_key";
    public static final String TEST_PROPERTY_PROP1_VALUE = "test_value";

    // CUSTOM PROPERTY FILES
    public static final String TEST_PROPERTY_INVALID_CUSTOM_PROPERTIES = "/invalid_ProfileSelector.properties";
    public static final String TEST_PROPERTY_VALID_CUSTOM_PROPERTIES = "/valid_ProfileSelector.properties";


    @Test
    public void openProperties_should_open_properties () throws IOException{

        Properties properties = Profile.openProperties(TEST_PROPERTY_FILENAME);

        MatcherAssert.assertThat(properties, Matchers.notNullValue());
        MatcherAssert.assertThat((String)properties.get(TEST_PROPERTY_PROP1_KEY), Matchers.equalTo(TEST_PROPERTY_PROP1_VALUE));

    }

    @Test
    public void openProperties_should_return_null_if_properties_cant_be_found () throws IOException{

        Properties properties = Profile.openProperties("NON_EXISTENT_FILE");
        MatcherAssert.assertThat(properties, Matchers.nullValue());

    }

    @Test
    public void openProperties_should_return_null_if_properties_is_set_to_null () throws IOException{

        Properties properties = Profile.openProperties(null);
        MatcherAssert.assertThat(properties, Matchers.nullValue());

    }

    @Test
    public void getProfileFromSystemProperties_should_return_null_for_missing_system_property () {
        System.getProperties().remove(ProfilePropertyNames.PROFILE_SET_GLOBALLY_VIA_SYSTEM_PROPERTIES);
        Profile profile = Profile.getProfileFromSystemProperties();

        MatcherAssert.assertThat(profile, Matchers.nullValue());
    }

    @Test
    public void getProfileFromSystemProperties_should_return_null_for_invalid_system_property () {
        System.getProperties().setProperty(ProfilePropertyNames.PROFILE_SET_GLOBALLY_VIA_SYSTEM_PROPERTIES, "INVALID");
        Profile profile = Profile.getProfileFromSystemProperties();

        MatcherAssert.assertThat(profile, Matchers.nullValue());
    }

    @Test
    public void getProfileFromSystemProperties_should_return_basic_profile_for_system_property () {
        System.getProperties().setProperty(ProfilePropertyNames.PROFILE_SET_GLOBALLY_VIA_SYSTEM_PROPERTIES, Profile.BASIC.name());
        Profile profile = Profile.getProfileFromSystemProperties();

        MatcherAssert.assertThat(profile, Matchers.equalTo(Profile.BASIC));

    }

    @Test
    public void getProfileFromSystemProperties_should_return_enhanced_profile_for_system_property () {
        System.getProperties().setProperty(ProfilePropertyNames.PROFILE_SET_GLOBALLY_VIA_SYSTEM_PROPERTIES, Profile.ENHANCED.name());
        Profile profile = Profile.getProfileFromSystemProperties();

        MatcherAssert.assertThat(profile, Matchers.equalTo(Profile.ENHANCED));

    }

    @Test
    public void getProfileFromSystemProperties_should_return_full_profile_for_system_property () {
        System.getProperties().setProperty(ProfilePropertyNames.PROFILE_SET_GLOBALLY_VIA_SYSTEM_PROPERTIES, Profile.FULL.name());
        Profile profile = Profile.getProfileFromSystemProperties();

        MatcherAssert.assertThat(profile, Matchers.equalTo(Profile.FULL));

    }

    @Test
    public void getProfileFromSystemProperties_should_return_custom_profile_for_system_property () {
        System.getProperties().setProperty(ProfilePropertyNames.PROFILE_SET_GLOBALLY_VIA_SYSTEM_PROPERTIES, Profile.CUSTOM.name());
        Profile profile = Profile.getProfileFromSystemProperties();

        MatcherAssert.assertThat(profile, Matchers.equalTo(Profile.CUSTOM));

    }

    @Test
    public void checkProfilePropertyFileExists_should_return_false_for_missing_profile_property_file () {

        // CUSTOM property file doesn't exists
        boolean customPropertyFileExists = Profile.checkProfilePropertyFileExists(Profile.CUSTOM);
        MatcherAssert.assertThat(customPropertyFileExists, Matchers.equalTo(false));

    }

    @Test
    public void checkProfilePropertyFileExists_should_return_true_for_existing_profile_property_file () throws IOException {

        // BASIC property file exists per default
        boolean customPropertyFileExists = Profile.checkProfilePropertyFileExists(Profile.BASIC);
        MatcherAssert.assertThat(customPropertyFileExists, Matchers.equalTo(true));

    }

    @Test
    public void getProfileFromFileInClasspath_should_return_null_for_nonexisting_property_file () {

        Profile profile = Profile.getProfileFromFileInClasspath("NON_EXISTING_FILE");
        MatcherAssert.assertThat(profile, Matchers.nullValue());

    }

    @Test
    public void getProfileFromFileInClasspath_should_return_null_for_existing_and_invalid_property_file () {

        Profile profile = Profile.getProfileFromFileInClasspath(TEST_PROPERTY_INVALID_CUSTOM_PROPERTIES);
        MatcherAssert.assertThat(profile, Matchers.nullValue());

    }

    @Test
    public void getProfileFromFileInClasspath_should_return_profile_for_existing_and_valid_property_file () {

        Profile profile = Profile.getProfileFromFileInClasspath(TEST_PROPERTY_VALID_CUSTOM_PROPERTIES);
        MatcherAssert.assertThat(profile, Matchers.notNullValue());

    }

    @Test
    public void getCurrentProfile_should_return_default_profile_if_nothing_is_defined () {
        System.getProperties().remove(ProfilePropertyNames.PROFILE_SET_GLOBALLY_VIA_SYSTEM_PROPERTIES);
        Profile profile = Profile.getCurrentProfile();

        MatcherAssert.assertThat(profile, Matchers.notNullValue());
        MatcherAssert.assertThat(profile, Matchers.equalTo(ProfilePropertyNames.DEFAULT_PROFILE));
    }

    @Test
    public void getCurrentProfile_should_return_enhanced_profile_if_defined_via_system_properties () {
        System.getProperties().put(ProfilePropertyNames.PROFILE_SET_GLOBALLY_VIA_SYSTEM_PROPERTIES, Profile.ENHANCED.name());
        Profile profile = Profile.getCurrentProfile();

        MatcherAssert.assertThat(profile, Matchers.notNullValue());
        MatcherAssert.assertThat(profile, Matchers.equalTo(Profile.ENHANCED));
    }

    @Test
    public void getCurrentProfile_should_return_enhanced_profile_if_defined_via_selector_property_file () {

        PowerMockito.stub(PowerMockito.method(Profile.class, "getProfileFromSystemProperties")).toReturn(null);
        PowerMockito.stub(PowerMockito.method(Profile.class, "getProfileFromFileInClasspath")).toReturn(Profile.FULL);

        System.getProperties().put(ProfilePropertyNames.PROFILE_SET_GLOBALLY_VIA_SYSTEM_PROPERTIES, Profile.ENHANCED.name());
        Profile profile = Profile.getCurrentProfile();

        MatcherAssert.assertThat(profile, Matchers.notNullValue());
        MatcherAssert.assertThat(profile, Matchers.equalTo(Profile.FULL));
    }

}
