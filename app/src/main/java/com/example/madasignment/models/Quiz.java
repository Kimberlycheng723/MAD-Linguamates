package com.example.madasignment.models;

import java.util.List;

public class Quiz {
    private String id;
    private String title;
    private List<QuizQuestion> questions;

    // Required for Firebase
    public Quiz() {}

    public Quiz(String id, String title, List<QuizQuestion> questions) {
        this.id = id;
        this.title = title;
        this.questions = questions;
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public List<QuizQuestion> getQuestions() { return questions; }
}
