package com.example.madasignment.home.lesson_unit.test;

import java.io.Serializable;

public class QuestionStatus implements Serializable {
    public int questionNumber;
    public boolean isCorrect;
    public boolean isVocabulary;
    public boolean isSpeaking;
    public boolean isListening;
    public boolean isMatching;
    // Constructor
    public QuestionStatus(int questionNumber, boolean isCorrect, boolean isVocabulary, boolean isSpeaking, boolean isListening) {
        this.questionNumber = questionNumber;
        this.isCorrect = isCorrect;
        this.isVocabulary = isVocabulary;
        this.isSpeaking = isSpeaking;
        this.isListening = isListening;
        this.isMatching = isMatching;
    }
}
