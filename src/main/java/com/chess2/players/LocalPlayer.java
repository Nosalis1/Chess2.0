package com.chess2.players;

import com.chess2.networking.Client;

public class LocalPlayer extends Player {
    @Override
    public void play() {
    }

    public LocalPlayer() {
        if (!Client.isConnected()) return;
        Player.threadPool.submit(Client::getInstance);
    }
}