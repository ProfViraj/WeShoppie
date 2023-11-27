package com.example.weshoppie.CustomerDashboard.OrderHistory;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.ViewHolder> {
    Context context;
    ArrayList<OrderHistoryModel> orderHistoryModelArrayList;
    SelectOrder selectOrder;
    FirebaseFirestore db;

    public void setFilteredList(ArrayList<OrderHistoryModel> filteredList){
        this.orderHistoryModelArrayList = filteredList;
        notifyDataSetChanged();
    }
    //Constructor ********************************************************************************************************************
    public OrderHistoryAdapter(Context context, ArrayList<OrderHistoryModel> orderHistoryModelArrayList, SelectOrder selectOrder) {
        this.context = context;
        this.orderHistoryModelArrayList = orderHistoryModelArrayList;
        this.selectOrder = selectOrder;
        db = FirebaseFirestore.getInstance();
    }
    //Creating the view for view holder ***********************************************************************************************
    @NonNull
    @Override
    public OrderHistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.order_history_row, parent, false);
        return new ViewHolder(v);
    }
    //On binding the data to the view
    @Override
    public void onBindViewHolder(@NonNull OrderHistoryAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        OrderHistoryModel orderHistoryModel = orderHistoryModelArrayList.get(position);
        holder.Status.setText(orderHistoryModel.Status);
        holder.BillNo.setText(orderHistoryModel.getDocumentID());
        holder.Date.setText(orderHistoryModel.Time);
        //Getting the shopname *********************************************************************************
        db.collection("Shopkeeper").document(orderHistoryModel.Shopkeeper_ID).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            DocumentSnapshot documentSnapshot = task.getResult();
                            holder.ShopName.setText(documentSnapshot.get("Shop_Name").toString());
                            orderHistoryModel.setShop_Name(documentSnapshot.get("Shop_Name").toString());
                        } else {
                            Toast.makeText(context, "Shop Name not accessible", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        //On selecting the order *************************************************************************
        holder.SelectOrderCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectOrder.onItemSelected(orderHistoryModelArrayList.get(position));
            }
        });

    }

    @Override
    public int getItemCount() {
        return orderHistoryModelArrayList.size();
    }
    //Getting the IDs *******************************************************************************
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView ShopName, BillNo, Date, Status;
        CardView SelectOrderCard;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ShopName = itemView.findViewById(R.id.Shop_Name);
            BillNo = itemView.findViewById(R.id.Order_id);
            Date = itemView.findViewById(R.id.Order_date);
            Status = itemView.findViewById(R.id.Order_status);
            SelectOrderCard = itemView.findViewById(R.id.select_order);
        }
    }
}
