package com.chess2.ai;

import com.chess2.Game;
import com.chess2.pieces.ChessPiece;
import com.chess2.utility.Move;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomAI implements ChessAI {
    @Override
    public void handleMove() {
        final List<Move> validMoves = new ArrayList<>();
        for (ChessPiece piece : Game.instance.board.getBlackPieces()) {
            validMoves.addAll(piece.getValidMoves(Game.instance.board.getPieces()));
        }

        Random prng = new Random();
        Move move = validMoves.get(prng.nextInt(0, validMoves.size()));

        Game.instance.makeMove(
                move.from().getX(), move.from().getY(),
                move.to().getX(), move.to().getY()
        );
    }
}