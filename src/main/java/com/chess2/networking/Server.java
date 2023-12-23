package com.chess2.networking;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Server {
    public static void main(String[] args) {
        System.out.println("Connected: " + Server.isConnected());
    }

    private static Server instance;
    private static boolean connected;

    public static boolean isConnected() {
        return connected;
    }

    static {
        connected = false;
        try {
            instance = new Server();
            connected = true;
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
    }

    public static Server getInstance() {
        return instance;
    }

    public static final int PORT = 8080;
    public static final int MAX_CONNECTIONS = 4;
    public static final int MAX_ROOMS = MAX_CONNECTIONS / 2;

    private final ServerSocket socket;
    private final Map<Long, ServerClient> clients = new HashMap<>();
    private final Map<Long, ServerRoom> rooms = new HashMap<>();
    private final Queue<ServerClient> waiting = new LinkedList<>();

    public Server() throws IOException {
        System.out.println("Tyring to setup server...");
        this.socket = new ServerSocket(PORT);
        if (this.socket.isClosed())
            throw new RuntimeException("Failed to create server!");
        System.out.println("Server is up!");
        if (instance != null && instance != this)
            throw new RuntimeException("Failed to create more than one instance!");
        System.out.println("Starting listening process...");
        this.startListening();
    }

    private void startListening() {
        while (true) {
            System.out.println("Listening for connection...");
            try {
                onClientConnected(this.socket.accept());
                if (clients.size() >= MAX_CONNECTIONS || rooms.size() >= MAX_ROOMS) {
                    System.out.println("LIMIT reached,closing listening process...");
                    break;
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println("Server stopped listening!");
    }

    private void onClientConnected(final Socket client) {
        System.out.println("Client connected!");
        final long networkId = NetworkID.getId();
        this.clients.put(networkId, new ServerClient(networkId, client));
        this.waiting.add(this.clients.get(networkId));

        System.out.println("Client data [\n\tAdder: " + client.getInetAddress() + "\n\tNetworkID: " + networkId + "\n]");

        while (this.waiting.size() >= 2) {
            final ServerClient client1 = this.waiting.poll();
            final ServerClient client2 = this.waiting.poll();

            final long roomId = NetworkID.getId();
            this.rooms.put(roomId, new ServerRoom(roomId, client1, client2));
        }
    }

    public void sendToAll(final Packet packet) {
        for (Map.Entry<Long, ServerClient> entry : this.clients.entrySet()) {
            entry.getValue().send(packet);
        }
    }

    public void sendTo(final Packet packet, final long... ids) {
        for (long id : ids)
            this.clients.get(id).send(packet);
    }

    public void sendToAllRooms(final Packet packet) {
        for (Map.Entry<Long, ServerRoom> entry : this.rooms.entrySet()) {
            entry.getValue().getPlayer1().send(packet);
            entry.getValue().getPlayer2().send(packet);
        }
    }

    public void sendToRooms(final Packet packet, final long... ids) {
        for (long id : ids) {
            final ServerRoom room = this.rooms.get(id);
            if (room != null) {
                room.getPlayer1().send(packet);
                room.getPlayer2().send(packet);
            }
        }
    }
}