package com.ibring_restaurantowner.views.home.adapter;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.ibring_restaurantowner.R;
import com.ibring_restaurantowner.model.OrderList;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.TimeZone;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder>
{
    Context context;
    ArrayList<OrderList> list;
    onClickListener listener;

    public HomeAdapter(Context context,ArrayList<OrderList> list)
    {
        this.context = context;
        this.list=list;
    }

    int time = 1 * 60 * 1000; //20 seconds
    int interval = 1000; // 1 second

    public static String getDateFromMillis(long d)
    {
        SimpleDateFormat df = new SimpleDateFormat("mm:ss");
        df.setTimeZone(TimeZone.getTimeZone("GMT"));
        return df.format(d);
    }

    CountDownTimer downTimer=null;

    public void callTimer(TextView textView,ConstraintLayout constraintLayout)
    {
        downTimer = new CountDownTimer(time, interval)
        {
            public void onTick(long millisUntilFinished)
            {
                textView.setVisibility(View.VISIBLE);
                textView.setText("ACCEPT ORDER( "+getDateFromMillis(millisUntilFinished)+")");
            }

            public void onFinish()
            {
                textView.setEnabled(false);
                textView.setClickable(false);
                textView.setText("ACCEPT ORDER");
                textView.setTextColor(context.getResources().getColor(R.color.black));
                constraintLayout.setBackground(context.getResources().getDrawable(R.drawable.gray_fill_background));
            }
        };
        downTimer.start();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view= LayoutInflater.from(context).inflate(R.layout.custom_home,parent,false);
        return new HomeAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position)
    {
        Log.e("PositionValue ",position+"");
        holder.id.setText("ID :"+list.get(position).getOrder_id());
        holder.tvPrice.setText("$ "+list.get(position).getPrice());
        holder.tvName.setText(list.get(position).getUser_name());

//        holder.tvMenu1.setText(list.get(position).getItems().toString().trim().replace("[","").replace("]",""));
//        holder.tvAccept.setText("Accept order (00.60)");
        holder.tvAccept.setText("Accept order");
        if (list.get(position).getHours().equalsIgnoreCase("0"))
        {
            holder.tvTime.setText(list.get(position).getMinutes()+" Minutes");
        }
        else
        {
            holder.tvTime.setText(list.get(position).getHours()+" hrs "+ list.get(position).getMinutes()+" mins");
        }

        if (list.get(position).getList().size()>0)
        {
            holder.recycler.setVisibility(View.VISIBLE);
            holder.recycler.setLayoutManager(new LinearLayoutManager(context));
            FoodItemsAdapter foodItemsAdapter=new FoodItemsAdapter(context,list.get(position).getList(), HomeAdapter.this);
            holder.recycler.setAdapter(foodItemsAdapter);
        }
        else
        {
            holder.recycler.setVisibility(View.GONE);
        }

        if (list.get(position).getDeliveryId().equalsIgnoreCase("abc"))
        {
            holder.tvAccept.setText("Accept Order");
        }
            else {
                holder.tvAccept.setText("Assigned");
            }

        holder.cc_accept.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
//                listener.onAcceptClick(position, v);
                if (list.get(position).getDeliveryId().equalsIgnoreCase("abc"))
                {
                    listener.onAcceptClick(position,v);
                }
                else
                {
                    Toast.makeText(context, "Driver Already Assigned", Toast.LENGTH_SHORT).show();
                }
            }
        });



       /* if (list.get(position).getValueTimer().equalsIgnoreCase("1"))
        {
            callTimer(holder.tvAccept,holder.cc_accept);
        }*/
    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        ConstraintLayout cc_accept;
        ConstraintLayout cc_reject;
        TextView id,tvPrice,tvAccept,tvTime,tvMinus,tvAdd,tvName;
        RecyclerView recycler;

        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);
            cc_accept=(ConstraintLayout)itemView.findViewById(R.id.cc_accept);
            cc_reject=(ConstraintLayout)itemView.findViewById(R.id.cc_reject);
            id=(TextView) itemView.findViewById(R.id.id);
            tvPrice=(TextView) itemView.findViewById(R.id.tvPrice);
            tvAccept=(TextView) itemView.findViewById(R.id.tvAccept);
            tvTime=(TextView) itemView.findViewById(R.id.tvTime);
            tvAdd=(TextView) itemView.findViewById(R.id.tvAdd);
            tvMinus=(TextView) itemView.findViewById(R.id.tvMinus);
            tvName=(TextView) itemView.findViewById(R.id.tvName);
            recycler=(RecyclerView) itemView.findViewById(R.id.recycler);
//            tvMenu1=(TextView) itemView.findViewById(R.id.tvMenu1);

        /*    cc_accept.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    listener.onAcceptClick(getAdapterPosition(),v);
                }
            });*/

            cc_reject.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    listener.onRejectClick(getAdapterPosition(),v);
                }
            });

            tvAdd.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    listener.onAddClick(getAdapterPosition(),v);
                }
            });

            tvMinus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onSubtractClick(getAdapterPosition(),v);
                }
            });
        }
    }

    public interface onClickListener
    {
         void onAcceptClick(int layoutPosition,View view);
         void onRejectClick(int layoutPositin,View view);
         void onAddClick(int layoutPositin,View view);
         void onSubtractClick(int layoutPositin,View view);
    }

    public void onItemSelectedListener(onClickListener clickListener)
    {
        listener=clickListener;
    }

}
