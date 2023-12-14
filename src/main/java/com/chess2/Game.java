package com.chess2;

import com.chess2.pieces.*;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

// TODO : 1.Turn Management
// TODO : 2.Check and Checkmate
// TODO : 3.Move History
// TODO : 4.User Interface (UI) Improvements
// TODO : 5.Event Handling
// TODO : 6.En Passant and Castling
// TODO : 7.Game Over Screen
// TODO : 8.Refactoring

public class Game {

    public static final Game instance = new Game();

    public final GridPane root = new GridPane();

    public final ChessBoard board = new ChessBoard();

    public void reset() {
        this.board.reset();

        this.root.getChildren().clear();
        this.createGrid();

        for (ChessPiece piece : this.board.getAllPieces()) {
            this.root.add(piece, piece.position.fxRow(), piece.position.fxCol());
        }
    }

    private void createGrid() {
        for (int row = 0; row < App.CELL_COUNT; row++) {
            for (int col = 0; col < App.CELL_COUNT; col++) {
                Rectangle square = new Rectangle(App.CELL_SIZE, App.CELL_SIZE);
                square.setFill((row + col) % 2 == 0 ? Color.valueOf("#B58863") : Color.valueOf("#F0D9B5"));
                this.root.add(square, col, row);

                square.setOnMouseClicked(new UserInput(col, row));
            }
        }
    }

    public void makeMove(final ChessPiece piece, final int targetRow, final int targetCol) {
        if (piece == null) return;

        final int originalRow = piece.position.row(), originalCol = piece.position.col();
        if (piece.isValidMove(originalRow, originalCol, targetRow, targetCol, this.board.getPieces())) {
            this.board.removePiece(piece);
            this.root.getChildren().remove(piece);

            final ChessPiece target = this.board.getPiece(targetRow, targetCol);
            if (target != null) {
                this.board.capturePiece(target);
                this.root.getChildren().remove(target);
                this.onPieceCaptured(piece, target);
            }

            this.board.setPiece(piece, targetRow, targetCol);
            this.root.add(piece, targetCol, targetRow);
            this.onPieceMoved(piece, originalRow, originalCol, targetRow, targetCol);
        } else {
            System.err.println("Move not valid!");
        }
    }

    public void makeMove(final int row, final int col, final int targetRow, final int targetCol) {
        this.makeMove(this.board.getPiece(row, col), targetRow, targetCol);
    }

    private void onPieceCaptured(final ChessPiece captor, final ChessPiece target) {
        if (target instanceof King king) {
            // TODO : Force stop game
            System.out.println("Game ended!");
        }
        // Do something with this (play sound...)
    }

    private void onPieceMoved(final ChessPiece piece, final int fromRow, final int fromCol, final int toRow, final int toCol) {
        // Do something with this (play sound...)

        if (isGameOver())
            System.out.println("Game ended!");
    }

    private void onPieceMoved(final ChessPiece piece, final int fromRow, final int fromCol) {
        this.onPieceMoved(piece, fromRow, fromCol, piece.position.row(), piece.position.col());
    }

    public final boolean isGameOver() {
        return this.board.isCheckmate(true) || this.board.isCheckmate(false);
    }
}