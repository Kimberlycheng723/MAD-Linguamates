package com.example.madasignment.community;

public class Friend {
    private String id; // Unique identifier for the friend
    private String name;
    private int xp; // Experience points

    // Default constructor for Firebase
    public Friend() {
    }

    public Friend(String id, String name, int xp) {
        this.id = id;
        this.name = name;
        this.xp = xp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getXp() {
        return xp;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }
}
