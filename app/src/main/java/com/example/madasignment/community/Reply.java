package com.example.madasignment.community;

public class Reply {
    private String replyId;
    private String userName;
    private String content;
    private String userProfilePic; // Optional, can be null
    private String timestamp;

    public Reply() {
        // Default constructor required for Firebase
    }

    public Reply(String replyId, String userName, String content, String userProfilePic, String timestamp) {
        this.replyId = replyId;
        this.userName = userName;
        this.content = content;
        this.userProfilePic = userProfilePic;
        this.timestamp = timestamp;
    }

    public String getReplyId() {
        return replyId;
    }

    public void setReplyId(String replyId) {
        this.replyId = replyId;
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

    public String getUserProfilePic() {
        return userProfilePic;
    }

    public void setUserProfilePic(String userProfilePic) {
        this.userProfilePic = userProfilePic;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
