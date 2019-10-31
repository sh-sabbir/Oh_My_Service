package com.codeian.ohmyservice.Model;

public class OrderModal {
    public String orderID,time,status,rating,customerID,sellerID;
    public NewService orderService;
    public User uCustomer,uServiceProvider;

    public OrderModal() {
    }

    public OrderModal(String customerID, String sellerID, String orderID, String time, String status, String rating, NewService orderService, User uCustomer, User uServiceProvider) {
        this.customerID = customerID;
        this.sellerID = sellerID;
        this.orderID = orderID;
        this.time = time;
        this.status = status;
        this.rating = rating;
        this.orderService = orderService;
        this.uCustomer = uCustomer;
        this.uServiceProvider = uServiceProvider;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public NewService getOrderService() {
        return orderService;
    }

    public void setOrderService(NewService orderService) {
        this.orderService = orderService;
    }

    public User getuCustomer() {
        return uCustomer;
    }

    public void setuCustomer(User uCustomer) {
        this.uCustomer = uCustomer;
    }

    public User getuServiceProvider() {
        return uServiceProvider;
    }

    public void setuServiceProvider(User uServiceProvider) {
        this.uServiceProvider = uServiceProvider;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public String getSellerID() {
        return sellerID;
    }

    public void setSellerID(String sellerID) {
        this.sellerID = sellerID;
    }
}
