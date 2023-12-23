package com.chess2.pieces;

import com.chess2.utility.MutableInt2;
import org.jetbrains.annotations.NotNull;

import static com.chess2.pieces.ChessPiece.PieceColor.BLACK;
import static com.chess2.pieces.ChessPiece.PieceColor.WHITE;

/**
 * Represents a king chess piece.
 * The king moves one square in any direction and is a crucial piece for checkmate.
 * Additionally, it may participate in a special move known as castling.
 */
public class King extends ChessPiece {

    /**
     * Constructs a king with the specified color.
     *
     * @param color The color of the king.
     */
    public King(final PieceColor color) {
        super(color);
    }

    /**
     * Constructs a king with the specified color and position.
     *
     * @param color    The color of the king.
     * @param position The position of the king on the chessboard.
     */
    public King(final PieceColor color, final @NotNull MutableInt2 position) {
        super(color, position);
    }

    /**
     * Checks if a move from the start position to the target position is a valid king move.
     * Kings can move one square in any direction and may also participate in castling.
     *
     * @param startX  The x-coordinate of the start position.
     * @param startY  The y-coordinate of the start position.
     * @param targetX The x-coordinate of the target position.
     * @param targetY The y-coordinate of the target position.
     * @param board   The current configuration of chess pieces on the chessboard.
     * @return {@code true} if the move is a valid king move, {@code false} otherwise.
     */
    @Override
    public boolean isValidMove(int startX, int startY, int targetX, int targetY, @NotNull ChessPiece[][] board) {
        int deltaX = Math.abs(targetX - startX);
        int deltaY = Math.abs(targetY - startY);

        if ((deltaX == 1 && deltaY == 0) || (deltaX == 0 && deltaY == 1) || (deltaX == 1 && deltaY == 1)) {
            return board[targetX][targetY] == null || board[targetX][targetY].isWhite() != isWhite();
        }

        // Check for castling (special move for the king)
        // Add your castling logic here if applicable

        return false;
    }

    /**
     * Gets the value of the king.
     *
     * @return The value of the king.
     */
    @Override
    public int getValue() {
        return 100;
    }

    /**
     * Creates a shallow copy of the king.
     *
     * @return A shallow copy of the king.
     */
    @Override
    public ChessPiece shallowCopy() {
        return new King(this.isWhite() ? WHITE : BLACK, this.getPosition());
    }

    /**
     * Creates a deep copy of the king.
     *
     * @return A deep copy of the king.
     */
    @Override
    public ChessPiece deepCopy() {
        return new King(this.isWhite() ? WHITE : BLACK, (MutableInt2) this.getPosition().copy());
    }
}