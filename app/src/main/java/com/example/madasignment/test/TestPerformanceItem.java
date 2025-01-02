package com.example.madasignment.test;

public class TestPerformanceItem {

    private String questionText;
    private String userAnswer;
    private String correctAnswer;
    private boolean isCorrect;

    public TestPerformanceItem(String questionText, String userAnswer, String correctAnswer, boolean isCorrect) {
        this.questionText = questionText;
        this.userAnswer = userAnswer;
        this.correctAnswer = correctAnswer;
        this.isCorrect = isCorrect;
    }

    public String getQuestionText() {
        return questionText;
    }

    public String getUserAnswer() {
        return userAnswer;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public boolean isCorrect() {
        return isCorrect;
    }
}
