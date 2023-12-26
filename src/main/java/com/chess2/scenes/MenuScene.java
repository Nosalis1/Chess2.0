package com.chess2.scenes;

import com.chess2.*;
import com.chess2.networking.clientside.Client;
import com.chess2.players.Player;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

import static com.chess2.App.CELL_COUNT;
import static com.chess2.App.CELL_SIZE;

public class MenuScene extends Scene {
    private static MenuScene instance = new MenuScene(new StackPane());

    public static MenuScene getInstance() {
        return instance;
    }

    public MenuScene(Parent parent) {
        super(parent, CELL_SIZE * CELL_COUNT,
                CELL_SIZE * CELL_COUNT);
        if (instance == null) instance = this;
        this.setup();
    }

    ComboBox<Player.Type> typeComboBox;
    Button playButton;

    private void setup() {
        StackPane root = (StackPane) super.getRoot();
        if (root == null) return;

        root.getChildren().add(new ImageView() {{
            setImage(Assets.getImage("chessBackground"));
            setFitWidth(CELL_SIZE * CELL_COUNT);
            setFitHeight(CELL_SIZE * CELL_COUNT);
        }});

        root.getChildren().add(new BorderPane() {{
            setCenter(
                    new VBox(
                            new Label("Chess 2.0"),
                            new ComboBox<Player.Type>() {{
                                typeComboBox = this;
                                setItems(FXCollections.observableArrayList(Player.Type.values()));
                                setValue(Player.Type.AI);
                            }},
                            new Button("Play") {{
                                playButton = this;
                                setOnAction(actionEvent -> startGame(typeComboBox.getValue()));
                            }}
                    ) {{
                        setAlignment(Pos.CENTER);
                        setSpacing(10);
                    }}
            );
        }});
    }

    private void startGame(final Player.Type type) {
        Game.instance.reset();
        switch (type) {
            case AI -> startAIGame();
            case LOCAL -> startLocalGame();
            case ONLINE -> {
                startOnlineGame();
                return;
            }
        }
        App.mainStage.setScene(GameScene.getInstance());
    }

    private void startLocalGame() {
        TurnManagement.setupLocalPlayer(Player.Type.LOCAL, true);
        TurnManagement.setupRemotePlayer(Player.Type.LOCAL, false);
    }

    private void startAIGame() {
        TurnManagement.setupLocalPlayer(Player.Type.LOCAL, true);
        TurnManagement.setupRemotePlayer(Player.Type.AI, false);
    }

    private Pair<String, String> displayLoginDialog() {
        Dialog<Pair<String, String>> loginDialog = new Dialog<>();
        loginDialog.setTitle("Login");

        VBox root = new VBox();
        TextField usernameField = new TextField();
        PasswordField passwordField = new PasswordField();
        root.getChildren().addAll(
                new Label("Username:"),
                usernameField,
                new Label("Password"),
                passwordField
        );
        loginDialog.setResultConverter(buttonType -> {
            if (buttonType.getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                return new Pair<>(usernameField.getText(), passwordField.getText());
            }
            return null;
        });
        loginDialog.getDialogPane().setContent(root);
        loginDialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        AtomicReference<Pair<String, String>> result = new AtomicReference<>();

        loginDialog.showAndWait().ifPresent(result::set);
        return result.get();
    }

    private boolean validateUser(final Pair<String, String> data) {
        return data != null && (data.getKey() != null && !data.getKey().isBlank()) && (data.getValue() != null && !data.getValue().isBlank());
    }

    private void startOnlineGame() {
        typeComboBox.setVisible(false);
        playButton.setVisible(false);
        try {
            Pair<String, String> data = displayLoginDialog();
            if (!validateUser(data)) {
                Console.log(Console.ERROR, "User not valid!");
                throw new IOException();
            }
            Client.connect(data.getKey(), data.getValue());
        } catch (IOException e) {
            Console.log(Console.ERROR, "Failed to connect to server!");
            typeComboBox.setVisible(true);
            playButton.setVisible(true);
        }
    }
}