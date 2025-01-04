package com.example.madasignment.gamification;

public class LeaderboardItem {
    private int rank;
    private String username;
    private int xp;
    private int imageResId;

    public LeaderboardItem(int rank, String username, int xp) {
        this.rank = rank;
        this.username = username;
        this.xp = xp;
        this.imageResId = imageResId;
    }

    public int getRank() {
        return rank;
    }

    public String getUsername() {
        return username;
    }

    public int getXp() {
        return xp;
    }

    public int getImageResId() {
        return imageResId;
    }
}
