package com.chess2.pieces;

import com.chess2.App;
import com.chess2.utility.MutableInt2;
import org.jetbrains.annotations.NotNull;

import static com.chess2.pieces.ChessPiece.PieceColor.BLACK;
import static com.chess2.pieces.ChessPiece.PieceColor.WHITE;

/**
 * Represents a pawn chess piece.
 * Pawns have unique movement rules, including an initial two-square advance option and capturing diagonally.
 * Pawns can also have special moves like en passant and pawn promotion.
 */
public class Pawn extends ChessPiece {

    /**
     * Constructs a pawn with the specified color.
     *
     * @param color The color of the pawn.
     */
    public Pawn(final PieceColor color) {
        super(color);
    }

    /**
     * Constructs a pawn with the specified color and position.
     *
     * @param color    The color of the pawn.
     * @param position The position of the pawn on the chessboard.
     */
    public Pawn(final PieceColor color, final @NotNull MutableInt2 position) {
        super(color, position);
    }

    /**
     * Checks if a move from the start position to the target position is a valid pawn move.
     *
     * @param startX  The x-coordinate of the start position.
     * @param startY  The y-coordinate of the start position.
     * @param targetX The x-coordinate of the target position.
     * @param targetY The y-coordinate of the target position.
     * @param board   The current configuration of chess pieces on the chessboard.
     * @return {@code true} if the move is a valid pawn move, {@code false} otherwise.
     */
    @Override
    public boolean isValidMove(int startX, int startY, int targetX, int targetY, @NotNull ChessPiece[][] board) {
        int direction = (isWhite()) ? -1 : 1;

        if (!isValidPosition(startX, startY) || !isValidPosition(targetX, targetY)) {
            return false;
        }
        if (startY == targetY && startX + direction == targetX && board[targetX][targetY] == null) {
            return true;
        }
        if (startY == targetY && startX + 2 * direction == targetX && startX == getStartingRow() &&
                board[startX + direction][targetY] == null && board[targetX][targetY] == null) {
            return true;
        }
        if (Math.abs(startY - targetY) == 1 && startX + direction == targetX &&
                board[targetX][targetY] != null && board[targetX][targetY].isWhite() != isWhite()) {
            return true;
        }

        // Add en passant or promotion logic here if needed

        return false;
    }

    /**
     * Gets the value of the pawn.
     *
     * @return The value of the pawn.
     */
    @Override
    public int getValue() {
        return 1;
    }

    /**
     * Creates a shallow copy of the pawn.
     *
     * @return A shallow copy of the pawn.
     */
    @Override
    public ChessPiece shallowCopy() {
        return new Pawn(this.isWhite() ? WHITE : BLACK, this.getPosition());
    }

    /**
     * Creates a deep copy of the pawn.
     *
     * @return A deep copy of the pawn.
     */
    @Override
    public ChessPiece deepCopy() {
        return new Pawn(this.isWhite() ? WHITE : BLACK, (MutableInt2) this.getPosition().copy());
    }

    /**
     * Checks if the given position is within the valid bounds of the chessboard.
     *
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     * @return {@code true} if the position is valid, {@code false} otherwise.
     */
    private boolean isValidPosition(int x, int y) {
        return x >= 0 && x < App.CELL_COUNT && y >= 0 && y < App.CELL_COUNT;
    }

    /**
     * Gets the starting row of the pawn based on its color.
     *
     * @return The starting row.
     */
    private int getStartingRow() {
        return (isWhite()) ? 6 : 1;
    }
}