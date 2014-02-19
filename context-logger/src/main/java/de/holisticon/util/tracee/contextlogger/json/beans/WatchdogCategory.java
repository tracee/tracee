package de.holisticon.util.tracee.contextlogger.json.beans;

import de.holisticon.util.tracee.contextlogger.json.beans.servlet.ServletRequestSubCategory;
import de.holisticon.util.tracee.contextlogger.json.beans.servlet.ServletResponseSubCategory;
import de.holisticon.util.tracee.contextlogger.json.beans.servlet.ServletSessionSubCategory;
import de.holisticon.util.tracee.contextlogger.json.beans.values.ServletRequestAttribute;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.List;

/**
 * Category for json output for watchdog context specific data.
 * Created by Tobias Gindler, holisticon AG on 24.01.14.
 */

@JsonAutoDetect(
        getterVisibility = JsonAutoDetect.Visibility.NONE,
        isGetterVisibility = JsonAutoDetect.Visibility.NONE,
        fieldVisibility = JsonAutoDetect.Visibility.ANY
)
@JsonPropertyOrder(
        value = {
                WatchdogCategory.ATTR_CLASS,
                WatchdogCategory.ATTR_METHOD,
                WatchdogCategory.ATTR_PARAMETERS,
                WatchdogCategory.ATTR_DESERIALIZED_INSTANCE
        }
)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class WatchdogCategory {

    public static final String ATTR_CLASS = "class";
    public static final String ATTR_METHOD = "method";
    public static final String ATTR_PARAMETERS = "parameters";
    public static final String ATTR_DESERIALIZED_INSTANCE = "deserialized-instance";

    @JsonProperty(value = WatchdogCategory.ATTR_CLASS)
    private final String clazz;

    @JsonProperty(value = WatchdogCategory.ATTR_METHOD)
    private final String method;

    @JsonProperty(value = WatchdogCategory.ATTR_PARAMETERS)
    private final List<String> parameters;

    @JsonProperty(value = WatchdogCategory.ATTR_DESERIALIZED_INSTANCE)
    private final String deserializedInstance;

    public WatchdogCategory(
            String clazz,
            String method,
            List<String> parameters,
            String deserializedInstance
    ) {

        this.clazz = clazz;
        this.method = method;
        this.parameters = parameters;
        this.deserializedInstance = deserializedInstance;

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
