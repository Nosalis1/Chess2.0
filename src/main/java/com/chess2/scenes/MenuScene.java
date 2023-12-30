package com.chess2.scenes;

import com.chess2.*;
import com.chess2.networking.clientside.Client;
import com.chess2.networking.database.GameData;
import com.chess2.players.Player;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.w3c.dom.Text;

import java.util.Objects;

import static com.chess2.App.CELL_COUNT;
import static com.chess2.App.CELL_SIZE;

public class MenuScene extends Scene {
    private static MenuScene instance = new MenuScene(new HBox());

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

    private TextField username = new TextField();
    private Label gamesWon = new Label("Games won: 0");
    private Label gamesLost = new Label("Games lost: 0");
    private Label playingSince = new Label("Playing since: NA");

    private TableView<GameData> gameHistory = new TableView<>();

    private void setup() {
        HBox root = (HBox) super.getRoot();
        if (root == null) return;

        VBox buttons = new VBox();
        buttons.getStyleClass().add("buttons");

        Button playLocalButton = new Button("Local Game");
        Button playAIButton = new Button("AI Game");
        Button playOnlineButton = new Button("Online Game");

        buttons.getChildren().addAll(playLocalButton, playAIButton, playOnlineButton);

        VBox user = new VBox();
        user.getStyleClass().add("user");

        gameHistory.setId("gameHistory");

        // Create columns
        TableColumn<GameData, String> opponentColumn = new TableColumn<>("Opponent Username");
        opponentColumn.setCellValueFactory(cellData -> cellData.getValue().opponentUsernameProperty());

        TableColumn<GameData, Boolean> lossColumn = new TableColumn<>("Loss");
        lossColumn.setCellValueFactory(cellData -> cellData.getValue().lossProperty());

        TableColumn<GameData, String> startTimeColumn = new TableColumn<>("Start Time");
        startTimeColumn.setCellValueFactory(cellData -> cellData.getValue().startTimeProperty());

        // Add columns to the TableView
        gameHistory.getColumns().addAll(opponentColumn, lossColumn, startTimeColumn);

        // Set data to the TableView
        ObservableList<GameData> gameDataList = FXCollections.observableArrayList(
                new GameData("Opponent1", true, "2023-01-01 12:00:00"),
                new GameData("Opponent2", false, "2023-01-02 14:30:00")
                // Add more GameData objects as needed
        );
        gameHistory.setItems(gameDataList);

        username.getStyleClass().add("text-field");
        username.setEditable(false);
        user.getChildren().addAll(
                new Label("User"), username, gamesWon, gamesLost, playingSince, gameHistory);

        root.getChildren().addAll(
                user, buttons
        );

        root.requestFocus();
        final String cssFile = Objects.requireNonNull(getClass().getResource("/Styles/MenuSceneStyle.css")).toExternalForm();
        root.getStylesheets().add(cssFile);

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
        VBox root = (VBox) ((HBox) super.getRoot()).getChildren().get(1);
        if (root == null) return;
        root.getChildren().get(0).setDisable(true);
        root.getChildren().get(1).setDisable(true);
        root.getChildren().get(2).setDisable(true);
    }

    private void refresh() {
        VBox root = (VBox) ((HBox) super.getRoot()).getChildren().get(1);
        if (root == null) return;
        root.getChildren().get(0).setDisable(false);
        root.getChildren().get(1).setDisable(false);
        root.getChildren().get(2).setDisable(!Client.isConnected());

        if (!Client.isConnected()) {
            username.setText("Player");
            gamesWon.setText("Games Won: 0");
            gamesLost.setText("Games Lost: 0");
            playingSince.setText("Playing since: NA");
            return;
        }
        username.setText(Client.getUserData().getUsername());
        gamesWon.setText("Games Won: " + Client.getUserData().getGamesWon());
        gamesLost.setText("Games Won: " + Client.getUserData().getGamesLost());
        playingSince.setText("Playing since: " + Client.getUserData().getDateCreated());
    }
}