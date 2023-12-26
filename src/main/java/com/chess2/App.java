package com.chess2;

import com.chess2.networking.clientside.Client;
import com.chess2.players.Player;
import com.chess2.scenes.LoginScene;
import com.chess2.scenes.MenuScene;
import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {
    public static final int CELL_SIZE = 80;
    public static final int CELL_COUNT = 8;

    public static Stage mainStage;

    @Override
    public void start(Stage stage) {
        Assets.initialize();

        mainStage = stage;

        stage.setTitle("Chess 2.0");
        stage.getIcons().add(Assets.getImage("chess-board"));

        LoginScene.displayScene();
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        Player.shutdownThreadPool();
        if (Client.isConnected()) Client.disconnect();
        super.stop();
        System.exit(0); // TODO : Check to see why thread is still working
    }

    public static void main(String[] args) {
        launch();
    }
}