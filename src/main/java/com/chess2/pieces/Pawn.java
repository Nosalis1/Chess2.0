package com.chess2.pieces;

import com.chess2.App;
import com.chess2.Assets;

/**
 * Represents a pawn chess piece.
 */
public class Pawn extends ChessPiece {

    /**
     * Constructs a pawn.
     *
     * @param isWhite Indicates whether the pawn is white or black.
     */
    public Pawn(final boolean isWhite) {
        super(Assets.getImage(isWhite ? "w_pawn_png_shadow_256px" : "b_pawn_png_shadow_256px"), isWhite);
    }

    /**
     * Checks if a move is valid for the pawn.
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
        int direction = (isWhite()) ? -1 : 1;

        // Array bounds check
        if (!isValidPosition(startX, startY) || !isValidPosition(targetX, targetY)) {
            return false;
        }

        // Pawn can move one square forward
        if (startY == targetY && startX + direction == targetX && board[targetX][targetY] == null) {
            return true;
        }

        // Pawn can move two squares forward from the starting position
        if (startY == targetY && startX + 2 * direction == targetX && startX == getStartingRow() &&
                board[startX + direction][targetY] == null && board[targetX][targetY] == null) {
            return true;
        }

        // Pawn can capture diagonally
        if (Math.abs(startY - targetY) == 1 && startX + direction == targetX &&
                board[targetX][targetY] != null && board[targetX][targetY].isWhite() != isWhite()) {
            return true;
        }

        // Add en passant or promotion logic here if needed

        return false;
    }

    /**
     * Gets the value of the pawn.
     *
     * @return The numerical value of the piece.
     */
    @Override
    public int getValue() {
        return 1;
    }

    // TODO : Pawn promotion
    // TODO : En Passant move

    /**
     * Checks if a position is within the chessboard bounds.
     *
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     * @return True if the position is valid, false otherwise.
     */
    private boolean isValidPosition(int x, int y) {
        return x >= 0 && x < App.CELL_COUNT && y >= 0 && y < App.CELL_COUNT;
    }

    /**
     * Gets the starting row for the pawn.
     *
     * @return The starting row for the pawn based on its color.
     */
    private int getStartingRow() {
        return (isWhite()) ? 6 : 1;
    }

    /**
     * Creates a copy of the pawn.
     *
     * @return A new instance of the pawn with the same properties.
     */
    @Override
    public ChessPiece copy() {
        final Pawn temp = new Pawn(isWhite());
        temp.position.set(this.position.row(), this.position.col());
        return temp;
    }
}