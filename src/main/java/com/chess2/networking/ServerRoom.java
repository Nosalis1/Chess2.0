package com.chess2.networking;

import com.chess2.Console;
import com.chess2.networking.packets.ClientConnected;

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

        this.player1.setRoom(this);
        this.player2.setRoom(this);

        this.service.submit(player1);
        this.service.submit(player2);

        final ClientConnected packet = new ClientConnected(Packet.SERVER_SENDER_ID);
        this.player1.send(packet);
        packet.setWhite(false);
        this.player2.send(packet);
    }

    public void shutdown() {
        Console.log(Console.INFO, "Shutting down ServerRoom[" + getRoomId() + "] thread pool!");
        this.service.shutdownNow();
    }
}