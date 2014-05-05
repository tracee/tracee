package io.tracee.contextlogger.utility;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Collection;

public class RecursiveReflectionToStringStyle extends ToStringStyle {

    private static final int MAX_DEPTH = 8;
    private static final int DEFAULT_DEPTH = 5;

    private int maxDepth;
    private int depth;

    public RecursiveReflectionToStringStyle() {
        this(DEFAULT_DEPTH);
    }

    public RecursiveReflectionToStringStyle(int maxDepth) {
        setUseShortClassName(true);
        setUseIdentityHashCode(true);

        this.maxDepth = maxDepth > MAX_DEPTH ? MAX_DEPTH : (maxDepth < 0 ? 0 : maxDepth);
    }

    @Override
    protected final void appendDetail(StringBuffer buffer, String fieldName, Object value) {
        if (value.getClass().getName().startsWith("java.lang.")
                || (depth >= maxDepth)) {
            buffer.append(value);
        } else {
            depth++;
            buffer.append(ReflectionToStringBuilder.toString(value, this, true, true));
            depth--;
        }
    }

    @Override
    protected final void appendDetail(StringBuffer buffer, String fieldName, Collection<?> coll) {
        depth++;
        buffer.append(ReflectionToStringBuilder.toString(coll.toArray(), this, true, true));
        depth--;
    }

}
