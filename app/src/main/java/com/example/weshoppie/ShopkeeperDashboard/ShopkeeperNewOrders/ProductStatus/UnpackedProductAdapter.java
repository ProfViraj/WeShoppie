package com.example.weshoppie.ShopkeeperDashboard.ShopkeeperNewOrders.ProductStatus;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weshoppie.R;

import java.util.ArrayList;

public class UnpackedProductAdapter extends RecyclerView.Adapter<UnpackedProductAdapter.ViewHolder> {
    Context context;
    ArrayList<UnpackedProductsModel> unpackedProductsModelArrayList;
    SelectUnpackedProduct selectUnpackedProduct;
    //Filtering the search list ***********************************************************************
    public void setFilteredList (ArrayList<UnpackedProductsModel> filteredList){
        this.unpackedProductsModelArrayList = filteredList;
        notifyDataSetChanged();
    }
    //Constructor ************************************************************************************************************
    public UnpackedProductAdapter(Context context, ArrayList<UnpackedProductsModel> unpackedProductsModelArrayList, SelectUnpackedProduct selectUnpackedProduct) {
        this.context = context;
        this.unpackedProductsModelArrayList = unpackedProductsModelArrayList;
        this.selectUnpackedProduct = selectUnpackedProduct;
    }
    //Creating the view for view holder ************************************************************************************
    @NonNull
    @Override
    public UnpackedProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.unpacked_product_row, parent, false);
        return new ViewHolder(v);
    }
    //binding the data to the view ******************************************************************************************
    @Override
    public void onBindViewHolder(@NonNull UnpackedProductAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        UnpackedProductsModel unpackedProductsModel = unpackedProductsModelArrayList.get(position);
        holder.ProductName.setText(unpackedProductsModel.Product_Name);
        holder.ProductPrice.setText(unpackedProductsModel.Product_Price);
        holder.ProductCount.setText("Count: "+unpackedProductsModel.getCount());
        holder.ProductTotalCost.setText("Total Cost: "+unpackedProductsModel.getTotal_Cost()+" Rs.");
        //Editing the product packing status ****************************************************************************
        holder.EditStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.EditStatus.setVisibility(View.GONE);
                selectUnpackedProduct.onItemClicked(unpackedProductsModelArrayList.get(position));
            }
        });

    }

    @Override
    public int getItemCount() {
        return unpackedProductsModelArrayList.size();
    }
    //Getting the IDs *******************************************************************************
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView ProductName, ProductPrice, ProductCount, ProductTotalCost;
        ImageView EditStatus;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ProductName = itemView.findViewById(R.id.txtNameUnpacked);
            ProductPrice = itemView.findViewById(R.id.txtPriceUnpacked);
            ProductCount = itemView.findViewById(R.id.txtCountUnpacked);
            ProductTotalCost = itemView.findViewById(R.id.txtCostUnpacked);
            EditStatus = itemView.findViewById(R.id.edit_status);
        }
    }
}
