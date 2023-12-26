package com.chess2.scenes;

import com.chess2.*;
import com.chess2.networking.clientside.Client;
import com.chess2.players.Player;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import static com.chess2.App.CELL_COUNT;
import static com.chess2.App.CELL_SIZE;

public class MenuScene extends Scene {
    private static MenuScene instance = new MenuScene(new VBox());

    public static MenuScene getInstance() {
        return instance;
    }

    public static void displayScene() {
        Platform.runLater(() -> App.mainStage.setScene(instance));
        instance.refresh();
    }

    public MenuScene(Parent parent) {
        super(parent, CELL_SIZE * CELL_COUNT,
                CELL_SIZE * CELL_COUNT);
        if (instance == null) instance = this;
        this.setup();
    }

    // TODO : User data
    private void setup() {
        VBox root = (VBox) super.getRoot();
        if (root == null) return;

        root.setStyle("-fx-background-color: #" + BoardField.FIELD_DARK_COLOR.toString().substring(2) + "; -fx-padding: 20;");

        root.setAlignment(Pos.CENTER);
        root.setSpacing(10);
        root.setPadding(new Insets(20));

        Button playLocalButton = new Button("Local Game");
        Button playAIButton = new Button("AI Game");
        Button playOnlineButton = new Button("Online Game");

        playLocalButton.setStyle("-fx-background-color: #" + BoardField.FIELD_MOVE_DARK_COLOR.toString().substring(2) + "; -fx-text-fill: white;");
        playAIButton.setStyle("-fx-background-color: #" + BoardField.FIELD_MOVE_LIGHT_COLOR.toString().substring(2) + "; -fx-text-fill: white;");
        playOnlineButton.setStyle("-fx-background-color: #" + BoardField.FIELD_MOVE_DARK_COLOR.toString().substring(2) + "; -fx-text-fill: white;");

        root.getChildren().addAll(
                playLocalButton, playAIButton, playOnlineButton
        );

        playLocalButton.setOnAction(this::onPlayLocalButtonPressed);
        playAIButton.setOnAction(this::onPlayAIButtonPressed);
        playOnlineButton.setOnAction(this::onPlayOnlineButtonPressed);
    }

    private void onPlayLocalButtonPressed(ActionEvent actionEvent) {
        TurnManagement.setupLocalPlayer(Player.Type.LOCAL, true);
        TurnManagement.setupRemotePlayer(Player.Type.LOCAL, false);
        GameScene.displayScene();
    }

    private void onPlayAIButtonPressed(ActionEvent actionEvent) {
        TurnManagement.setupLocalPlayer(Player.Type.LOCAL, true);
        TurnManagement.setupRemotePlayer(Player.Type.AI, false);
        GameScene.displayScene();
    }

    private void onPlayOnlineButtonPressed(ActionEvent actionEvent) {
        Client.requestQueue();
        VBox root = (VBox) super.getRoot();
        if (root == null) return;
        root.getChildren().get(0).setDisable(true);
        root.getChildren().get(1).setDisable(true);
        root.getChildren().get(2).setDisable(true);
    }

    private void refresh() {
        VBox root = (VBox) super.getRoot();
        if (root == null) return;
        root.getChildren().get(0).setDisable(false);
        root.getChildren().get(1).setDisable(false);
        root.getChildren().get(2).setDisable(!Client.isConnected());
    }
}