package com.example.oneshot.model;

import java.util.List;

public class Chapter {
    private String Chapter_name;
    private List<String> Chapter_images;

    public Chapter() {
    }

    public Chapter(String Chapter_name, List<String> Chapter_images) {
        this.Chapter_name = Chapter_name;
        this.Chapter_images = Chapter_images;
    }

    public String getChapter_name() {
        return Chapter_name;
    }

    public void setChapter_name(String Chapter_name) {
        this.Chapter_name = Chapter_name;
    }

    public List<String> getChapter_images() {
        return Chapter_images;
    }

    public void setChapter_images(List<String> Chapter_images) {
        this.Chapter_images = Chapter_images;
    }
}