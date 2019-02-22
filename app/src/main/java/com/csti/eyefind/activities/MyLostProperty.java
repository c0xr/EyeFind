package com.csti.eyefind.activities;

public class MyLostProperty {
    private String name;
    private String introduce;
    private String place;
    private String price;

    private int imageId;

    public MyLostProperty(String name, String introduce, String place, String price , int imageId){
        this.name = name;
        this.introduce = introduce;
        this.place = place;
        this.price = price;
        this.imageId = imageId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }
}
