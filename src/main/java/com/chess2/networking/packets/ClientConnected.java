package com.chess2.networking.packets;

import com.chess2.networking.Packet;

public class ClientConnected extends Packet {
    @SuppressWarnings("unused")
    public ClientConnected() {
        super();
    }

    public ClientConnected(long senderId) {
        super(senderId);
    }

    private boolean white = true;

    public final boolean isWhite() {
        return this.white;
    }

    public void setWhite(final boolean value) {
        this.white = value;
    }
}