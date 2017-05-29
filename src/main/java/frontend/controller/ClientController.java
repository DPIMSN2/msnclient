package frontend.controller;

import domain.Message;
import domain.Status;
import domain.User;
import javafx.application.Platform;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import service.ClientListener;
import service.ClientService;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Created by Kevin.
 */
public class ClientController implements Initializable, ClientListener {
    @FXML
    private Button btnLogin;
    @FXML
    private Button btnSendMessage;
    @FXML
    private TextField tfUsername;
    @FXML
    private TextField tfPassword;
    @FXML
    private TextField tfReceiver;
    @FXML
    private ListView lvMessages;
    @FXML
    private TextField tfMessage;
    @FXML
    private Button btnUpdateStatus;
    @FXML
    private ChoiceBox cbStatus;
    @FXML
    private TabPane tpPane;

    private ClientService clientService;
    ObservableList<String> messages;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnLogin.setOnAction((T) -> btnLogin());
        btnSendMessage.setOnAction((T) -> btnSendMessageClick());
        btnUpdateStatus.setOnAction((T) -> btnUpdateStatus());

        messages = FXCollections.observableArrayList();
        messages.add("Connected! You can now talk with your partner");
        lvMessages.setItems(messages);

        cbStatus.setItems(createStatusList());
        cbStatus.getSelectionModel().clearAndSelect(0);

        //Disable chat and status till login is complete
        tpPane.getTabs().get(1).setDisable(true);
        tpPane.getTabs().get(2).setDisable(true);

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

            clientService.updateStatus(Status.Online);
        }
        tpPane.getTabs().get(1).setDisable(false);
        tpPane.getTabs().get(2).setDisable(false);
    }

    private void btnUpdateStatus() {
        Status currentStatus = (Status)cbStatus.getSelectionModel().getSelectedItem();
        clientService.updateStatus(currentStatus);
    }

    private ObservableList<Status> createStatusList(){
        ArrayList<Status> statuses = new ArrayList();
        statuses.add(Status.Online);
        statuses.add(Status.Busy);
        statuses.add(Status.Offline);

        return FXCollections.observableList(statuses);
    }

    @Override
    public void handleMessageReceived(Message message) {
        String stringMessage = message.getSender().getUsername() + ": " + message.getMessageText();
        Platform.runLater(() -> messages.add(stringMessage));
    }
}
