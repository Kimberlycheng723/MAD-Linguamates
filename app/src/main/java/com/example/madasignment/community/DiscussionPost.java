package com.example.madasignment.community;

public class DiscussionPost {
    private String postId;
    private String content;
    private String userName;
    private String timestamp;

    // Default constructor for Firebase
    public DiscussionPost() {
    }

    // Constructor with all fields
    public DiscussionPost(String postId, String content, String userName, String timestamp) {
        this.postId = postId;
        this.content = content;
        this.userName = userName;
        this.timestamp = timestamp;
    }

    // Getters and setters
    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
