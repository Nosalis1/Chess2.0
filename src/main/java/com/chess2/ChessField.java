package com.chess2;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class ChessField extends Rectangle {

    public final Color FIELD_DARK_COLOR = Color.valueOf("#B58863");
    public final Color FIELD_LIGHT_COLOR = Color.valueOf("#F0D9B5");


    public final Color FIELD_SELECTED_DARK_COLOR = Color.valueOf("#8C5C2E");
    public final Color FIELD_SELECTED_LIGHT_COLOR = Color.valueOf("#DDBB8E");


    public final Color FIELD_MOVE_DARK_COLOR = Color.valueOf("#4CAF50");
    public final Color FIELD_MOVE_LIGHT_COLOR = Color.valueOf("#8BC34A");


    public final Color FIELD_ATTACKING_DARK_COLOR = Color.valueOf("#DC143C");
    public final Color FIELD_ATTACKING_LIGHT_COLOR = Color.valueOf("#FFA07A");

    private final boolean dark;

    public ChessField(final int row, final int col) {
        super(App.CELL_SIZE, App.CELL_SIZE);
        this.dark = (row + col) % 2 == 0;
        this.reset();
        super.setOnMouseClicked(new UserInput(col, row));
    }

    public void select() {
        super.setFill(this.dark ? FIELD_SELECTED_DARK_COLOR : FIELD_SELECTED_LIGHT_COLOR);
    }

    public void highlightMove() {
        super.setFill(this.dark ? FIELD_MOVE_DARK_COLOR : FIELD_MOVE_LIGHT_COLOR);
    }

    public void highlightAttack(){
        super.setFill(this.dark ? FIELD_ATTACKING_DARK_COLOR : FIELD_ATTACKING_LIGHT_COLOR);
    }

    public void reset() {
        super.setFill(this.dark ? FIELD_DARK_COLOR : FIELD_LIGHT_COLOR);
    }
}