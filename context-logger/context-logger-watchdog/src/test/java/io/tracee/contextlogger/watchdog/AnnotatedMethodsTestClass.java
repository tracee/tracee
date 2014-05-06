package io.tracee.contextlogger.watchdog;

/**
 * Annotated Method Test class.
 * Created by Tobias Gindler, holisticon AG on 20.02.14.
 */
public class AnnotatedMethodsTestClass {

    public static final String NAME_1 = "TEST1";
    public static final String NAME_2 = "TEST2";
    public static final String NAME_3 = "TEST3";

    public static final boolean SURPRESS_THROWS_EXCEPTIONS = true;

    @Watchdog(id = AnnotatedMethodsTestClass.NAME_1, suppressThrowsExceptions = AnnotatedMethodsTestClass.SURPRESS_THROWS_EXCEPTIONS)
    public void test1(String a, int b) {
        String value = a.toString() + b;
    }

    @Watchdog(id = AnnotatedMethodsTestClass.NAME_2, suppressThrowsExceptions = AnnotatedMethodsTestClass.SURPRESS_THROWS_EXCEPTIONS)
    public void test1(String a, int b, String x) {
        String value = a.toString() + b;
    }

    @Watchdog()
    public void test1() {

    }

    @Watchdog
    public void test2(String a, int b, String x) {

    }

    @Watchdog(id = AnnotatedMethodsTestClass.NAME_3)
    public void test3() throws IllegalArgumentException {

    }

}
