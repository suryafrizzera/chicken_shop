package com.example.chickenshop.model;

public class category_item {
    private int category_image;
    private String category;

    public category_item(int category_image, String category) {
        this.category_image = category_image;
        this.category = category;
    }

    public category_item() {
    }

    public int getCategory_image() {
        return category_image;
    }

    public void setCategory_image(int category_image) {
        this.category_image = category_image;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
