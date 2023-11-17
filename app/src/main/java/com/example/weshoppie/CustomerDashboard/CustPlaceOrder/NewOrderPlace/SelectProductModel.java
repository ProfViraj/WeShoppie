package com.example.weshoppie.CustomerDashboard.CustPlaceOrder.NewOrderPlace;

public class SelectProductModel {
    String Product_Name, Product_Price, Product_Price_per, Brand, documentID, countstr, coststr;
    int count, cost;

    public String getCountstr() {
        return String.valueOf(count);
    }

    public void setCountstr(int count) {
        this.countstr = String.valueOf(count);
    }

    public void setCoststr(int cost) {
        this.coststr = String.valueOf(cost);
    }

    public String getCoststr(){
        int price = intPrice();
        cost = count*price;
        return String.valueOf(cost);
    }

    public int intPrice(){
        String[] SplitPrice = Product_Price.split("\\s");
        return Integer.parseInt(SplitPrice[0]);
    }
    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
    public int getCost() {
        int price = intPrice();
        cost = count*price;
        return cost;
    }

    public SelectProductModel() {
    }

    public SelectProductModel(String product_Name, String product_Price, String product_Price_per, String brand) {
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
