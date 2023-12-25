package com.chess2.networking.packets;

import com.chess2.networking.Packet;

public class LoginStatus extends Packet {
    public LoginStatus() {
    }

    public LoginStatus(long senderId) {
        super(senderId);
    }
}
