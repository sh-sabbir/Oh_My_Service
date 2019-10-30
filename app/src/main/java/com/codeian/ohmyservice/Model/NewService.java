package com.codeian.ohmyservice.Model;

public class NewService {
    public String spID,spPhone,spArea,spService,spPrice,spDetails;

    public NewService(){

    }

    public NewService(String spService,String spPrice, String spDetails){
        this.spService = spService;
        this.spPrice = spPrice;
        this.spDetails = spDetails;
    }

    public NewService(String spID, String spPhone, String spArea, String spService, String spPrice, String spDetails) {
        this.spID = spID;
        this.spPhone = spPhone;
        this.spArea = spArea;
        this.spService = spService;
        this.spPrice = spPrice;
        this.spDetails = spDetails;
    }

    public String getSpID() {
        return spID;
    }

    public void setSpID(String spID) {
        this.spID = spID;
    }

    public String getSpPhone() {
        return spPhone;
    }

    public void setSpPhone(String spPhone) {
        this.spPhone = spPhone;
    }

    public String getSpArea() {
        return spArea;
    }

    public void setSpArea(String spArea) {
        this.spArea = spArea;
    }

    public String getSpService() {
        return spService;
    }

    public void setSpService(String spService) {
        this.spService = spService;
    }

    public String getSpPrice() {
        return spPrice;
    }

    public void setSpPrice(String spPrice) {
        this.spPrice = spPrice;
    }

    public String getSpDetails() {
        return spDetails;
    }

    public void setSpDetails(String spDetails) {
        this.spDetails = spDetails;
    }
}
