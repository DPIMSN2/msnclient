package jms;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by Kevin.
 */
public class TopicReceiverGateway {
    private static TopicReceiverGateway instance;
    private static ConnectionFactory connectionFactory;
    private static Connection connection;
    private static Channel channel;
    private static final String EXCHANGE_NAME = "statusUpdate";
    private static Consumer topicConsumer;
    private static String queueName;

    private TopicReceiverGateway() {
        connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");

        try {
            connection = connectionFactory.newConnection();
            channel = connection.createChannel();
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
            queueName = channel.queueDeclare().getQueue();

            createTopicConsumer();
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static TopicReceiverGateway getInstance() {
        if (instance == null) {
            instance = new TopicReceiverGateway();
        }
        return instance;
    }

    public static void subscribeToTopic(String routingKey) {
        try {
            channel.queueBind(queueName, EXCHANGE_NAME, routingKey);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createTopicConsumer() {
        topicConsumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println(" [x] Received '" + envelope.getRoutingKey() + "':'" + message + "'");
            }
        };
        try {
            channel.basicConsume(queueName, true, topicConsumer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
