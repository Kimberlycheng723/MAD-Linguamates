package com.example.madasignment.models;

import java.util.List;

public class QuizQuestion {
    private String id;
    private String questionText;
    private List<String> options;
    private String correctAnswer;

    // Required for Firebase
    public QuizQuestion() {}

    public QuizQuestion(String id, String questionText, List<String> options, String correctAnswer) {
        this.id = id;
        this.questionText = questionText;
        this.options = options;
        this.correctAnswer = correctAnswer;
    }

    public String getId() { return id; }
    public String getQuestionText() { return questionText; }
    public List<String> getOptions() { return options; }
    public String getCorrectAnswer() { return correctAnswer; }
}
