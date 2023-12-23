package com.chess2.pieces;

import com.chess2.utility.MutableInt2;
import org.jetbrains.annotations.NotNull;

import static com.chess2.pieces.ChessPiece.PieceColor.BLACK;
import static com.chess2.pieces.ChessPiece.PieceColor.WHITE;

/**
 * Represents a queen chess piece.
 * Queens have combined movement rules of rooks and bishops, allowing them to move horizontally, vertically, and diagonally.
 */
public class Queen extends ChessPiece {

    /**
     * Constructs a queen with the specified color.
     *
     * @param color The color of the queen.
     */
    public Queen(final PieceColor color) {
        super(color);
    }

    /**
     * Constructs a queen with the specified color and position.
     *
     * @param color    The color of the queen.
     * @param position The position of the queen on the chessboard.
     */
    public Queen(final PieceColor color, final @NotNull MutableInt2 position) {
        super(color, position);
    }

    /**
     * Checks if a move from the start position to the target position is a valid queen move.
     *
     * @param startX  The x-coordinate of the start position.
     * @param startY  The y-coordinate of the start position.
     * @param targetX The x-coordinate of the target position.
     * @param targetY The y-coordinate of the target position.
     * @param board   The current configuration of chess pieces on the chessboard.
     * @return {@code true} if the move is a valid queen move, {@code false} otherwise.
     */
    @Override
    public boolean isValidMove(int startX, int startY, int targetX, int targetY, @NotNull ChessPiece[][] board) {
        if (!isHorizontalVerticalOrDiagonal(startX, startY, targetX, targetY)) {
            return false;
        }
        if (!isPathClear(startX, startY, targetX, targetY, board)) {
            return false;
        }
        return board[targetX][targetY] == null || board[targetX][targetY].isWhite() != isWhite();
    }

    /**
     * Checks if the move is along a horizontal, vertical, or diagonal path.
     *
     * @param startX  The x-coordinate of the start position.
     * @param startY  The y-coordinate of the start position.
     * @param targetX The x-coordinate of the target position.
     * @param targetY The y-coordinate of the target position.
     * @return {@code true} if the move is along a horizontal, vertical, or diagonal path, {@code false} otherwise.
     */
    private boolean isHorizontalVerticalOrDiagonal(int startX, int startY, int targetX, int targetY) {
        return startX == targetX || startY == targetY || Math.abs(startX - targetX) == Math.abs(startY - targetY);
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
        if (startX == targetX || startY == targetY) {
            if (!isVerticalClear(startX, startY, targetX, targetY, board)) {
                return false;
            }
            if (!isHorizontalClear(startX, startY, targetX, targetY, board)) {
                return false;
            }
        } else {
            return isDiagonalClear(startX, startY, targetX, targetY, board);
        }
        return true;
    }

    /**
     * Checks if the vertical path between start and target positions is clear.
     *
     * @param startX  The x-coordinate of the start position.
     * @param startY  The y-coordinate of the start position.
     * @param targetX The x-coordinate of the target position.
     * @param targetY The y-coordinate of the target position.
     * @param board   The current configuration of chess pieces on the chessboard.
     * @return {@code true} if the vertical path is clear, {@code false} otherwise.
     */
    private boolean isVerticalClear(int startX, int startY, int targetX, int targetY, @NotNull ChessPiece[][] board) {
        if (startY != targetY) {
            int step = Integer.compare(targetY, startY);
            for (int i = 1; i < Math.abs(startY - targetY); i++) {
                int nextY = startY + i * step;
                if (board[startX][nextY] != null) {
                    return false; // There's a piece blocking the path
                }
            }
        }
        return true; // Path is clear
    }

    /**
     * Checks if the horizontal path between start and target positions is clear.
     *
     * @param startX  The x-coordinate of the start position.
     * @param startY  The y-coordinate of the start position.
     * @param targetX The x-coordinate of the target position.
     * @param targetY The y-coordinate of the target position.
     * @param board   The current configuration of chess pieces on the chessboard.
     * @return {@code true} if the horizontal path is clear, {@code false} otherwise.
     */
    private boolean isHorizontalClear(int startX, int startY, int targetX, int targetY, @NotNull ChessPiece[][] board) {
        if (startX != targetX) {
            int step = Integer.compare(targetX, startX);
            for (int i = 1; i < Math.abs(startX - targetX); i++) {
                int nextX = startX + i * step;
                if (board[nextX][startY] != null) {
                    return false; // There's a piece blocking the path
                }
            }
        }
        return true; // Path is clear
    }

    /**
     * Checks if the diagonal path between start and target positions is clear.
     *
     * @param startX  The x-coordinate of the start position.
     * @param startY  The y-coordinate of the start position.
     * @param targetX The x-coordinate of the target position.
     * @param targetY The y-coordinate of the target position.
     * @param board   The current configuration of chess pieces on the chessboard.
     * @return {@code true} if the diagonal path is clear, {@code false} otherwise.
     */
    private boolean isDiagonalClear(int startX, int startY, int targetX, int targetY, @NotNull ChessPiece[][] board) {
        if (Math.abs(startX - targetX) == Math.abs(startY - targetY)) {
            int stepX = Integer.compare(targetX, startX);
            int stepY = Integer.compare(targetY, startY);
            for (int i = 1; i < Math.abs(targetX - startX); i++) {
                int nextX = startX + i * stepX;
                int nextY = startY + i * stepY;
                if (board[nextX][nextY] != null) {
                    return false; // There's a piece blocking the path
                }
            }
        }
        return true; // Path is clear
    }

    /**
     * Gets the value of the queen.
     *
     * @return The value of the queen.
     */
    @Override
    public int getValue() {
        return 9;
    }

    /**
     * Creates a shallow copy of the queen.
     *
     * @return A shallow copy of the queen.
     */
    @Override
    public ChessPiece shallowCopy() {
        return new Queen(this.isWhite() ? WHITE : BLACK, this.getPosition());
    }

    /**
     * Creates a deep copy of the queen.
     *
     * @return A deep copy of the queen.
     */
    @Override
    public ChessPiece deepCopy() {
        return new Queen(this.isWhite() ? WHITE : BLACK, (MutableInt2) this.getPosition().copy());
    }
}