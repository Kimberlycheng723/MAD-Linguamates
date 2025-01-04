package com.example.madasignment.community;

public class DiscussionPost {
    private String postId;
    private String title;
    private String content;
    private String userName;
    private String timestamp;

    // Default constructor (required for Firebase)
    public DiscussionPost() {
    }

    // Full constructor
    public DiscussionPost(String postId, String title, String content, String userName,String timestamp) {
        this.content = content;
        this.postId = postId;
        this.timestamp = timestamp;
        this.title = title;
        this.userName = userName;
    }

    // Getters and setters
    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
    public void setEmail(String userName) {
        this.userName = userName;
    }


    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
