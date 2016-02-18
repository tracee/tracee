package io.tracee;

import java.util.HashSet;
import java.util.Set;

/**
 * A thread local hash-set that will be copied to a child thread upon creation.
 */
public final class ThreadLocalHashSet<T> extends InheritableThreadLocal<Set<T>> {


    @Override
    protected Set<T> childValue(Set<T> parentValue) {
        return new HashSet<>(parentValue);
    }

    @Override
    protected Set<T> initialValue() {
        return new HashSet<>();
    }
}
