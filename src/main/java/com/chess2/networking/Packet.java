package com.chess2.networking;

import java.io.Serializable;

public class Packet implements Serializable {
    public static final long SERVER_SENDER_ID = 1001;

    public Packet() {
        this.senderId = SERVER_SENDER_ID;
    }

    public Packet(final long senderId) {
        this.senderId = senderId;
    }

    private final long senderId;
    public final long getSenderId(){return this.senderId;}
}