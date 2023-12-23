package com.chess2.ai;

import com.chess2.ChessBoard;
import com.chess2.Game;
import com.chess2.pieces.ChessPiece;
import com.chess2.utility.Move;
import javafx.application.Platform;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ABPruningAI implements ChessAI {

    private static final ExecutorService threadPool = Executors.newFixedThreadPool(1);
    public static void shutdownThreadPool() {
        threadPool.shutdown();
    }
    private static final int MAX_DEPTH = 4;

    @Override
    public void handleMove() {
        threadPool.submit(() -> {
            double begin = System.currentTimeMillis();
            Move move = getBestMove(Game.instance.board, false);
            System.out.println("AI timestamp : " + (System.currentTimeMillis() - begin) + "ms");
            Platform.runLater(() -> Game.instance.makeMove(
                    move.from().getX(), move.from().getY(),
                    move.to().getX(), move.to().getY()
            ));
        });
    }

    private Move getBestMove(ChessBoard board, boolean isWhite) {
        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;

        Move bestMove = null;

        for (Move move : getAllPossibleMoves(board, isWhite)) {
            ChessBoard copyBoard = board.deepCopy();
            copyBoard.executeMove(move);
            int evaluation = alphaBetaPruning(copyBoard, MAX_DEPTH - 1, alpha, beta, !isWhite);

            if (isWhite && evaluation > alpha) {
                alpha = evaluation;
                bestMove = move;
            } else if (!isWhite && evaluation < beta) {
                beta = evaluation;
                bestMove = move;
            }
        }

        return bestMove;
    }

    private int alphaBetaPruning(ChessBoard board, int depth, int alpha, int beta, boolean isMaximizingPlayer) {
        if (depth == 0 || board.isCheckmate(!isMaximizingPlayer)) {
            return evaluateBoard(board);
        }

        List<Move> possibleMoves = getAllPossibleMoves(board, isMaximizingPlayer);

        if (isMaximizingPlayer) {
            int maxEval = Integer.MIN_VALUE;
            for (Move move : possibleMoves) {
                ChessBoard copyBoard = board.deepCopy();
                copyBoard.executeMove(move);
                int eval = alphaBetaPruning(copyBoard, depth - 1, alpha, beta, false);
                maxEval = Math.max(maxEval, eval);
                alpha = Math.max(alpha, eval);
                if (beta <= alpha) {
                    break; // Beta cut-off
                }
            }
            return maxEval;
        } else {
            int minEval = Integer.MAX_VALUE;
            for (Move move : possibleMoves) {
                ChessBoard copyBoard = board.deepCopy();
                copyBoard.executeMove(move);
                int eval = alphaBetaPruning(copyBoard, depth - 1, alpha, beta, true);
                minEval = Math.min(minEval, eval);
                beta = Math.min(beta, eval);
                if (beta <= alpha) {
                    break; // Alpha cut-off
                }
            }
            return minEval;
        }
    }

    private List<Move> getAllPossibleMoves(ChessBoard board, boolean isWhite) {
        List<Move> allMoves = new ArrayList<>();
        List<ChessPiece> pieces = isWhite ? board.getWhitePieces() : board.getBlackPieces();

        for (ChessPiece piece : pieces) {
            List<Move> pieceMoves = piece.getValidMoves(board.getPieces());
            allMoves.addAll(pieceMoves);
        }

        return allMoves;
    }

    private int evaluateBoard(ChessBoard board) {
        return board.evaluate();
    }
}