package com.ibring_restaurantowner.views.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.ibring_restaurantowner.R;
import com.ibring_restaurantowner.model.MenuItemModel;
import java.util.ArrayList;

public class MenuSubAdapter extends RecyclerView.Adapter<MenuSubAdapter.MyViewHolder>
{
    Context context;
    MenuAdapter adapter;
    ArrayList<MenuItemModel> list;

    public MenuSubAdapter(Context context, MenuAdapter adapter,ArrayList<MenuItemModel> list)
    {
        this.context = context;
        this.adapter = adapter;
        this.list=list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view= LayoutInflater.from(context).inflate(R.layout.custom_menu_sub,parent,false);
        return new MenuSubAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position)
    {
        holder.tvName.setText(list.get(position).getMenu_name());
        holder.tvPrice.setText("$ "+list.get(position).getPrice());
    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView tvName,tvPrice,tvDelete;
        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);
            tvName=(TextView)itemView.findViewById(R.id.tvName);
            tvPrice=(TextView)itemView.findViewById(R.id.tvPrice);
            tvDelete=(TextView)itemView.findViewById(R.id.tvDelete);

            tvDelete.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    mysubItemClckListener.onDeleteClick(getAdapterPosition(),v);
                }
            });
        }
    }

    mysubItemClckListener mysubItemClckListener;
    public void onItemSelectedListener(mysubItemClckListener listener)
    {
        this.mysubItemClckListener=listener;
    }

    public interface mysubItemClckListener
    {
        public void onDeleteClick(int layoutPosition,View  view);
    }

}
