package com.example.madasignment.lessons.Module.read;

public class ReadOnlyTranscription {
    private final String chinese;
    private final String english;

    public ReadOnlyTranscription(String chinese, String english) {
        this.chinese = chinese;
        this.english = english;
    }

    public String getChinese() {
        return chinese;
    }

    public String getEnglish() {
        return english;
    }
}
