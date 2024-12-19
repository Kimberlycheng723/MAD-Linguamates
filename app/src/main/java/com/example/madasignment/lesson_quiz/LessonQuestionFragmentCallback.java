package com.example.madasignment.lesson_quiz;

public interface LessonQuestionFragmentCallback {
    void onQuestionAnswered(boolean isCorrect);
    void onQuestionSkipped(); // Add this for skipping
}