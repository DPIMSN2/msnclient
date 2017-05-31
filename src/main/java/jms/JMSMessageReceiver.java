package jms;

/**
 * Created by Kevin.
 */
public interface JMSMessageReceiver {
    void messageReceived(String message);
}
