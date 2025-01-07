package com.example.madasignment.lessons.Module.module;

public class ModuleData {
    private String title;
    private int imageResource;
    private String readContent;
    private String videoUrl;

    public ModuleData(String title, int imageResource, String readContent, String videoUrl) {
        this.title = title;
        this.imageResource = imageResource;
        this.readContent = readContent;
        this.videoUrl = videoUrl;

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
}


