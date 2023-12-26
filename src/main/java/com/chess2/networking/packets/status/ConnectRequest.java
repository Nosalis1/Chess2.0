package com.chess2.networking.packets.status;

import com.chess2.networking.Packet;
import com.chess2.networking.database.UserData;

public class ConnectRequest extends Packet {
    public ConnectRequest() {
    }

    public ConnectRequest(final String username, final String password) {
        this.username = username;
        this.password = password;
    }

    private String username, password;

    public final String getUsername() {
        return this.username;
    }

    public final String getPassword() {
        return this.password;
    }

    private boolean valid = false;

    public void setValid() {
        this.valid = true;
    }

    public final boolean isValid() {
        return this.valid;
    }

    private UserData userData;

    public final UserData getUserData() {
        return this.userData;
    }

    public void setUserData(final UserData userData) {
        this.userData = userData;
    }
}