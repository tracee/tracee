package de.holisticon.util.tracee.contextlogger.data.subdata;

import de.holisticon.util.tracee.contextlogger.utility.RecursiveReflectionToStringStyle;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * Value class for JSON generation.
 * Created by Tobias Gindler, holisticon AG on 20.03.14.
 */
public class NameObjectValuePair extends NameValuePair<Object>{

    public NameObjectValuePair(String name, Object value) {
        super(name, value);
    }

}
