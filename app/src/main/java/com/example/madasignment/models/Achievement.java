package com.example.madasignment.models;

public class Achievement {
    private String type;
    private boolean unlocked;

    public Achievement() {}

    public Achievement(String type, boolean unlocked) {
        this.type = type;
        this.unlocked = unlocked;
    }

    public String getType() { return type; }
    public boolean isUnlocked() { return unlocked; }
}
