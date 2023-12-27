package com.chess2.networking.database;

import com.chess2.Console;
import com.mysql.cj.exceptions.ClosedOnExpiredPasswordException;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UserData implements Serializable {
    private int userId;
    private String username;
    private String dateCreated;
    private int gamesWon;
    private int gamesLost;
    private transient long totalPlaytime;

    public UserData(int userId, String username, String dateCreated, int gamesWon, int gamesLost) {
        this.userId = userId;
        this.username = username;
        this.dateCreated = dateCreated;
        this.gamesWon = gamesWon;
        this.gamesLost = gamesLost;

        this.totalPlaytime = calculateTotalPlaytime();
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
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

    public long getTotalPlaytime() {
        calculateTotalPlaytime();
        return totalPlaytime;
    }

    public void setTotalPlaytime(long totalPlaytime) {
        this.totalPlaytime = totalPlaytime;
    }

    private long calculateTotalPlaytime() {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date createdDate = dateFormat.parse(dateCreated);
            Date currentDate = new Date();

            return currentDate.getTime() - createdDate.getTime();
        } catch (ParseException e) {
            Console.log(Console.ERROR, "Error parsing Date!");
            return 0;
        }
    }
}