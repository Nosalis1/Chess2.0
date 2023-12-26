package com.chess2.networking.packets.state;

import com.chess2.networking.Packet;
import com.chess2.networking.database.UserData;

public class BeginGame extends Packet {
    public BeginGame(final boolean localPlayerWhite, final UserData opponentData) {
        this.localPlayerWhite = localPlayerWhite;
        this.opponentData = opponentData;
    }

    private boolean localPlayerWhite;

    public final boolean isLocalPlayerWhite() {
        return this.localPlayerWhite;
    }

    private UserData opponentData;

    public final UserData getOpponentData() {
        return this.opponentData;
    }
}