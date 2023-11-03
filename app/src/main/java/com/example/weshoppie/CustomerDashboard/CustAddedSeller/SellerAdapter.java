package com.example.weshoppie.CustomerDashboard.CustAddedSeller;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weshoppie.R;
import com.example.weshoppie.ShopkeeperDashboard.ShopkeeperAddedCust.CustomerAdapter;

import java.util.ArrayList;

public class SellerAdapter extends RecyclerView.Adapter<SellerAdapter.MyViewHolder> {

    Context context;
    ArrayList<SellerShow> sellerShowArrayList;
    SelectSeller selectSeller;

    public SellerAdapter(Context context, ArrayList<SellerShow> sellerShowArrayList, SelectSeller selectSeller) {
        this.context = context;
        this.sellerShowArrayList = sellerShowArrayList;
        this.selectSeller = selectSeller;
    }

    @NonNull
    @Override
    public SellerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.seller_list_row, parent, false);
        return new SellerAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SellerAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        SellerShow Seller = sellerShowArrayList.get(position);
        holder.Seller_Name.setText("   "+Seller.Name+"   ");
        holder.Seller_Number.setText("   "+Seller.Phone+"   ");
        holder.Shop_Type.setText(" ( "+ Seller.Shop_Type + " ) ");
        holder.Shop_Name.setText("   "+Seller.Shop_Name+"   ");

        holder.sellerCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectSeller.onItemClicked(sellerShowArrayList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return sellerShowArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView Seller_Name, Seller_Number, Shop_Type, Shop_Name;
        CardView sellerCard;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            Seller_Name = itemView.findViewById(R.id.Seller_Name);
            Seller_Number = itemView.findViewById(R.id.Seller_Number);
            Shop_Type = itemView.findViewById(R.id.Shop_Type);
            Shop_Name = itemView.findViewById(R.id.Shop_Name);
            sellerCard = itemView.findViewById(R.id.SellerCard);
        }
    }
}
