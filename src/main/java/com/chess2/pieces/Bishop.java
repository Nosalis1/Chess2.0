package com.chess2.pieces;

import com.chess2.Assets;
import com.chess2.ChessMove;

import java.util.List;

/**
 * Represents a bishop chess piece.
 */
public class Bishop extends ChessPiece {

    /**
     * Constructs a bishop.
     *
     * @param isWhite Indicates whether the bishop is white or black.
     */
    public Bishop(final boolean isWhite) {
        super(Assets.getImage(isWhite ? "w_bishop_png_shadow_256px" : "b_bishop_png_shadow_256px"), isWhite);
    }

    /**
     * Checks if a move is valid for the bishop.
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
        // Bishop can move diagonally any number of squares

        int deltaX = Math.abs(targetX - startX);
        int deltaY = Math.abs(targetY - startY);

        // Check if the move is diagonal
        if (deltaX == deltaY) {
            // Check if there are no pieces blocking the path
            int stepX = Integer.compare(targetX, startX);
            int stepY = Integer.compare(targetY, startY);

            for (int i = 1; i < Math.abs(targetX - startX); i++) {
                int nextX = startX + i * stepX;
                int nextY = startY + i * stepY;

                if (board[nextX][nextY] != null) {
                    return false; // There's a piece blocking the path
                }
            }

            // Check if the target square is empty or has an opponent's piece
            return board[targetX][targetY] == null || board[targetX][targetY].isWhite() != isWhite();
        }

        return false;
    }

    /**
     * Gets the value of the bishop.
     *
     * @return The numerical value of the piece.
     */
    @Override
    public int getValue() {
        return 3;
    }

    /**
     * Creates a copy of the bishop.
     *
     * @return A new instance of the bishop with the same properties.
     */
    @Override
    public ChessPiece copy() {
        final Bishop temp = new Bishop(isWhite());
        temp.position.set(this.position.row(), this.position.col());
        return temp;
    }
}