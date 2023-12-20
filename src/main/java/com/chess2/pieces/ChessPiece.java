package com.chess2.pieces;

import com.chess2.App;
import com.chess2.ChessMove;
import com.chess2.ChessPosition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * An abstract class representing a chess piece.
 */
public abstract class ChessPiece extends ImageView {

    private final boolean isWhite;
    public final ChessPosition position = new ChessPosition();
    private boolean hasMoved = false;

    /**
     * Constructs a chess piece.
     *
     * @param image   The image representing the chess piece.
     * @param isWhite Indicates whether the piece is white or black.
     */
    public ChessPiece(final Image image, final boolean isWhite) {
        super(image);
        this.isWhite = isWhite;
        setFitWidth(App.CELL_SIZE);
        setFitHeight(App.CELL_SIZE);
        setMouseTransparent(true);
    }

    /**
     * Checks if the chess piece is white.
     *
     * @return True if the piece is white, false otherwise.
     */
    public final boolean isWhite() {
        return isWhite;
    }

    public void setPosition(final int row, final int col) {
        this.hasMoved = false;
        this.position.set(row, col);
    }

    /**
     * Moves the chess piece to a new position.
     *
     * @param x The new x-coordinate.
     * @param y The new y-coordinate.
     */
    public void moveTo(final int x, final int y) {
        setPosition(x, y);
        if (!hasMoved) {
            hasMoved = true;
        }
    }

    /**
     * Checks if the chess piece has moved.
     *
     * @return True if the piece has moved, false otherwise.
     */
    public final boolean hasMoved() {
        return hasMoved;
    }

    /**
     * Checks if a move is valid for the chess piece.
     *
     * @param startX  The x-coordinate of the starting position.
     * @param startY  The y-coordinate of the starting position.
     * @param targetX The x-coordinate of the target position.
     * @param targetY The y-coordinate of the target position.
     * @param board   The chessboard represented as a 2D array of chess pieces.
     * @return True if the move is valid, false otherwise.
     */
    public abstract boolean isValidMove(int startX, int startY, int targetX, int targetY, final ChessPiece[][] board);

    public boolean isValidMove(final ChessPosition from, final ChessPosition to, final ChessPiece[][] board) {
        return isValidMove(from.row(), from.col(), to.row(), to.col(), board);
    }

    public boolean isValidMove(final ChessMove move, final ChessPiece[][] board) {
        return isValidMove(move.getFrom(), move.getTo(), board);
    }

    /**
     * Gets the value of the chess piece.
     *
     * @return The numerical value of the piece.
     */
    public abstract int getValue();

    /**
     * Performs actions when the chess piece is captured.
     */
    public void onCapture() {
    }

    /**
     * Gets a list of valid moves for the chess piece.
     *
     * @return A list of valid chess moves.
     */
    public List<ChessMove> getValidMoves(final ChessPiece[][] board) {
        final List<ChessMove> legalMoves = new ArrayList<>();
        for (int row = 0; row < App.CELL_COUNT; ++row)
            for (int col = 0; col < App.CELL_COUNT; ++col) {
                if (isValidMove(position.row(), position.col(), row, col, board))
                    legalMoves.add(new ChessMove(position, row, col));
            }
        return legalMoves;
    }

    /**
     * Creates a copy of the chess piece.
     *
     * @return A new instance of the chess piece with the same properties.
     */
    public abstract ChessPiece copy();
}