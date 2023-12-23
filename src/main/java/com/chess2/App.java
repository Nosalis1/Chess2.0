package com.chess2;

import com.chess2.networking.Client;
import com.chess2.players.Player;
import com.chess2.scenes.MenuScene;
import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {
    public static final int CELL_SIZE = 80;
    public static final int CELL_COUNT = 8;

    public static Stage mainStage;

    @Override
    public void start(Stage stage) {
        System.out.println("Connected: " + Client.isConnected());
        Assets.initialize();

        mainStage = stage;

        stage.setTitle("Chess 2.0");
        stage.getIcons().add(Assets.getImage("chess-board"));

        stage.setScene(MenuScene.getInstance());
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        Player.shutdownThreadPool();
        super.stop();
    }

    public static void main(String[] args) {
        launch();
    }
}