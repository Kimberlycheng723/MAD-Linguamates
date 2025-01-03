package com.example.madasignment.community;

public class Report {
    private String reportId;
    private String reportedPostId;
    private String reportedBy;
    private String reason;
    private String timestamp;

    public Report() {
        // Default constructor for Firebase
    }

    public Report(String reportId, String reportedPostId, String reportedBy, String reason, String timestamp) {
        this.reportId = reportId;
        this.reportedPostId = reportedPostId;
        this.reportedBy = reportedBy;
        this.reason = reason;
        this.timestamp = timestamp;
    }

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

