package com.chess2;

import com.chess2.networking.clientside.Client;
import com.chess2.networking.packets.NetworkMove;
import com.chess2.pieces.ChessPiece;
import com.chess2.utility.Move;
import com.chess2.utility.MutableInt2;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import java.util.List;

public record UserInput(int col, int row) implements EventHandler<MouseEvent> {
    private static ChessPiece selectedPiece = null;

    @Override
    public void handle(MouseEvent mouseEvent) {
        for (BoardField[] fields : Game.instance.getFields())
            for (BoardField field : fields)
                field.reset();

        if (!TurnManagement.isValidTurn()) return;

        if (selectedPiece == null) {
            selectedPiece = Game.instance.board.getPiece(row, col);
            if (selectedPiece != null) {
                Game.instance.getFields()[row][col].select();

                List<Move> legalMoves = selectedPiece.getValidMoves(Game.instance.board.getPieces());
                for (Move move : legalMoves) {
                    final int row = move.to().getY(), col = move.to().getX();
                    if (move.isAttackingMove())
                        Game.instance.getFields()[col][row].highlightAttack();
                    else
                        Game.instance.getFields()[col][row].highlightMove();
                }
            }
        } else {
            if (selectedPiece != Game.instance.board.getPiece(row, col)) {
                if (Client.isConnected()) {
                    final NetworkMove networkMove = new NetworkMove();
                    networkMove.setMove(new Move(selectedPiece.getPosition(), new MutableInt2(row, col)));
                    Client.trySend(networkMove);
                }
                Game.instance.makeMove(selectedPiece, row, col);
            }
            selectedPiece = null;
        }
    }
}