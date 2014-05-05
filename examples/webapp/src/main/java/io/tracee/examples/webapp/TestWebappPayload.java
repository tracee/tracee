package io.tracee.examples.webapp;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 * Created by Tobias Gindler, holisticon AG on 10.01.14.
 */

@ManagedBean(name = "payload")
@RequestScoped
public class TestWebappPayload {

    private Integer firstArgument;

    private Integer secondArgument;

    private Integer result;

    public Integer getFirstArgument() {
        return firstArgument;
    }

    public void setFirstArgument(Integer firstArgument) {
        this.firstArgument = firstArgument;
    }

    public Integer getSecondArgument() {
        return secondArgument;
    }

    public void setSecondArgument(Integer secondArgument) {
        this.secondArgument = secondArgument;
    }

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }
}
