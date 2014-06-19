package io.tracee.contextlogger.api.internal;

import io.tracee.contextlogger.api.ConfigBuilder;
import io.tracee.contextlogger.profile.Profile;

import java.util.Map;

/**
 * Interface for getting configuration from ConfigBuilder.
 * Created by Tobias Gindler on 19.06.14.
 */
public interface Configuration extends ConfigBuilder{

    /**
     * Gets a map containing all manual context overrides.
     * @return a map containing all manual context overrides
     */
    Map<String, Boolean> getManualContextOverrides();

    /**
     * Gets the profile.
     * @return the profile
     */
    Profile getProfile();

    /**
     * Gets if the order must be kept
     * @return
     */
    boolean getKeepOrder();


}
