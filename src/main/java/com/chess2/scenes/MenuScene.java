package com.chess2.scenes;

import com.chess2.*;
import com.chess2.networking.Client;
import com.chess2.players.Player;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.IOException;

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
        TurnManagement.setupLocalPlayer(Player.Type.LOCAL, new Player.Data(true));
        TurnManagement.setupRemotePlayer(Player.Type.LOCAL, new Player.Data(false));
    }

    private void startAIGame() {
        TurnManagement.setupLocalPlayer(Player.Type.LOCAL, new Player.Data(true));
        TurnManagement.setupRemotePlayer(Player.Type.AI, new Player.Data(false));
    }

    private void startOnlineGame() {
        typeComboBox.setVisible(false);
        playButton.setVisible(false);
        try {
            Client.getInstance().connect();
        } catch (IOException e) {
            Console.log(Console.ERROR, "Failed to connect to server!");
            typeComboBox.setVisible(true);
            playButton.setVisible(true);
        }
    }
}