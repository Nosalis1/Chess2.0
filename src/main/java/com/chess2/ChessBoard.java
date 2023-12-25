package com.chess2;

import com.chess2.pieces.*;
import com.chess2.utility.Int2;
import com.chess2.utility.Move;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("unused")
public class ChessBoard {
    private ChessPiece[][] pieces;

    public final @Nullable ChessPiece[][] getPieces() {
        return this.pieces;
    }

    @Contract(pure = true)
    public final @Nullable ChessPiece getPiece(final int row, final int col) {
        return inBounds(row, col) ? this.pieces[row][col] : null;
    }

    @Contract(pure = true)
    public final @Nullable ChessPiece getPiece(final @NotNull Int2 at) {
        return this.getPiece(at.getX(), at.getY());
    }

    public void setPiece(final ChessPiece piece, final int row, final int col) {
        if (inBounds(row, col)) {
            if (piece == null) {
                final ChessPiece target = this.getPiece(row, col);
                if (target != null) {
                    this.allPieces.remove(target);
                    if (target.isWhite()) this.whitePieces.remove(target);
                    else this.blackPieces.remove(target);
                }
            } else {
                if (!this.allPieces.contains(piece)) {
                    this.allPieces.add(piece);
                    if (piece.isWhite()) this.whitePieces.add(piece);
                    else this.blackPieces.add(piece);
                }
                piece.getPosition().set(row, col);
            }
            this.pieces[row][col] = piece;
        } else Console.log(Console.ERROR,"Position [" + row + "][" + col + "] not in board bounds!");
    }

    public void setPiece(final ChessPiece piece, final Int2 pos) {
        this.setPiece(piece, pos.getX(), pos.getY());
    }

    public void setPiece(final @NotNull ChessPiece piece) {
        this.setPiece(piece, piece.getPosition().getX(), piece.getPosition().getY());
    }

    public void removePiece(final int row, final int col) {
        this.setPiece(null, row, col);
    }

    public void removePiece(final @NotNull ChessPiece piece) {
        this.removePiece(piece.getPosition().getX(), piece.getPosition().getY());
    }

    public void capturePiece(final @NotNull ChessPiece piece) {
        // piece.onCapture();
        this.removePiece(piece.getPosition().getX(), piece.getPosition().getY());
    }

    public void capturePiece(final int row, final int col) {
        final ChessPiece piece = this.getPiece(row, col);
        if (piece != null) this.capturePiece(piece);
    }

    public final ChessPiece[][] shallowPiecesCopy() {
        return this.pieces.clone();
    }

    public final ChessPiece[] @NotNull [] deepPiecesCopy() {
        final ChessPiece[][] result = new ChessPiece[App.CELL_COUNT][App.CELL_COUNT];
        for (int row = 0; row < App.CELL_COUNT; ++row)
            for (int col = 0; col < App.CELL_COUNT; ++col) {
                final ChessPiece piece = this.getPiece(row, col);
                if (piece != null) {
                    result[row][col] = piece.deepCopy();
                }
            }
        return result;
    }

    private final List<ChessPiece> allPieces = new ArrayList<>();
    private final List<ChessPiece> whitePieces = new ArrayList<>();
    private final List<ChessPiece> blackPieces = new ArrayList<>();

    public final @NotNull List<ChessPiece> getAllPieces() {
        return allPieces.subList(0, this.allPieces.size());
    }

    public final @NotNull List<ChessPiece> getWhitePieces() {
        return whitePieces.subList(0, this.whitePieces.size());
    }

    public final @NotNull List<ChessPiece> getBlackPieces() {
        return blackPieces.subList(0, this.blackPieces.size());
    }

    @Contract(pure = true)
    public final @Nullable King getKing(final boolean isWhite) {
        final List<ChessPiece> from = isWhite ? this.whitePieces : this.blackPieces;
        for (ChessPiece piece : from)
            if (piece instanceof King king)
                return king;
        return null;
    }

    private void updatePieces() {
        allPieces.clear();
        whitePieces.clear();
        blackPieces.clear();

        for (ChessPiece[] chessPieces : this.pieces)
            for (final ChessPiece piece : chessPieces)
                if (piece != null) {
                    this.allPieces.add(piece);
                    if (piece.isWhite()) this.whitePieces.add(piece);
                    else this.blackPieces.add(piece);
                }
    }

    public ChessBoard() {
        this.pieces = new ChessPiece[App.CELL_COUNT][App.CELL_COUNT];
    }

    public final boolean isEmpty(final int row, final int col) {
        return this.getPiece(row, col) != null;
    }

