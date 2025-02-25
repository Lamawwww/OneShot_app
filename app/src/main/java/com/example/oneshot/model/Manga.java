package com.example.oneshot.model;

import java.util.List;

public class Manga {
    private String Name;
    private String Cover;
    private String Category;
    private String Description;
    private List<Chapter> Chapter;
    private String Index; // New field for storing the index

    public Manga() {
    }

    public Manga(String Name, String Cover, String Category, String Description, List<Chapter> Chapter, String Index) {
        this.Name = Name;
        this.Cover = Cover;
        this.Category = Category;
        this.Description = Description;
        this.Chapter = Chapter;
        this.Index = Index;
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

    public String getCategory() {
        return Category;
    }

    public void setCategory(String Category) {
        this.Category = Category;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String Description) {
        this.Description = Description;
    }

    public List<Chapter> getChapter() {
        return Chapter;
    }

    public void setChapter(List<Chapter> Chapter) {
        this.Chapter = Chapter;
    }

    public String getIndex() {
        return Index;
    }

    public void setIndex(String Index) {
        this.Index = Index;
    }
}