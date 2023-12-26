package com.chess2.networking.serverside;

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
import java.util.Stack;
import java.util.regex.Pattern;

public class ServerGUI extends Application {
    private static ServerGUI instance;

    public static ServerGUI getInstance() {
        return instance;
    }

    private final Label serverStatusLabel = new Label("Server Status: Stopped");
    private final Label clientCountLabel = new Label("Clients Connected: 0");
    private final Label roomCountLabel = new Label("Active Rooms: 0");

    private final Button runningButton = new Button("Start");
    private final Button connectionsButton = new Button("Open");

    private final TextArea statusArea = new TextArea();

    @Override
    public void stop() throws Exception {
        if (Server.isRunning()) {
            Server.stop();
        }
        super.stop();
    }

    @Override
    public void start(Stage stage) throws Exception {
        instance = this;
        stage.setTitle("Server");

        setupStreams();
        setupNodes();

        BorderPane root = new BorderPane();
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.TOP_CENTER);

        ScrollPane scrollPane = new ScrollPane(statusArea);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);

        HBox buttonsBox = new HBox(10, runningButton, connectionsButton);
        buttonsBox.setAlignment(Pos.CENTER);

        vBox.getChildren().addAll(serverStatusLabel, clientCountLabel,roomCountLabel, buttonsBox, scrollPane);

        stage.setScene(new Scene(root, 300, 300));
        root.setCenter(vBox);
        stage.show();
    }

    private void setupStreams() {
        class CustomOutputStream extends OutputStream {
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

        PrintStream customOut = new PrintStream(new CustomOutputStream(System.out, statusArea));
        PrintStream customErr = new PrintStream(new CustomOutputStream(System.err, statusArea));
        System.setOut(customOut);
        System.setErr(customErr);
    }

    private void setupNodes() {
        this.serverStatusLabel.setStyle("-fx-font-size: 14; -fx-font-weight: bold;");
        VBox.setMargin(this.serverStatusLabel, new Insets(0, 0, 10, 0));

        this.clientCountLabel.setStyle("-fx-font-size: 14; -fx-font-weight: bold;");
        VBox.setMargin(this.clientCountLabel, new Insets(0, 0, 10, 0));

        this.roomCountLabel.setStyle("-fx-font-size: 14; -fx-font-weight: bold;");
        VBox.setMargin(this.roomCountLabel, new Insets(0, 0, 10, 0));

        this.statusArea.setEditable(false);
        VBox.setMargin(statusArea, new Insets(10));

        connectionsButton.setDisable(true);
        runningButton.setOnAction(actionEvent -> {
            try {
                if (Server.isRunning()) Server.stop();
                else Server.start();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        connectionsButton.setOnAction(actionEvent -> {
            if (Server.isOpen()) Server.close();
            else Server.open();
        });
    }

    protected void serverStarted() {
        Platform.runLater(() -> {
            this.runningButton.setText("Stop");
            this.connectionsButton.setDisable(false);
            this.serverStatusLabel.setText("Server Status: Running");
        });
    }

    protected void serverStopped() {
        Platform.runLater(() -> {
            this.runningButton.setText("Start");
            this.connectionsButton.setDisable(true);
            this.serverStatusLabel.setText("Server Status: Stopped");
        });
    }

    protected void serverOpened() {
        Platform.runLater(() -> {
            this.connectionsButton.setText("Close");
            this.connectionsButton.requestLayout();
        });
    }

    protected void serverClosed() {
        Platform.runLater(() -> {
            this.connectionsButton.setText("Open");
            this.connectionsButton.requestLayout();
        });
    }

    protected void updateClientsConnectedCount(final int count) {
        Platform.runLater(() -> {
            this.clientCountLabel.setText("Clients Connected: " + count);
        });
    }
    protected void updateActiveRoomsCount(final int count) {
        Platform.runLater(() -> {
            this.roomCountLabel.setText("Active Rooms: " + count);
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
