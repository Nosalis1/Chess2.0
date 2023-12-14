package com.chess2.pieces;

import com.chess2.Assets;
import com.chess2.ChessMove;

import java.util.List;

/**
 * Represents a knight chess piece.
 */
public class Knight extends ChessPiece {

    /**
     * Constructs a knight.
     *
     * @param isWhite Indicates whether the knight is white or black.
     */
    public Knight(final boolean isWhite) {
        super(Assets.getImage(isWhite ? "w_knight_png_shadow_256px" : "b_knight_png_shadow_256px"), isWhite);
    }

    /**
     * Checks if a move is valid for the knight.
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
        // Knight can move in an L-shape: two squares in one direction and one square perpendicular to that direction

        int deltaX = Math.abs(targetX - startX);
        int deltaY = Math.abs(targetY - startY);

        // Check for the L-shaped move
        if ((deltaX == 2 && deltaY == 1) || (deltaX == 1 && deltaY == 2)) {
            // Check if the target square is empty or has an opponent's piece
            return board[targetX][targetY] == null || board[targetX][targetY].isWhite() != isWhite();
        }

        return false;
    }

    /**
     * Gets the value of the knight.
     *
     * @return The numerical value of the piece.
     */
    @Override
    public int getValue() {
        return 3;
    }

    /**
     * Creates a copy of the knight.
     *
     * @return A new instance of the knight with the same properties.
     */
    @Override
    public ChessPiece copy() {
        final Knight temp = new Knight(isWhite());
        temp.position.set(this.position.row(), this.position.col());
        return temp;
    }
}