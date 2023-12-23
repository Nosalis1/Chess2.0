package com.chess2.players;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class Player {
    protected static final ExecutorService threadPool = Executors.newFixedThreadPool(1);

    public static void shutdownThreadPool() {
        threadPool.shutdown();
    }

    public enum Type {
        LOCAL, AI, ONLINE
    }

    public record Data(boolean white) {
    }

    public void setup(final @NotNull Data data) {
        this.white = data.white;
    }

    private boolean white = true;

    public final boolean isWhite() {
        return white;
    }

    public abstract void play();
}