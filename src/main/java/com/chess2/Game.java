package com.chess2;

import com.chess2.ai.ABPruningAI;
import com.chess2.ai.ChessAI;
import com.chess2.pieces.*;
import com.chess2.utility.ImmutableInt2;
import com.chess2.utility.Move;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
    public final VBox stats = new VBox();
    private BoardField[][] fields;
    public final ChessBoard board = new ChessBoard();
    protected final ChessAI chessAI = new ABPruningAI();

    private static class TurnManagement {
        private static boolean whiteTurn = true;

        public static boolean isWhiteTurn() {
            return whiteTurn;
        }

        public static void next() {
            whiteTurn = !whiteTurn;
            if (!whiteTurn) {
                Game.instance.chessAI.handleMove();
            }
        }

        private static final ObservableList<Move> moveHistory = FXCollections.observableArrayList();

        public static ObservableList<Move> getMoveHistory() {
            return moveHistory;
        }

        public static void add(final int row, final int col, final int targetRow, final int targetCol) {
            moveHistory.add(new Move(new ImmutableInt2(row, col), new ImmutableInt2(targetRow, targetCol)));
        }
    }

    public void reset() {
        this.board.reset();

        this.root.getChildren().clear();
        this.createGrid();
        this.createStats();

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

    private void createStats() {
        this.stats.setPrefWidth(App.CELL_SIZE * 2);
        VBox a = new VBox();
        a.setBackground(Background.fill(BoardField.FIELD_DARK_COLOR));
        a.getChildren().addAll(
                new Label("Black Player") {{
                    setAlignment(Pos.CENTER);
                    setTextAlignment(TextAlignment.CENTER);
                    setFont(Font.font("Times New Roman", FontWeight.BOLD, FontPosture.REGULAR, 20));
                }}
        );

        VBox b = new VBox();
        b.setBackground(Background.fill(BoardField.FIELD_LIGHT_COLOR));
        b.getChildren().addAll(
                new Label("White Player") {{
                    setAlignment(Pos.CENTER);
                    setTextAlignment(TextAlignment.CENTER);
                    setFont(Font.font("Times New Roman", FontWeight.BOLD, FontPosture.REGULAR, 20));
                }}
        );

        this.stats.getChildren().addAll(a, b, new ListView<Move>() {{
            setItems(TurnManagement.getMoveHistory());
        }});
    }

    public final BoardField[][] getFields() {
        return this.fields;
    }

    public void makeMove(final ChessPiece piece, final int targetRow, final int targetCol) {
        if (piece == null || piece.isWhite() != TurnManagement.isWhiteTurn()) return;

        final int originalRow = piece.getPosition().getX(), originalCol = piece.getPosition().getY();
        if (piece.isValidMove(originalRow, originalCol, targetRow, targetCol, this.board.getPieces())) {
            this.board.removePiece(piece);
            this.fields[piece.getPosition().getX()][piece.getPosition().getY()].setPiece(null);
            //this.root.getChildren().remove(piece);

            final ChessPiece target = this.board.getPiece(targetRow, targetCol);
            if (target != null) {
                this.board.capturePiece(target);
                this.fields[target.getPosition().getX()][target.getPosition().getY()].setPiece(null);
                // this.root.getChildren().remove(target);
                this.onPieceCaptured(piece, target);
            }

            this.board.setPiece(piece, targetRow, targetCol);
            this.fields[targetRow][targetCol].setPiece(piece);
            // this.root.add(piece, targetCol, targetRow);
            this.onPieceMoved(piece, originalRow, originalCol, targetRow, targetCol);

            TurnManagement.add(originalRow, originalCol, targetRow, targetCol);
            TurnManagement.next();
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

        if (this.board.isCheck(true)) {
            System.out.println("White in check");
            final King king = this.board.getKing(true);
            if (king == null) return;
            this.fields[king.getPosition().getX()][king.getPosition().getY()].highlightAttack();
        } else {
            final King king = this.board.getKing(true);
            if (king == null) return;
            this.fields[king.getPosition().getX()][king.getPosition().getY()].reset();
        }
        if (this.board.isCheck(false)) {
            System.out.println("Black in check");
            final King king = this.board.getKing(false);
            if (king == null) return;
            this.fields[king.getPosition().getX()][king.getPosition().getY()].highlightAttack();
        } else {
            final King king = this.board.getKing(false);
            if (king == null) return;
            this.fields[king.getPosition().getX()][king.getPosition().getY()].reset();
        }
        if (isGameOver()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Game over!");
            alert.showAndWait();
            ABPruningAI.shutdownThreadPool();
            System.exit(0); // TODO : remove later
        }
    }

    private void onPieceMoved(final ChessPiece piece, final int fromRow, final int fromCol) {
        this.onPieceMoved(piece, fromRow, fromCol, piece.getPosition().getX(), piece.getPosition().getY());
    }

    public final boolean isGameOver() {
        return this.board.isCheckmate(true) || this.board.isCheckmate(false);
    }
}