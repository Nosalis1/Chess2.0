package com.chess2.networking;

import com.chess2.Console;
import com.chess2.networking.packets.LoginStatus;
import com.chess2.networking.packets.RequestLogin;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.regex.Pattern;

public class Server extends Application {
    private final TextArea statusArea = new TextArea();
    private final Button startButton = new Button("Start");
    private final Button stopButton = new Button("Stop");
    private final Label serverStatusLabel = new Label("Server Status: Stopped");
    private final Label connectedClientLabel = new Label("Connected Clients: 0");
    private final Label activeRoomLabel = new Label("Active Rooms: 0");

    private void updateStatsLabels() {
        synchronized (connectedClientLabel) {
            synchronized (activeRoomLabel) {
                Platform.runLater(() -> {
                    connectedClientLabel.setText("Connected Clients: " + clients.size());
                    activeRoomLabel.setText("Active Rooms: " + rooms.size());
                });
            }
        }
    }

    public static class CustomOutputStream extends OutputStream {
        private final PrintStream originalStream;
        private final StringBuilder buffer = new StringBuilder();
        private final TextArea textArea;
        private static final Pattern ANSI_ESCAPE_PATTERN = Pattern.compile("\u001B\\[[;\\d]*m");


        public CustomOutputStream(PrintStream originalStream, TextArea textArea) {
            this.originalStream = originalStream;
            this.textArea = textArea;
        }

        @Override
        public void write(int b) {
            originalStream.write(b);
            buffer.append((char) b);

            // Check for newline character
            if (b == '\n') {
                updateTextArea();
            }
        }

        private final Stack<String> appendable = new Stack<>();

        private void updateTextArea() {
            appendable.add(stripAnsiCodes(buffer.toString()));
            buffer.setLength(0);

            Platform.runLater(() -> {
                while (!appendable.empty()) {
                    textArea.appendText(appendable.pop());
                }
            });
        }

        private String stripAnsiCodes(String input) {
            return ANSI_ESCAPE_PATTERN.matcher(input).replaceAll("");
        }
    }

    private void redirectSystemStreams() {
        PrintStream customOut = new PrintStream(new CustomOutputStream(System.out, statusArea));
        PrintStream customErr = new PrintStream(new CustomOutputStream(System.err, statusArea));
        System.setOut(customOut);
        System.setErr(customErr);
    }

    @Override
    public void start(Stage stage) throws Exception {
        DataBase.connect();
        stage.setTitle("Server");

        BorderPane borderPane = new BorderPane();
        VBox root = new VBox();
        root.setAlignment(Pos.TOP_CENTER);

        ScrollPane scrollPane = new ScrollPane(statusArea);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);

        HBox buttonsBox = new HBox(10, startButton, stopButton);
        buttonsBox.setAlignment(Pos.CENTER);

        serverStatusLabel.setStyle("-fx-font-size: 14; -fx-font-weight: bold;");
        VBox.setMargin(serverStatusLabel, new Insets(0, 0, 10, 0));

        connectedClientLabel.setStyle("-fx-font-size: 14; -fx-font-weight: bold;");
        activeRoomLabel.setStyle("-fx-font-size: 14; -fx-font-weight: bold;");

        VBox.setMargin(statusArea, new Insets(10));
        VBox.setMargin(buttonsBox, new Insets(10));
        VBox.setMargin(scrollPane, new Insets(10));

        root.getChildren().addAll(serverStatusLabel, connectedClientLabel, activeRoomLabel, buttonsBox, scrollPane);

        statusArea.setStyle("-fx-font-family: 'Courier New'; -fx-font-size: 12;");
        statusArea.setEditable(false);

        redirectSystemStreams();

        startButton.setOnAction(actionEvent -> new Thread(() -> {
            try {
                Platform.runLater(() -> {
                    statusArea.setText("");
                    serverStatusLabel.setText("Server Status: Running");
                    startButton.setVisible(false);
                    stopButton.setVisible(true);
                });
                Server.instance.startServer();
            } catch (IOException | InterruptedException e) {
                Console.log(Console.ERROR, "Failed to start the server!");
                serverStatusLabel.setText("Server Status: Error");
                stopButton.setVisible(false);
                startButton.setVisible(true);
            }
        }).start());
        stopButton.setVisible(false);
        stopButton.setOnAction(actionEvent -> {
            Server.instance.stopServer();
            serverStatusLabel.setText("Server Status: Stopped");
            stopButton.setVisible(false);
            startButton.setVisible(true);
        });

