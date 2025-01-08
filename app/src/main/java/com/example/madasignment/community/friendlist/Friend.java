package com.example.madasignment.community.friendlist;

public class Friend {
    private String userId;
    private String name;

    public Friend() {
        // Default constructor required for Firebase
    }

    public Friend(String userId, String name) {
        this.userId = userId;
        this.name = name;
    }

    // Getters and setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
