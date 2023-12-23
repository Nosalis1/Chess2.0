package com.chess2.pieces;

import com.chess2.utility.MutableInt2;
import org.jetbrains.annotations.NotNull;

import static com.chess2.pieces.ChessPiece.PieceColor.BLACK;
import static com.chess2.pieces.ChessPiece.PieceColor.WHITE;

/**
 * Represents a rook chess piece.
 * Rooks move horizontally or vertically along ranks and files on the chessboard.
 */
public class Rook extends ChessPiece {

    /**
     * Constructs a rook with the specified color.
     *
     * @param color The color of the rook.
     */
    public Rook(final PieceColor color) {
        super(color);
    }

    /**
     * Constructs a rook with the specified color and position.
     *
     * @param color    The color of the rook.
     * @param position The position of the rook on the chessboard.
     */
    public Rook(final PieceColor color, final @NotNull MutableInt2 position) {
        super(color, position);
    }

    /**
     * Checks if a move from the start position to the target position is a valid rook move.
     *
     * @param startX  The x-coordinate of the start position.
     * @param startY  The y-coordinate of the start position.
     * @param targetX The x-coordinate of the target position.
     * @param targetY The y-coordinate of the target position.
     * @param board   The current configuration of chess pieces on the chessboard.
     * @return {@code true} if the move is a valid rook move, {@code false} otherwise.
     */
    @Override
    public boolean isValidMove(int startX, int startY, int targetX, int targetY, @NotNull ChessPiece[][] board) {
        if (!isHorizontalOrVerticalMove(startX, startY, targetX, targetY)) {
            return false;
        }
        if (!isPathClear(startX, startY, targetX, targetY, board)) {
            return false;
        }
        return board[targetX][targetY] == null || board[targetX][targetY].isWhite() != isWhite();
    }

    /**
     * Checks if the move is along a horizontal or vertical path.
     *
     * @param startX  The x-coordinate of the start position.
     * @param startY  The y-coordinate of the start position.
     * @param targetX The x-coordinate of the target position.
     * @param targetY The y-coordinate of the target position.
     * @return {@code true} if the move is along a horizontal or vertical path, {@code false} otherwise.
     */
    private boolean isHorizontalOrVerticalMove(int startX, int startY, int targetX, int targetY) {
        return startX == targetX || startY == targetY;
    }

    /**
     * Checks if the path between the start and target positions is clear of obstructions.
     *
     * @param startX  The x-coordinate of the start position.
     * @param startY  The y-coordinate of the start position.
     * @param targetX The x-coordinate of the target position.
     * @param targetY The y-coordinate of the target position.
     * @param board   The current configuration of chess pieces on the chessboard.
     * @return {@code true} if the path is clear, {@code false} otherwise.
     */
    private boolean isPathClear(int startX, int startY, int targetX, int targetY, @NotNull ChessPiece[][] board) {
        int stepX = Integer.compare(targetX, startX);
        int stepY = Integer.compare(targetY, startY);

        for (int i = 1; i < Math.max(Math.abs(targetX - startX), Math.abs(targetY - startY)); i++) {
            int nextX = startX + i * stepX;
            int nextY = startY + i * stepY;

            if (board[nextX][nextY] != null) {
                return false; // There's a piece blocking the path
            }
        }
        return true; // Path is clear
    }

    /**
     * Gets the value of the rook.
     *
     * @return The value of the rook.
     */
    @Override
    public int getValue() {
        return 5;
    }

    /**
     * Creates a shallow copy of the rook.
     *
     * @return A shallow copy of the rook.
     */
    @Override
    public ChessPiece shallowCopy() {
        return new Rook(this.isWhite() ? WHITE : BLACK, this.getPosition());
    }

    /**
     * Creates a deep copy of the rook.
     *
     * @return A deep copy of the rook.
     */
    @Override
    public ChessPiece deepCopy() {
        return new Rook(this.isWhite() ? WHITE : BLACK, (MutableInt2) this.getPosition().copy());
    }
}