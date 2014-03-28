package de.holisticon.util.tracee.contextlogger;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

/**
 * Tobias Gindler, holisticon AG
 * Created by Tobias Gindler, holisticon AG on 21.03.14.
 */
public class RegexMatcher extends BaseMatcher<String> {

    private final String regex;

    public RegexMatcher(String regex){
        this.regex = regex;
    }

    public boolean matches(Object o){
        return ((String)o).matches(regex);

    }

    public void describeTo(Description description){
        description.appendText("doesn't match regex='" + regex + "'");
    }

    public static RegexMatcher matches(String regex){
        return new RegexMatcher(regex);
    }

}
