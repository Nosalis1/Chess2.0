package com.chess2.networking;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client implements Runnable {
    private static Client instance;
    private static boolean connected;

    public static boolean isConnected() {
        return connected;
    }

    static {
        connected = false;
        try {
            instance = new Client();
            connected = true;
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
    }

    public static Client getInstance() {
        return instance;
    }

    public static final String HOST = "localhost";

    private final Socket socket;
    private ObjectInputStream input;
    private ObjectOutputStream output;

    public Client() throws IOException {
        System.out.println("Trying to connect to server...");
        this.socket = new Socket(HOST, Server.PORT);
        if (this.socket.isClosed() || !this.socket.isConnected())
            throw new RuntimeException("Failed to connect to the server!");
        System.out.println("Connected to the server!");
        if (instance != null && instance != this)
            throw new RuntimeException("Failed to create more than one instance!");
        System.out.println("Initializing streams...");
        this.input = new ObjectInputStream(this.socket.getInputStream());
        this.output = new ObjectOutputStream(this.socket.getOutputStream());
        System.out.println("Streams initialized!");
        System.out.println("Creating new Thread...");
        new Thread(this).start();
        System.out.println("New thread created!");
    }

    public void send(final Packet packet) {
        if (!isConnected()) return;
        try {
            this.output.writeObject(packet);
            this.output.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Packet receive() {
        try {
            return (Packet) this.input.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        if (!isConnected()) return;
        while (this.socket.isConnected()) {
            handlePacket(this.receive());
        }
    }

    private void handlePacket(final Packet packet) {

    }
}