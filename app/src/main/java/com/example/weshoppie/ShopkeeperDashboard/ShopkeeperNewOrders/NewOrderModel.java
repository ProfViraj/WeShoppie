package com.example.weshoppie.ShopkeeperDashboard.ShopkeeperNewOrders;

public class NewOrderModel {
    String Customer_Name, Customer_Number, Status, Customer_ID, Shopkeeper_ID, documentID;

    public NewOrderModel() {
    }

    public NewOrderModel(String customer_Name, String customer_Number, String status, String customer_ID, String shopkeeper_ID) {
        Customer_Name = customer_Name;
        Customer_Number = customer_Number;
        Status = status;
        Customer_ID = customer_ID;
        Shopkeeper_ID = shopkeeper_ID;
    }

    public String getCustomer_Name() {
        return Customer_Name;
    }

    public void setCustomer_Name(String customer_Name) {
        Customer_Name = customer_Name;
    }

    public String getCustomer_Number() {
        return Customer_Number;
    }

    public void setCustomer_Number(String customer_Number) {
        Customer_Number = customer_Number;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getCustomer_ID() {
        return Customer_ID;
    }

    public void setCustomer_ID(String customer_ID) {
        Customer_ID = customer_ID;
    }

    public String getShopkeeper_ID() {
        return Shopkeeper_ID;
    }

    public void setShopkeeper_ID(String shopkeeper_ID) {
        Shopkeeper_ID = shopkeeper_ID;
    }

    public String getDocumentID() {
        return documentID;
    }

    public void setDocumentID(String documentID) {
        this.documentID = documentID;
    }
}
