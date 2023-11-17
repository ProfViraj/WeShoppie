package com.example.weshoppie.ShopkeeperDashboard.ShopkeeperCartOrders.UndeliveredOrders;

public interface SeeShopOrders {
    void onOrderSelected(ShopkeeperOrderModel shopkeeperOrderModel);
    void onDeliveryDone(ShopkeeperOrderModel shopkeeperOrderModel);
}
