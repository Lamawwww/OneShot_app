package com.example.oneshot.model;

import java.util.List;

public class Manga {
    private String Name;
    private String Cover;
    private String Category;
    private String Description;
    private List<Chapter> Chapters;

    public Manga() {
        // bawal mawala
    }

    public Manga(String Name, String Cover) {
        this.Name = Name;
        this.Cover = Cover;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getCover() {
        return Cover;
    }

    public void setCover(String Cover) {
        this.Cover = Cover;
    }
}