package com.example.weshoppie.CustomerDashboard.CustPlaceOrder.NewOrderPlace;

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

public class SelectProductAdapter extends RecyclerView.Adapter<SelectProductAdapter.ViewHolder> {
    View v;
    Context context;
    ArrayList<SelectProductModel> selectProductModels;
    ArrayList<SelectProductModel> selectedProductList = new ArrayList<>();
    SelectCount selectCount;
    public void setFilteredList (ArrayList<SelectProductModel> filteredList){
        this.selectedProductList = filteredList;
        notifyDataSetChanged();
    }
    //Constructor ***************************************************************************************************************
    public SelectProductAdapter(Context context, ArrayList<SelectProductModel> selectProductModels, SelectCount selectCount) {
        this.context = context;
        this.selectProductModels = selectProductModels;
        this.selectCount = selectCount;
    }

    public View getV() {
        return v;
    }
    //Creating the view for view holder *************************************************************************************
    @NonNull
    @Override
    public SelectProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        v = LayoutInflater.from(context).inflate(R.layout.select_products,parent,false);
        return new ViewHolder(v);
    }
    //Binding the data to the view ***********************************************************************************************
    @Override
    public void onBindViewHolder(@NonNull SelectProductAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        boolean ifadd=false;
        SelectProductModel selectProductModel = selectProductModels.get(position);
        final int[] cnt = {0};
        holder.Pname.setText(selectProductModel.Product_Name);
        holder.Pprice.setText(selectProductModel.Product_Price);
        holder.Per.setText(selectProductModel.Product_Price_per);
        holder.Brand.setText(selectProductModel.Brand);
        //Increasing the Product Count **************************************************************************************
        holder.Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cnt[0]++;
                holder.Count.setText(String.valueOf(cnt[0]));
                selectProductModel.setCount(cnt[0]);
            }
        });
        //Decreasing the product Count ****************************************************************************************
        holder.Subtract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cnt[0] == 0){
                    selectCount.onCountToast();
                    return;
                }
                cnt[0]--;
                holder.Count.setText(String.valueOf(cnt[0]));
                selectProductModel.setCount(cnt[0]);
            }
        });
        //Add the product ****************************************************************************************8
        holder.AddIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cnt[0] == 0){
                    selectCount.onCountToast();
                    return;
                }
                holder.AddIn.setVisibility(View.GONE);
                holder.Added.setVisibility(View.VISIBLE);
                selectCount.onQuantitySelected(selectProductModels.get(position));
            }
        });

        holder.Added.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.Added.setVisibility(View.GONE);
                holder.AddIn.setVisibility(View.VISIBLE);
                selectCount.onQuantityNotSelected(selectProductModels.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return selectProductModels.size();
    }
    //Getting the ID ************************************************************************************************
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView Pname, Pprice, Per, Brand, Count;
        ImageView Subtract, Add, AddIn, Added;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            Pname = itemView.findViewById(R.id.txtName1);
            Pprice = itemView.findViewById(R.id.txtPrice1);
            Per = itemView.findViewById(R.id.txtPricePer1);
            Brand = itemView.findViewById(R.id.txtBrand1);
            Count = itemView.findViewById(R.id.count1);
            Subtract = itemView.findViewById(R.id.decrease);
            Add = itemView.findViewById(R.id.increase);
            AddIn = itemView.findViewById(R.id.add_in);
            Added = itemView.findViewById(R.id.added);
        }
    }
}
