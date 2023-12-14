package com.chess2;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class App extends Application {
    public static final int CELL_SIZE = 80;
    public static final int CELL_COUNT = 8;

    @Override
    public void start(Stage stage) {
        stage.setTitle("Chess 2.0");

        Assets.initialize();
        stage.setScene(new Scene(
                new BorderPane() {{
                    setCenter(
                            new VBox(
                                    new Label("Chess 2.0"),
                                    new Button("AI game") {{
                                        setOnAction(actionEvent -> {
                                            Game.instance.reset();
                                            stage.setScene(new Scene(Game.instance.root, CELL_SIZE * CELL_COUNT, CELL_SIZE * CELL_COUNT));
                                        });
                                    }},
                                    new Button("Online game") {{
                                        setOnAction(actionEvent -> {

                                        });
                                    }}
                            ) {{
                                setAlignment(Pos.CENTER);
                                setSpacing(10);
                            }}
                    );
                }},
                CELL_SIZE * CELL_COUNT,
                CELL_SIZE * CELL_COUNT)
        );
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}