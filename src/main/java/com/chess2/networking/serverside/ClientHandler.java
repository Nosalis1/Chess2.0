package com.chess2.networking.serverside;

import com.chess2.Console;
import com.chess2.networking.Packet;
import com.chess2.networking.packets.status.DisconnectRequest;
import com.chess2.networking.packets.status.QueueRequest;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private final Socket socket;

    public final Socket getSocket() {
        return this.socket;
    }

    private final long networkID;

    public long getNetworkID() {
        return this.networkID;
    }

    private final ObjectInputStream input;
    private final ObjectOutputStream output;

    private Room room = null;

    public final Room getRoom() {
        return this.room;
    }

    public final boolean inRoom() {
        return this.room != null;
    }

    public void setRoom(final Room room) {
        this.room = room;
    }

    public void leaveRoom() {
        if (this.room != null)
            this.room.removeHandler(this);
        this.room = null;
    }

    private int databaseID = -1;

    public final int getDatabaseID() {
        return this.databaseID;
    }

    public void setDatabaseID(final int id) {
        this.databaseID = id;
    }

    public ClientHandler(final long networkID, final Socket socket) throws IOException {
        this.socket = socket;
        this.networkID = networkID;
        this.output = new ObjectOutputStream(this.socket.getOutputStream());
        this.input = new ObjectInputStream(this.socket.getInputStream());
    }

    public void send(final Packet packet) throws IOException {
        this.output.writeObject(packet);
        this.output.flush();
    }

    public void trySend(final Packet packet) {
        try {
            this.send(packet);
        } catch (IOException e) {
            Console.log(Console.ERROR, "Client[" + getNetworkID() + "] Failed to send packet!");
        }
    }

    public final Packet receive() throws IOException, ClassNotFoundException {
        return (Packet) this.input.readObject();
    }

    public final Packet tryReceive() {
        try {
            return (Packet) this.input.readObject();
        } catch (IOException e) {
            Console.log(Console.ERROR, "Client[" + getNetworkID() + "] Failed to receive packet!");
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            Console.log(Console.ERROR, "Client[" + getNetworkID() + "] Failed to convert received packet!");
            throw new RuntimeException(e);
        }
    }

    public void disconnect() {
        Server.disconnectClient(this.getNetworkID());
    }

    public void forceDisconnect() {
        trySend(new DisconnectRequest());
        try {
            this.input.close();
            this.output.close();
            this.socket.close();
        } catch (IOException e) {
            Console.log(Console.ERROR, "Client[" + getNetworkID() + " Failed to terminate ClientHandler!");
        }
    }

    @Override
    public void run() {
        while (!this.socket.isClosed()) {
            if (this.handlePacket(this.tryReceive()))
                break;
        }
    }

    private boolean handlePacket(final Packet packet) {
        if (packet instanceof DisconnectRequest) {
            this.disconnect();
            return true;
        } else if (packet instanceof QueueRequest) {
            Server.addInQueue(this);
        }
        return false;
    }
}
