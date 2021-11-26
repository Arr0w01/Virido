package com.dnext.virido.model;

public class CartModel {
    String uid, name, price,discountprice,isdiscounted,discountpercent, totalprice, rating,ratingCount,description,type, imageurl, itemcount;

    public CartModel(String uid, String name, String price,String discountprice,String isdiscounted,String discountpercent,String totalprice, String itemcount, String rating,String ratingCount, String imageurl, String description, String type){
        this.uid = uid;
        this.name = name;
        this.price = price;
        this.discountprice = discountprice;
        this.isdiscounted = isdiscounted;
        this.totalprice = totalprice;
        this.itemcount = itemcount;
        this.rating = rating;
        this.ratingCount = ratingCount;
        this.imageurl = imageurl;
        this.description = description;
        this.type = type;
        this.discountpercent = discountpercent;
    }

    public String getRatingCount() {
        return ratingCount;
    }

    public void setRatingCount(String ratingCount) {
        this.ratingCount = ratingCount;
    }

    public String getDiscountpercent() {
        return discountpercent;
    }

    public void setDiscountpercent(String discountpercent) {
        this.discountpercent = discountpercent;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIsdiscounted() {
        return isdiscounted;
    }

    public void setIsdiscounted(String isdiscounted) {
        this.isdiscounted = isdiscounted;
    }

    public String getDiscountprice() {
        return discountprice;
    }

    public void setDiscountprice(String discountprice) {
        this.discountprice = discountprice;
    }

    public String getTotalprice() {
        return totalprice;
    }

    public void setTotalprice(String totalprice) {
        this.totalprice = totalprice;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getItemcount() {
        return itemcount;
    }

    public void setItemcount(String itemcount) {
        this.itemcount = itemcount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public CartModel(){}
}
