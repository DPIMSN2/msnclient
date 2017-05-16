package service;

import domain.Message;

/**
 * Created by Kevin.
 */
public interface ClientListener {
    void handleMessageReceived(Message message);
}
