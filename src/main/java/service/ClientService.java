package service;

import domain.Message;
import domain.User;
import jms.JMSClient;

/**
 * Created by Kevin.
 */
public class ClientService implements ClientListener {
    JMSClient jmsClient;
    User loggedInUser;

    public ClientService(User loggedInUser) {
        this.loggedInUser = loggedInUser;
        jmsClient = new JMSClient(loggedInUser);
        jmsClient.addListener(this);
    }

    private void sendMessage(Message message) {
        jmsClient.sendMessage(message);
    }

    public void sendMessage(String textMessage, String receiverUsername){
        User receiver = new User(receiverUsername);
        Message toSendMessage = new Message(loggedInUser, receiver, textMessage);

        sendMessage(toSendMessage);
    }

    @Override
    public void handleMessageReceived(Message message) {
        System.out.println(message.getSender().getUsername() + ": " + message.getMessageText());
    }
}
