package de.holisticon.util.tracee.contextlogger.json.beans;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Category for json output for common context specific data like system name, stage and timestamp.
 * Created by Tobias Gindler, holisticon AG on 19.12.13.
 */
public final class CommonCategory {

    public static final String ATTR_TIMESTAMP = "timestamp";
    public static final String ATTR_SYSTEMNAME = "system-name";
    public static final String ATTR_STAGE = "stage";
    public static final String ATTR_THREADNAME = "thread-name";
    public static final String ATTR_THREADID = "thread-id";


    @SerializedName(CommonCategory.ATTR_TIMESTAMP)
    private final Date timestamp;
    @SerializedName(CommonCategory.ATTR_SYSTEMNAME)
    private final String systemName;
    @SerializedName(CommonCategory.ATTR_STAGE)
    private final String stage;
    @SerializedName(CommonCategory.ATTR_THREADNAME)
    private final String threadName;
    @SerializedName(CommonCategory.ATTR_THREADID)
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

    @SuppressWarnings("unused")
    public Date getTimestamp() {
        return timestamp;
    }

    @SuppressWarnings("unused")
    public String getSystemName() {
        return systemName;
    }

    @SuppressWarnings("unused")
    public String getStage() {
        return stage;
    }

    @SuppressWarnings("unused")
    public String getThreadName() {
        return threadName;
    }

    @SuppressWarnings("unused")
    public Long getThreadId() {
        return threadId;
    }

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		CommonCategory that = (CommonCategory) o;

		if (stage != null ? !stage.equals(that.stage) : that.stage != null) return false;
		if (systemName != null ? !systemName.equals(that.systemName) : that.systemName != null) return false;
		if (threadId != null ? !threadId.equals(that.threadId) : that.threadId != null) return false;
		if (threadName != null ? !threadName.equals(that.threadName) : that.threadName != null) return false;
		if (timestamp != null ? !timestamp.equals(that.timestamp) : that.timestamp != null) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = timestamp != null ? timestamp.hashCode() : 0;
		result = 31 * result + (systemName != null ? systemName.hashCode() : 0);
		result = 31 * result + (stage != null ? stage.hashCode() : 0);
		result = 31 * result + (threadName != null ? threadName.hashCode() : 0);
		result = 31 * result + (threadId != null ? threadId.hashCode() : 0);
		return result;
	}
}
