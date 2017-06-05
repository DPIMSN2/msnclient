package frontend.controller;

import domain.Message;
import domain.Status;
import domain.User;
import javafx.application.Platform;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import service.ClientListener;
import service.ClientService;

import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Created by Kevin.
 */
public class ClientController implements Initializable, ClientListener {
    @FXML
    private Button btnLogin;
    @FXML
    private TextField tfUsername;
    @FXML
    private ListView lvMessages;
    @FXML
    private Button btnUpdateStatus;
    @FXML
    private ChoiceBox cbStatus;
    @FXML
    private TabPane tpPane;
    @FXML
    private Button btnAddFriend;
    @FXML
    private ListView lvFriends;
    @FXML
    private TextField tfFriend;


    private ClientService clientService;
    ObservableList<String> messages;
    ObservableList<User> friends;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnLogin.setOnAction((T) -> btnLogin());
        btnUpdateStatus.setOnAction((T) -> btnUpdateStatus());
        btnAddFriend.setOnAction((T) -> btnAddFriend());
        lvFriends.setOnMouseClicked(click -> {
            if (click.getClickCount() == 2) {
                lvFriendsClicked();
            }
        });

        messages = FXCollections.observableArrayList();
        messages.add("Connected! You can now talk with your partner");
        lvMessages.setItems(messages);

        friends = FXCollections.observableArrayList();
        lvFriends.setItems(friends);

        cbStatus.setItems(createStatusList());
        cbStatus.getSelectionModel().clearAndSelect(0);

        //Disable chat and status till login is complete
        tpPane.getTabs().get(1).setDisable(true);
        tpPane.getTabs().get(2).setDisable(true);

        Platform.setImplicitExit(false);
    }

    private void lvFriendsClicked() {
        User receiver = (User) lvFriends.getSelectionModel().getSelectedItem();
        if(receiver != null) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/chatscreen.fxml"));
                ChatController chatController = new ChatController(receiver.getUsername(), clientService);
                fxmlLoader.setController(chatController);
                Parent root1 = fxmlLoader.load();
                Stage stage = new Stage();
                stage.setScene(new Scene(root1));
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private void btnAddFriend() {
        String friendName = tfFriend.getText();

        if (!friendName.isEmpty() && friends.stream().noneMatch(f -> f.getUsername().equals(friendName))) {
            User newFriend = new User(friendName, Status.Offline);
            friends.add(newFriend);

            clientService.addFriend(friendName);
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
        Status currentStatus = (Status) cbStatus.getSelectionModel().getSelectedItem();
        clientService.updateStatus(currentStatus);
    }

    private ObservableList<Status> createStatusList() {
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

    @Override
    public void handleStatusUpdateReceived(User user) {
        Platform.runLater(() -> {
            for (User friend : friends) {
                if (friend.getUsername().equals(user.getUsername())) {
                    friend.setStatus(user.getStatus());
                    lvFriends.setItems(null);
                    lvFriends.setItems(friends);
                }
            }
        });
    }
}
