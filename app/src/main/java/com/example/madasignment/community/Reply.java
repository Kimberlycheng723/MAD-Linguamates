package com.example.madasignment.community;

public class Reply {
    private final String userName;
    private final String content;

    public Reply(String userName, String content) {
        this.userName = userName;
        this.content = content;
    }

    public String getUserName() {
        return userName;
    }

    public String getContent() {
        return content;
    }
}
