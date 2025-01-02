package com.example.madasignment.community;

public class DiscussionPost {
    private final String userName;
    private final String content;
    private final int answersCount;

    public DiscussionPost(String userName, String content, int answersCount) {
        this.userName = userName;
        this.content = content;
        this.answersCount = answersCount;
    }

    public String getUserName() {
        return userName;
    }

    public String getContent() {
        return content;
    }

    public int getAnswersCount() {
        return answersCount;
    }
}
