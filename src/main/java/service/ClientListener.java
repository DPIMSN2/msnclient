package service;

import domain.Message;
import domain.User;

/**
 * Created by Kevin.
 */
public interface ClientListener {
    void handleMessageReceived(Message message);
    void handleStatusUpdateReceived(User user);
}
