package de.holisticon.util.tracee.contextlogger.utility;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Collection;

/**
 * Created by Tobias Gindler, holisticon ag on 20.02.14.
 */
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
    protected void appendDetail(StringBuffer buffer, String fieldName, Object value) {
        if (value.getClass().getName().startsWith("java.lang.")
                || (depth >= maxDepth)) {
            buffer.append(value);
        } else {
            depth++;
            buffer.append(ReflectionToStringBuilder.toString(value, this, true, true));
            depth--;
        }
    }

    // another helpful method
    @Override
    protected void appendDetail(StringBuffer buffer, String fieldName, Collection<?> coll) {
        depth++;
        buffer.append(ReflectionToStringBuilder.toString(coll.toArray(), this, true, true));
        depth--;
    }

}
