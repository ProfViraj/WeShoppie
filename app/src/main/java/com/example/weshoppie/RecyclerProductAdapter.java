package com.example.weshoppie;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.weshoppie.R;

import java.util.ArrayList;

public class RecyclerProductAdapter extends RecyclerView.Adapter<RecyclerProductAdapter.ViewHolder> {
    Context context;
    ArrayList<ProductModel> arrProducts;
    public RecyclerProductAdapter(Context context, ArrayList<ProductModel> arrProducts){
        this.context = context;
        this.arrProducts = arrProducts;
    }

    @NonNull
    @Override
    public RecyclerProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.product_row,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerProductAdapter.ViewHolder holder, int position) {
        holder.Pname.setText(arrProducts.get(position).pname);
        holder.Pprice.setText(arrProducts.get(position).pprice);
        holder.Per.setText(arrProducts.get(position).per);
        holder.Brand.setText(arrProducts.get(position).brand);
    }

    @Override
    public int getItemCount() {
        return arrProducts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView Pname, Pprice, Per, Brand;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Pname = itemView.findViewById(R.id.txtName);
            Pprice = itemView.findViewById(R.id.txtPrice);
            Per = itemView.findViewById(R.id.txtPricePer);
            Brand = itemView.findViewById(R.id.txtBrand);
        }
    }
}
