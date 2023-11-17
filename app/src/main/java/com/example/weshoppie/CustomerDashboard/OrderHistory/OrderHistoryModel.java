package com.example.weshoppie.CustomerDashboard.OrderHistory;

public class OrderHistoryModel {
    String documentID, Status, Time, Shopkeeper_ID, Shop_Name;

    public String getShop_Name() {
        return Shop_Name;
    }

    public void setShop_Name(String shop_Name) {
        Shop_Name = shop_Name;
    }

    public OrderHistoryModel() {
    }

    public OrderHistoryModel(String status, String time) {
        Status = status;
        Time = time;
    }

    public String getDocumentID() {
        return documentID;
    }

    public void setDocumentID(String documentID) {
        this.documentID = documentID;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getShopkeeper_ID() {
        return Shopkeeper_ID;
    }

    public void setShopkeeper_ID(String shopkeeper_ID) {
        Shopkeeper_ID = shopkeeper_ID;
    }
}
