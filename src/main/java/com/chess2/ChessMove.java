package com.chess2;

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

    public final ChessPosition getFrom() {
        return this.from;
    }

    public final ChessPosition getTo() {
        return this.to;
    }
}