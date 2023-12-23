package com.chess2;

import com.chess2.players.AIPlayer;
import com.chess2.players.LocalPlayer;
import com.chess2.players.OnlinePlayer;
import com.chess2.players.Player;
import com.chess2.utility.Int2;
import com.chess2.utility.Move;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.jetbrains.annotations.NotNull;

public abstract class TurnManagement {
    private static Player localPlayer;
    private static Player remotePlayer;

    public static void setupLocalPlayer(final @NotNull Player.Data data) {
        localPlayer = new LocalPlayer() {{
            setup(data);
        }};
    }

    public static void setupRemotePlayer(final Player.Type type, final @NotNull Player.Data data) {
        remotePlayer = type == Player.Type.AI ? new AIPlayer() {{
            setup(data);
        }} : (type == Player.Type.ONLINE ? new OnlinePlayer() {{
            setup(data);
        }} : new LocalPlayer() {{
            setup(data);
        }});
    }

    public static Player getLocalPlayer() {
        return localPlayer;
    }

    public static Player getRemotePlayer() {
        return remotePlayer;
    }

    private static boolean whiteTurn = true;

    public static boolean isWhiteTurn() {
        return whiteTurn;
    }

    public static boolean isValidTurn() {
        if (remotePlayer instanceof LocalPlayer other) {
            return true;
        }
        return localPlayer.isWhite() == whiteTurn;
    }

    public static void next() {
        whiteTurn = !whiteTurn;
        if (localPlayer.isWhite() == whiteTurn) {
            localPlayer.play();
        } else {
            remotePlayer.play();
        }
    }

    private static final ObservableList<Move> moveHistory = FXCollections.observableArrayList();

    public static ObservableList<Move> getMoveHistory() {
        return moveHistory;
    }

    public static void addMove(final @NotNull Int2 from,final @NotNull Int2 to) {
        moveHistory.add(new Move(from, to));
    }
    public static void addMove(final @NotNull Move move) {
        moveHistory.add(move);
    }

    public static void clearHistory() {
        moveHistory.clear();
    }
}