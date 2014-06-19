package io.tracee.contextlogger.testdata;

import io.tracee.contextlogger.api.TraceeContextProvider;
import io.tracee.contextlogger.api.TraceeContextProviderMethod;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tobias Gindler, holisticon AG on 14.03.14.
 */
@TraceeContextProvider(displayName = "")
public class AnnotationTestClass {

    private int fieldA = 0;
    private String fieldB = "test";
    private List<Integer> fieldC = new ArrayList<Integer>();
    private List<Integer> fieldD = new ArrayList<Integer>();
    private List<Integer> fieldE = new ArrayList<Integer>();

    {
        fieldC.add(5);
        fieldC.add(8);

        fieldE.add(3);
        fieldE.add(2);
    }

    @TraceeContextProviderMethod(displayName = "A", propertyName = "A", order=200)
    public int getFieldA() {
        return this.fieldA;
    }

    @TraceeContextProviderMethod(displayName = "B", propertyName = "B")
    public String getFieldB() {
        return this.fieldB;
    }

    @TraceeContextProviderMethod(displayName = "C", propertyName = "C", order=50)
    public List<Integer> getFieldC() {
        return this.fieldC;
    }

    @TraceeContextProviderMethod(displayName = "D", propertyName = "D", order=50)
    public List<Integer> getFieldD(int x) {
        return this.fieldD;
    }

    @TraceeContextProviderMethod(displayName = "E", propertyName = "E", order=50)
    public List<Integer> getFieldE() {
        return this.fieldE;
    }

    @TraceeContextProviderMethod(displayName = "F", propertyName = "F", order=50)
    public void getFieldF() {
    }

    public void getNothing() {

    }

    public void getNothingForParameters(int a, int b) {

    }

}
