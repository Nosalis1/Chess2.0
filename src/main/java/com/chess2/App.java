package com.chess2;

import com.chess2.ai.ABPruningAI;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class App extends Application {
    public static final int CELL_SIZE = 80;
    public static final int CELL_COUNT = 8;

    @Override
    public void start(Stage stage) {
        Assets.initialize();

        stage.setTitle("Chess 2.0");
        stage.getIcons().add(Assets.getImage("chess-board"));

        StackPane stackPane = new StackPane();
        stackPane.getChildren().add(new ImageView() {{
            setImage(Assets.getImage("chessBackground"));
            setFitWidth(CELL_SIZE * CELL_COUNT);
            setFitHeight(CELL_SIZE * CELL_COUNT);
        }});
        stackPane.getChildren().add(new BorderPane() {{
            setCenter(
                    new VBox(
                            new Label("Chess 2.0"),
                            new Button("AI game") {{
                                setOnAction(actionEvent -> {
                                    Game.instance.reset();
                                    HBox hBox = new HBox(Game.instance.root, Game.instance.stats);
                                    stage.setScene(new Scene(hBox, CELL_SIZE * (CELL_COUNT + 2), CELL_SIZE * CELL_COUNT));
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
        }});
        stage.setScene(new Scene(
                stackPane,
                CELL_SIZE * CELL_COUNT,
                CELL_SIZE * CELL_COUNT)
        );
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        ABPruningAI.shutdownThreadPool();
        super.stop();
    }

    public static void main(String[] args) {
        launch();
    }
}