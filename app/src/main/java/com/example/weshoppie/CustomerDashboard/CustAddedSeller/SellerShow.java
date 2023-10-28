package com.example.weshoppie.CustomerDashboard.CustAddedSeller;

public class SellerShow {
    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getShop_Type() {
        return Shop_Type;
    }

    public void setShop_Type(String shop_Type) {
        Shop_Type = shop_Type;
    }

    String Name;
    String Shop_Type;

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    String Phone;

    public String getDocumentID() {
        return documentID;
    }

    public void setDocumentID(String documentID) {
        this.documentID = documentID;
    }

    String documentID;

    public SellerShow(String name, String shop_Type, String phone) {
        this.Name = name;
        this.Shop_Type = shop_Type;
        this.Phone = phone;
    }

    public SellerShow() {
    }
}
