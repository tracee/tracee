package de.holisticon.util.tracee.contextlogger.utility;

import java.util.List;

/**
 * An utility class for typesafe casting of instances to {@link java.util.List}.
 * Created by Tobias Gindler on 21.03.14.
 */
public final class ListUtilities {

	private ListUtilities() { }

    /**
     * Checks if passed instance is of type {@link java.util.List} and contains elements of the passed type.
     * Warning: Only the first element of the list will be checked
     * @param instance
     * @param type
     * @return
     */
    public static boolean isListOfType(Object instance, Class type) {

        if (instance == null || type == null) {
            return false;
        }

        if (List.class.isAssignableFrom(instance.getClass())) {

            // check whether the list is empty or the first instance is of passed type
            List list = (List) instance;
            boolean isEmpty = list.size() == 0;

            for (Object element : list) {
                if (element != null && !type.isAssignableFrom(element.getClass())) {
                    return false;
                }
            }

            return true;

        }

        return false;
    }



    /**
     * Check if the passed instance is a {@link java.util.List} and contains only elements of the passed element types.
     * In that case it casts the passed instance to a List of the passed element type.
     * @param instance
     * @return The instance cast to a List of the passed element type if the passed instance is a list and only contains
	 * element of the passed element type, otherwise it return null.
     */
    public static <T> List<T> getListOfType(Object instance, Class<T> elementType) {

        if (instance == null || elementType == null || !isListOfType(instance, elementType)) {
            return null;
        }

        try {
            return (List<T>) instance;
        } catch (ClassCastException e) {
            return null;
        }

    }

}
