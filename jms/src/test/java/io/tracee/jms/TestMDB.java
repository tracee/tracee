package io.tracee.jms;

import javax.annotation.Resource;
import javax.ejb.MessageDriven;
import javax.interceptor.Interceptors;
import java.lang.IllegalStateException;
import javax.jms.*;

@MessageDriven
@Interceptors(TraceeMessageListener.class)
public class TestMDB implements MessageListener {

    @Resource
    private ConnectionFactory connectionFactory;


    @Resource(name = "Response")
    private Queue responses;

    @Override
    public void onMessage(Message message) {
        Connection connection = null;
        Session session = null;
        try {
            final TextMessage incomingMessage = (TextMessage) message;
            connection = connectionFactory.createConnection();
            connection.start();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            final MessageProducer producer = TraceeMessageWriter.wrap(session.createProducer(responses));
            producer.setDeliveryMode(DeliveryMode.PERSISTENT);
            final TextMessage responseMessage = session.createTextMessage(incomingMessage.getText());
            producer.send(responseMessage);
        } catch (JMSException e) {
            throw new IllegalStateException(e);
        } finally {
            try {
                if (session != null) session.close();
                if (connection != null) connection.close();
            } catch (JMSException ignored) {
            }
        }

    }
}
