package com.example.madasignment.gamification.leaderboard;

public class LeaderboardItem {
    private int rank;
    private String username;
    private int xp;
    private String userId; // Add this field

    public LeaderboardItem(int rank, String username, int xp, String userId) {
        this.rank = rank;
        this.username = username;
        this.xp = xp;
        this.userId = userId;
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

    public String getUserId() {
        return userId;
    }
}
