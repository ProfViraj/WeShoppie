package com.example.weshoppie.ShopkeeperDashboard.ShopkeeperAddedCust;

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

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.MyViewHolder> {
    Context context;
    ArrayList<CustShow> custShowArrayList;
    SelectCustomer selectCustomer;
    public void setFilteredList(ArrayList<CustShow> filteredList){
        this.custShowArrayList = filteredList;
        notifyDataSetChanged();
    }
    //Constructor ***************************************************************************************************
    public CustomerAdapter(Context context, ArrayList<CustShow> custShowArrayList, SelectCustomer selectCustomer) {
        this.context = context;
        this.custShowArrayList = custShowArrayList;
        this.selectCustomer = selectCustomer;
    }
    //Creating the view for view holder *****************************************************************************
    @NonNull
    @Override
    public CustomerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.customer_list_row, parent, false);
        return new MyViewHolder(v);
    }
    //On binding the data to the view **************************************************************************************
    @Override
    public void onBindViewHolder(@NonNull CustomerAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

        CustShow Cust = custShowArrayList.get(position);
        holder.Name.setText(Cust.Name);
        holder.Mobile_Number.setText(Cust.Mobile_Number);
        //on clicking the customer ***************************************************************************************
        holder.CustCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectCustomer.onItemClicked(custShowArrayList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return custShowArrayList.size();
    }
    //Getting the IDs ************************************************************************************************
    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView Name, Mobile_Number;
        CardView CustCard;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            Name = itemView.findViewById(R.id.Cust_Name);
            Mobile_Number = itemView.findViewById(R.id.Cust_Number);
            CustCard = itemView.findViewById(R.id.CustCard);
        }
    }
}
