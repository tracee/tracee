package de.holisticon.util.tracee.contextlogger;

/**
 * Annotated Test class.
 * Created by Tobias Gindler, holisticon AG on 20.02.14.
 */
@Watchdog(id=AnnotatedTestClass.NAME, suppressThrowsExceptions = AnnotatedTestClass.SURPRESS_THROWS_EXCEPTIONS)
public class AnnotatedTestClass {

    public static final String NAME = "TEST";
    public static final boolean SURPRESS_THROWS_EXCEPTIONS = true;

    public void test (String a, int b) {
        String value = a.toString() + b;
    }

}
