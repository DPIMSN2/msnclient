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
import java.util.concurrent.TimeoutException;

/**
 * Created by Kevin.
 */
public class ClientApplicationGateway implements JMSMessageReceiver, JMSTopicReceiver {
    private final String MESSAGESERVER = "chatServer";
    private final String STATUSSERVER = "statusServer";
    private User loggedInUser;
    private List<ClientListener> listeners;

    public ClientApplicationGateway(User loggedInUser) {
        MessageSenderGateway.getInstance();
        MessageReceiverGateway.getInstance();
        TopicReceiverGateway.getInstance();

        this.loggedInUser = loggedInUser;
        listeners = new ArrayList<>();
        startListening();
    }

    public void sendMessage(Message message) {
        String jsonMessage = messageToJson(message);
        try {
            MessageSenderGateway.publishMessage(MESSAGESERVER, jsonMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addFriend(String friendName) {
        TopicReceiverGateway.subscribeToTopic(friendName);
    }

    public void updateStatus(Status status) {
        this.loggedInUser.setStatus(status);
        String jsonUser = userToJson(loggedInUser);
        try {
            MessageSenderGateway.publishMessage(STATUSSERVER, jsonUser);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void addListener(ClientListener clientListener) {
        if (!listeners.contains(clientListener)) {
            this.listeners.add(clientListener);
        }
    }

    @Override
    public void messageReceived(String message) {
        Message receivedMessage = jsonToMessage(message);

        for (ClientListener listener : listeners) {
            listener.handleMessageReceived(receivedMessage);
        }
    }

    @Override
    public void handleTopicReceived(String message) {
        User receivedUser = jsonToUser(message);

        for (ClientListener listener : listeners) {
            listener.handleStatusUpdateReceived(receivedUser);
        }
    }

    public void closeConnection() {
        try {
            MessageSenderGateway.closeDispatcher();
            MessageReceiverGateway.closeConsumer();
            ;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }

    private void startListening() {
        try {
            MessageReceiverGateway.addListener(this);
            MessageReceiverGateway.receiveMessages(loggedInUser.getUsername());

            TopicReceiverGateway.addListener(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String messageToJson(Message message) {
        Gson jsonMessage = new GsonBuilder().create();
        return jsonMessage.toJson(message);
    }

    private Message jsonToMessage(String json) {
        Gson jsonMessage = new GsonBuilder().create();
        return jsonMessage.fromJson(json, Message.class);
    }

    private String userToJson(User user) {
        Gson jsonStatus = new GsonBuilder().create();
        return jsonStatus.toJson(user);
    }

    private User jsonToUser(String json) {
        Gson jsonStatus = new GsonBuilder().create();
        return jsonStatus.fromJson(json, User.class);
    }
}
