package com.chess2.networking;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerRoom {
    private final long roomId;

    public final long getRoomId() {
        return this.roomId;
    }

    private final ExecutorService service = Executors.newFixedThreadPool(2);
    private final ServerClient player1, player2;

    public final ServerClient getPlayer1() {
        return this.player1;
    }

    public final ServerClient getPlayer2() {
        return this.player2;
    }

    public ServerRoom(final long roomId, final ServerClient first, final ServerClient second) {
        this.roomId = roomId;
        this.player1 = first;
        this.player2 = second;
        this.service.submit(player1);
        this.service.submit(player2);
    }

    public void shutdown() {
        this.service.shutdownNow();
    }
}