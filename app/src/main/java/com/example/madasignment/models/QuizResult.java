package com.example.madasignment.models;

public class QuizResult {
    private String quizId;
    private int score;
    private int xp;
    private long timeSpent;

    // Required for Firebase
    public QuizResult() {}

    public QuizResult(String quizId, int score, int xp, long timeSpent) {
        this.quizId = quizId;
        this.score = score;
        this.xp = xp;
        this.timeSpent = timeSpent;
    }

    public String getQuizId() { return quizId; }
    public int getScore() { return score; }
    public int getXp() { return xp; }
    public long getTimeSpent() { return timeSpent; }
}
