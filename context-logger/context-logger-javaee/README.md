> This document contains documentation for the context-logger-jaxws module. Click [here](/README.md) to get an overview that TracEE is about.

# context-logger-jaxws

> The TracEE context-logger-javaee project offers support for acquiring contextual information in JMS and for EJB calls, if an exception is thrown during the invocation of the webservice. 


Therefore the context-logger-javaee module provides an Interceptors / message listeners for gathering contextual information of ejb calls and a jms message processing.


## Example output
Depending on the selected context logger profile the output of the ejb interceptor consists of method invocation parameters, exception data and jms message related data in case of JMS.

    TODO



## Maven artifacts
You need to add the following Dependencies to your projects pom.xml:
   
    <!-- Binds the TracEE api -->
    <dependency>
        <groupId>io.tracee</groupId>
        <artifactId>tracee-api</artifactId>
        <version>${tracee.version}</version>
    </dependency>
    
    <!-- Log Backend depending on your logging configuration-->
    <dependency>
        <groupId>io.tracee.backend</groupId>
        <artifactId>tracee-slf4j</artifactId>
        <version>${tracee.version}</version>
    </dependency>
        
    <!-- Binds context logging -->
    <dependency>
        <groupId>io.tracee.contextlogger</groupId>
        <artifactId>tracee-context-logger-javaee</artifactId>
        <version>${tracee.version}</version>
    </dependency>        
                 


## Using EJB/CDI Interceptors 

You can use the context logger by annotating the ejb method you want to be handled.

    @Stateless
    public class TestEjbImpl implements TestEjb {
    
        // ...
    
        @Interceptors({ TraceeEjbErrorContextLoggingInterceptor.class })
        public int multiply(int a, int b) {
            // ...
        }
        
        // ...
    }



## Using JMS message listener

You can use the context logger by annotating your jms message listener class you want to be handled.

    @MessageDriven(activationConfig = {
    		@ActivationConfigProperty(
    				propertyName = "destinationType", propertyValue = "javax.javaee.Topic"),
    		@ActivationConfigProperty(
    				propertyName = "destination", propertyValue = "exampleTopic") })
    @Interceptors(TraceeJmsErrorMessageListener.class)
    public class MessageTopicListener implements MessageListener {
    
    	private static final Logger LOG = LoggerFactory.getLogger(MessageTopicListener.class);
    
    	@Override
    	public void onMessage(Message message) {
    		LOG.info("I just received the message \"{}\" on javaee/exampleTopic", message);
    	}
    
    }


