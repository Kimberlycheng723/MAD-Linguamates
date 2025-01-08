package com.example.madasignment.community.discussion_forum;

public class Report {
    private String reportId;
    private String reportedPostId;
    private String reportedBy;
    private String reason;
    private String timestamp;

    public Report() {
        // Default constructor required for calls to DataSnapshot.getValue(Report.class)
    }

    public Report(String reportId, String reportedPostId, String reportedBy, String reason, String timestamp) {
        this.reportId = reportId;
        this.reportedPostId = reportedPostId;
        this.reportedBy = reportedBy;
        this.reason = reason;
        this.timestamp = timestamp;
    }

    // Getters and Setters
    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public String getReportedPostId() {
        return reportedPostId;
    }

    public void setReportedPostId(String reportedPostId) {
        this.reportedPostId = reportedPostId;
    }

    public String getReportedBy() {
        return reportedBy;
    }

    public void setReportedBy(String reportedBy) {
        this.reportedBy = reportedBy;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
