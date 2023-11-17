package com.example.weshoppie.CustomerDashboard.OrderHistory.SeeOrderDetails;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weshoppie.R;

import java.util.ArrayList;

public class OrderSeeAdapter extends RecyclerView.Adapter<OrderSeeAdapter.ViewHolder> {
    Context context;
    ArrayList<OrderSeeModel> orderSeeModelArrayList;

    public OrderSeeAdapter(Context context, ArrayList<OrderSeeModel> orderSeeModelArrayList) {
        this.context = context;
        this.orderSeeModelArrayList = orderSeeModelArrayList;
    }

    @NonNull
    @Override
    public OrderSeeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.ordered_product_row, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderSeeAdapter.ViewHolder holder, int position) {
        OrderSeeModel orderSeeModel = orderSeeModelArrayList.get(position);
        holder.ProductName.setText(orderSeeModel.Product_Name);
        holder.ProductPrice.setText(orderSeeModel.Product_Price);
        holder.ProductPricePer.setText(orderSeeModel.Product_Price_Per);
        holder.Brand.setText(orderSeeModel.Brand);
        holder.Count.setText(orderSeeModel.Count);
        holder.TotalCost.setText(orderSeeModel.Total_Cost);
    }

    @Override
    public int getItemCount() {
        return orderSeeModelArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView ProductName, ProductPrice, ProductPricePer, Brand, Count, TotalCost;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ProductName = itemView.findViewById(R.id.product_name_invoice);
            ProductPrice = itemView.findViewById(R.id.price_invoice);
            ProductPricePer = itemView.findViewById(R.id.per_invoice);
            Brand = itemView.findViewById(R.id.brandinvoice);
            Count = itemView.findViewById(R.id.count_invoice);
            TotalCost = itemView.findViewById(R.id.cost_invoice);
        }
    }
}
