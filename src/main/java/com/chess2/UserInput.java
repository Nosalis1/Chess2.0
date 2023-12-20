package com.chess2;

import com.chess2.pieces.ChessPiece;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import java.util.List;

public record UserInput(int col, int row) implements EventHandler<MouseEvent> {
    private static ChessPiece selectedPiece = null;

    @Override
    public void handle(MouseEvent mouseEvent) {
        for (ChessField[] fields : Game.instance.getFields())
            for (ChessField field : fields)
                field.reset();

        if (selectedPiece == null) {
            selectedPiece = Game.instance.board.getPiece(row, col);
            if (selectedPiece != null) {
                Game.instance.getFields()[row][col].select();

                List<ChessMove> legalMoves = selectedPiece.getValidMoves(Game.instance.board.getPieces());
                for (ChessMove move : legalMoves) {
                    final int row = move.getTo().fxRow(), col = move.getTo().fxCol();
                    if (move.isAttackingMove())
                        Game.instance.getFields()[col][row].highlightAttack();
                    else
                        Game.instance.getFields()[col][row].highlightMove();
                }
            }
        } else {
            if (selectedPiece != Game.instance.board.getPiece(row, col))
                Game.instance.makeMove(selectedPiece, row, col);
            selectedPiece = null;
        }
    }
}