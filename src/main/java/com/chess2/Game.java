package com.chess2;

import com.chess2.pieces.*;
import com.chess2.players.Player;
import com.chess2.utility.ImmutableInt2;
import com.chess2.utility.Move;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

// TODO : 1.Turn Management *
// TODO : 2.Check and Checkmate *
// TODO : 3.Move History *
// TODO : 4.User Interface (UI) Improvements
// TODO : 5.Event Handling
// TODO : 6.En Passant and Castling
// TODO : 7.Game Over Screen
// TODO : 8.Refactoring

public class Game {

    public static final Game instance = new Game();

    public final GridPane root = new GridPane();
    private BoardField[][] fields;
    public final ChessBoard board = new ChessBoard();

    public void reset() {
        this.board.reset();

        this.root.getChildren().clear();
        this.createGrid();

        for (ChessPiece piece : this.board.getAllPieces()) {
            this.fields[piece.getPosition().getX()][piece.getPosition().getY()].setPiece(piece);
            // TODO : Add sprite logic here
            //this.root.add(piece, piece.position.fxRow(), piece.position.fxCol());
        }
    }

    private void createGrid() {
        this.fields = new BoardField[App.CELL_COUNT][App.CELL_COUNT];
        for (int row = 0; row < App.CELL_COUNT; row++) {
            for (int col = 0; col < App.CELL_COUNT; col++) {
                BoardField field = new BoardField(this.root, row, col);
                this.fields[row][col] = field;
            }
        }
    }

    public final BoardField[][] getFields() {
        return this.fields;
    }

    public void makeMove(final ChessPiece piece, final int targetRow, final int targetCol) {
        if (piece == null || piece.isWhite() != TurnManagement.isWhiteTurn()) {
            Console.log(Console.ERROR, "Move not valid!");
            return;
        }

        final int originalRow = piece.getPosition().getX(), originalCol = piece.getPosition().getY();
        if (piece.isValidMove(originalRow, originalCol, targetRow, targetCol, this.board.getPieces())) {
            this.board.removePiece(piece);
            this.fields[piece.getPosition().getX()][piece.getPosition().getY()].setPiece(null);

            final ChessPiece target = this.board.getPiece(targetRow, targetCol);
            if (target != null) {
                this.board.capturePiece(target);
                this.fields[target.getPosition().getX()][target.getPosition().getY()].setPiece(null);
                this.onPieceCaptured(piece, target);
            }

            this.board.setPiece(piece, targetRow, targetCol);
            this.fields[targetRow][targetCol].setPiece(piece);
            this.onPieceMoved(piece, originalRow, originalCol, targetRow, targetCol);

            TurnManagement.addMove(new ImmutableInt2(originalRow, originalCol), new ImmutableInt2(targetRow, targetCol));
            TurnManagement.next();
        } else {
            Console.log(Console.ERROR, "Move not valid!");
        }
    }

    public void makeMove(final int row, final int col, final int targetRow, final int targetCol) {
        this.makeMove(this.board.getPiece(row, col), targetRow, targetCol);
    }

    private void onPieceCaptured(final ChessPiece captor, final ChessPiece target) {
        if (target instanceof King) {
            // TODO : Force stop game
            Console.log(Console.INFO,"Game ended!");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Game over!");
            alert.showAndWait();
            Player.shutdownThreadPool();
            System.exit(0); // TODO : remove later
        }
        // Do something with this (play sound...)
    }

    private void onPieceMoved(final ChessPiece piece, final int fromRow, final int fromCol, final int toRow, final int toCol) {
        // Do something with this (play sound...)

        if (this.board.isCheck(true)) {
            Console.log(Console.WARNING,"White in check!");
            final King king = this.board.getKing(true);
            if (king == null) return;
            this.fields[king.getPosition().getX()][king.getPosition().getY()].highlightAttack();
        } else {
            final King king = this.board.getKing(true);
            if (king == null) return;
            this.fields[king.getPosition().getX()][king.getPosition().getY()].reset();
        }
        if (this.board.isCheck(false)) {
            Console.log(Console.WARNING,"Black in check!");
            final King king = this.board.getKing(false);
            if (king == null) return;
            this.fields[king.getPosition().getX()][king.getPosition().getY()].highlightAttack();
        } else {
            final King king = this.board.getKing(false);
            if (king == null) return;
            this.fields[king.getPosition().getX()][king.getPosition().getY()].reset();
        }
        if (isGameOver()) {
            Console.log(Console.INFO,"Game ended!");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Game over!");
            alert.showAndWait();
            Player.shutdownThreadPool();
            System.exit(0); // TODO : remove later
        }
    }

    @SuppressWarnings("unused")
    private void onPieceMoved(final ChessPiece piece, final int fromRow, final int fromCol) {
        this.onPieceMoved(piece, fromRow, fromCol, piece.getPosition().getX(), piece.getPosition().getY());
    }

    public final boolean isGameOver() {
        return this.board.isCheckmate(true) || this.board.isCheckmate(false);
    }
}