package com.chess2.networking.packets.status;

import com.chess2.networking.Packet;
import com.chess2.networking.database.UserData;

public class ConnectRequest extends Packet {
    public ConnectRequest() {
    }

    public ConnectRequest(final String username, final String password, final boolean register) {
        this.username = username;
        this.password = password;
        this.register = register;
    }

    private String username, password;

    public final String getUsername() {
        return this.username;
    }

    public final String getPassword() {
        return this.password;
    }

    private boolean register;

    public final boolean isRegistering() {
        return this.register;
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