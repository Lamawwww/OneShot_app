package com.example.oneshot.model;

public class Manga {
    private String Name;
    private String Cover;

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