package com.ibring_restaurantowner.views.home.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.ibring_restaurantowner.R;
import com.ibring_restaurantowner.model.OrderList;
import java.util.ArrayList;
import de.hdodenhof.circleimageview.CircleImageView;

public class AcceptedOrdersAdapter extends RecyclerView.Adapter<AcceptedOrdersAdapter.MyViewHolder>
{
    Context context;
    ArrayList<OrderList> list;

    public AcceptedOrdersAdapter(Context context,ArrayList<OrderList> list)
    {
        this.context = context;
        this.list=list;
    }

    @NonNull
    @Override
    public AcceptedOrdersAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view= LayoutInflater.from(context).inflate(R.layout.custom_accepted_home,parent,false);
        return new AcceptedOrdersAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AcceptedOrdersAdapter.MyViewHolder holder, int position)
    {
            holder.id.setText("ID:-"+list.get(position).getOrder_id());
            holder.tvName.setText(list.get(position).getUser_name());
//            holder.tvMenu1.setText(list.get(position).getItems().toString().replace("[","").replace("]",""));

        if (list.get(position).getList().size()>0)
        {
            holder.recycler.setVisibility(View.VISIBLE);
            holder.recycler.setLayoutManager(new LinearLayoutManager(context));

            FoodItemsAdapter foodItemsAdapter=new FoodItemsAdapter(context,list.get(position).getList(), AcceptedOrdersAdapter.this);
            holder.recycler.setAdapter(foodItemsAdapter);
        }
        else
        {
            holder.recycler.setVisibility(View.GONE);
        }

            if (list.get(position).getStatus().equalsIgnoreCase("accept"))
            {
                holder.tvStatus.setText("Preparing");
                holder.cc_ready.setVisibility(View.VISIBLE);
                holder.cc_prepare.setVisibility(View.GONE);
                holder.ivCall.setVisibility(View.VISIBLE);

                if (list.get(position).getHours().equalsIgnoreCase("0"))
                {
                 holder.tvAccept.setText("ORDER READY ("+list.get(position).getMinutes()+"mins)");
                }
                else
                {
                   holder.tvAccept.setText("ORDER READY ("+list.get(position).getHours()+"hrs "+list.get(position).getMinutes()+"mins)");
                }
            }
            else if (list.get(position).getStatus().equalsIgnoreCase("prepared"))
            {
                holder.tvStatus.setText("Ready");
                holder.cc_ready.setVisibility(View.GONE);
                holder.cc_prepare.setVisibility(View.VISIBLE);
                holder.ivCall.setVisibility(View.VISIBLE);
//              holder.tvAccept.setTag("Order PickedUp");
            }
            else if (list.get(position).getStatus().equalsIgnoreCase("delivered"))
            {
                holder.tvStatus.setText("Delivered");
                holder.cc_ready.setVisibility(View.GONE);
                holder.cc_prepare.setVisibility(View.VISIBLE);
                holder.ivCall.setVisibility(View.GONE);
            }
            holder.tvTotal.setText("Total Price :-$ "+list.get(position).getPrice());

            holder.tvRiderTime.setText(list.get(position).getDeliveryName()+" arriving in "+list.get(position).getHours()+
                    " hrs "+list.get(position).getMinutes()+" mins");

            holder.ivCall.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + list.get(position).getDeliveryPhone()));
                    context.startActivity(intent);
                }
            });
    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView id,tvName,tvStatus,tvTotal,tvRiderTime,tvPaymentStatus,tvAccept;
        ConstraintLayout cc_ready,cc_prepare;
        ImageView ivCall;
        CircleImageView ivImage;
        RecyclerView recycler;

        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);
            id=(TextView)itemView.findViewById(R.id.id);
            tvName=(TextView)itemView.findViewById(R.id.tvName);
            tvStatus=(TextView)itemView.findViewById(R.id.tvStatus);
            tvTotal=(TextView)itemView.findViewById(R.id.tvTotal);
            recycler=(RecyclerView) itemView.findViewById(R.id.recycler);

            tvPaymentStatus=(TextView)itemView.findViewById(R.id.tvPaymentStatus);
            tvAccept=(TextView)itemView.findViewById(R.id.tvAccept);
            cc_ready=(ConstraintLayout) itemView.findViewById(R.id.cc_ready);
            cc_prepare=(ConstraintLayout) itemView.findViewById(R.id.cc_prepare);
            ivCall=(ImageView) itemView.findViewById(R.id.ivCall);
            ivImage=(CircleImageView) itemView.findViewById(R.id.ivImage);
            tvRiderTime=(TextView) itemView.findViewById(R.id.tvRiderTime);

            cc_ready.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    myclick.onButtonClick(getAdapterPosition(),v);
                }
            });
        }
    }

    public void onItemSelectedListener(myclick myclick1)
    {
       this.myclick=myclick1;
    }

    myclick myclick;
    public interface myclick
    {
         void onButtonClick(int layoutPosition,View view);
    }

}
