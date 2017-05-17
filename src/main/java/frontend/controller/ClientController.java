package frontend.controller;

import domain.Message;
import domain.User;
import javafx.application.Platform;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import service.ClientListener;
import service.ClientService;

import javax.xml.soap.Text;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Kevin.
 */
public class ClientController implements Initializable, ClientListener {
    @FXML
    Button btnLogin;
    @FXML
    Button btnSendMessage;
    @FXML
    TextField tfUsername;
    @FXML
    TextField tfPassword;
    @FXML
    TextField tfReceiver;
    @FXML
    ListView lvMessages;
    @FXML
    TextField tfMessage;

    private ClientService clientService;
    ObservableList<String> messages;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnLogin.setOnAction((T) -> btnLogin());
        btnSendMessage.setOnAction((T) -> btnSendMessageClick());
        messages = FXCollections.observableArrayList();
        messages.add("Connected! You can now talk with your partner");
        lvMessages.setItems(messages);
        Platform.setImplicitExit(false);

    }

    private void btnSendMessageClick() {
        String receiver = tfReceiver.getText();
        String message = tfMessage.getText();
        if (!receiver.isEmpty() && !message.isEmpty()) {
            Message sendmessage = clientService.sendMessage(message, receiver);
            handleMessageReceived(sendmessage);
        }
    }

    private void btnLogin() {
        String username = tfUsername.getText();
        if (!username.isEmpty()) {
            System.out.println("U:" + username);
            User loggedInUser = new User(username);
            clientService = new ClientService(loggedInUser);
            clientService.addListener(this);
        }
    }

    @Override
    public void handleMessageReceived(Message message) {
        String stringMessage = message.getSender().getUsername() + ": " + message.getMessageText();
        Platform.runLater(() -> messages.add(stringMessage));
    }
}
