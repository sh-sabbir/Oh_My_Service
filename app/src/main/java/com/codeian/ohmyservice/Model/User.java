package com.codeian.ohmyservice.Model;

/**
 * Created by Belal on 4/15/2018.
 */

public class User {
    public String name, area, address, type, service, status, shopName;

    public User(){

    }

    public User(String name, String area, String address, String type, String status) {
        this.name = name;
        this.area = area;
        this.address = address;
        this.type = type;
        this.status = status;
    }

    public User(String name, String area, String shopName, String service, String type, String status) {
        this.name = name;
        this.area = area;
        this.shopName = shopName;
        this.service = service;
        this.type = type;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }
}
