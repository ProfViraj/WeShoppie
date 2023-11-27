package com.example.weshoppie.CustomerDashboard.CustAddedSeller.SameNumDiffSellers;

public class RegisteredShopModel {
    String Shop_Name, Shop_Type, DocumentID, Owner_Name, Owner_Phone;
    //Constructor *******************************************************************************************
    public RegisteredShopModel() {
    }
    public RegisteredShopModel(String shop_Name, String shop_Type, String owner_Name, String owner_Phone) {
        Shop_Name = shop_Name;
        Shop_Type = shop_Type;
        Owner_Name = owner_Name;
        Owner_Phone = owner_Phone;
    }
    //Getter Setter Methods ******************************************************************************
    public String getShop_Name() {
        return Shop_Name;
    }
    public void setShop_Name(String shop_Name) {
        Shop_Name = shop_Name;
    }
    public String getShop_Type() {
        return Shop_Type;
    }
    public void setShop_Type(String shop_Type) {
        Shop_Type = shop_Type;
    }
    public String getDocumentID() {
        return DocumentID;
    }
    public void setDocumentID(String documentID) {
        DocumentID = documentID;
    }
    public String getOwner_Name() {
        return Owner_Name;
    }
    public void setOwner_Name(String owner_Name) {
        Owner_Name = owner_Name;
    }
    public String getOwner_Phone() {
        return Owner_Phone;
    }
    public void setOwner_Phone(String owner_Phone) {
        Owner_Phone = owner_Phone;
    }
}
