package com.chess2.networking.serverside;

import com.chess2.Console;
import com.chess2.networking.database.DatabaseService;
import com.chess2.networking.database.DatabaseServiceException;
import com.chess2.networking.NetworkID;
import com.chess2.networking.packets.status.ConnectRequest;
import com.chess2.networking.packets.status.DataRequest;
import javafx.util.Pair;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class Server {
    public static final int PORT = 8080;
    private static boolean running = false;

    public static boolean isRunning() {
        return running;
    }

    private static volatile boolean open = false;

    public static boolean isOpen() {
        return open;
    }

    private static ServerSocket serverSocket;

    public static ServerSocket getServerSocket() {
        return serverSocket;
    }

    private static ExecutorService threadPool;
    private static final Map<Long, ClientHandler> clients = new HashMap<>();
    private static final Map<Long, Room> rooms = new HashMap<>();

    private static void disconnectClients() {
        for (Map.Entry<Long, Room> entry : rooms.entrySet()) {
            final long networkID = entry.getKey();
            final Room room = entry.getValue();
            room.stop();
            NetworkID.freeId(networkID);
        }
        rooms.clear();
        for (Map.Entry<Long, ClientHandler> entry : clients.entrySet()) {
            final long networkID = entry.getKey();
            final ClientHandler handler = entry.getValue();
            handler.forceDisconnect();
            NetworkID.freeId(networkID);
        }
        clients.clear();
        ServerGUI.getInstance().updateClientsConnectedCount(0);
    }

    protected static void disconnectClient(final long networkID) {
        Console.log(Console.INFO, "Client[" + networkID + "] requested disconnect!");
        final ClientHandler handler = clients.get(networkID);
        if (handler == null) {
            Console.log(Console.WARNING, "Client with networkID[" + networkID + "] doesn't exist!");
            return;
        }

        if (handler.inRoom()) {
            final Room room = handler.getRoom();
            handler.leaveRoom();
            if (room.isOpen()) {
                room.stop();
            }
        }

        handler.forceDisconnect();
        NetworkID.freeId(networkID);
        clients.remove(networkID);
        Console.log(Console.INFO, "Client[" + networkID + "] disconnected!");
        ServerGUI.getInstance().updateClientsConnectedCount(clients.size());
    }

    public static void start() throws IOException {
        if (isRunning()) {
            Console.log(Console.WARNING, "Can't start Server.Server is already running!");
            return;
        }

        DatabaseService.connect();
        if (!DatabaseService.isConnected()) {
            Console.log(Console.ERROR, "Failed to start server!");
            return;
        }

        Console.log(Console.INFO, "Starting Server on port: " + PORT);
        serverSocket = new ServerSocket(PORT);
        if (serverSocket.isClosed()) {
            Console.log(Console.ERROR, "Failed to start Server on port: " + PORT);
            return;
        }

        Console.log(Console.INFO, "Creating Executor Thread Pool!");
        threadPool = Executors.newFixedThreadPool(10);
        Console.log(Console.INFO, "Thread Pool created!");

        running = true;
        Console.log(Console.INFO, "Server started on port: " + PORT);
        Console.log(Console.INFO, "Server InetAdder: " + serverSocket.getInetAddress());

        onServerStarted();
    }

    public static void open() {
        if (!isRunning() || serverSocket.isClosed()) {
            Console.log(Console.ERROR, "Can't open Server.Server is not running!");
            return;
        }
        if (isOpen()) {
            Console.log(Console.WARNING, "Can't open Server.Server is already open!");
            return;
        }
        Console.log(Console.INFO, "Opening Server for connections!");
        open = true;
        new Thread(() -> {
            try {
                serverSocket.setSoTimeout(1000);
            } catch (SocketException e) {
                throw new RuntimeException(e);
            }
            Console.log(Console.INFO, "Server opened for connections!");
            onServerOpened();
            while (isOpen()) {
                try {
                    onConnectionAccepted(serverSocket.accept());
                } catch (SocketTimeoutException ex) {
                    continue;
                } catch (IOException ex) {
                    Console.log(Console.ERROR, "Failed to accept new connection!");
                }
            }
            Console.log(Console.INFO, "Server closed for connections!");
            onServerClosed();
        }).start();
    }

    public static void close() {
        if (!isOpen()) {
            Console.log(Console.WARNING, "Can't close Server.Server is already closed!");
            return;
        }
        Console.log(Console.INFO, "Closing Server for connections!");
        open = false;
    }

    public static void stop() throws IOException {
        if (!isRunning()) {
            Console.log(Console.WARNING, "Can't stop Server.Server is already not running!");
            return;
        }
        Console.log(Console.INFO, "Stopping Server!");
        close();
        disconnectClients();
        serverSocket.close();
        Console.log(Console.INFO, "Terminating Executor Thread Pool!");
        threadPool.shutdownNow();
        Console.log(Console.INFO, "Thread Pool terminated!");
        DatabaseService.disconnect();
        running = false;
        Console.log(Console.INFO, "Server stopped!");
        onServerStopped();
    }

    private static void onServerStarted() {
        ServerGUI.getInstance().serverStarted();
    }

    private static void onServerStopped() {
        ServerGUI.getInstance().serverStopped();
    }

    private static void onServerOpened() {
        ServerGUI.getInstance().serverOpened();
    }

    private static void onServerClosed() {
        ServerGUI.getInstance().serverClosed();
    }

    private static Room findAvailableRoom() {
        Console.log(Console.INFO, "Searching for available Room!");
        for (Map.Entry<Long, Room> entry : rooms.entrySet()) {
            final Room room = entry.getValue();
            if (room.isOpen()) {
                Console.log(Console.INFO, "Available Room[" + room.getNetworkID() + "] found!");
                return room;
            }
        }
        Console.log(Console.INFO, "No available Room found,creating new!");
        final long networkID = NetworkID.getId();
        final Room room = new Room(networkID);
        rooms.put(networkID, room);
        Console.log(Console.INFO, "New Room[" + networkID + "] created!");
        return room;
    }

    protected static void destroyRoom(final long networkID) {
        Console.log(Console.INFO, "Room[" + networkID + "] requested destroying!");
        final Room room = rooms.get(networkID);
        if (room == null) {
            Console.log(Console.WARNING, "Room with networkID[" + networkID + "] doesn't exist!");
            return;
        }

        if (!room.isEmpty()) {
            room.stop();
            return;
        }

        NetworkID.freeId(networkID);
        rooms.remove(networkID);

        Console.log(Console.INFO, "Room[" + networkID + "] destroyed!");
        ServerGUI.getInstance().updateActiveRoomsCount(rooms.size());
    }

    private static boolean isClientAlreadyConnected(final int databaseID) {
        for (Map.Entry<Long, ClientHandler> entry : clients.entrySet()) {
            if (entry.getValue().getDatabaseID() == databaseID)
                return true;
        }
        return false;
    }

    private static void onConnectionAccepted(final Socket socket) {
        Console.log(Console.INFO, "New connection accepted!");

        final long networkID = NetworkID.getId();
        try {
            final ClientHandler handler = new ClientHandler(networkID, socket);
            if (handleUserValidation(handler)) {
                clients.put(networkID, handler);
                Console.log(Console.INFO, "New Client[" + networkID + "] registered!");
                threadPool.submit(handler);
                ServerGUI.getInstance().updateClientsConnectedCount(clients.size());
            } else {
                Console.log(Console.WARNING, "New Client[" + networkID + "] failed to register!");
                NetworkID.freeId(networkID);
            }
        } catch (IOException e) {
            NetworkID.freeId(networkID);
            Console.log(Console.ERROR, "Failed to initialize ClientHandler!");
        }
    }

    private static boolean handleUserValidation(final ClientHandler handler) throws IOException {
        final Socket socket = handler.getSocket();
        final long networkID = handler.getNetworkID();
        boolean isValid = false;
        try {
            final int timeout = socket.getSoTimeout();
            socket.setSoTimeout(1000);

            ConnectRequest request = (ConnectRequest) handler.receive();
            Console.log(Console.INFO, "Received Packet from Client[" + networkID + "]!");
            if (request == null) {
                Console.log(Console.ERROR, "Client[" + networkID + "] didn't requested registration!");
                handler.forceDisconnect();
                return false;
            }

            int userID = DatabaseService.INVALID_USER;
            if (request.isRegistering()) {
                Console.log(Console.INFO, "Validating registration!");
                if (DatabaseService.addUser(request.getUsername(), request.getPassword())) {
                    isValid = true;
                }
            }
            if (!isValid) {
                Console.log(Console.INFO, "Validating login!");
                userID = DatabaseService.validateLogin(request.getUsername(), request.getPassword());
                isValid = userID != DatabaseService.INVALID_USER && !isClientAlreadyConnected(userID);
            }
            if (isValid) request.setValid();
            Console.log(Console.INFO, "Login validated[" + isValid + "]!");
            handler.setDatabaseID(userID);
            request.setUserData(DatabaseService.getUserDataById(userID));

            handler.trySend(request);
            Console.log(Console.INFO, "Packet sent to Client[" + networkID + "]!");

            socket.setSoTimeout(timeout);
        } catch (SocketTimeoutException ex) {
            Console.log(Console.WARNING, "Client[" + networkID + "] didn't requested login!");
        } catch (SocketException | ClassNotFoundException ex) {
            Console.log(Console.ERROR, "Client[" + networkID + "] failed to request login!");
        }
        return isValid;
    }

    protected static void addInQueue(final ClientHandler handler) {
        if (handler.inRoom()) {
            Console.log(Console.WARNING, "Can't add Client[" + handler.getNetworkID() + "] in queue.Client is already in queue!");
            return;
        }
        final Room room = findAvailableRoom();
        room.setHandler(handler);

        ServerGUI.getInstance().updateActiveRoomsCount(rooms.size());
    }
}