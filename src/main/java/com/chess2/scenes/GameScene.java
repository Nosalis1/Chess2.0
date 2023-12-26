package com.chess2.scenes;

import com.chess2.App;
import com.chess2.BoardField;
import com.chess2.Game;
import com.chess2.TurnManagement;
import com.chess2.networking.clientside.Client;
import com.chess2.networking.database.UserData;
import com.chess2.utility.Move;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

import static com.chess2.App.CELL_COUNT;
import static com.chess2.App.CELL_SIZE;

public class GameScene extends Scene {
    private static GameScene instance = new GameScene(new HBox());

    public static GameScene getInstance() {
        return instance;
    }

    public GameScene(Parent parent) {
        super(parent, CELL_SIZE * (CELL_COUNT + 2),
                CELL_SIZE * CELL_COUNT);
        if (instance == null) instance = this;
        this.setup();
    }

    private void setup() {
        HBox root = (HBox) super.getRoot();
        if (root == null) return;

        setupStats();

        root.getChildren().addAll(Game.instance.root, this.stats);
    }

    private final VBox stats = new VBox();

    private final Label aUsernameLabel = createLabel("Username: ");
    private final Label aGamesWonLabel = createLabel("Games Won: 0");
    private final Label aGamesLostLabel = createLabel("Games Lost: 0");
    private final Label aPlaytimeLabel = createLabel("Playtime: 0h");

    private final Label bUsernameLabel = createLabel("Username: ");
    private final Label bGamesWonLabel = createLabel("Games Won: 0");
    private final Label bGamesLostLabel = createLabel("Games Lost: 0");
    private final Label bPlaytimeLabel = createLabel("Playtime: 0h");


    private static Label createLabel(final String text) {
        Label label = new Label(text);
        label.setStyle("-fx-font-family: 'Times New Roman';" +
                "-fx-font-weight: bold;" +
                "-fx-font-size: 18;");
        return label;
    }

    private void setupStats() {
        this.stats.setPrefWidth(App.CELL_SIZE * 2);

        VBox a = new VBox();
        a.setBackground(Background.fill(BoardField.FIELD_DARK_COLOR));
        a.getChildren().addAll(
                new Label("Black Player") {{
                    setAlignment(Pos.CENTER);
                    setTextAlignment(TextAlignment.CENTER);
                    setFont(Font.font("Times New Roman", FontWeight.BOLD, FontPosture.REGULAR, 20));
                }},
                aUsernameLabel,
                aGamesWonLabel, aGamesLostLabel,
                aPlaytimeLabel
        );

        VBox b = new VBox();
        b.setBackground(Background.fill(BoardField.FIELD_LIGHT_COLOR));
        b.getChildren().addAll(
                new Label("White Player") {{
                    setAlignment(Pos.CENTER);
                    setTextAlignment(TextAlignment.CENTER);
                    setFont(Font.font("Times New Roman", FontWeight.BOLD, FontPosture.REGULAR, 20));
                }},
                bUsernameLabel,
                bGamesWonLabel, bGamesLostLabel,
                bPlaytimeLabel
        );

        this.stats.getChildren().addAll(a, b, new ListView<Move>() {{
            setItems(TurnManagement.getMoveHistory());
        }});
    }

    public void updateStats() {
        Platform.runLater(() -> {
            final UserData whiteData = TurnManagement.getLocalPlayer().isWhite() ? Client.getUserData() : Client.getOpponentData();
            final UserData blackData = TurnManagement.getLocalPlayer().isWhite() ? Client.getOpponentData() : Client.getUserData();

            if (whiteData != null && blackData != null) {
                aUsernameLabel.setText("Username: " + blackData.getUsername());
                aGamesWonLabel.setText("Games Won: " + blackData.getGamesWon());
                aGamesLostLabel.setText("Games Lost: " + blackData.getGamesLost());
                aPlaytimeLabel.setText("Playtime: " + blackData.getPlaytime() + "h");

                bUsernameLabel.setText("Username: " + whiteData.getUsername());
                bGamesWonLabel.setText("Games Won: " + whiteData.getGamesWon());
                bGamesLostLabel.setText("Games Lost: " + whiteData.getGamesLost());
                bPlaytimeLabel.setText("Playtime: " + whiteData.getPlaytime() + "h");
            } else resetStats();
        });
    }

    private void resetStats() {
        aUsernameLabel.setText("Username: ");
        aGamesWonLabel.setText("Games Won: 0");
        aGamesLostLabel.setText("Games Lost: 0");
        aPlaytimeLabel.setText("Playtime: 0h");

        bUsernameLabel.setText("Username: ");
        bGamesWonLabel.setText("Games Won: 0");
        bGamesLostLabel.setText("Games Lost: 0");
        bPlaytimeLabel.setText("Playtime: 0h");
    }
}