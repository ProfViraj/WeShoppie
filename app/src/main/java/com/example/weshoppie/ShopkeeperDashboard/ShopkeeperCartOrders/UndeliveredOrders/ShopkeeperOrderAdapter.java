package com.example.weshoppie.ShopkeeperDashboard.ShopkeeperCartOrders.UndeliveredOrders;

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

public class ShopkeeperOrderAdapter extends RecyclerView.Adapter<ShopkeeperOrderAdapter.ViewHolder> {
    Context context;
    ArrayList<ShopkeeperOrderModel> shopkeeperOrderModelArrayList;
    SeeShopOrders seeShopOrders;
    FirebaseFirestore db;

    public void setFilteredList (ArrayList<ShopkeeperOrderModel> filteredList){
        this.shopkeeperOrderModelArrayList = filteredList;
        notifyDataSetChanged();
    }

    public ShopkeeperOrderAdapter(Context context, ArrayList<ShopkeeperOrderModel> shopkeeperOrderModelArrayList, SeeShopOrders seeShopOrders) {
        this.context = context;
        this.shopkeeperOrderModelArrayList = shopkeeperOrderModelArrayList;
        this.seeShopOrders = seeShopOrders;
        db = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public ShopkeeperOrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.order_history_row, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ShopkeeperOrderAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ShopkeeperOrderModel shopkeeperOrderModel = shopkeeperOrderModelArrayList.get(position);
        holder.Status.setText(shopkeeperOrderModel.Status);
        holder.BillNo.setText(shopkeeperOrderModel.getDocumentID());
        holder.Date.setText(shopkeeperOrderModel.Time);

        holder.CardImage.setVisibility(View.GONE);
        holder.DeliveryDone.setVisibility(View.VISIBLE);

        db.collection("Customer").document(shopkeeperOrderModel.Customer_ID).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            DocumentSnapshot documentSnapshot = task.getResult();
                            holder.CustomerName.setText(documentSnapshot.get("Name").toString());
                            shopkeeperOrderModel.setCust_Name(documentSnapshot.get("Name").toString());
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
                seeShopOrders.onOrderSelected(shopkeeperOrderModelArrayList.get(position));
            }
        });

        holder.DeliveryDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seeShopOrders.onDeliveryDone(shopkeeperOrderModelArrayList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return shopkeeperOrderModelArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView CustomerName, BillNo, Date, Status;
        CardView SelectOrderCard;
        ImageView CardImage, DeliveryDone;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            CustomerName = itemView.findViewById(R.id.Shop_Name);
            BillNo = itemView.findViewById(R.id.Order_id);
            Date = itemView.findViewById(R.id.Order_date);
            Status = itemView.findViewById(R.id.Order_status);
            SelectOrderCard = itemView.findViewById(R.id.select_order);

            CardImage = itemView.findViewById(R.id.card_image);
            DeliveryDone = itemView.findViewById(R.id.delivery_done);
        }
    }
}
