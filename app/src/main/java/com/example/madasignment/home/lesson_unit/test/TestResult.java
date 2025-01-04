package com.example.madasignment.home.lesson_unit.test;

import java.util.List;

public class TestResult {
    public String userId;
    public int score;
    public int totalQuestions;
    public List<QuestionStatus> questionStatuses; // Track each question's correctness
    public String timestamp;

    public TestResult() {
        // Required for Firebase
    }

    public TestResult(String userId, int score, int totalQuestions, List<QuestionStatus> questionStatuses, String timestamp) {
        this.userId = userId;
        this.score = score;
        this.totalQuestions = totalQuestions;
        this.questionStatuses = questionStatuses;
        this.timestamp = timestamp;
    }
}
