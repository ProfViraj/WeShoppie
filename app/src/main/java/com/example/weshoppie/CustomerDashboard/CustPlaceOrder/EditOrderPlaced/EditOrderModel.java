package com.example.weshoppie.CustomerDashboard.CustPlaceOrder.EditOrderPlaced;

public class EditOrderModel {
    String Product_Name, Product_Price, Product_Price_Per, Brand, documentID, Count, Total_Cost, Product_Status;
    int count, cost;
    //Constructors **********************************************************************************************
    public EditOrderModel() {
    }
    public EditOrderModel(String product_Name, String product_Price, String product_Price_per, String brand, String count, String total_Cost, String product_Status) {
        Product_Name = product_Name;
        Product_Price = product_Price;
        Product_Price_Per = product_Price_per;
        Brand = brand;
        Count = count;
        Total_Cost = total_Cost;
        Product_Status = product_Status;
    }
    //Getter and Setter ******************************************************************************************
    public void setCount(int count) {
        this.count = count;
    }
    public void setCost(int cost) {
        this.cost = cost;
    }
    public String getProduct_Status() {
        return Product_Status;
    }
    public void setProduct_Status(String product_Status) {
        Product_Status = product_Status;
    }
    public int getCount() {
        return count;
    }
    public void setCount() {
        this.count=Integer.parseInt(this.Count);
    }
    public void setCountStr( int count) {this.Count = String.valueOf(count);}
    public void setTotal_Cost(){
        String[] SplitPrice = Product_Price.split("\\s");
        int value = count * Integer.parseInt(SplitPrice[0]);
        this.Total_Cost = String.valueOf(value);
    }
    public int getCost() {
        return cost;
    }
    public void setCost() {
        this.cost = Integer.parseInt(this.Total_Cost);
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
        return Product_Price_Per;
    }
    public void setProduct_Price_per(String product_Price_per) {
        Product_Price_Per = product_Price_per;
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
