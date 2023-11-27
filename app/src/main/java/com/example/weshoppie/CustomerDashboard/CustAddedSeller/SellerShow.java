package com.example.weshoppie.CustomerDashboard.CustAddedSeller;

public class SellerShow {
    String Name, Shop_Name, Shop_Type, Phone, documentID;
    //Constructor *******************************************************************************
    public SellerShow() {
    }
    public SellerShow(String name, String shop_Name, String shop_Type, String phone) {
        this.Name = name;
        this.Shop_Name = shop_Name;
        this.Shop_Type = shop_Type;
        this.Phone = phone;
    }
    //Getter Setter Methods ********************************************************************
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

    public String getShop_Name() {
        return Shop_Name;
    }
    public void setShop_Name(String shop_Name) {
        Shop_Name = shop_Name;
    }

    public String getPhone() {
        return Phone;
    }
    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getDocumentID() {
        return documentID;
    }
    public void setDocumentID(String documentID) {
        this.documentID = documentID;
    }
}
