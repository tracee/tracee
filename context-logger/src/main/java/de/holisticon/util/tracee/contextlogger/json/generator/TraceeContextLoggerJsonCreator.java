package de.holisticon.util.tracee.contextlogger.json.generator;

import de.holisticon.util.tracee.TraceeBackend;
import de.holisticon.util.tracee.contextlogger.json.beans.*;
import de.holisticon.util.tracee.contextlogger.json.beans.values.TraceeContextValue;
import org.codehaus.jackson.map.ObjectMapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

/**
 * Creator class to generate context json via a fluent api.
 * Created by Tobias Gindler, holisticon AG on 17.12.13.
 */
public class TraceeContextLoggerJsonCreator {

    private CommonCategory categoryCommon = null;
    private ServletCategory categoryServlet = null;
    private ExceptionCategory categoryException = null;
    private JaxWsCategory categoryJaxws = null;
    private List<TraceeContextValue> categoryTracee = null;
    private String prefixedMessage = null;

    private TraceeContextLoggerJsonCreator() {
    }

    public static TraceeContextLoggerJsonCreator createJsonCreator() {
        return new TraceeContextLoggerJsonCreator();
    }

    public final TraceeContextLoggerJsonCreator addCommonCategory() {
        this.categoryCommon = CommonCategoryCreator.createCommonCategory();
        return this;
    }

    public final TraceeContextLoggerJsonCreator addExceptionCategory(Throwable throwable) {
        this.categoryException = ExceptionCategoryCreator.createExceptionCategory(throwable);
        return this;
    }

    public final TraceeContextLoggerJsonCreator addJaxwsCategory(String soapRequest, String soapResponse) {
        this.categoryJaxws = JaxWsCategoryCreator.createJaxWsCategory(soapRequest, soapResponse);
        return this;
    }

    public final TraceeContextLoggerJsonCreator addServletCategory(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        this.categoryServlet = ServletCategoryCreator.createServletCategory(httpServletRequest, httpServletResponse);
        return this;
    }

    public final TraceeContextLoggerJsonCreator addTraceeCategory(TraceeBackend traceeBackend) {
        this.categoryTracee = TraceeCategoryCreator.createTraceeCategory(traceeBackend);
        return this;
    }

    public final TraceeContextLoggerJsonCreator addPrefixedMessage(String prefixedMessage) {
        this.prefixedMessage = prefixedMessage;
        return this;
    }



    @Override
    public final  String toString() {
        return this.createJson();
    }

    private String createJson() {


        TraceeJsonEnvelope envelope = new TraceeJsonEnvelope(
                categoryCommon,
                categoryTracee,
                categoryServlet,
                categoryException,
                categoryJaxws
        );


        final StringWriter stringWriter = new StringWriter();
        if (this.prefixedMessage != null) {
            stringWriter.append(this.prefixedMessage).append(" - ");
        }

        try {

            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(stringWriter, envelope);

            return stringWriter.toString();

        } catch (IOException e) {
            throw new RuntimeException("Couldn't create JSON for error output", e);
        }


    }


}
