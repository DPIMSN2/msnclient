import domain.User;
import service.ClientService;

import java.util.Scanner;

/**
 * Created by Kevin.
 */
public class Main {
    private static ClientService clientService;

    public static void main(String[] argv) {
        User loggedInUser = new User("U1");
        clientService = new ClientService(loggedInUser);

        System.out.println("Connected to the server. You can now chat!");
        readConsole();
    }

    public static void readConsole(){
        Scanner scanner = new Scanner(System.in);
        String messageText = scanner.nextLine();

        clientService.sendMessage(messageText, "U2");
        readConsole();
    }
}
