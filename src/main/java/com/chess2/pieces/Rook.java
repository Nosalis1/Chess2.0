package com.chess2.pieces;

import com.chess2.Assets;
import com.chess2.ChessMove;

import java.util.List;

/**
 * Represents a rook chess piece.
 */
public class Rook extends ChessPiece {

    /**
     * Constructs a rook.
     *
     * @param isWhite Indicates whether the rook is white or black.
     */
    public Rook(final boolean isWhite) {
        super(Assets.getImage(isWhite ? "w_rook_png_shadow_256px" : "b_rook_png_shadow_256px"), isWhite);
    }

    /**
     * Checks if a move is valid for the rook.
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
        // Rook can move horizontally or vertically

        // Check if the move is horizontal or vertical
        if (!isHorizontalOrVerticalMove(startX, startY, targetX, targetY)) {
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
     * Checks if a move is horizontal or vertical.
     *
     * @param startX  The x-coordinate of the starting position.
     * @param startY  The y-coordinate of the starting position.
     * @param targetX The x-coordinate of the target position.
     * @param targetY The y-coordinate of the target position.
     * @return True if the move is horizontal or vertical, false otherwise.
     */
    private boolean isHorizontalOrVerticalMove(int startX, int startY, int targetX, int targetY) {
        return startX == targetX || startY == targetY;
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
     * @return The numerical value of the piece.
     */
    @Override
    public int getValue() {
        return 5;
    }

    /**
     * Creates a copy of the rook.
     *
     * @return A new instance of the rook with the same properties.
     */
    @Override
    public ChessPiece copy() {
        final Rook temp = new Rook(isWhite());
        temp.position.set(this.position.row(), this.position.col());
        return temp;
    }
}