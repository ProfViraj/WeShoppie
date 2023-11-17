package com.example.weshoppie.ShopkeeperDashboard.ShopkeeperCartOrders.DeliveredOrders;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weshoppie.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class DeliveredOrdersAdapter extends RecyclerView.Adapter<DeliveredOrdersAdapter.ViewHolder> {
    Context context;
    ArrayList<DeliveredOrdersModel> deliveredOrdersModelArrayList;
    SeeDeliveredOrders seeDeliveredOrders;
    FirebaseFirestore db;
    public void setFilteredList (ArrayList<DeliveredOrdersModel> filteredList){
        this.deliveredOrdersModelArrayList = filteredList;
        notifyDataSetChanged();
    }
    public DeliveredOrdersAdapter(Context context, ArrayList<DeliveredOrdersModel> deliveredOrdersModelArrayList, SeeDeliveredOrders seeDeliveredOrders) {
        this.context = context;
        this.deliveredOrdersModelArrayList = deliveredOrdersModelArrayList;
        this.seeDeliveredOrders = seeDeliveredOrders;
        db = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public DeliveredOrdersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.order_history_row, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DeliveredOrdersAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        DeliveredOrdersModel deliveredOrdersModel = deliveredOrdersModelArrayList.get(position);
        holder.Status.setText(deliveredOrdersModel.Status);
        holder.BillNo.setText(deliveredOrdersModel.getDocumentID());
        holder.Date.setText(deliveredOrdersModel.Time);

        db.collection("Customer").document(deliveredOrdersModel.Customer_ID).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            DocumentSnapshot documentSnapshot = task.getResult();
                            holder.CustomerName.setText(documentSnapshot.get("Name").toString());
                            deliveredOrdersModel.setCust_Name(documentSnapshot.get("Name").toString());
                        } else {
                            Toast.makeText(context, "Name not accessible", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        holder.SelectOrderCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seeDeliveredOrders.onOrderSelected(deliveredOrdersModelArrayList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return deliveredOrdersModelArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView CustomerName, BillNo, Date, Status;
        CardView SelectOrderCard;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            CustomerName = itemView.findViewById(R.id.Shop_Name);
            BillNo = itemView.findViewById(R.id.Order_id);
            Date = itemView.findViewById(R.id.Order_date);
            Status = itemView.findViewById(R.id.Order_status);
            SelectOrderCard = itemView.findViewById(R.id.select_order);
        }
    }
}
