package com.chess2.networking.packets;

import com.chess2.networking.Packet;
import com.chess2.utility.Move;

public class NetworkMove extends Packet {
    public NetworkMove() {
        super();
    }

    @SuppressWarnings("unused")
    public NetworkMove(long senderId) {
        super(senderId);
    }

    private Move move;

    public final Move getMove() {
        return this.move;
    }

    public void setMove(final Move move) {
        this.move = move.deepCopy();
    }
}