    public void reset() {
        // Clear pieces
        for (ChessPiece[] row : this.pieces)
            Arrays.fill(row, null);

        // Setup pawns
        for (int col = 0; col < App.CELL_COUNT; ++col) {
            this.pieces[App.CELL_COUNT - 2][col] = new Pawn(ChessPiece.PieceColor.WHITE);
            this.pieces[1][col] = new Pawn(ChessPiece.PieceColor.BLACK);
        }

        // Setup rooks
        this.pieces[App.CELL_COUNT - 1][0] = new Rook(ChessPiece.PieceColor.WHITE);
        this.pieces[App.CELL_COUNT - 1][App.CELL_COUNT - 1] = new Rook(ChessPiece.PieceColor.WHITE);
        this.pieces[0][0] = new Rook(ChessPiece.PieceColor.BLACK);
        this.pieces[0][App.CELL_COUNT - 1] = new Rook(ChessPiece.PieceColor.BLACK);

        // Setup knights
        this.pieces[App.CELL_COUNT - 1][1] = new Knight(ChessPiece.PieceColor.WHITE);
        this.pieces[App.CELL_COUNT - 1][App.CELL_COUNT - 2] = new Knight(ChessPiece.PieceColor.WHITE);
        this.pieces[0][1] = new Knight(ChessPiece.PieceColor.BLACK);
        this.pieces[0][App.CELL_COUNT - 2] = new Knight(ChessPiece.PieceColor.BLACK);

        // Setup bishops
        this.pieces[App.CELL_COUNT - 1][2] = new Bishop(ChessPiece.PieceColor.WHITE);
        this.pieces[App.CELL_COUNT - 1][App.CELL_COUNT - 3] = new Bishop(ChessPiece.PieceColor.WHITE);
        this.pieces[0][2] = new Bishop(ChessPiece.PieceColor.BLACK);
        this.pieces[0][App.CELL_COUNT - 3] = new Bishop(ChessPiece.PieceColor.BLACK);

        // Setup queens
        this.pieces[App.CELL_COUNT - 1][3] = new Queen(ChessPiece.PieceColor.WHITE);
        this.pieces[0][3] = new Queen(ChessPiece.PieceColor.BLACK);

        // Setup kings
        this.pieces[App.CELL_COUNT - 1][4] = new King(ChessPiece.PieceColor.WHITE);
        this.pieces[0][4] = new King(ChessPiece.PieceColor.BLACK);

        this.forceUpdatePositions();
        this.updatePieces();
    }

    public void forceUpdatePositions() {
        for (int row = 0; row < App.CELL_COUNT; ++row)
            for (int col = 0; col < App.CELL_COUNT; ++col) {
                final ChessPiece piece = this.getPiece(row, col);
                if (piece == null) continue;
                piece.getPosition().set(row, col);
            }
    }

    public final @NotNull ChessBoard shallowCopy() {
        final ChessBoard result = new ChessBoard();
        result.pieces = this.shallowPiecesCopy();
        result.allPieces.addAll(this.allPieces);
        result.whitePieces.addAll(this.whitePieces);
        result.blackPieces.addAll(this.blackPieces);
        return result;
    }

    public final @NotNull ChessBoard deepCopy() {
        final ChessBoard result = new ChessBoard();
        result.pieces = this.deepPiecesCopy();
        result.updatePieces();
        return result;
    }

    public final int evaluate() {
        int totalEvaluation = 0;
        for (ChessPiece piece : this.allPieces)
            totalEvaluation += piece.isWhite() ? piece.getValue() : -piece.getValue();
        return totalEvaluation;
    }

    @Contract(pure = true)
    public static boolean inBounds(final int @NotNull ... values) {
        for (int value : values) if (value < 0 || value > App.CELL_COUNT - 1) return false;
        return true;
    }

    public boolean isCheck(final boolean isWhite) {
        King king = getKing(isWhite);
        if (king == null) {
            // No king found, not a valid chess position
            return false;
        }

        List<ChessPiece> opponentPieces = isWhite ? blackPieces : whitePieces;
        for (ChessPiece opponentPiece : opponentPieces) {
            List<Move> possibleMoves = opponentPiece.getValidMoves(this.getPieces());
            for (Move move : possibleMoves) {
                if (move.to().equals(king.getPosition())) {
                    // King is under attack
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isCheckmate(final boolean isWhite) {
        if (!isCheck(isWhite)) {
            // If the king is not in check, then it's not checkmate
            return false;
        }

        List<ChessPiece> playerPieces = isWhite ? whitePieces : blackPieces;
        for (ChessPiece playerPiece : playerPieces) {
            List<Move> possibleMoves = playerPiece.getValidMoves(this.getPieces());
            for (Move move : possibleMoves) {
                ChessBoard testBoard = this.deepCopy();
                testBoard.executeMove(move);
                if (!testBoard.isCheck(isWhite)) {
                    // There is at least one move that gets the king out of check
                    return false;
                }
            }
        }

        // No moves found to get the king out of check, it's checkmate
        return true;
    }

    public void executeMove(final @NotNull Move move) {
        ChessPiece piece = getPiece(move.from().getX(), move.from().getY());
        assert piece != null;
        setPiece(piece, move.to().getX(), move.to().getY());
        removePiece(move.from().getX(), move.from().getY());
        piece.getPosition().set(move.to().getX(), move.to().getY());
    }
}