package com.example.weshoppie.ShopkeeperDashboard.ShopkeeperAddedProducts;
public class ProductModel {
    String Product_Name;
    String Product_Price;
    String Product_Price_per;
    String Brand;



    String documentID;

    public ProductModel() {
    }

    public ProductModel(String product_Name, String product_Price, String product_Price_per, String brand) {
        Product_Name = product_Name;
        Product_Price = product_Price;
        Product_Price_per = product_Price_per;
        Brand = brand;
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
