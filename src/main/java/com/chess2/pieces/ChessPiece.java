package com.chess2.pieces;

import com.chess2.App;
import com.chess2.ChessBoard;
import com.chess2.utility.Int2;
import com.chess2.utility.Move;
import com.chess2.utility.MutableInt2;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * An abstract class representing a chess piece.
 * Subclasses can extend this class to represent specific types of chess pieces.
 */
public abstract class ChessPiece {

    /**
     * Enum representing the color of the chess piece.
     */
    public enum PieceColor {
        WHITE, BLACK
    }

    /** The color of the chess piece. */
    private final PieceColor color;

    /** The position of the chess piece on the chessboard. */
    private final MutableInt2 position;

    /**
     * Constructs a chess piece with the specified color.
     *
     * @param color The color of the chess piece.
     */
    public ChessPiece(final PieceColor color) {
        this.color = color;
        this.position = new MutableInt2();
    }

    /**
     * Constructs a chess piece with the specified color and position.
     *
     * @param color    The color of the chess piece.
     * @param position The position of the chess piece.
     */
    public ChessPiece(final PieceColor color, final @NotNull MutableInt2 position) {
        this.color = color;
        this.position = Objects.requireNonNull(position, "position must not be null");
    }

    /**
     * Constructs a chess piece with the specified color and position coordinates.
     *
     * @param color The color of the chess piece.
     * @param x     The x-coordinate of the initial position.
     * @param y     The y-coordinate of the initial position.
     */
    public ChessPiece(final PieceColor color, final int x, final int y) {
        this.color = color;
        this.position = new MutableInt2(x, y);
    }

    /**
     * Constructs a chess piece by copying another chess piece.
     *
     * @param other The chess piece to copy.
     */
    public ChessPiece(final @NotNull ChessPiece other) {
        this.color = other.color;
        this.position = (MutableInt2) Objects.requireNonNull(other.position.copy(), "position must not be null");
    }

    /**
     * Gets the color of the chess piece.
     *
     * @return The color of the chess piece.
     */
    public final PieceColor getColor() {
        return this.color;
    }

    /**
     * Checks if the chess piece is white.
     *
     * @return {@code true} if the chess piece is white, {@code false} if black.
     */
    public final boolean isWhite() {
        return this.color == PieceColor.WHITE;
    }

    /**
     * Gets the position of the chess piece.
     *
     * @return The position of the chess piece.
     */
    public final MutableInt2 getPosition() {
        return this.position;
    }

    /**
     * Checks if a move from the start position to the target position is valid.
     *
     * @param startX The x-coordinate of the start position.
     * @param startY The y-coordinate of the start position.
     * @param targetX The x-coordinate of the target position.
     * @param targetY The y-coordinate of the target position.
     * @param pieces The current configuration of chess pieces on the chessboard.
     * @return {@code true} if the move is valid, {@code false} otherwise.
     */
    public abstract boolean isValidMove(final int startX, final int startY, final int targetX, final int targetY, final @NotNull ChessPiece[][] pieces);

    /**
     * Checks if a move from the start position to the target position is valid.
     *
     * @param start The start position.
     * @param target The target position.
     * @param pieces The current configuration of chess pieces on the chessboard.
     * @return {@code true} if the move is valid, {@code false} otherwise.
     */
    public final boolean isValidMove(final @NotNull Int2 start, final @NotNull Int2 target, final @NotNull ChessPiece[][] pieces) {
        return this.isValidMove(start.getX(), start.getY(), target.getX(), target.getY(), pieces);
    }

    /**
     * Checks if a move from the start position to the target position is valid.
     *
     * @param start The start position.
     * @param target The target position.
     * @param board The chessboard configuration.
     * @return {@code true} if the move is valid, {@code false} otherwise.
     */
    public final boolean isValidMove(final @NotNull Int2 start, final @NotNull Int2 target, final @NotNull ChessBoard board) {
        return this.isValidMove(start, target, board.getPieces());
    }

    /**
     * Checks if a move is valid.
     *
     * @param move The move to validate.
     * @param pieces The current configuration of chess pieces on the chessboard.
     * @return {@code true} if the move is valid, {@code false} otherwise.
     */
    public final boolean isValidMove(final @NotNull Move move, final @NotNull ChessPiece[][] pieces) {
        return this.isValidMove(move.from(), move.to(), pieces);
    }

    /**
     * Checks if a move is valid.
     *
     * @param move The move to validate.
     * @param board The chessboard configuration.
     * @return {@code true} if the move is valid, {@code false} otherwise.
     */
    public final boolean isValidMove(final @NotNull Move move, final @NotNull ChessBoard board) {
        return this.isValidMove(move, board.getPieces());
    }

    /**
     * Gets the value of the chess piece.
     *
     * @return The value of the chess piece.
     */
    public abstract int getValue();

    /**
     * Gets a list of valid moves for the chess piece on the given chessboard configuration.
     *
     * @param pieces The current configuration of chess pieces on the chessboard.
     * @return A list of valid moves for the chess piece.
     */
    public @NotNull List<Move> getValidMoves(final @NotNull ChessPiece[][] pieces) {
        Objects.requireNonNull(pieces, "pieces must not be null");
        final List<Move> legalMoves = new ArrayList<>();
        for (int row = 0; row < App.CELL_COUNT; ++row)
            for (int col = 0; col < App.CELL_COUNT; ++col) {
                if (isValidMove(position.getX(), position.getY(), row, col, pieces))
                    legalMoves.add(new Move(position.copy(), new MutableInt2(row, col)));
            }
        return legalMoves;
    }

    /**
     * Gets a list of valid moves for the chess piece on the given chessboard configuration.
     *
     * @param board The chessboard configuration.
     * @return A list of valid moves for the chess piece.
     */
    public @NotNull List<Move> getValidMoves(final @NotNull ChessBoard board) {
        return this.getValidMoves(board.getPieces());
    }

    /**
     * Creates a shallow copy of the chess piece.
     *
     * @return A shallow copy of the chess piece.
     */
    public abstract ChessPiece shallowCopy();

    /**
     * Creates a deep copy of the chess piece.
     *
     * @return A deep copy of the chess piece.
     */
    public abstract ChessPiece deepCopy();
}
