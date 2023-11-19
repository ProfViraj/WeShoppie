package com.example.weshoppie.CustomerDashboard.CustPlaceOrder.EditOrderPlaced.AddNewProduct;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weshoppie.R;

import java.util.ArrayList;

public class ProductAddInAdapter extends RecyclerView.Adapter<ProductAddInAdapter.ViewHolder> {
    Context context;
    ArrayList<ProductAddInModel> productAddInModelArrayList;
    SelectProductAddIN selectProductAddIN;

    public ProductAddInAdapter(Context context, ArrayList<ProductAddInModel> productAddInModelArrayList, SelectProductAddIN selectProductAddIN) {
        this.context = context;
        this.productAddInModelArrayList = productAddInModelArrayList;
        this.selectProductAddIN = selectProductAddIN;
    }

    @NonNull
    @Override
    public ProductAddInAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.add_product_row,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAddInAdapter.ViewHolder holder, int position) {
        ProductAddInModel productAddInModel = productAddInModelArrayList.get(position);
        int[] count = {0};

        productAddInModel.setCount(String.valueOf(count[0]));
        productAddInModel.setCountINT(count[0]);

        holder.ProductName.setText(productAddInModel.Product_Name);
        holder.Price.setText(productAddInModel.Product_Price);
        holder.Per.setText(productAddInModel.Product_Price_per);
        holder.Brand.setText(productAddInModel.Brand);
        holder.Count.setText(productAddInModel.getCount());

        holder.Increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count[0]++;
                productAddInModel.setCountINT(count[0]);
                productAddInModel.setCount(String.valueOf(count[0]));
                holder.Count.setText(productAddInModel.getCount());
            }
        });
        holder.Decrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (productAddInModel.Count.equals("0")){
                } else {
                    count[0]--;
                    productAddInModel.setCountINT(count[0]);
                    productAddInModel.setCount(String.valueOf(count[0]));
                    holder.Count.setText(productAddInModel.getCount());
                }
            }
        });
        holder.AddNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (productAddInModel.Count.equals("0")){
                    selectProductAddIN.onCountZero(productAddInModelArrayList.get(position));
                } else {
                    selectProductAddIN.onProductAdd(productAddInModelArrayList.get(position));
                    holder.AddNew.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return productAddInModelArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView ProductName, Price, Per, Brand, Count;
        ImageView Increase, Decrease, AddNew;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ProductName = itemView.findViewById(R.id.ProductNameAdd);
            Price = itemView.findViewById(R.id.PriceAdd);
            Per = itemView.findViewById(R.id.PricePerAdd);
            Brand = itemView.findViewById(R.id.BrandAdd);
            Count = itemView.findViewById(R.id.countAdd);

            Increase = itemView.findViewById(R.id.increaseAdd);
            Decrease = itemView.findViewById(R.id.decreaseAdd);
            AddNew = itemView.findViewById(R.id.add_in_Order);
        }
    }
}
