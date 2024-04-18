package com.example.cpr.Model;

public class Round {

    private int roundId;

    private int points;

    private int user;

    public Round(int points, int user) {
        this.points = points;
        this.user = user;
    }

    public int getRoundId() {
        return roundId;
    }

    public void setRoundId(int roundId) {
        this.roundId = roundId;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }
}
