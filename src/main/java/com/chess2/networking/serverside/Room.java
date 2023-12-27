package com.chess2.networking.serverside;

import com.chess2.Console;
import com.chess2.networking.database.DatabaseService;
import com.chess2.networking.packets.state.BeginGame;
import com.chess2.networking.packets.state.EndGame;

import java.util.Random;

public class Room {
    private final long networkID;

    public final long getNetworkID() {
        return this.networkID;
    }

    public Room(final long networkID) {
        this.networkID = networkID;
    }

    private ClientHandler handler1;
    private ClientHandler handler2;

    public final ClientHandler getHandler1() {
        return this.handler1;
    }

    public final ClientHandler getHandler2() {
        return this.handler2;
    }

    public final boolean isOpen() {
        return handler1 == null || handler2 == null;
    }

    public final boolean isEmpty() {
        return this.handler1 == null && this.handler2 == null;
    }

    public void setHandler(final ClientHandler handler) {
        if (isOpen()) {
            if (this.handler1 == null)
                this.handler1 = handler;
            else this.handler2 = handler;
            handler.setRoom(this);
            if (!isOpen()) start();
        }
    }

    public void removeHandler(final ClientHandler handler) {
        if (handler1 == handler)
            handler1 = null;
        else if (handler2 == handler)
            handler2 = null;
        else Console.log(Console.WARNING, "Client[" + handler.getNetworkID() + "] is not in the room!");
    }

    public void start() {
        Random random = new Random();
        boolean value = random.nextBoolean();
        this.handler1.trySend(new BeginGame(value, DatabaseService.getUserDataById(this.handler2.getDatabaseID())));
        this.handler2.trySend(new BeginGame(!value, DatabaseService.getUserDataById(this.handler1.getDatabaseID())));
    }

    public void stop() {
        if (handler1 != null) {
            this.handler1.trySend(new EndGame());
            this.handler1.leaveRoom();
        }
        if (handler2 != null) {
            this.handler2.trySend(new EndGame());
            this.handler2.leaveRoom();
        }
        Server.destroyRoom(this.getNetworkID());
    }
}