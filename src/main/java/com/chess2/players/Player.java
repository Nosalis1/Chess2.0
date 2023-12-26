package com.chess2.players;

import com.chess2.Console;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class Player {
    protected static final ExecutorService threadPool = Executors.newFixedThreadPool(1);

    public static void shutdownThreadPool() {
        Console.log(Console.INFO, "Shutting down Player thread pool!");
        threadPool.shutdown();
    }

    public enum Type {
        LOCAL, AI, ONLINE
    }

    public void setup(final boolean white) {
        this.white = white;
    }

    private boolean white = true;

    public final boolean isWhite() {
        return white;
    }

    public abstract void play();
}