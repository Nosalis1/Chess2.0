package com.chess2.networking.packets;

import com.chess2.networking.Packet;

public class ForceDisconnect extends Packet {
    public ForceDisconnect() {
        super();
    }

    public ForceDisconnect(long senderId) {
        super(senderId);
    }
}
