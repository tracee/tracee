package de.holisticon.util.tracee.ejb3;

import javax.annotation.Resource;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;
import java.util.List;
import java.util.Map;

/**
 * @author Daniel
 */
public class WebContextTraceeInterceptor {

    @Resource
    WebServiceContext webServiceContext;


    @AroundInvoke
    public void handle(InvocationContext context) {
        final MessageContext messageContext = webServiceContext.getMessageContext();
        final Map<String,List<String>> httpServletRequest = (messageContext == null)?null:(Map<String,List<String>>) messageContext.get(MessageContext.HTTP_REQUEST_HEADERS);
        

    }


}
