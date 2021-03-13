package com.example.chickenshop.model;

public class model_chickenitem {
    private int image;
    private String pid;
    private String item_name;
    private String price;

    public model_chickenitem(int image, String item_name, String price,String pid) {
        this.image = image;
        this.item_name = item_name;
        this.price = price;
        this.pid = pid;
    }

    public model_chickenitem() {
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
