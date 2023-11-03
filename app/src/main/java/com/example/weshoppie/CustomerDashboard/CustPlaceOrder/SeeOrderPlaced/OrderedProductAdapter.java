package com.example.weshoppie.CustomerDashboard.CustPlaceOrder.SeeOrderPlaced;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weshoppie.CustomerDashboard.CustPlaceOrder.SelectProductAdapter;
import com.example.weshoppie.R;

import java.util.ArrayList;

public class OrderedProductAdapter extends RecyclerView.Adapter<OrderedProductAdapter.ViewHolder> {
    Context context;
    ArrayList<OrderedProductModel> orderedProductModelArrayList;

    public OrderedProductAdapter(Context context, ArrayList<OrderedProductModel> orderedProductModelArrayList) {
        this.context = context;
        this.orderedProductModelArrayList = orderedProductModelArrayList;
    }

    @NonNull
    @Override
    public OrderedProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.ordered_product_row, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderedProductAdapter.ViewHolder holder, int position) {
        OrderedProductModel orderedProductModel = orderedProductModelArrayList.get(position);
        holder.ProductName.setText(orderedProductModel.Product_Name);
        holder.ProductPrice.setText(orderedProductModel.Product_Price);
        holder.ProductPricePer.setText(orderedProductModel.Product_Price_Per);
        holder.Brand.setText(orderedProductModel.Brand);
        holder.Count.setText(orderedProductModel.Count);
        holder.TotalCost.setText(orderedProductModel.Total_Cost);
    }

    @Override
    public int getItemCount() {
        return orderedProductModelArrayList.size();
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
