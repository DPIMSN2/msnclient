package jms;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import domain.Message;
import domain.Status;
import domain.User;

import java.io.IOException;

/**
 * Created by Kevin.
 */
public class JMSClient {
    private JMSDispatcher dispatcher;
    private JMSConsumer consumer;
    private User loggedInUser;

    public JMSClient(User loggedInUser){
        dispatcher = JMSDispatcher.getInstance();
        consumer = JMSConsumer.getInstance();
        this.loggedInUser = loggedInUser;
    }

    public void sendMessage(Message message, User receiver){
        // todo send message
        String jsonMessage = messageToJson(message);
        try {
            dispatcher.publishMessage(receiver.getUsername(), jsonMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateStatus(Status status){
        // todo update status
    }

    private String messageToJson(Message message){
        Gson jsonMessage = new GsonBuilder().create();
        return jsonMessage.toJson(message);
    }

    private static Message jsonToMessage(String json){
        Gson jsonMessage = new GsonBuilder().create();
        return jsonMessage.fromJson(json, Message.class);
    }
    // todo event handler voor het receiven van messages
}
