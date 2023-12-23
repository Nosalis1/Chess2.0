package com.chess2.scenes;

import com.chess2.App;
import com.chess2.Assets;
import com.chess2.Game;
import com.chess2.TurnManagement;
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
import org.jetbrains.annotations.NotNull;

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
                                if (!Client.isConnected()) getItems().remove(Player.Type.ONLINE);
                                setValue(Player.Type.AI);
                            }},
                            new Button("Play") {{
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
            case ONLINE -> startOnlineGame();
        }
        App.mainStage.setScene(GameScene.getInstance());
    }

    private void startLocalGame() {
        TurnManagement.setupLocalPlayer(new Player.Data(true));
        TurnManagement.setupRemotePlayer(Player.Type.LOCAL, new Player.Data(false));
    }

    private void startAIGame() {
        TurnManagement.setupLocalPlayer(new Player.Data(true));
        TurnManagement.setupRemotePlayer(Player.Type.AI, new Player.Data(false));
    }

    private void startOnlineGame() {
        TurnManagement.setupLocalPlayer(new Player.Data(true));
        TurnManagement.setupRemotePlayer(Player.Type.ONLINE, new Player.Data(false));
    }
}