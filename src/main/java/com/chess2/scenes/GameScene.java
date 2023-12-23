package com.chess2.scenes;

import com.chess2.Game;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;

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

        root.getChildren().addAll(Game.instance.root, Game.instance.stats);
    }
}