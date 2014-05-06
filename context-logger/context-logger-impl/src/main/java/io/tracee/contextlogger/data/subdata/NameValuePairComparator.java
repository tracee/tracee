package io.tracee.contextlogger.data.subdata;

import java.util.Comparator;

/**
 * Comparator for {@link io.tracee.contextlogger.data.subdata.NameValuePair}.
 * Created by Tobias Gindler, holisticon AG on 16.03.14.
 */
public final class NameValuePairComparator implements Comparator<NameValuePair> {

	@Override
    public int compare(NameValuePair instance1, NameValuePair instance2) {

        // primary sort criteria is the order value of the annotation
        if (instance1 == null && instance2 == null) {
            return 0;
        } else if (instance1 != null && instance2 == null) {
            return -1;
        } else if (instance1 == null) {
            return 1;
        } else {

            String name1 = instance1.getName();
            String name2 = instance2.getName();

            if (name1 == null && name2 == null) {
                return 0;
            } else if (name1 != null && name2 == null) {
                return -1;
            } else if (name1 == null) {
                return 1;
            } else {
                return name1.compareTo(name2);
            }

        }

    }
}
