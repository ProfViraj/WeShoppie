package com.example.weshoppie.ShopkeeperDashboard.ShopkeeperNewOrders.ProductStatus;

import java.util.Stack;

public class UnpackedProductsModel {
    String Brand, Product_Name, Product_Price, Product_Price_Per, Product_Status, documentID, Count, Total_Cost;
    //Constructor ************************************************************************************************
    public UnpackedProductsModel() {
    }

    public UnpackedProductsModel(String brand, String count, String product_Name, String product_Price, String product_Price_Per, String product_Status, String total_Cost) {
        Brand = brand;
        Count = count;
        Product_Name = product_Name;
        Product_Price = product_Price;
        Product_Price_Per = product_Price_Per;
        Product_Status = product_Status;
        Total_Cost = total_Cost;
    }
    //Getter and Setter Methods **************************************************************************************************
    public String getBrand() {
        return Brand;
    }

    public void setBrand(String brand) {
        Brand = brand;
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

    public String getProduct_Price_Per() {
        return Product_Price_Per;
    }

    public void setProduct_Price_Per(String product_Price_Per) {
        Product_Price_Per = product_Price_Per;
    }

    public String getProduct_Status() {
        return Product_Status;
    }

    public void setProduct_Status(String product_Status) {
        Product_Status = product_Status;
    }

    public String getTotal_Cost() {
        return Total_Cost;
    }

    public void setTotal_Cost(String total_Cost) {
        Total_Cost = total_Cost;
    }

    public String getDocumentID() {
        return documentID;
    }

    public void setDocumentID(String documentID) {
        this.documentID = documentID;
    }
}
