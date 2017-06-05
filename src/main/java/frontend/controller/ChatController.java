package frontend.controller;

import domain.Message;
import domain.User;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import service.ClientListener;
import service.ClientService;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Kevin.
 */
public class ChatController implements Initializable, ClientListener {
    @FXML
    Label lblchatting;
    @FXML
    ListView lvMessages;
    @FXML
    TextField tfMessage;
    @FXML
    Button btnSendMessage;

    private String receiver;
    private ClientService clientService;
    ObservableList<String> messages;


    public ChatController(String receiver, ClientService clientService) {
        this.receiver = receiver;
        this.clientService = clientService;
        messages = FXCollections.observableArrayList();
    }

    private void btnSendMessageClick() {
        String message = tfMessage.getText();
        if (!message.isEmpty()) {
            Message sendMessage = clientService.sendMessage(message, receiver);

            String stringMessage = sendMessage.getSender().getUsername() + ": " + sendMessage.getMessageText();
            Platform.runLater(() -> messages.add(stringMessage));
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnSendMessage.setOnAction((T) -> btnSendMessageClick());
        lblchatting.setText("Chatting with:" + receiver);
        clientService.addListener(this);
        lvMessages.setItems(messages);
    }

    @Override
    public void handleMessageReceived(Message message) {
        System.out.println("receiver set to "+receiver);
        System.out.println("message sender:" + message.getSender().getUsername() + " message receiver:" + message.getReceiver().getUsername());
        if(message.getSender().getUsername().equals(receiver)){
            String stringMessage = message.getSender().getUsername() + ": " + message.getMessageText();
            Platform.runLater(() -> messages.add(stringMessage));
        }
    }

    @Override
    public void handleStatusUpdateReceived(User user) {
    }
}
