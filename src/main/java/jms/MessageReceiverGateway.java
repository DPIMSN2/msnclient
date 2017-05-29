package jms;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

/**
 * Created by Kevin.
 */
public class MessageReceiverGateway {
    private static MessageReceiverGateway instance;
    private static ConnectionFactory connectionFactory;
    private static Connection connection;
    private static Channel channel;
    private static com.rabbitmq.client.Consumer jmsConsumer;
    private static List<JMSMessageReceiver> listeners;

    private MessageReceiverGateway() {
        listeners = new ArrayList<JMSMessageReceiver>();
        connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");


        try {
            connection = connectionFactory.newConnection();
            channel = connection.createChannel();
            createjmsConsumer();
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static MessageReceiverGateway getInstance() {
        if (instance == null) {
            instance = new MessageReceiverGateway();
        }
        return instance;
    }

    public static void receiveMessages(String queueName) throws IOException {
        declareQueue(queueName);
    }

    public static void addListener(JMSMessageReceiver listener) {
        listeners.add(listener);
    }

    public static void closeConsumer(){
        try {
            connection.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void declareQueue(String queueName) throws IOException {
        channel.queueDeclare(queueName, false, false, false, null);
        channel.basicConsume(queueName, true, jmsConsumer);
    }

    private static void createjmsConsumer() {
        jmsConsumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       AMQP.BasicProperties properties, byte[] body)
                    throws IOException {
                String message = new String(body, "UTF-8");
                notifyListeners(message);
            }
        };
    }

    private static void notifyListeners(String message) {
        for (JMSMessageReceiver listener : listeners) {
            listener.messageReceived(message);
        }
    }

}