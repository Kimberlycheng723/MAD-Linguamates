package com.example.madasignment.gamification;

public class LeaderboardItem {
    private int rank;
    private String username;
    private int xp;

    public LeaderboardItem(int rank, String username, int xp) {
        this.rank = rank;
        this.username = username;
        this.xp = xp;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getUsername() {
        return username;
    }

    public int getXp() {
        return xp;
    }
}
