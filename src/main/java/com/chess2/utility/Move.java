package com.chess2.utility;

import com.chess2.Game;
import com.chess2.pieces.ChessPiece;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public record Move(Int2 from, Int2 to) {

    /**
     * Checks if the move coordinates are within the specified range.
     *
     * @param min The minimum allowed coordinate value.
     * @param max The maximum allowed coordinate value.
     * @return {@code true} if both 'from' and 'to' are within the range, {@code false} otherwise.
     */
    public boolean inBound(final int min, final int max) {
        return this.from.inBound(min, max) && this.to.inBound(min, max);
    }

    /**
     * Clamps the move coordinates within the specified range.
     *
     * @param min The minimum allowed coordinate value.
     * @param max The maximum allowed coordinate value.
     */
    public void clamp(final int min, final int max) {
        if (this.from instanceof MutableInt2 from) {
            from.clamp(min, max);
        }
        if (this.to instanceof MutableInt2 to) {
            to.clamp(min, max);
        }
    }

    /**
     * Checks if the move represents an attacking move.
     *
     * @return {@code true} if the move is attacking, {@code false} otherwise.
     */
    public boolean isAttackingMove() {
        final ChessPiece piece = Game.instance.board.getPiece(from);
        final ChessPiece field = Game.instance.board.getPiece(to);
        return piece != null && field != null && piece.isWhite() != field.isWhite();
    }

    /**
     * Checks if the move is a valid move on the current game board.
     *
     * @return {@code true} if the move is valid, {@code false} otherwise.
     */
    public boolean isValidMove() {
        final ChessPiece piece = Game.instance.board.getPiece(from);
        return piece != null && piece.isValidMove(this, Game.instance.board);
    }

    /**
     * Creates a shallow copy of the move.
     *
     * @return A shallow copy of the move.
     */
    public @NotNull Move shallowCopy() {
        return new Move(this.from, this.to);
    }

    /**
     * Creates a deep copy of the move.
     *
     * @return A deep copy of the move.
     */
    public @NotNull Move deepCopy() {
        return new Move(this.from.copy(), this.to.copy());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Move move = (Move) o;
        return Objects.equals(from, move.from) && Objects.equals(to, move.to);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to);
    }

    @Override
    public String toString() {
        return squareToString(from) + " -> " + squareToString(to);
    }

    private String squareToString(Int2 square) {
        if (square == null) {
            return "null";
        }
        char colChar = (char) ('A' + square.getY());
        int row = square.getX() + 1;
        return colChar + Integer.toString(row);
    }
}