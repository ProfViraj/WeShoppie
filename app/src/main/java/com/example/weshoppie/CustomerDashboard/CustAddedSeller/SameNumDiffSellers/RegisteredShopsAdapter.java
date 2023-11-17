package com.example.weshoppie.CustomerDashboard.CustAddedSeller.SameNumDiffSellers;

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

import java.util.ArrayList;

public class RegisteredShopsAdapter extends RecyclerView.Adapter<RegisteredShopsAdapter.ViewHolder> {
    Context context;
    ArrayList<RegisteredShopModel> registeredShopModelArrayList;
    SelectRegisterShop selectRegisterShop;

    public void setFilteredList(ArrayList<RegisteredShopModel> filteredList){
        this.registeredShopModelArrayList = filteredList;
        notifyDataSetChanged();
    }

    public RegisteredShopsAdapter(Context context, ArrayList<RegisteredShopModel> registeredShopModelArrayList, SelectRegisterShop selectRegisterShop) {
        this.context = context;
        this.registeredShopModelArrayList = registeredShopModelArrayList;
        this.selectRegisterShop = selectRegisterShop;
    }

    @NonNull
    @Override
    public RegisteredShopsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.registered_shops_row, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RegisteredShopsAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        RegisteredShopModel registeredShopModel = registeredShopModelArrayList.get(position);
        holder.ShopName.setText(registeredShopModel.Shop_Name);
        holder.ShopType.setText(registeredShopModel.Shop_Type);
        holder.RegisteredShopCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectRegisterShop.onItemClicked(registeredShopModelArrayList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return registeredShopModelArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView RegisteredShopCard;
        TextView ShopName, ShopType;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ShopName = itemView.findViewById(R.id.ShopName);
            ShopType = itemView.findViewById(R.id.ShopType);
            RegisteredShopCard = itemView.findViewById(R.id.RegisteredShopCard);
        }
    }
}
