package com.chess2.networking;

import com.chess2.Console;
import com.chess2.networking.packets.NetworkMove;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerClient implements Runnable {
    private final Socket socket;
    private final ObjectInputStream input;
    private final ObjectOutputStream output;
    private final long networkId;

    public final long getNetworkId() {
        return this.networkId;
    }

    private ServerRoom room;

    public final boolean inRoom() {
        return this.room != null;
    }

    @SuppressWarnings("unused")
    public final ServerRoom getRoom() {
        return this.room;
    }

    public void setRoom(final ServerRoom room) {
        this.room = room;
    }

    public ServerClient(final long networkId, final Socket socket) {
        this.networkId = networkId;
        this.socket = socket;
        try {
            this.output = new ObjectOutputStream(this.socket.getOutputStream());
            this.input = new ObjectInputStream(this.socket.getInputStream());
        } catch (IOException e) {
            Console.log(Console.ERROR, "Client[" + this.networkId + "]: Failed to initialize input/output streams!");
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        while (!Thread.interrupted() && this.socket.isConnected()) {
            this.handlePacket(this.receive());
        }

        try {
            this.socket.close();
        } catch (IOException e) {
            Console.log(Console.ERROR, "Client[" + this.networkId + "]: Failed to close socket!");
            throw new RuntimeException(e);
        }
    }

    public void send(final Packet packet) {
        try {
            this.output.writeObject(packet);
            this.output.flush();
        } catch (IOException e) {
            Console.log(Console.ERROR, "Client[" + this.networkId + "]: Failed to send packet to the output stream!");
            throw new RuntimeException(e);
        }
    }

    public final Packet receive() {
        try {
            return (Packet) this.input.readObject();
        } catch (IOException ex) {
            Console.log(Console.ERROR, "Client[" + this.networkId + "]: Failed to receive packet from the input stream!");
            throw new RuntimeException(ex);
        } catch (ClassNotFoundException ex) {
            Console.log(Console.ERROR, "Client[" + this.networkId + "]: Failed to convert packet!");
            throw new RuntimeException(ex);
        }
    }

    private void handlePacket(final Packet packet) {
        if (packet == null) {
            // TODO : Disconnect
            return;
        }
        Console.log(Console.INFO, "Client[" + this.networkId + "]: Received packet!");
        if (packet instanceof NetworkMove networkMove) {
            if (!this.inRoom()) return;
            final ServerClient opponent = this.room.getPlayer1().getNetworkId() == this.networkId ? this.room.getPlayer2() : this.room.getPlayer1();
            opponent.send(networkMove);
        }
    }
}