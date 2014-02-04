package de.holisticon.util.tracee.errorlogger.json.generator;

import de.holisticon.util.tracee.errorlogger.TraceeErrorConstants;
import de.holisticon.util.tracee.errorlogger.json.beans.CommonCategory;

import java.util.Calendar;
import java.util.Date;

/**
 * Factory for common context specific data.
 * Created by Tobias Gindler, holisticon AG on 19.12.13.
 */
public final class CommonCategoryCreator {


    private CommonCategoryCreator() {

    }

    public static CommonCategory createCommonCategory() {

        Date timestamp = Calendar.getInstance().getTime();
        String systemName = getSystemProperty(TraceeErrorConstants.SYSTEM_PROPERTY_NAME_SYSTEM);
        String stage = getSystemProperty(TraceeErrorConstants.SYSTEM_PROPERTY_NAME_STAGE);
        String threadName = Thread.currentThread().getName();
        Long threadId = Thread.currentThread().getId();

        return new CommonCategory(systemName, stage, timestamp, threadName, threadId);

    }

    private static String getSystemProperty(final String attributeName) {
        return System.getProperty(attributeName);
    }

}
