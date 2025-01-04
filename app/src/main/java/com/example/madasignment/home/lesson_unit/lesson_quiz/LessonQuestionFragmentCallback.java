package com.example.madasignment.home.lesson_unit.lesson_quiz;

public interface LessonQuestionFragmentCallback {
    void onQuestionAnswered(boolean isCorrect);
    void onQuestionSkipped(); // Add this for skipping
}