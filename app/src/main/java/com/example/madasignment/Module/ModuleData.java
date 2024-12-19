package com.example.madasignment.Module;

public class ModuleData {
    private String title;
    private int imageResource;
    private String readContent;
    private String videoUrl;
    private boolean readCompleted;
    private boolean listenCompleted;

    public ModuleData(String title, int imageResource, String readContent, String videoUrl) {
        this.title = title;
        this.imageResource = imageResource;
        this.readContent = readContent;
        this.videoUrl = videoUrl;
        this.readCompleted = false;
        this.listenCompleted = false;
    }

    public String getTitle() {
        return title;
    }

    public int getImageResource() {
        return imageResource;
    }

    public String getReadContent() {
        return readContent;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public boolean isReadCompleted() {
        return readCompleted;
    }

    public void setReadCompleted(boolean readCompleted) {
        this.readCompleted = readCompleted;
    }

    public boolean isListenCompleted() {
        return listenCompleted;
    }

    public void setListenCompleted(boolean listenCompleted) {
        this.listenCompleted = listenCompleted;
    }

    public boolean isCompleted() {
        return readCompleted && listenCompleted;
    }
}
