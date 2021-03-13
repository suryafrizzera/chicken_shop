package com.example.chickenshop.model;

public class order {

    private String itemname, pid;
    private int chicken_image;
    private String date_added,time_added;
    private String status;
    private float qty,price,total_price;

    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public int getChicken_image() {
        return chicken_image;
    }

    public void setChicken_image(int chicken_image) {
        this.chicken_image = chicken_image;
    }

    public String getDate_added() {
        return date_added;
    }

    public void setDate_added(String date_added) {
        this.date_added = date_added;
    }

    public String getTime_added() {
        return time_added;
    }

    public void setTime_added(String time_added) {
        this.time_added = time_added;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public float getQty() {
        return qty;
    }

    public void setQty(float qty) {
        this.qty = qty;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getTotal_price() {
        return total_price;
    }

    public void setTotal_price(float total_price) {
        this.total_price = total_price;
    }
}
