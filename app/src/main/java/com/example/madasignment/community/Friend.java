package com.example.madasignment.community;

public class Friend {
    private String name;
    private int xp;
    private int profileImage;

    public Friend(String name, int xp, int profileImage) {
        this.name = name;
        this.xp = xp;
        this.profileImage = profileImage;
    }

    public Friend(int icProfile, String abcdef) {
    }

    public String getName() {
        return name;
    }

    public int getXp() {
        return xp;
    }

    public int getProfileImage() {
        return profileImage;
    }
}
