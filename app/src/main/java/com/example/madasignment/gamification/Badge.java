package com.example.madasignment.gamification;

public class Badge {
    private String name;
    private String description;
    private String state; // "locked", "in_progress", "completed"
    private int imageResId;
    private int goal; // Target count for completion
    private int progress; // Current progress

    public Badge(String name, String description, String state, int imageResId, int goal, int progress) {
        this.name = name;
        this.description = description;
        this.state = state;
        this.imageResId = imageResId;
        this.goal = goal;
        this.progress = progress;
    }

    public Badge(String name, String badgeDescription, String state, int badgeImage) {
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getImageResId() {
        return imageResId;
    }

    public void setImageResId(int imageResId) {
        this.imageResId = imageResId;
    }

    public int getGoal() {
        return goal;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public void updateState() {
        if (progress >= goal) {
            this.state = "completed";
        } else if (progress > 0) {
            this.state = "in_progress";
        } else {
            this.state = "locked";
        }
    }
}
