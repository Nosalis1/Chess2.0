package com.chess2.networking.clientside;

import com.chess2.App;
import com.chess2.Console;
import com.chess2.TurnManagement;
import com.chess2.networking.Packet;
import com.chess2.networking.database.UserData;
import com.chess2.networking.packets.state.BeginGame;
import com.chess2.networking.packets.state.EndGame;
import com.chess2.networking.packets.status.ConnectRequest;
import com.chess2.networking.packets.status.DisconnectRequest;
import com.chess2.networking.packets.status.QueueRequest;
import com.chess2.networking.serverside.Server;
import com.chess2.players.Player;
import com.chess2.scenes.GameScene;
import com.chess2.scenes.MenuScene;
import javafx.application.Platform;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Client {
    public static final String HOST = "localhost";
    private static boolean connected = false;

    public static boolean isConnected() {
        return connected;
    }

    private static Socket socket;

    public static Socket getSocket() {
        return socket;
    }

    private static ObjectInputStream input;
    private static ObjectOutputStream output;

    private static UserData userData;
    private static UserData opponentData;

    public static UserData getUserData() {
        return userData;
    }

    public static UserData getOpponentData() {
        return opponentData;
    }

    private static void setupLocalUser(final UserData data) {
        userData = data;
    }

    private static void setupOpponent(final UserData data) {
        opponentData = data;
    }

    public static void connect(final String username, final String password) throws IOException {
        if (isConnected()) {
            Console.log(Console.WARNING, "Can't connect to Server.Client is already connected!");
            return;
        }
        Console.log(Console.INFO, "Connecting to the Server!");

        socket = new Socket(HOST, Server.PORT);
        if (socket.isClosed() || !socket.isConnected()) {
            Console.log(Console.ERROR, "Failed to connect to the Server!");
            return;
        }
        connected = true;
        Console.log(Console.INFO, "Connected to the Server!");

        Console.log(Console.INFO, "Initializing streams!");
        input = new ObjectInputStream(socket.getInputStream());
        output = new ObjectOutputStream(socket.getOutputStream());
        Console.log(Console.INFO, "Streams initialized!");

        ConnectRequest connectRequest = new ConnectRequest(username, password);
        trySend(connectRequest);

        connectRequest = (ConnectRequest) tryReceive();
        if (connectRequest == null || !connectRequest.isValid()) {
            input.close();
            output.close();
            socket.close();
            connected = false;
            throw new IOException("Failed to register!");
        }
        setupLocalUser(connectRequest.getUserData());
        Console.log(Console.INFO, "Registered!");
        onConnectedToServer();

        ExecutorService threadPool = Executors.newFixedThreadPool(1);
        threadPool.submit(() -> {
            try {
                socket.setSoTimeout(1000);
            } catch (SocketException e) {
                throw new RuntimeException(e);
            }
            while (!socket.isClosed()) {
                try {
                    if (handlePacket(receive()))
                        break;
                } catch (SocketTimeoutException ex) {
                    if (socket.isClosed()) break;
                } catch (IOException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public static void disconnect() {
        trySend(new DisconnectRequest());
        try {
            socket.close();
            onDisconnectedFromServer();
        } catch (IOException e) {
            if (socket.isClosed()) Console.log(Console.WARNING, "Socket is already closed!");
            else Console.log(Console.ERROR, "Failed to close socket!");
            throw new RuntimeException(e);
        }
    }

    public static void send(final Packet packet) throws IOException {
        output.writeObject(packet);
        output.flush();
    }

    public static void trySend(final Packet packet) {
        try {
            send(packet);
        } catch (IOException e) {
            Console.log(Console.ERROR, "Client Failed to send packet!");
        }
    }

    public static Packet receive() throws IOException, ClassNotFoundException {
        return (Packet) input.readObject();
    }

    public static Packet tryReceive() {
        try {
            return receive();
        } catch (IOException | ClassNotFoundException e) {
            Console.log(Console.ERROR, "Client Failed to receive packet!");
            throw new RuntimeException(e);
        }
    }

    private static boolean handlePacket(final Packet packet) {
        if (packet instanceof BeginGame beginGame) {
            setupOpponent(beginGame.getOpponentData());

            final boolean localColor = beginGame.isLocalPlayerWhite();
            TurnManagement.setupLocalPlayer(Player.Type.LOCAL, localColor);
            TurnManagement.setupRemotePlayer(Player.Type.ONLINE, !localColor);
            Platform.runLater(GameScene::displayScene);
        } else if (packet instanceof EndGame) {
            Platform.runLater(() -> {
                App.mainStage.setScene(MenuScene.getInstance());
            });
        } else if (packet instanceof DisconnectRequest) {
            try {
                socket.close();
                onDisconnectedFromServer();
            } catch (IOException e) {
                if (socket.isClosed()) Console.log(Console.WARNING, "Socket is already closed!");
                else Console.log(Console.ERROR, "Failed to close socket!");
                throw new RuntimeException(e);
            }
            return true;
        }
        return false;
    }

    public static void requestQueue() {
        trySend(new QueueRequest());
    }

    private static void onConnectedToServer() {

    }

    private static void onDisconnectedFromServer() {

    }
}