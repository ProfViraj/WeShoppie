package com.example.weshoppie.ShopkeeperDashboard.ShopkeeperAddedCust;

public class CustShow {
    String Name;
    public String getName() {
        return Name;
    }
    public void setName(String name) {
        Name = name;
    }

    String documentID;
    public String getDocumentID() {
        return documentID;
    }
    public void setDocumentID(String documentID) {
        this.documentID = documentID;
    }

    String Mobile_Number;
    public String getMobile_Number() {
        return Mobile_Number;
    }
    public void setMobile_Number(String mobile_Number) {
        Mobile_Number = mobile_Number;
    }
    //Constructor ***************************************************************************************
    public CustShow(){}
    public CustShow(String Name, String Mobile_Number) {
        this.Name = Name;
        this.Mobile_Number = Mobile_Number;
    }


}
