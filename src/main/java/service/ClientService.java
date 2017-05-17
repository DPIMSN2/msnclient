package service;

import domain.Message;
import domain.User;
import jms.JMSClient;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kevin.
 */
public class ClientService implements ClientListener {
    JMSClient jmsClient;
    User loggedInUser;
    List<ClientListener> listeners;

    public ClientService(User loggedInUser) {
        this.loggedInUser = loggedInUser;
        listeners = new ArrayList<>();
        jmsClient = new JMSClient(loggedInUser);
        jmsClient.addListener(this);
    }

    private void sendMessage(Message message) {
        jmsClient.sendMessage(message);
    }

    public Message sendMessage(String textMessage, String receiverUsername){
        User receiver = new User(receiverUsername);
        Message toSendMessage = new Message(loggedInUser, receiver, textMessage);
        sendMessage(toSendMessage);
        return toSendMessage;
    }

    public void logout(){
        jmsClient.closeConnection();
    }

    public void addListener(ClientListener listener){
        listeners.add(listener);
    }

    @Override
    public void handleMessageReceived(Message message) {
        for (ClientListener listener:listeners){
            listener.handleMessageReceived(message);
        }
    }
}
