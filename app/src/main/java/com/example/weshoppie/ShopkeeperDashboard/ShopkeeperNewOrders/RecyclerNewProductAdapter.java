package com.example.weshoppie.ShopkeeperDashboard.ShopkeeperNewOrders;

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
import com.example.weshoppie.ShopkeeperDashboard.ShopkeeperAddedProducts.RecyclerProductAdapter;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class RecyclerNewProductAdapter extends RecyclerView.Adapter<RecyclerNewProductAdapter.ViewHolder> {
    Context context;
    ArrayList<NewOrderModel> arrNewOrderModel;
    SelectNewOrder selectNewOrderListener;
    FirebaseFirestore db;

    public void setFilteredList (ArrayList<NewOrderModel> filteredList){
        this.arrNewOrderModel = filteredList;
        notifyDataSetChanged();
    }

    public RecyclerNewProductAdapter(Context context, ArrayList<NewOrderModel> arrNewOrderModel, SelectNewOrder selectNewOrderListener) {
        this.context = context;
        this.arrNewOrderModel = arrNewOrderModel;
        this.selectNewOrderListener = selectNewOrderListener;
        db = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public RecyclerNewProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.new_orders_row, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerNewProductAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        NewOrderModel newOrderModel = arrNewOrderModel.get(position);
        holder.Customer_Name.setText(newOrderModel.Customer_Name);
        holder.Number.setText(newOrderModel.Customer_Number);
        holder.OrderID.setText(newOrderModel.getDocumentID());

        holder.NewOrderCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectNewOrderListener.onItemClicked(arrNewOrderModel.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrNewOrderModel.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView Customer_Name, Number, OrderID;
        CardView NewOrderCard;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Customer_Name = itemView.findViewById(R.id.Customer_Name);
            Number = itemView.findViewById(R.id.Customer_Number);
            OrderID = itemView.findViewById(R.id.OrderID);
            NewOrderCard = itemView.findViewById(R.id.NewOrderCard);
        }
    }
}
