package com.chess2.networking;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerClient implements Runnable {
    private final Socket socket;
    private final ObjectInputStream input;
    private final ObjectOutputStream output;
    private final long networkId;
    public final long getNetworkId(){return this.networkId;}

    public ServerClient(final long networkId,final Socket socket) {
        this.networkId = networkId;
        this.socket = socket;
        try {
            this.output = new ObjectOutputStream(this.socket.getOutputStream());
            this.input = new ObjectInputStream(this.socket.getInputStream());
        } catch (IOException e) {
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
            throw new RuntimeException(e);
        }
    }

    public void send(final Packet packet) {
        try {
            this.output.writeObject(packet);
            this.output.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public final Packet receive() {
        try {
            return (Packet) this.input.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void handlePacket(final Packet packet) {

    }
}