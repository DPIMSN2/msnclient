package jms;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by Kevin.
 */
public class JMSConsumer {
    private static JMSConsumer instance;
    private static ConnectionFactory connectionFactory;
    private static Connection connection;
    private static Channel channel;
    private static com.rabbitmq.client.Consumer jmsConsumer;

    private JMSConsumer() {
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

    public static JMSConsumer getInstance() {
        if (instance == null) {
            instance = new JMSConsumer();
        }
        return instance;
    }

    public static void receiveMessages(String queueName) throws IOException {
        declareQueue(queueName);
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
                System.out.println(" [x] Received '" + message + "'");
            }
        };
    }
}