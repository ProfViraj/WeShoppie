package com.example.weshoppie.CustomerDashboard.CustPlaceOrder.EditOrderPlaced.AddNewProduct;

public class ProductAddInModel {
    String Product_Name, Product_Price, Product_Price_per, Brand, documentID, Count, Total_Cost;
    int countINT, costINT;
    //Constructor ******************************************************************************************************
    public ProductAddInModel() {
    }
    public ProductAddInModel(String product_Name, String product_Price, String product_Price_per, String brand) {
        Product_Name = product_Name;
        Product_Price = product_Price;
        Product_Price_per = product_Price_per;
        Brand = brand;
    }
    //Getter and Setter Methods ***************************************************************************************
    public String getTotal_Cost() {
        return Total_Cost;
    }
    public void setTotal_Cost(String total_Cost) {
        Total_Cost = total_Cost;
    }
    public int getCostINT() {
        return costINT;
    }
    public void setCostINT(int costINT) {
        this.costINT = costINT;
    }
    public int getCountINT() {
        return countINT;
    }
    public void setCountINT(int countINT) {
        this.countINT = countINT;
    }
    public String getCount() {
        return Count;
    }
    public void setCount(String count) {
        Count = count;
    }
    public String getProduct_Name() {
        return Product_Name;
    }
    public void setProduct_Name(String product_Name) {
        Product_Name = product_Name;
    }
    public String getProduct_Price() {
        return Product_Price;
    }
    public void setProduct_Price(String product_Price) {
        Product_Price = product_Price;
    }
    public String getProduct_Price_per() {
        return Product_Price_per;
    }
    public void setProduct_Price_per(String product_Price_per) {
        Product_Price_per = product_Price_per;
    }
    public String getBrand() {
        return Brand;
    }
    public void setBrand(String brand) {
        Brand = brand;
    }
    public String getDocumentID() {
        return documentID;
    }
    public void setDocumentID(String documentID) {
        this.documentID = documentID;
    }
}
