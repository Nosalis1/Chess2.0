package com.chess2;

import com.chess2.pieces.ChessPiece;

public class ChessMove {

    public ChessMove(int startX, int startY, int targetX, int targetY) {
        this.from.set(startX, startY);
        this.to.set(targetX, targetY);
    }

    public ChessMove(final ChessPosition from, int targetX, int targetY) {
        this.from.set(from.row(), from.col());
        this.to.set(targetX, targetY);
    }

    private final ChessPosition from = new ChessPosition();
    private final ChessPosition to = new ChessPosition();

    public final boolean isAttackingMove() {
        final ChessPiece a = Game.instance.board.getPiece(from.row(), from.col());
        if (a == null) return false;
        final ChessPiece b = Game.instance.board.getPiece(to.row(), to.col());
        if (b == null) return false;

        return a.isWhite() != b.isWhite();
    }

    public final ChessPosition getFrom() {
        return this.from;
    }

    public final ChessPosition getTo() {
        return this.to;
    }
}