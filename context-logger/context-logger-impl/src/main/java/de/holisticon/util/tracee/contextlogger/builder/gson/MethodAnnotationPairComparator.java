package de.holisticon.util.tracee.contextlogger.builder.gson;

import java.util.Comparator;

/**
 * Comparator to sort {@link de.holisticon.util.tracee.contextlogger.builder.gson.MethodAnnotationPair} instances.
 * Created by Tobias Gindler, holisticon AG on 14.03.14.
 */
public final class MethodAnnotationPairComparator implements Comparator<MethodAnnotationPair> {

	@Override
    public int compare(MethodAnnotationPair instance1, MethodAnnotationPair instance2) {

        // primary sort criteria is the order value of the annotation
        if (instance1 == null && instance2 == null) {
            return 0;
        } else if (instance1 != null && instance2 == null) {
            return 1;
        } else if (instance1 == null) {
            return -1;
        } else  {
            int result = Integer.valueOf(instance1.getAnnotation().order()).compareTo(instance2.getAnnotation().order());
            if (result == 0) {
                result = instance1.getAnnotation().displayName().compareTo(instance2.getAnnotation().displayName());
            }
            return result;
        }

    }
}
