package com.chess2.pieces;

import com.chess2.utility.MutableInt2;
import org.jetbrains.annotations.NotNull;

import static com.chess2.pieces.ChessPiece.PieceColor.BLACK;
import static com.chess2.pieces.ChessPiece.PieceColor.WHITE;

/**
 * Represents a bishop chess piece.
 * A bishop moves diagonally on the chessboard and captures opponents in its path.
 */
public class Bishop extends ChessPiece {

    /**
     * Constructs a bishop with the specified color.
     *
     * @param color The color of the bishop.
     */
    public Bishop(final PieceColor color) {
        super(color);
    }

    /**
     * Constructs a bishop with the specified color and position.
     *
     * @param color    The color of the bishop.
     * @param position The position of the bishop on the chessboard.
     */
    public Bishop(final PieceColor color, final @NotNull MutableInt2 position) {
        super(color, position);
    }

    /**
     * Checks if a move from the start position to the target position is a valid bishop move.
     *
     * @param startX  The x-coordinate of the start position.
     * @param startY  The y-coordinate of the start position.
     * @param targetX The x-coordinate of the target position.
     * @param targetY The y-coordinate of the target position.
     * @param pieces  The current configuration of chess pieces on the chessboard.
     * @return {@code true} if the move is a valid bishop move, {@code false} otherwise.
     */
    @Override
    public boolean isValidMove(int startX, int startY, int targetX, int targetY, @NotNull ChessPiece[][] pieces) {
        int deltaX = Math.abs(targetX - startX);
        int deltaY = Math.abs(targetY - startY);

        if (deltaX == deltaY) {
            int stepX = Integer.compare(targetX, startX);
            int stepY = Integer.compare(targetY, startY);

            for (int i = 1; i < Math.abs(targetX - startX); i++) {
                int nextX = startX + i * stepX;
                int nextY = startY + i * stepY;

                if (pieces[nextX][nextY] != null) {
                    return false;
                }
            }
            return pieces[targetX][targetY] == null || pieces[targetX][targetY].isWhite() != isWhite();
        }
        return false;
    }

    /**
     * Gets the value of the bishop.
     *
     * @return The value of the bishop.
     */
    @Override
    public int getValue() {
        return 3;
    }

    /**
     * Creates a shallow copy of the bishop.
     *
     * @return A shallow copy of the bishop.
     */
    @Override
    public ChessPiece shallowCopy() {
        return new Bishop(this.isWhite() ? WHITE : BLACK, this.getPosition());
    }

    /**
     * Creates a deep copy of the bishop.
     *
     * @return A deep copy of the bishop.
     */
    @Override
    public ChessPiece deepCopy() {
        return new Bishop(this.isWhite() ? WHITE : BLACK, (MutableInt2) this.getPosition().copy());
    }
}