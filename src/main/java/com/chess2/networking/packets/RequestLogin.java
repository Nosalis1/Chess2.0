package com.chess2.networking.packets;

import com.chess2.networking.Packet;
import javafx.util.Pair;

public class RequestLogin extends Packet {
    public RequestLogin() {
        super();
    }

    public RequestLogin(long senderId) {
        super(senderId);
    }

    private Pair<String, String> data;

    public final Pair<String, String> getData() {
        return this.data;
    }

    public void setData(final Pair<String, String> data) {
        this.data = data;
    }
}
