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

    private static final Rectangle selectionHighlight = new Rectangle() {{
        setFill(Color.TRANSPARENT);
        setStroke(Color.RED);
        setWidth(App.CELL_SIZE - 2);
        setHeight(App.CELL_SIZE - 2);
        setStrokeWidth(2);
        setMouseTransparent(true);
    }};

    private static void setSelectionHighlight() {
        if (selected == null) return;
        Game.instance.root.add(selectionHighlight, selected.position.fxRow(), selected.position.fxCol());
    }

    private static void clearSelectionHighlight() {
        Game.instance.root.getChildren().remove(selectionHighlight);
    }

    private static final List<Circle> moveHighlights = new ArrayList<>();

    private static void setMoveHighlights() {
        if (selected == null) return;
        List<ChessMove> legalMoves = selected.getValidMoves(Game.instance.board.getPieces());
        for (ChessMove move : legalMoves) {
            final int row = move.getTo().fxRow(), col = move.getTo().fxCol();
            Circle circle = new Circle() {{
                setFill(Color.TRANSPARENT);
                setStroke((row + col) % 2 == 0 ? Color.valueOf("#008080") : Color.valueOf("#FF6F61"));
                setRadius((double) App.CELL_SIZE / 2 - 2);
                setStrokeWidth(2);
                setMouseTransparent(true);
            }};
            moveHighlights.add(circle);
            Game.instance.root.add(circle, row, col);
        }
    }

    private static void clearMoveHighlights() {
        for (Circle circle : moveHighlights) {
            Game.instance.root.getChildren().remove(circle);
        }
        moveHighlights.clear();
    }

    private static ChessPiece selected = null;

    @Override
    public void handle(MouseEvent mouseEvent) {
        clearSelectionHighlight();
        clearMoveHighlights();

        if (selected == null) {
            selected = Game.instance.board.getPiece(row, col);
            if (selected != null) {
                setSelectionHighlight();
                setMoveHighlights();
            }
        } else {
            final ChessPiece target = Game.instance.board.getPiece(row, col);
            if (target != null && target.isWhite() == selected.isWhite()) {
                selected = target;
                setSelectionHighlight();
                setMoveHighlights();
            } else {
                Game.instance.makeMove(selected, row, col);
                selected = null;
            }
        }
    }
}