        stage.setScene(new Scene(borderPane, 800, 500));

        borderPane.setCenter(root);
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        if (isConnected()) Server.instance.stopServer();
        super.stop();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private static final Server instance;
    private static boolean connected;

    public static boolean isConnected() {
        return connected;
    }

    static {
        connected = false;
        instance = new Server();
    }

    public static Server getInstance() {
        return instance;
    }

    public static final int PORT = 8080;
    public static final int MAX_CONNECTIONS = 4;
    public static final int MAX_ROOMS = MAX_CONNECTIONS / 2;

    private ServerSocket socket;
    private final Map<Long, ServerClient> clients = Collections.synchronizedMap(new HashMap<>());
    private final Map<Long, ServerRoom> rooms = Collections.synchronizedMap(new HashMap<>());
    private final Queue<ServerClient> waiting = new LinkedList<>();

    public Server() {
    }

    public void startServer() throws IOException, InterruptedException {
        if (isConnected()) return;
        Console.log(Console.INFO, "Starting server on port: " + PORT);
        this.socket = new ServerSocket(PORT);
        Thread.sleep(100);
        if (this.socket.isClosed())
            throw new RuntimeException("Failed to start server!");
        connected = true;
        Console.log(Console.INFO, "Server started on port: " + PORT);
        Thread.sleep(100);
        Console.log(Console.INFO, "Server InetAdder: " + this.socket.getInetAddress());
        Thread.sleep(100);
        if (instance != null && instance != this)
            throw new RuntimeException("Failed to create more than one instance!");
        Console.log(Console.INFO, "Starting listening process...");
        this.startListening();
    }

    public void stopServer() {
        for (Map.Entry<Long, ServerRoom> entry : rooms.entrySet())
            entry.getValue().shutdown();

        try {
            this.socket.close();
            connected = false;
        } catch (IOException e) {
            Console.log(Console.ERROR, "Failed to close the socket!");
            throw new RuntimeException(e);
        }

        updateStatsLabels();
    }

    private void startListening() {
        while (!this.socket.isClosed()) {
            Console.log(Console.INFO, "Listening for new connection...");
            try {
                onClientConnected(this.socket.accept());
                if (clients.size() >= MAX_CONNECTIONS || rooms.size() >= MAX_ROOMS) {
                    Console.log(Console.WARNING, "Limit reached,closing listening process...");
                    break;
                }
            } catch (IOException e) {
                if (!this.socket.isClosed()) throw new RuntimeException(e);
                break;
            }
        }
        Console.log(Console.INFO, "Server stopped listening process!");
    }

    private void onClientConnected(final Socket client) {
        Console.log(Console.INFO, "New socket accepted");

        final long networkId = NetworkID.getId();
        final ServerClient serverClient = new ServerClient(networkId, client);
        this.clients.put(networkId, serverClient);
        this.waiting.add(this.clients.get(networkId));

        RequestLogin requestLogin = (RequestLogin) serverClient.receive();
        if (requestLogin == null) throw new RuntimeException("Fatal");// TODO : Handle later
        serverClient.send(new LoginStatus());

        Console.log(Console.INFO, "Client: [InetAdder:" + client.getInetAddress() + " , NetworkID:" + networkId + "]");

        while (this.waiting.size() >= 2) {
            Console.log(Console.INFO, "Creating new ServerRoom...");
            final ServerClient client1 = this.waiting.poll();
            if (client1 == null) continue;
            final ServerClient client2 = this.waiting.poll();
            if (client2 == null) continue;

            final long roomId = NetworkID.getId();
            this.rooms.put(roomId, new ServerRoom(roomId, client1, client2));
            Console.log(Console.INFO, "ServerRoom: [RoomID:" + roomId + " , Client1:" + client1.getNetworkId() + " , Client2:" + client2.getNetworkId() + "]");
        }

        updateStatsLabels();
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