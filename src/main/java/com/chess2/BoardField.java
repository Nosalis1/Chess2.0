package com.chess2;

import com.chess2.pieces.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class BoardField {

    public static final Color FIELD_DARK_COLOR = Color.valueOf("#B58863");
    public static final Color FIELD_LIGHT_COLOR = Color.valueOf("#F0D9B5");


    public static final Color FIELD_SELECTED_DARK_COLOR = Color.valueOf("#8C5C2E");
    public static final Color FIELD_SELECTED_LIGHT_COLOR = Color.valueOf("#DDBB8E");


    public static final Color FIELD_MOVE_DARK_COLOR = Color.valueOf("#4CAF50");
    public static final Color FIELD_MOVE_LIGHT_COLOR = Color.valueOf("#8BC34A");


    public static final Color FIELD_ATTACKING_DARK_COLOR = Color.valueOf("#DC143C");
    public static final Color FIELD_ATTACKING_LIGHT_COLOR = Color.valueOf("#FFA07A");

    private final Rectangle rectangle;
    private final ImageView imageView;
    private final boolean dark;

    public BoardField(final GridPane gridPane, final int row, final int col) {
        this.rectangle = new Rectangle(App.CELL_SIZE, App.CELL_SIZE);
        this.imageView = new ImageView();
        this.imageView.setFitWidth(App.CELL_SIZE);
        this.imageView.setFitHeight(App.CELL_SIZE);
        this.imageView.setMouseTransparent(true);
        this.rectangle.setOnMouseClicked(new UserInput(col, row));
        this.dark = (row + col) % 2 == 0;
        this.reset();

        gridPane.add(this.rectangle, col, row);
        gridPane.add(this.imageView, col, row);
    }

    public void select() {
        this.rectangle.setFill(this.dark ? FIELD_SELECTED_DARK_COLOR : FIELD_SELECTED_LIGHT_COLOR);
    }

    public void highlightMove() {
        this.rectangle.setFill(this.dark ? FIELD_MOVE_DARK_COLOR : FIELD_MOVE_LIGHT_COLOR);
    }

    public void highlightAttack() {
        this.rectangle.setFill(this.dark ? FIELD_ATTACKING_DARK_COLOR : FIELD_ATTACKING_LIGHT_COLOR);
    }

    public void reset() {
        this.rectangle.setFill(this.dark ? FIELD_DARK_COLOR : FIELD_LIGHT_COLOR);
    }

    public void setPiece(final ChessPiece piece) {
        if (piece == null) {
            this.imageView.setImage(null);
        } else if (piece instanceof Bishop) {
            this.imageView.setImage(Assets.getImage(piece.isWhite() ? "w_bishop_png_shadow_256px" : "b_bishop_png_shadow_256px"));
        } else if (piece instanceof King) {
            this.imageView.setImage(Assets.getImage(piece.isWhite() ? "w_king_png_shadow_256px" : "b_king_png_shadow_256px"));
        } else if (piece instanceof Knight) {
            this.imageView.setImage(Assets.getImage(piece.isWhite() ? "w_knight_png_shadow_256px" : "b_knight_png_shadow_256px"));
        } else if (piece instanceof Pawn) {
            this.imageView.setImage(Assets.getImage(piece.isWhite() ? "w_pawn_png_shadow_256px" : "b_pawn_png_shadow_256px"));
        } else if (piece instanceof Queen) {
            this.imageView.setImage(Assets.getImage(piece.isWhite() ? "w_queen_png_shadow_256px" : "b_queen_png_shadow_256px"));
        } else if (piece instanceof Rook) {
            this.imageView.setImage(Assets.getImage(piece.isWhite() ? "w_rook_png_shadow_256px" : "b_rook_png_shadow_256px"));
        }
    }
}