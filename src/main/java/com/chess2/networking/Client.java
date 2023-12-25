package com.chess2.networking;

import com.chess2.App;
import com.chess2.Console;
import com.chess2.Game;
import com.chess2.TurnManagement;
import com.chess2.networking.packets.ClientConnected;
import com.chess2.networking.packets.NetworkMove;
import com.chess2.players.Player;
import com.chess2.scenes.GameScene;
import com.chess2.utility.Move;
import javafx.application.Platform;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client implements Runnable {
    private final static Client instance;
    private static boolean connected;

    public static boolean isConnected() {
        return connected;
    }

    static {
        connected = false;
        instance = new Client();
    }

    public static Client getInstance() {
        return instance;
    }

    public static final String HOST = "localhost";

    private Socket socket;
    private ObjectInputStream input;
    private ObjectOutputStream output;

    public Client() {
    }

    public void connect() throws IOException {
        Console.log(Console.INFO, "Trying to connect to the server...");
        this.socket = new Socket(HOST, Server.PORT);
        if (this.socket.isClosed() || !this.socket.isConnected())
            throw new RuntimeException("Failed to connect to the server!");
        connected = true;
        Console.log(Console.INFO, "Successfully connected to the server!");
        if (instance != null && instance != this)
            throw new RuntimeException("Failed to create more than one instance!");
        Console.log(Console.INFO, "Initializing streams...");
        this.input = new ObjectInputStream(this.socket.getInputStream());
        this.output = new ObjectOutputStream(this.socket.getOutputStream());
        Console.log(Console.INFO, "Streams initialized!");
        Console.log(Console.INFO, "Starting listening thread!");
        new Thread(this).start();
        Console.log(Console.INFO, "Listening thread started!");
    }

    public void disconnect() {
        try {
            this.socket.close();
        } catch (IOException e) {
            if (this.socket.isClosed()) Console.log(Console.WARNING, "Socket is already closed!");
            else Console.log(Console.ERROR, "Failed to close socket!");
            throw new RuntimeException(e);
        }
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
        } catch (IOException ex) {
            if (!this.socket.isClosed()) Console.log(Console.ERROR, "Failed receiving packet from input stream!");
        } catch (ClassNotFoundException ex) {
            Console.log(Console.ERROR, "Failed to convert packet!");
        }
        return null;
    }

    @Override
    public void run() {
        if (!isConnected()) return;
        while (!this.socket.isClosed() && this.socket.isConnected()) {
            handlePacket(this.receive());
        }
    }

    private void handlePacket(final Packet packet) {
        if (packet == null) {
            this.disconnect();
            return;
        }
        Console.log(Console.INFO, "Received a packet from [" + packet.getSenderId() + "]");

        if (packet instanceof ClientConnected clientConnected) {
            Platform.runLater(() -> {
                TurnManagement.setupLocalPlayer(Player.Type.LOCAL, new Player.Data(!clientConnected.isWhite()));
                TurnManagement.setupRemotePlayer(Player.Type.ONLINE, new Player.Data(clientConnected.isWhite()));
                App.mainStage.setScene(GameScene.getInstance());
            });
        } else if (packet instanceof NetworkMove networkMove) {
            final Move move = networkMove.getMove();
            Platform.runLater(() -> Game.instance.makeMove(
                    move.from().getX(), move.from().getY(),
                    move.to().getX(), move.to().getY()
            ));
        }
    }
}