package com.example.madasignment.gamification;

public class Badge {
    private String name;
    private String description;
    private String state; // "locked", "in_progress", "completed"
    private int imageResId; // Drawable resource ID for the badge image

    public Badge(String name, String description, String state, int imageResId) {
        this.name = name;
        this.description = description;
        this.state = state;
        this.imageResId = imageResId;
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

    public int getImageResId() {
        return imageResId;
    }
}
