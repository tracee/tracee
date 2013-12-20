package de.holisticon.util.tracee.errorlogger.json.beans;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;

import java.util.Date;

/**
 * Created by Tobias Gindler, holisticon AG on 19.12.13.
 */
@JsonAutoDetect(
        getterVisibility = JsonAutoDetect.Visibility.NONE,
        isGetterVisibility = JsonAutoDetect.Visibility.NONE,
        fieldVisibility = JsonAutoDetect.Visibility.ANY
)
@JsonPropertyOrder(value = {CommonCategory.ATTR_SYSTEMNAME,
        CommonCategory.ATTR_STAGE,
        CommonCategory.ATTR_TIMESTAMP,
        CommonCategory.ATTR_THREADNAME}
)
public final class CommonCategory {

    public static final String ATTR_TIMESTAMP = "timestamp";
    public static final String ATTR_SYSTEMNAME = "system-name";
    public static final String ATTR_STAGE = "stage";
    public static final String ATTR_THREADNAME = "thread-name";
    public static final String ATTR_THREADID = "thread-id";


    @JsonProperty(value = CommonCategory.ATTR_TIMESTAMP)
    private final Date timestamp;
    @JsonProperty(value = CommonCategory.ATTR_SYSTEMNAME)
    private final String systemName;
    @JsonProperty(value = CommonCategory.ATTR_STAGE)
    private final String stage;
    @JsonProperty(value = CommonCategory.ATTR_THREADNAME)
    private final String threadName;
    @JsonProperty(value = CommonCategory.ATTR_THREADID)
    private final Long threadId;

    @SuppressWarnings("unused")
    private CommonCategory() {
        this(null, null, null, null, null);
    }

    public CommonCategory(
            String systemName,
            String stage,
            Date timestamp,
            String threadName,
            Long threadId) {

        this.systemName = systemName;
        this.stage = stage;
        this.timestamp = timestamp;
        this.threadName = threadName;
        this.threadId = threadId;

    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getSystemName() {
        return systemName;
    }

    public String getStage() {
        return stage;
    }

    public String getThreadName() {
        return threadName;
    }

    public Long getThreadId() {
        return threadId;
    }
}
