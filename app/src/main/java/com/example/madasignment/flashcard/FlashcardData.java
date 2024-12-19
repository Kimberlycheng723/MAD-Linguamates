package com.example.madasignment.flashcard;

public class FlashcardData {
    private String title;
    private int frontImage;
    private int backImage;
    private int descriptionImage;
    private int audioResId; // New field for audio resource

    public FlashcardData(String title, int frontImage,int backImage, int descriptionImage, int audioResId) {
        this.title = title;
        this.frontImage = frontImage;
        this.backImage = backImage; // Update here
        this.descriptionImage = descriptionImage;
        this.audioResId = audioResId;
    }

    public String getTitle() {
        return title;
    }

    public int getFrontImage() {
        return frontImage;
    }

    public int getDescriptionImage() {
        return descriptionImage;
    }

    public int getAudioResId() {
        return audioResId;
    }

    public int getBackImage() {
        return backImage; // Getter for back image
    }
}
