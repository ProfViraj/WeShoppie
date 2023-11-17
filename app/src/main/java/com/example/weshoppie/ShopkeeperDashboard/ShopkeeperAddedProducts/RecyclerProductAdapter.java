package com.example.weshoppie.ShopkeeperDashboard.ShopkeeperAddedProducts;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

public class RecyclerProductAdapter extends RecyclerView.Adapter<RecyclerProductAdapter.ViewHolder> {
    Context context;
    ArrayList<ProductModel> arrProducts;
    FirebaseFirestore db;

    public void setFilteredList (ArrayList<ProductModel> filteredList){
        this.arrProducts = filteredList;
        notifyDataSetChanged();
    }
    public RecyclerProductAdapter(Context context, ArrayList<ProductModel> arrProducts){
        this.context = context;
        this.arrProducts = arrProducts;
        db = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public RecyclerProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.product_row,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerProductAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ProductModel productModel = arrProducts.get(position);
        holder.Pname.setText(productModel.Product_Name);
        holder.Pprice.setText(productModel.Product_Price);
        holder.Per.setText(productModel.Product_Price_per);
        holder.Brand.setText(productModel.Brand);

        holder.delproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context)
                        .setTitle("Delete Product")
                        .setMessage("Are you sure want to delete?")
                        .setIcon(R.drawable.baseline_delete_24)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                db.collection("Products").document(arrProducts.get(position).getDocumentID())
                                        .delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()){
                                                    arrProducts.remove(arrProducts.get(position));
                                                    notifyDataSetChanged();
                                                    Toast.makeText(context, "Product Deleted", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(context, "Unsuccessful", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                builder.show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return arrProducts.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView Pname, Pprice, Per, Brand;
        LinearLayout llRow;
        ImageView delproduct;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Pname = itemView.findViewById(R.id.txtName);
            Pprice = itemView.findViewById(R.id.txtPrice);
            Per = itemView.findViewById(R.id.txtPricePer);
            Brand = itemView.findViewById(R.id.txtBrand);
            delproduct = itemView.findViewById(R.id.delproduct);
            llRow = itemView.findViewById(R.id.llRow);
        }
    }
}
