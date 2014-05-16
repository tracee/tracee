package io.tracee.contextlogger;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Common Constants used by Tracee Context logging.
 * Created by Tobias Gindler, holisticon AG on 16.12.13.
 */
public final class TraceeContextLoggerConstants {

    @SuppressWarnings("unused")
    private TraceeContextLoggerConstants() {
        // hide constructor
    }

    public static final Set<Class> IGNORED_AT_DESERIALIZATION;

    static {

        Set<Class> tmpIgnoredAtDeserialization = new HashSet<Class>();

        tmpIgnoredAtDeserialization.add(String.class);
        tmpIgnoredAtDeserialization.add(Boolean.class);
        tmpIgnoredAtDeserialization.add(Integer.class);
        tmpIgnoredAtDeserialization.add(Double.class);
        tmpIgnoredAtDeserialization.add(Long.class);
        tmpIgnoredAtDeserialization.add(Float.class);
        tmpIgnoredAtDeserialization.add(Byte.class);

        IGNORED_AT_DESERIALIZATION = Collections.unmodifiableSet(tmpIgnoredAtDeserialization);
    }

    public static final String WRAPPER_CONTEXT_PROVIDER_INTERNAL_RESOURCE_URL = "/io.tracee.contextlogger.internal.wrappercontextproviders";
    public static final String WRAPPER_CONTEXT_PROVIDER_CUSTOM_RESOURCE_URL = "/io.tracee.contextlogger.custom.wrappercontextproviders";

    public static final String IMPLICIT_CONTEXT_PROVIDER_CLASS_INTERNAL_RESOURCE_URL = "/io.tracee.contextlogger.internal.implicitcontextproviders";
    public static final String IMPLICIT_CONTEXT_PROVIDER_CLASS_CUSTOM_RESOURCE_URL = "/io.tracee.contextlogger.custom.implicitcontextproviders";

    public static final String SYSTEM_PROPERTY_PREFIX = "io.tracee.contextlogger.";
    public static final String SYSTEM_PROPERTY_CONNECTOR_PREFIX = SYSTEM_PROPERTY_PREFIX + "connector.";

    public static final String SYSTEM_PROPERTY_NAME_STAGE = SYSTEM_PROPERTY_PREFIX + "tracee-standard-stage";
    public static final String SYSTEM_PROPERTY_NAME_SYSTEM = SYSTEM_PROPERTY_PREFIX + "tracee-standard-system";

    public static final String SYSTEM_PROPERTY_CONTEXT_LOGGER_PRESET = SYSTEM_PROPERTY_PREFIX + "preset";
    public static final String SYSTEM_PROPERTY_CONTEXT_LOGGER_PRESET_CLASS = SYSTEM_PROPERTY_PREFIX + "preset.class";

    public static final String SYSTEM_PROPERTY_CONTEXT_LOGGER_CONNECTOR_TYPE = "class";
    public static final String SYSTEM_PROPERTY_CONTEXT_LOGGER_CONNECTOR_KEY_PATTERN = "io\\.tracee\\.contextlogger\\.connector\\.(\\w*?)\\."
			+ SYSTEM_PROPERTY_CONTEXT_LOGGER_CONNECTOR_TYPE;


}
