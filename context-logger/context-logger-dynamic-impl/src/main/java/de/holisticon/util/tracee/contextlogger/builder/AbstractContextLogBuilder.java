package de.holisticon.util.tracee.contextlogger.builder;

import de.holisticon.util.tracee.contextlogger.api.TraceeContextLogBuilder;
import de.holisticon.util.tracee.contextlogger.profile.Profile;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Abstract base class for all context toJson builder implementations.
 * Created by Tobias Gindler, holisticon AG on 20.03.14.
 */
public abstract class AbstractContextLogBuilder implements TraceeContextLogBuilder{

    private Set<Class> wrapperClasses;
    private Profile profile;
    private Map<String,Boolean> manualContextOverrides;

    @Override
    public Set<Class> getWrapperClasses() {
        return wrapperClasses;
    }

    @Override
    public void setWrapperClasses(Set<Class> wrapperClasses) {
        this.wrapperClasses = wrapperClasses;
    }

    public void setProfile(final Profile profile) {
        this.profile = profile;
    }

    public Profile getProfile(){
        return this.profile;
    }

    public void setManualContextOverrides(final Map<String,Boolean> manualContextOverrides) {
        this.manualContextOverrides = manualContextOverrides;
    }

    public Map<String, Boolean> getManualContextOverrides () {
        return manualContextOverrides != null ? manualContextOverrides : new HashMap<String, Boolean>();
    }

}
