import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import domain.Message;
import domain.User;
import jms.JMSClient;
import jms.JMSDispatcher;
import service.ClientService;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

/**
 * Created by Kevin.
 */
public class Main {
    private static ClientService clientService;

    public static void main(String[] argv) {
        User loggedInUser = new User("U2");
        clientService = new ClientService(loggedInUser);

        System.out.println("Connected to the server. You can now chat!");
        readConsole();
    }

    public static void readConsole(){
        Scanner scanner = new Scanner(System.in);
        String messageText = scanner.nextLine();

        clientService.sendMessage(messageText, "U1");
        readConsole();
    }
}
