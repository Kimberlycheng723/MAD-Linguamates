package com.example.madasignment.lesson_unit;
public class LessonUnitData {
    private String title;
    private int progress; // Progress percentage
    private String action; // Action text for the button
    private int imageResId; // Image resource ID
    private String backgroundColor; // Background color for the card

    private String buttonColor; // Button color (Hex or Color Resource)

    public LessonUnitData(String title, int progress, String action, int imageResId, String backgroundColor, String buttonColor) {
        this.title = title;
        this.progress = progress;
        this.action = action;
        this.imageResId = imageResId;
        this.backgroundColor = backgroundColor;
        this.buttonColor = buttonColor;
    }

    public String getTitle() {
        return title;
    }

    public int getProgress() {
        return progress;
    }

    public String getAction() {
        return action;
    }

    public int getImageResId() {
        return imageResId;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public String getButtonColor() {
        return buttonColor;
    }
}
