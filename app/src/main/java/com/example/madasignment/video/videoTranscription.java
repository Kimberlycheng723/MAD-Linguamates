package com.example.madasignment.video;

public class videoTranscription {
    public String startTime;
    public String endTime;
    public String chinese;
    public String english;

    public videoTranscription(String startTime, String endTime, String chinese, String english) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.chinese = chinese;
        this.english = english;
    }
}
