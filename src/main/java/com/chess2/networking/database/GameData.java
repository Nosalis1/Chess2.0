package com.chess2.networking.database;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class GameData {
    private final StringProperty opponentUsername;
    private final SimpleBooleanProperty loss;
    private final StringProperty startTime;

    public GameData(final String opponentUsername, final boolean loss, final String startTime) {
        this.opponentUsername = new SimpleStringProperty(opponentUsername);
        this.loss = new SimpleBooleanProperty(loss);
        this.startTime = new SimpleStringProperty(startTime);
    }

    public String getOpponentUsername() {
        return opponentUsername.get();
    }

    public StringProperty opponentUsernameProperty() {
        return opponentUsername;
    }

    public boolean isLoss() {
        return loss.get();
    }

    public SimpleBooleanProperty lossProperty() {
        return loss;
    }

    public String getStartTime() {
        return startTime.get();
    }

    public StringProperty startTimeProperty() {
        return startTime;
    }
}