package jms;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import domain.Message;
import domain.Status;
import domain.User;
import service.ClientListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kevin.
 */
public class JMSClient implements JMSMessageReceiver {
    private final String SERVERQUEUE = "chatServer";
    private User loggedInUser;
    private List<ClientListener> listeners;

    public JMSClient(User loggedInUser) {
        JMSDispatcher.getInstance();
        JMSConsumer.getInstance();
        this.loggedInUser = loggedInUser;
        listeners = new ArrayList<ClientListener>();
        startListening();
    }

    public void sendMessage(Message message) {
        String jsonMessage = messageToJson(message);
        try {
            JMSDispatcher.publishMessage(SERVERQUEUE, jsonMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateStatus(Status status) {
        // todo update status
    }

    private String messageToJson(Message message) {
        Gson jsonMessage = new GsonBuilder().create();
        return jsonMessage.toJson(message);
    }

    private static Message jsonToMessage(String json) {
        Gson jsonMessage = new GsonBuilder().create();
        return jsonMessage.fromJson(json, Message.class);
    }

    public void addListener(ClientListener clientListener) {
        this.listeners.add(clientListener);
    }

    private void startListening() {
        try {
            JMSConsumer.addListener(this);
            JMSConsumer.receiveMessages(loggedInUser.getUsername());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void messageReceived(String message) {
        Message receivedMessage = jsonToMessage(message);

        for(ClientListener listener: listeners){
            listener.handleMessageReceived(receivedMessage);
        }
    }
}
