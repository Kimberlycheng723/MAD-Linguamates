package com.example.madasignment.gamification;

public class BadgeFirebaseModel {
    private String state;
    private int progress;
    private int goal;

    public BadgeFirebaseModel() {
        // Default constructor for Firebase
    }

    public BadgeFirebaseModel(String state, int progress, int goal) {
        this.state = state;
        this.progress = progress;
        this.goal = goal;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int getGoal() {
        return goal;
    }

    public void setGoal(int goal) {
        this.goal = goal;
    }
}
