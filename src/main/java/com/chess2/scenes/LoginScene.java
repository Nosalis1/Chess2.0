package com.chess2.scenes;

import com.chess2.App;
import com.chess2.Console;
import com.chess2.networking.clientside.Client;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

import java.io.IOException;
import java.util.Objects;

import static com.chess2.App.CELL_COUNT;
import static com.chess2.App.CELL_SIZE;

public class LoginScene extends Scene {
    private static LoginScene instance = new LoginScene(new VBox());

    public static LoginScene getInstance() {
        return instance;
    }

    public static void displayScene() {
        Platform.runLater(() -> App.mainStage.setScene(instance));
    }

    public LoginScene(Parent parent) {
        super(parent, CELL_SIZE * CELL_COUNT,
                CELL_SIZE * CELL_COUNT);
        if (instance == null) instance = this;
        this.setup();
    }

    private void setup() {
        VBox root = (VBox) super.getRoot();
        if (root == null) return;

        Button loginButton = new Button("Login");
        Button registerButton = new Button("Register");
        Button playOfflineButton = new Button("Play Offline");

        TextField usernameField = new TextField();
        PasswordField passwordField = new PasswordField();

        Label usernameLabel = new Label("Username:");
        Label passwordLabel = new Label("Password");

        loginButton.setId("loginButton");
        registerButton.setId("registerButton");
        playOfflineButton.setId("playOfflineButton");

        usernameField.setPromptText("Enter username");
        passwordField.setPromptText("Enter password");

        root.getChildren().addAll(
                usernameLabel,
                usernameField,
                passwordLabel,
                passwordField,
                new HBox(loginButton, registerButton) {{
                    setAlignment(Pos.CENTER);
                    setSpacing(10);
                }},
                playOfflineButton
        );

        root.requestFocus();
        final String cssFile = Objects.requireNonNull(getClass().getResource("/Styles/LoginSceneStyle.css")).toExternalForm();
        root.getStylesheets().add(cssFile);

        loginButton.setOnAction(this::onLoginButtonPressed);
        registerButton.setOnAction(this::onRegisterButtonPressed);
        playOfflineButton.setOnAction(this::onPlayOfflineButtonPressed);
    }

    private Pair<String, String> getCredentials() {
        VBox root = (VBox) super.getRoot();
        if (root == null) return null;

        TextField usernameField = (TextField) root.getChildren().get(1);
        PasswordField passwordField = (PasswordField) root.getChildren().get(3);
        if (usernameField == null || passwordField == null) return null;

        Pair<String, String> credentials = new Pair<>(usernameField.getText(), passwordField.getText());
        if (credentials.getKey().isBlank() || credentials.getValue().isBlank()) {
            Console.log(Console.ERROR, "Credentials not valid!");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid credentials");
            alert.setContentText("Username or password not valid!");
            alert.showAndWait();
            return null;
        }
        return credentials;
    }

    private void onLoginButtonPressed(ActionEvent actionEvent) {
        Pair<String, String> credentials = getCredentials();
        if (credentials == null) return;

        try {
            Client.connect(credentials.getKey(), credentials.getValue());
        } catch (IOException e) {
            Console.log(Console.ERROR, "Failed to connect to server!");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Connection error");
            alert.setContentText("Failed to connect to server!\n" + e.getMessage());
            alert.showAndWait();
        }

        if (Client.isConnected()) MenuScene.displayScene();
    }

    private void onRegisterButtonPressed(ActionEvent actionEvent) {
        Pair<String, String> credentials = getCredentials();
        if (credentials == null) return;

        try {
            Client.connect(credentials.getKey(), credentials.getValue(), true);
        } catch (IOException e) {
            Console.log(Console.ERROR, "Failed to connect to server!");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Connection error");
            alert.setContentText("Failed to connect to server!\n" + e.getMessage());
            alert.showAndWait();
        }

        if (Client.isConnected()) MenuScene.displayScene();
    }

    private void onPlayOfflineButtonPressed(ActionEvent actionEvent) {
        MenuScene.displayScene();
    }
}