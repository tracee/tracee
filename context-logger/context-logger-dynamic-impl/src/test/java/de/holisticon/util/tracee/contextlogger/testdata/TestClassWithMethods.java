package de.holisticon.util.tracee.contextlogger.testdata;

import de.holisticon.util.tracee.contextlogger.api.Flatten;

/**
 * Created by Tobias Gindler, holisticon AG on 14.03.14.
 */
public class TestClassWithMethods {

    public void methodWithNoParameters () {

    }

    public void methodWithParameters (int a, int b) {

    }

    public void methodWithVoidReturnType() {

    }
    public String methodWithNonVoidReturnType() {
        return "OK";
    }

    private void privateMethod(){}

    void packagePrivateMethod(){}

    protected void protectedMethod(){}

    @Flatten
    public void flattenTest() {

    }

}
