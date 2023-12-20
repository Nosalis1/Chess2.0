package com.chess2.ai;

import com.chess2.ChessMove;
import com.chess2.Game;
import com.chess2.pieces.ChessPiece;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomAI implements ChessAI {
    @Override
    public void handleMove() {
        final List<ChessMove> validMoves = new ArrayList<>();
        for (ChessPiece piece : Game.instance.board.getBlackPieces()) {
            validMoves.addAll(piece.getValidMoves(Game.instance.board.getPieces()));
        }

        Random prng = new Random();
        ChessMove move = validMoves.get(prng.nextInt(0, validMoves.size()));

        Game.instance.makeMove(
                move.getFrom().row(), move.getFrom().col(),
                move.getTo().row(), move.getTo().col()
        );
    }
}