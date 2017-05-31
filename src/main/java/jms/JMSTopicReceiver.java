package jms;

/**
 * Created by Kevin.
 */
public interface JMSTopicReceiver {
    void handleTopicReceived(String message);
}
