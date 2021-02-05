package com.ibring_restaurantowner.views.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ibring_restaurantowner.R;
import com.ibring_restaurantowner.model.NearbyDriversModel;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class NearbyDriversAdapter extends RecyclerView.Adapter<NearbyDriversAdapter.MyViewHolder> {

    ArrayList<NearbyDriversModel> list;
    Context  context;

    public NearbyDriversAdapter(ArrayList<NearbyDriversModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view= LayoutInflater.from(context).inflate(R.layout.custom_nearby,parent,false);
        return new NearbyDriversAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position)
    {
        holder.tvName.setText(list.get(position).getName());
        if (list.get(position).getDescription().equalsIgnoreCase("null"))
        {
            holder.tvDesc.setText("NA");
        }
        else {
            holder.tvDesc.setText(list.get(position).getDescription());
        }
        holder.tvRating.setText(list.get(position).getRating());
    }
    onClickListner onClickListner;

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvName,tvAssign,tvRating,tvDesc;
        CircleImageView ivDelievryBoy;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName=(TextView)itemView.findViewById(R.id.tvName);
            tvAssign=(TextView)itemView.findViewById(R.id.tvAssign);
            tvRating=(TextView)itemView.findViewById(R.id.tvRating);
            tvDesc=(TextView)itemView.findViewById(R.id.tvDesc);
            ivDelievryBoy=(CircleImageView) itemView.findViewById(R.id.ivDelievryBoy);

            tvAssign.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListner.onAssignCLick(getAdapterPosition(),v);
                }
            });
        }
    }


    public void onItesmSelectedListener(onClickListner onClickListner)
    {
        this.onClickListner=onClickListner;
    }

    public interface onClickListner{
        public void onAssignCLick(int layoutPosition,View view);
    }
}
