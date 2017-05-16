package jms;

import domain.Message;

/**
 * Created by Kevin.
 */
public interface JMSMessageReceiver {
    void messageReceived(String message);
}
