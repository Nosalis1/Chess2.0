package com.chess2.networking.database;

import java.io.Serializable;
import java.util.Date;

public class UserData implements Serializable {
    private String username;
    private Date dateCreated;
    private transient int playtime;
    private int gamesWon;
    private int gamesLost;

    public UserData() {
    }

    public UserData(String username, Date dateCreated, int gamesWon, int gamesLost) {
        this.username = username;
        this.dateCreated = dateCreated;
        this.gamesWon = gamesWon;
        this.gamesLost = gamesLost;
        calculatePlaytime();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
        calculatePlaytime();
    }

    public int getGamesWon() {
        return gamesWon;
    }

    public void setGamesWon(int gamesWon) {
        this.gamesWon = gamesWon;
    }

    public int getGamesLost() {
        return gamesLost;
    }

    public void setGamesLost(int gamesLost) {
        this.gamesLost = gamesLost;
    }

    private void calculatePlaytime() {
        Date today = new Date();
        long diffMillis = today.getTime() - dateCreated.getTime();
        playtime = (int) (diffMillis / (60 * 60 * 1000));
    }

    public final int getPlaytime(){
        calculatePlaytime();
        return this.playtime;
    }

    @Override
    public String toString() {
        return "UserData{" +
                "dateCreated=" + dateCreated +
                ", playtime=" + playtime +
                ", gamesWon=" + gamesWon +
                ", gamesLost=" + gamesLost +
                '}';
    }
}