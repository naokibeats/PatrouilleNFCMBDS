package com.mbds.material.PatrouilleNFC.RecyclerView.RecyclerViewClasses;


public class Scan {

    String title;
    String excerpt;
    String image;
    public Scan(String title, String excerpt, String image) {
        this.title = title;
        this.excerpt = excerpt;
        this.image = image;
    }
    public String getTitle() {
        return title;
    }

    public String getExcerpt() {
        return excerpt;
    }

    public String getImage() {
        return image;
    }
}
