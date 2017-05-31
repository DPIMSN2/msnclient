package service;

import domain.Message;
import domain.Status;
import domain.User;
import jms.ClientApplicationGateway;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kevin.
 */
public class ClientService implements ClientListener {
    ClientApplicationGateway client;
    User loggedInUser;
    List<ClientListener> listeners;

    public ClientService(User loggedInUser) {
        this.loggedInUser = loggedInUser;
        listeners = new ArrayList<>();
        client = new ClientApplicationGateway(loggedInUser);
        client.addListener(this);
    }

    private void sendMessage(Message message) {
        client.sendMessage(message);
    }

    public Message sendMessage(String textMessage, String receiverUsername) {
        User receiver = new User(receiverUsername);
        Message toSendMessage = new Message(loggedInUser, receiver, textMessage);
        sendMessage(toSendMessage);
        return toSendMessage;
    }

    public void logout() {
        client.closeConnection();
    }

    public void addListener(ClientListener listener) {
        listeners.add(listener);
    }

    public void addFriend(String friendName) {
        client.addFriend(friendName);
    }

    @Override
    public void handleMessageReceived(Message message) {
        for (ClientListener listener : listeners) {
            listener.handleMessageReceived(message);
        }
    }

    @Override
    public void handleStatusUpdateReceived(User user) {
        for (ClientListener listener : listeners) {
            listener.handleStatusUpdateReceived(user);
        }
    }

    public void updateStatus(Status currentStatus) {
        client.updateStatus(currentStatus);
    }
}
