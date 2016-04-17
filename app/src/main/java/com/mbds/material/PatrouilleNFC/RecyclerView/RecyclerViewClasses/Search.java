package com.mbds.material.PatrouilleNFC.RecyclerView.RecyclerViewClasses;


public class Search {

    String title;
    String excerpt;
    String image;
    private String imageprofile;
    private String tel;
    private String map;
    private String speciality;
    public Search(String title, String excerpt, String image) {
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

    public String getImageprofile() {
        return imageprofile;
    }

    public void setImageprofile(String imageprofile) {
        this.imageprofile = imageprofile;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getMap() {
        return map;
    }

    public void setMap(String map) {
        this.map = map;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }
}
