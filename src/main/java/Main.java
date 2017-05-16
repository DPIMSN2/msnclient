import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import domain.Message;
import domain.User;
import jms.JMSClient;
import jms.JMSDispatcher;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by Kevin.
 */
public class Main {

    static User loggedInUser;
    static User receiverUser;
    static Message message;

    public static void main(String[] argv) {
        loggedInUser = new User("senderUser");
        receiverUser = new User("receiverUser");
        message = new Message(loggedInUser, receiverUser, "aiiiiiight");

        /*
        JMSClient client = new JMSClient(loggedInUser);
        client.sendMessage(message, receiverUser); */
    }
}
