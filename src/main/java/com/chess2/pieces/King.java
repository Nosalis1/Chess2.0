package com.chess2.pieces;

import com.chess2.Assets;
import com.chess2.ChessMove;

import java.util.List;

/**
 * Represents a king chess piece.
 */
public class King extends ChessPiece {

    /**
     * Constructs a king.
     *
     * @param isWhite Indicates whether the king is white or black.
     */
    public King(final boolean isWhite) {
        super(Assets.getImage(isWhite ? "w_king_png_shadow_256px" : "b_king_png_shadow_256px"), isWhite);
    }

    /**
     * Checks if a move is valid for the king.
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
        // King can move one square in any direction

        int deltaX = Math.abs(targetX - startX);
        int deltaY = Math.abs(targetY - startY);

        // Check for a regular one-square move
        if ((deltaX == 1 && deltaY == 0) || (deltaX == 0 && deltaY == 1) || (deltaX == 1 && deltaY == 1)) {
            // Check if the target square is empty or has an opponent's piece
            return board[targetX][targetY] == null || board[targetX][targetY].isWhite() != isWhite();
        }

        // Check for castling (special move for the king)
        // Add your castling logic here if applicable

        return false;
    }

    /**
     * Gets the value of the king.
     *
     * @return The numerical value of the piece.
     */
    @Override
    public int getValue() {
        return 100;
    }


    /**
     * Creates a copy of the king.
     *
     * @return A new instance of the king with the same properties.
     */
    @Override
    public ChessPiece copy() {
        final King temp = new King(isWhite());
        temp.position.set(this.position.row(), this.position.col());
        return temp;
    }
}