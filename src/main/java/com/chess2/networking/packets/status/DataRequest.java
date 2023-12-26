package com.chess2.networking.packets.status;

import com.chess2.networking.Packet;
import com.chess2.networking.database.UserData;

public class DataRequest extends Packet {
    public DataRequest(final int databaseID) {
        if (databaseID == -1) throw new RuntimeException();
        this.databaseID = databaseID;
    }

    public DataRequest(final int databaseID, final UserData userData) {
        if (databaseID == -1) throw new RuntimeException();
        this.databaseID = databaseID;
        this.setUserData(userData);
    }

    private final int databaseID;

    public final int getDatabaseID() {
        return this.databaseID;
    }

    private UserData userData;

    public UserData getUserData() {
        return this.userData;
    }

    public void setUserData(final UserData userData) {
        this.userData = userData;
    }
}