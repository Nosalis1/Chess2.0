package com.chess2.pieces;

import com.chess2.Assets;
import com.chess2.ChessMove;

import java.util.List;

/**
 * Represents a queen chess piece.
 */
public class Queen extends ChessPiece {

    /**
     * Constructs a queen.
     *
     * @param isWhite Indicates whether the queen is white or black.
     */
    public Queen(final boolean isWhite) {
        super(Assets.getImage(isWhite ? "w_queen_png_shadow_256px" : "b_queen_png_shadow_256px"), isWhite);
    }

    /**
     * Checks if a move is valid for the queen.
     *
     * @param startX  The x-coordinate of the starting position.
     * @param startY  The y-coordinate of the starting position.
     * @param targetX The x-coordinate of the target position.
     * @param targetY The y-coordinate of the target position.
     * @param board   The chessboard represented as a 2D array of chess pieces.
     * @return True if the move is valid, false otherwise.
     */
    @Override
    public boolean isValidMove(int startX, int startY, int targetX, int targetY, ChessPiece[][] board) {
        // Queen can move horizontally, vertically, or diagonally

        // Check if the move is horizontal, vertical, or diagonal
        if (!isHorizontalVerticalOrDiagonal(startX, startY, targetX, targetY)) {
            return false;
        }

        // Check if there are no pieces blocking the path
        if (!isPathClear(startX, startY, targetX, targetY, board)) {
            return false;
        }

        // Check if the target square is empty or has an opponent's piece
        return board[targetX][targetY] == null || board[targetX][targetY].isWhite() != isWhite();
    }

    /**
     * Checks if a move is horizontal, vertical, or diagonal.
     *
     * @param startX  The x-coordinate of the starting position.
     * @param startY  The y-coordinate of the starting position.
     * @param targetX The x-coordinate of the target position.
     * @param targetY The y-coordinate of the target position.
     * @return True if the move is horizontal, vertical, or diagonal, false otherwise.
     */
    private boolean isHorizontalVerticalOrDiagonal(int startX, int startY, int targetX, int targetY) {
        return startX == targetX || startY == targetY || Math.abs(startX - targetX) == Math.abs(startY - targetY);
    }

    /**
     * Checks if there are no pieces blocking the path.
     *
     * @param startX  The x-coordinate of the starting position.
     * @param startY  The y-coordinate of the starting position.
     * @param targetX The x-coordinate of the target position.
     * @param targetY The y-coordinate of the target position.
     * @param board   The chessboard represented as a 2D array of chess pieces.
     * @return True if the path is clear, false if there are pieces blocking the path.
     */
    private boolean isPathClear(int startX, int startY, int targetX, int targetY, ChessPiece[][] board) {
        int stepX = Integer.compare(targetX, startX);
        int stepY = Integer.compare(targetY, startY);

        // For horizontal movement
        if (startX != targetX) {
            for (int i = 1; i < Math.abs(targetX - startX); ++i) {
                int nextX = startX + i * stepX;
                int nextY = startY;

                if (board[nextX][nextY] != null) {
                    return false;
                }
            }
        }

        // For vertical movement
        if (startY != targetY) {
            for (int i = 1; i < Math.abs(targetY - startY); ++i) {
                int nextX = startX;
                int nextY = startY + i * stepY;

                if (board[nextX][nextY] != null) {
                    return false;
                }
            }
        }

        // For diagonal movement
        if (Math.abs(startX - targetX) == Math.abs(startY - targetY)) {
            for (int i = 1; i < Math.abs(targetX - startX); ++i) {
                int nextX = startX + i * stepX;
                int nextY = startY + i * stepY;

                if (board[nextX][nextY] != null) {
                    return false;
                }
            }
        }
        return true; // Path is clear
    }

    /**
     * Gets the value of the queen.
     *
     * @return The numerical value of the piece.
     */
    @Override
    public int getValue() {
        return 9;
    }

    /**
     * Creates a copy of the queen.
     *
     * @return A new instance of the queen with the same properties.
     */
    @Override
    public ChessPiece copy() {
        final Queen temp = new Queen(isWhite());
        temp.position.set(this.position.row(), this.position.col());
        return temp;
    }
}