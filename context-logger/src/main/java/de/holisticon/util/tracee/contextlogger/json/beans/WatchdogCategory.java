package de.holisticon.util.tracee.contextlogger.json.beans;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Category for json output for watchdog context specific data.
 * Created by Tobias Gindler, holisticon AG on 24.01.14.
 */

public class WatchdogCategory {

    public static final String ATTR_ID = "id";
    public static final String ATTR_CLASS = "class";
    public static final String ATTR_METHOD = "method";
    public static final String ATTR_PARAMETERS = "parameters";
    public static final String ATTR_DESERIALIZED_INSTANCE = "deserialized-instance";

    @SerializedName(WatchdogCategory.ATTR_ID)
    private final String id;

    @SerializedName(WatchdogCategory.ATTR_CLASS)
    private final String clazz;

    @SerializedName(WatchdogCategory.ATTR_METHOD)
    private final String method;

    @SerializedName(WatchdogCategory.ATTR_PARAMETERS)
    private final List<String> parameters;

    @SerializedName(WatchdogCategory.ATTR_DESERIALIZED_INSTANCE)
    private final String deserializedInstance;

    public WatchdogCategory(
            String id,
            String clazz,
            String method,
            List<String> parameters,
            String deserializedInstance
    ) {

        this.id = id;
        this.clazz = clazz;
        this.method = method;
        this.parameters = parameters;
        this.deserializedInstance = deserializedInstance;

    }

    @SuppressWarnings("unused")
    public String getId() {
        return id;
    }

    @SuppressWarnings("unused")
    public String getClazz() {
        return clazz;
    }

    @SuppressWarnings("unused")
    public String getMethod() {
        return method;
    }

    @SuppressWarnings("unused")
    public List<String> getParameters() {
        return parameters;
    }

    @SuppressWarnings("unused")
    public String getDeserializedInstance() {
        return deserializedInstance;
    }
}
