package de.holisticon.util.tracee.errorlogger.json.generator;

import de.holisticon.util.tracee.TraceeBackend;
import de.holisticon.util.tracee.errorlogger.json.beans.*;
import de.holisticon.util.tracee.errorlogger.json.beans.values.TraceeContextValue;
import org.codehaus.jackson.map.ObjectMapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

/**
 * Created by Tobias Gindler, holisticon AG on 17.12.13.
 */
public class TraceeErrorLoggerJsonCreator {

    private enum Category {
        COMMON, EXCEPTION, JAXWS
    }


    private CommonCategory categoryCommon = null;
    private ServletCategory categoryServlet = null;
    private ExceptionCategory categoryException = null;
    private JaxWsCategory categoryJaxws = null;
    private List<TraceeContextValue> categoryTracee = null;

    private TraceeErrorLoggerJsonCreator() {
    }

    public final TraceeErrorLoggerJsonCreator addCommonCategory() {
        this.categoryCommon = CommonCategoryCreator.createCommonCategory();
        return this;
    }

    public final TraceeErrorLoggerJsonCreator addExceptionCategory(Throwable throwable) {
        this.categoryException = ExceptionCategoryCreator.createExceptionCategory(throwable);
        return this;
    }

    public final TraceeErrorLoggerJsonCreator addJaxwsCategory(String soapRequest, String soapResponse) {
        this.categoryJaxws = JaxWsCategoryCreator.createJaxWsCategory(soapRequest, soapResponse);
        return this;
    }

    public final TraceeErrorLoggerJsonCreator addServletCategory(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        this.categoryServlet = ServletCategoryCreator.createServletCategory(httpServletRequest, httpServletResponse);
        return this;
    }

    public final TraceeErrorLoggerJsonCreator addTraceeCategory(TraceeBackend traceeBackend) {
        this.categoryTracee = TraceeCategoryCreator.createTraceeCategory(traceeBackend);
        return this;
    }


    public final String createJson() {


        TraceeErrorJsonEnvelope envelope = new TraceeErrorJsonEnvelope(
                categoryCommon,
                categoryTracee,
                categoryServlet,
                categoryException,
                categoryJaxws
        );


        final StringWriter stringWriter = new StringWriter();

        try {

            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(stringWriter, envelope);

            return stringWriter.toString();

        } catch (IOException e) {
            throw new RuntimeException("Couldn't create JSON for error output", e);
        }


    }

    public static TraceeErrorLoggerJsonCreator createJsonCreator() {
        return new TraceeErrorLoggerJsonCreator();
    }

}
