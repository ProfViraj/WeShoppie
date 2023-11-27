package com.example.weshoppie.CustomerDashboard.CustPlaceOrder.EditOrderPlaced;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weshoppie.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Objects;

public class EditOrderAdapter extends RecyclerView.Adapter<EditOrderAdapter.ViewHolder> {
    Context context;
    ArrayList<EditOrderModel> editOrderModelArrayList;
    SelectEditOrder selectEditOrder;
    FirebaseFirestore db;
    //Constructor *************************************************************************************************************
    public EditOrderAdapter(Context context, ArrayList<EditOrderModel> editOrderModelArrayList, SelectEditOrder selectEditOrder) {
        this.context = context;
        this.editOrderModelArrayList = editOrderModelArrayList;
        this.selectEditOrder = selectEditOrder;
        db = FirebaseFirestore.getInstance();
    }
    //Creating the view for viewholder *********************************************************************************
    @NonNull
    @Override
    public EditOrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.edit_product_in_order_row, parent, false);
        return new ViewHolder(v);
    }
    //Binding the data to the viewHolder ******************************************************************************************8
    @Override
    public void onBindViewHolder(@NonNull EditOrderAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        EditOrderModel editOrderModel = editOrderModelArrayList.get(position);
        editOrderModel.setCount();
        final int[] countt = {editOrderModel.getCount()};
        holder.ProductName.setText(editOrderModel.Product_Name);
        holder.ProductPrice.setText(editOrderModel.Product_Price);
        holder.ProductPricePer.setText(editOrderModel.Product_Price_Per);
        holder.Brand.setText(editOrderModel.Brand);
        holder.Count.setText(editOrderModel.Count);
        //Increase the Count ***************************************************************************************************
        holder.Increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countt[0]++;
                holder.Count.setText(String.valueOf(countt[0]));
                editOrderModel.setCount(countt[0]);

                if (editOrderModel.Count.equals(String.valueOf(countt[0]))){
                    holder.Confirm.setVisibility(View.GONE);
                } else {
                    holder.Confirm.setVisibility(View.VISIBLE);
                }
            }
        });
        //Decrease the Count ******************************************************************************************************8
        holder.Decrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (countt[0]==1){
                    selectEditOrder.onDeleteAlert(editOrderModelArrayList.get(position));
                } else {
                    countt[0]--;
                    holder.Count.setText(String.valueOf(countt[0]));
                    editOrderModel.setCount(countt[0]);

                    if (editOrderModel.Count.equals(String.valueOf(countt[0]))){
                        holder.Confirm.setVisibility(View.GONE);
                    } else {
                        holder.Confirm.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        //Delete the Product **********************************************************************************
        holder.Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectEditOrder.onDeleteAlert(editOrderModelArrayList.get(position));
            }
        });
        //Confirm the edit order *****************************************************************************
        holder.Confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectEditOrder.onUpdateAlert(editOrderModelArrayList.get(position));
            }
        });

    }

    @Override
    public int getItemCount() {
        return editOrderModelArrayList.size();
    }
    //Getting the IDs *********************************************************************************************
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView ProductName, ProductPrice, ProductPricePer, Brand, Count;
        ImageView Increase, Decrease, Delete, Confirm;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ProductName = itemView.findViewById(R.id.product_name_edit);
            ProductPrice = itemView.findViewById(R.id.price_edit);
            ProductPricePer = itemView.findViewById(R.id.price_per_edit);
            Brand = itemView.findViewById(R.id.brand_edit);
            Count = itemView.findViewById(R.id.count_edit);

            Increase = itemView.findViewById(R.id.increase_edit);
            Decrease = itemView.findViewById(R.id.decrease_edit);
            Delete = itemView.findViewById(R.id.delete_edit);
            Confirm = itemView.findViewById(R.id.confirm_edit);
        }
    }
}
