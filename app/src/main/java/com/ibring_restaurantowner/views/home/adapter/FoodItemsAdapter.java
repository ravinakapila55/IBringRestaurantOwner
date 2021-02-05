package com.ibring_restaurantowner.views.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.ibring_restaurantowner.R;
import com.ibring_restaurantowner.model.FoodItemModel;
import java.util.ArrayList;

public class FoodItemsAdapter extends RecyclerView.Adapter<FoodItemsAdapter.MyViewHolder>
{
    Context context;
    ArrayList<FoodItemModel> list;
    HomeAdapter foodOrderHistoryAdapter;
    AcceptedOrdersAdapter acceptedOrdersAdapter;
    String key="";

    public FoodItemsAdapter(Context context, ArrayList<FoodItemModel> list, HomeAdapter foodOrderHistoryAdapter)
    {
        this.context = context;
        this.list = list;
        this.foodOrderHistoryAdapter=foodOrderHistoryAdapter;
        this.key=key;
    }

    public FoodItemsAdapter(Context context, ArrayList<FoodItemModel> list, AcceptedOrdersAdapter foodOrderHistoryAdapter)
    {
        this.context = context;
        this.list = list;
        this.acceptedOrdersAdapter=foodOrderHistoryAdapter;
        this.key=key;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view= LayoutInflater.from(context).inflate(R.layout.custom_food_items,parent,false);
        return new FoodItemsAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position)
    {
        holder.tv_name.setText(list.get(position).getQuantity()+"* "+list.get(position).getItemName());
        holder.tvPrice.setText("$ "+list.get(position).getPrice());
    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView tv_name,tvPrice;
        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);
            tv_name=(TextView)itemView.findViewById(R.id.tv_name);
            tvPrice=(TextView)itemView.findViewById(R.id.tvPrice);
        }
    }
}
