package com.chess2;

import com.chess2.pieces.ChessPiece;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;

public record UserInput(int col, int row) implements EventHandler<MouseEvent> {
    private static final Rectangle selectionRect = new Rectangle() {{
        setFill(Color.TRANSPARENT);
        setStroke(Color.RED);
        setWidth(App.CELL_SIZE - 2);
        setHeight(App.CELL_SIZE - 2);
        setStrokeWidth(2);
        setMouseTransparent(true);
    }};
    private static ChessPiece selectedPiece = null;
    private static List<Circle> circles = new ArrayList<>();

    @Override
    public void handle(MouseEvent mouseEvent) {
        Game.instance.root.getChildren().remove(selectionRect);
        for (Circle circle : circles)
            Game.instance.root.getChildren().remove(circle);
        if (selectedPiece == null) {
            selectedPiece = Game.instance.board.getPiece(row, col);
            if (selectedPiece != null) {
                Game.instance.root.add(selectionRect, col, row);

                List<ChessMove> legalMoves = selectedPiece.getValidMoves(Game.instance.board.getPieces());
                circles.clear();
                for (ChessMove move : legalMoves) {
                    final int row = move.getTo().fxRow(), col = move.getTo().fxCol();
                    Circle circle = new Circle() {{
                        setFill(Color.TRANSPARENT);
                        setStroke((row + col) % 2 == 0 ? Color.valueOf("#008080") : Color.valueOf("#FF6F61"));
                        setRadius((double) App.CELL_SIZE / 2 - 2);
                        setStrokeWidth(2);
                        setMouseTransparent(true);
                    }};
                    circles.add(circle);
                    Game.instance.root.add(circle, row, col);
                }
            }
        } else {
            if (selectedPiece != Game.instance.board.getPiece(row, col))
                Game.instance.makeMove(selectedPiece, row, col);
            selectedPiece = null;
        }
    }
}