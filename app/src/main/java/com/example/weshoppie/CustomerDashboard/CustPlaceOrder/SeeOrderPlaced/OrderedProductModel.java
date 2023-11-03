package com.example.weshoppie.CustomerDashboard.CustPlaceOrder.SeeOrderPlaced;

public class OrderedProductModel {
    String Product_Name, Product_Price, Product_Price_Per, Brand, Count, Total_Cost;

    public OrderedProductModel() {
    }

    public OrderedProductModel(String product_Name, String product_Price, String product_Price_Per, String brand, String count, String total_Cost) {
        Product_Name = product_Name;
        Product_Price = product_Price;
        Product_Price_Per = product_Price_Per;
        Brand = brand;
        Count = count;
        Total_Cost = total_Cost;
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

    public String getTotal_Cost() {
        return Total_Cost;
    }

    public void setTotal_Cost(String total_Cost) {
        Total_Cost = total_Cost;
    }
}
