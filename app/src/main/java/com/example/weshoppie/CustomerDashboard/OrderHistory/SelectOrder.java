package com.example.weshoppie.CustomerDashboard.OrderHistory;

import com.example.weshoppie.ShopkeeperDashboard.ShopkeeperCartOrders.ShopkeeperOrderModel;

public interface SelectOrder {
    void onItemSelected(OrderHistoryModel orderHistoryModel);
    void onOrderSelected(ShopkeeperOrderModel shopkeeperOrderModel);
}
