package com.dnext.virido.model;

public class ProductModel {
    String uid, name, price,prepaid, quantity,rating,ratingCount,description,fresh, imageurl,imageurl2, category, type, discountprice, discountpercent, isdiscounted;


    public ProductModel(String uid, String name, String price,String prepaid, String quantity,String rating,String ratingCount, String imageurl,String imageurl2, String fresh, String description, String category, String type, String discountprice, String discountpercent, String isdiscounted){
        this.uid = uid;
        this.name = name;
        this.price = price;
        this.prepaid = prepaid;
        this.quantity  = quantity;
        this.rating = rating;
        this.rating = ratingCount;
        this.fresh = fresh;
        this.imageurl = imageurl;
        this.imageurl2 = imageurl2;
        this.description = description;
        this.category = category;
        this.type = type;
        this.discountprice = discountprice;
        this.discountpercent = discountpercent;
        this.isdiscounted = isdiscounted;


    }

    public String getRatingCount() {
        return ratingCount;
    }

    public void setRatingCount(String ratingCount) {
        this.ratingCount = ratingCount;
    }

    public String getImageurl2() {
        return imageurl2;
    }

    public void setImageurl2(String imageurl2) {
        this.imageurl2 = imageurl2;
    }

    public String getPrepaid() {
        return prepaid;
    }

    public void setPrepaid(String prepaid) {
        this.prepaid = prepaid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getDiscountprice() {
        return discountprice;
    }

    public void setDiscountprice(String discountprice) {
        this.discountprice = discountprice;
    }

    public String getDiscountpercent() {
        return discountpercent;
    }

    public void setDiscountpercent(String discountpercent) {
        this.discountpercent = discountpercent;
    }

    public String getIsdiscounted() {
        return isdiscounted;
    }

    public void setIsdiscounted(String isdiscounted) {
        this.isdiscounted = isdiscounted;
    }

    public String getFresh() {
        return fresh;
    }

    public void setFresh(String fresh) {
        this.fresh = fresh;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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


    public ProductModel(){}
}
