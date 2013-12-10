package de.holisticon.util.tracee.jms;

import javax.jms.Message;
import javax.jms.MessageListener;

/**
 * @author Daniel Wegener (Holisticon AG)
 */
public class TraceeMessageListener implements MessageListener {


    @Override
    public void onMessage(Message message) {


        //todo read TraceeContext from messageproperty

        //todo write TraceeContext to message property


    }

}
