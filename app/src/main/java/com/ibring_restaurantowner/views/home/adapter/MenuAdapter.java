package com.ibring_restaurantowner.views.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.ibring_restaurantowner.R;
import com.ibring_restaurantowner.model.MenuModel;
import java.util.ArrayList;


public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MyViewHolder>
{
    Context context;
    private static int currentPosition = 0;
    ArrayList<MenuModel> menuModelList;


    public MenuAdapter(Context context,ArrayList<MenuModel> menuModelList)
    {
        this.context = context;
        this.menuModelList=menuModelList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view= LayoutInflater.from(context).inflate(R.layout.custom_menu,parent,false);
        return new MenuAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position)
    {
        holder.tvType.setText(menuModelList.get(position).getMenu_type());

        if (menuModelList.get(position).getNoOfItems().equalsIgnoreCase("0"))
        {
            holder.tvItems.setText(menuModelList.get(position).getNoOfItems()+ " Item");
        }
        else {
            holder.tvItems.setText(menuModelList.get(position).getNoOfItems()+ " Items");
        }



        if (menuModelList.get(position).getIsStarterFlag().equalsIgnoreCase("1"))
        {
            holder.recyclerSubItems.setVisibility(View.VISIBLE);
            holder.recyclerSubItems.setLayoutManager(new LinearLayoutManager(context));
            MenuSubAdapter menuSubAdapter=new MenuSubAdapter(context,MenuAdapter.this,menuModelList.get(position).getStarterItemList());
            holder.recyclerSubItems.setAdapter(menuSubAdapter);

            menuSubAdapter.onItemSelectedListener(new MenuSubAdapter.mysubItemClckListener() {
                @Override
                public void onDeleteClick(int layoutPosition, View view) {
                    mainItemListener.onItemClick(position,view,layoutPosition);
                }
            });
        }
        else {
            holder.tvNoData.setVisibility(View.VISIBLE);
            holder.recyclerSubItems.setVisibility(View.GONE);
        }

        if (menuModelList.get(position).getIsDesertsFlag().equalsIgnoreCase("1"))
        {
            holder.recyclerSubItems.setVisibility(View.VISIBLE);

            holder.recyclerSubItems.setLayoutManager(new LinearLayoutManager(context));
            MenuSubAdapter menuSubAdapter=new MenuSubAdapter(context,MenuAdapter.this,menuModelList.get(position).getDesertItemList());
            holder.recyclerSubItems.setAdapter(menuSubAdapter);

            menuSubAdapter.onItemSelectedListener(new MenuSubAdapter.mysubItemClckListener() {
                @Override
                public void onDeleteClick(int layoutPosition, View view) {
                    mainItemListener.onItemClick(position,view,layoutPosition);
                }
            });

        }
        else {
            holder.tvNoData.setVisibility(View.VISIBLE);
            holder.recyclerSubItems.setVisibility(View.GONE);
        }
        if (menuModelList.get(position).getIsMainCourseFlag().equalsIgnoreCase("1"))
        {
            holder.recyclerSubItems.setVisibility(View.VISIBLE);

            holder.recyclerSubItems.setLayoutManager(new LinearLayoutManager(context));
            MenuSubAdapter menuSubAdapter=new MenuSubAdapter(context,MenuAdapter.this,menuModelList.get(position).getMainCourdeItemList());
            holder.recyclerSubItems.setAdapter(menuSubAdapter);


            menuSubAdapter.onItemSelectedListener(new MenuSubAdapter.mysubItemClckListener() {
                @Override
                public void onDeleteClick(int layoutPosition, View view) {
                    mainItemListener.onItemClick(position,view,layoutPosition);
                }
            });
        }
       /* else {
            holder.tvNoData.setVisibility(View.VISIBLE);
            holder.recyclerSubItems.setVisibility(View.GONE);        }*/





        //if the position is equals to the item position which is to be expanded
        if (currentPosition == position)
        {
            if (!menuModelList.get(position).getNoOfItems().equalsIgnoreCase("0"))
            {
                //creating an animation
                Animation slideDown = AnimationUtils.loadAnimation(context, R.anim.slide_down);

                //toggling visibility
                holder.cc_sub.setVisibility(View.VISIBLE);

                //adding sliding effect
                holder.cc_sub.startAnimation(slideDown);
            }

        }

        holder.cc_main.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {

                if (!menuModelList.get(position).getNoOfItems().equalsIgnoreCase("0"))
                {
                    //getting the position of the item to expand it
                    currentPosition = position;

                    //reloding the list
                    notifyDataSetChanged();
                }

            }
        });

    }

    @Override
    public int getItemCount()
    {
        return menuModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView tvType,tvItems,tvEdit,tvNoData;
        RecyclerView recyclerSubItems;
        ConstraintLayout cc_main,cc_sub;
        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);
            tvType=(TextView)itemView.findViewById(R.id.tvType);
            tvItems=(TextView)itemView.findViewById(R.id.tvItems);
            tvEdit=(TextView)itemView.findViewById(R.id.tvEdit);
            tvNoData=(TextView)itemView.findViewById(R.id.tvNoData);
            cc_main=(ConstraintLayout) itemView.findViewById(R.id.cc_main);
            cc_sub=(ConstraintLayout) itemView.findViewById(R.id.cc_sub);
            recyclerSubItems=(RecyclerView) itemView.findViewById(R.id.recyclerSubItems);


        }
    }

    mainItemListener mainItemListener;

    public void onItemClickListener(mainItemListener listener)
    {
        this.mainItemListener=listener;
    }

    public interface mainItemListener{
        public void onItemClick(int layoutPosition,View view,int subPosition);
    }
}
