package de.holisticon.util.tracee.contextlogger.builder.gson;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import de.holisticon.util.tracee.Tracee;
import de.holisticon.util.tracee.TraceeLogger;
import de.holisticon.util.tracee.contextlogger.api.TraceeContextLogProvider;
import de.holisticon.util.tracee.contextlogger.data.subdata.NameValuePair;
import de.holisticon.util.tracee.contextlogger.data.subdata.NameValuePairComparator;
import de.holisticon.util.tracee.contextlogger.utility.TraceeContextLogAnnotationUtilities;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Generic serializer for context logging output.
 * Handles field order and applies Profiles (suppresses output)
 * Created by Tobias Gindler on 14.03.14.
 */
public class TraceeGenericGsonSerializer implements JsonSerializer<Object> {

    private final TraceeLogger LOGGER = Tracee.getBackend().getLoggerFactory().getLogger(TraceeGenericGsonSerializer.class);

    @Override
    public JsonElement serialize(Object instance, Type type, JsonSerializationContext jsonSerializationContext) {

        JsonObject result;

        TraceeContextLogProvider annotation = TraceeContextLogAnnotationUtilities.getAnnotationFromType(instance);
        if (annotation == null) {

            // to be ignored
            LOGGER.debug("TRACEE-CONTEXTLOGGER-GSON-SERIALIZER - Got non annotated class");
            result = new JsonObject();

        } else {

            // get those annotated methods
            List<MethodAnnotationPair> entriesToPrint = TraceeContextLogAnnotationUtilities.getAnnotatedMethodsOfInstance(instance);

            // sort those methods
            Collections.sort(entriesToPrint, new MethodAnnotationPairComparator());

            // now create json object and add those items
            result = new JsonObject();

            for (MethodAnnotationPair singleEntry : entriesToPrint) {

                try {

                    Object returnValue = singleEntry.getMethod().invoke(instance, null);

                    if (TraceeContextLogAnnotationUtilities.isFlatable(singleEntry.getMethod()) && isNameValuePair(returnValue)) {

                        // returnValue is single NameValuePair
                        NameValuePair nameValuePair = (NameValuePair) returnValue;
                        String name = nameValuePair.getName() != null ? nameValuePair.getName() : "<null>";
                        result.add(name, jsonSerializationContext.serialize(returnValue));

                    } else if (TraceeContextLogAnnotationUtilities.isFlatable(singleEntry.getMethod()) && isCollectionOfNameValuePair(returnValue)) {

                        // returnValue is Collection of NameValuePairs
                        Collection<NameValuePair> collection = sortNameValuePairCollection(getCollectionOfNameValuePair(returnValue));

                        for (NameValuePair nameValuePair : collection) {
                            String name = nameValuePair.getName() != null ? nameValuePair.getName() : "<null>";
                            result.add(name, jsonSerializationContext.serialize(returnValue));
                        }

                    } else {

                        // returnValue is not a NameValuePair == static context data
                        result.add(singleEntry.getAnnotation().displayName(), jsonSerializationContext.serialize(returnValue));

                    }
                } catch (Exception e) {

                    // to be ignored
                    LOGGER.debug("TRACEE-CONTEXTLOGGER-GSON-SERIALIZER - Exception during serialization.", e);

                }

            }

        }

        return result;
    }

    /**
     * Sorts a collection of {@link de.holisticon.util.tracee.contextlogger.data.subdata.NameValuePair}
     * @param collection the Collection to sort
     * @return
     */
    static Collection<NameValuePair> sortNameValuePairCollection (Collection<NameValuePair> collection) {

        List<NameValuePair> result;

        if (collection instanceof List) {
            result = (List)collection;
        } else {
            result = new ArrayList<NameValuePair>(collection);
        }

        return (Collection<NameValuePair>) result;
    }

    /**
     * Checks if the passed instance is of type {@link de.holisticon.util.tracee.contextlogger.data.subdata.NameValuePair}
     * @param instance the instance to check
     * @return returns true if the instance is of type {@link de.holisticon.util.tracee.contextlogger.data.subdata.NameValuePair}, otherwise false
     */
    static boolean isNameValuePair(Object instance) {

        return instance != null && NameValuePair.class.isInstance(instance);

    }


    static boolean isCollectionOfNameValuePair(Object instance) {

        return getCollectionOfNameValuePair(instance) != null;

    }

    static Collection<NameValuePair> getCollectionOfNameValuePair(Object instance) {

        if (instance == null) {
            return null;
        }

        try {
            return (Collection<NameValuePair>) instance;
        } catch (ClassCastException e) {
            return null;
        }

    }

}
