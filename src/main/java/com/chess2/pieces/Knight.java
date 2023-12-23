package com.chess2.pieces;

import com.chess2.utility.MutableInt2;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a knight chess piece.
 * Knights move in an L-shaped pattern: two squares in one direction and one square perpendicular to that direction.
 * Knights can jump over other pieces on the board.
 */
public class Knight extends ChessPiece {

    /**
     * Constructs a knight with the specified color.
     *
     * @param color The color of the knight.
     */
    public Knight(final PieceColor color) {
        super(color);
    }

    /**
     * Constructs a knight with the specified color and position.
     *
     * @param color    The color of the knight.
     * @param position The position of the knight on the chessboard.
     */
    public Knight(final PieceColor color, final @NotNull MutableInt2 position) {
        super(color, position);
    }

    /**
     * Checks if a move from the start position to the target position is a valid knight move.
     * Knights move in an L-shaped pattern: two squares in one direction and one square perpendicular to that direction.
     * Knights can jump over other pieces on the board.
     *
     * @param startX  The x-coordinate of the start position.
     * @param startY  The y-coordinate of the start position.
     * @param targetX The x-coordinate of the target position.
     * @param targetY The y-coordinate of the target position.
     * @param board   The current configuration of chess pieces on the chessboard.
     * @return {@code true} if the move is a valid knight move, {@code false} otherwise.
     */
    @Override
    public boolean isValidMove(int startX, int startY, int targetX, int targetY, @NotNull ChessPiece[][] board) {
        int deltaX = Math.abs(targetX - startX);
        int deltaY = Math.abs(targetY - startY);
        if ((deltaX == 2 && deltaY == 1) || (deltaX == 1 && deltaY == 2)) {
            return board[targetX][targetY] == null || board[targetX][targetY].isWhite() != isWhite();
        }
        return false;
    }

    /**
     * Gets the value of the knight.
     *
     * @return The value of the knight.
     */
    @Override
    public int getValue() {
        return 3;
    }

    /**
     * Creates a shallow copy of the knight.
     *
     * @return A shallow copy of the knight.
     */
    @Override
    public ChessPiece shallowCopy() {
        return new Knight(this.isWhite() ? PieceColor.WHITE : PieceColor.BLACK, this.getPosition());
    }

    /**
     * Creates a deep copy of the knight.
     *
     * @return A deep copy of the knight.
     */
    @Override
    public ChessPiece deepCopy() {
        return new Knight(this.isWhite() ? PieceColor.WHITE : PieceColor.BLACK, (MutableInt2) this.getPosition().copy());
    }
}