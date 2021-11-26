package com.dnext.virido.model;

import java.util.ArrayList;

public class OrderModel {
    String orderId, customerName, customerNumber, customerPincode, customerAddress, customerId, productCount, deliveryCharge, customerLat, customerLang, deliverySlot, totalDiscount, totalAmount;
    ArrayList<CartModel> products;
    public OrderModel(String orderId, String customerName, String customerNumber, String customerPincode, String customerAddress, String customerId, String productCount, String deliveryCharge, String customerLat, String customerLang,
                      String deliverySlot, String totalDiscount, String totalAmount, ArrayList<CartModel> products)
    {
        this.orderId = orderId;
        this.customerName = customerName;
        this.customerNumber =customerNumber;
        this.customerPincode =customerPincode;
        this.customerAddress =customerAddress;
        this.customerId =customerId;
        this.productCount = productCount;
        this.deliveryCharge = deliveryCharge;
        this.deliverySlot = deliverySlot;
        this.customerLat =customerLat;
        this.customerLang =customerLang;
        this.totalAmount =totalAmount;
        this.totalDiscount = totalDiscount;
        this.products = products;

    }

    public ArrayList<CartModel> getProducts() {
        return products;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public void setProducts(ArrayList<CartModel> products) {
        this.products = products;
    }

    public String getProductCount() {
        return productCount;
    }

    public void setProductCount(String productCount) {
        this.productCount = productCount;
    }

    public String getDeliveryCharge() {
        return deliveryCharge;
    }

    public void setDeliveryCharge(String deliveryCharge) {
        this.deliveryCharge = deliveryCharge;
    }

    public String getCustomerLat() {
        return customerLat;
    }

    public void setCustomerLat(String customerLat) {
        this.customerLat = customerLat;
    }

    public String getCustomerLang() {
        return customerLang;
    }

    public void setCustomerLang(String customerLang) {
        this.customerLang = customerLang;
    }

    public String getDeliverySlot() {
        return deliverySlot;
    }

    public void setDeliverySlot(String deliverySlot) {
        this.deliverySlot = deliverySlot;
    }

    public String getTotalDiscount() {
        return totalDiscount;
    }

    public void setTotalDiscount(String totalDiscount) {
        this.totalDiscount = totalDiscount;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(String customerNumber) {
        this.customerNumber = customerNumber;
    }

    public String getCustomerPincode() {
        return customerPincode;
    }

    public void setCustomerPincode(String customerPincode) {
        this.customerPincode = customerPincode;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
    public OrderModel(){}
}
