package com.example.madasignment.community.discussion_forum;

public class Reply {
    private String replyId;
    private String userId;
    private String userName;
    private String content;
    private String timestamp;

    // Default constructor required for Firebase
    public Reply() {
    }

    public Reply(String replyId, String userId, String userName, String content, String timestamp) {
        this.replyId = replyId;
        this.userId = userId;
        this.userName = userName;
        this.content = content;
        this.timestamp = timestamp;
    }

    public String getReplyId() {
        return replyId;
    }

    public void setReplyId(String replyId) {
        this.replyId = replyId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
