package com.ibring_restaurantowner.views.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.ibring_restaurantowner.R;
import com.ibring_restaurantowner.model.FoodItemModel;
import com.ibring_restaurantowner.model.OrderList;
import com.ibring_restaurantowner.retrofit.RetrofitResponse;
import com.ibring_restaurantowner.retrofit.RetrofitService;
import com.ibring_restaurantowner.utils.SharedHelper;
import com.ibring_restaurantowner.utils.URLHelper;
import com.ibring_restaurantowner.views.Login;
import com.ibring_restaurantowner.views.home.adapter.AcceptedOrdersAdapter;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;

public class AcceptedOrders extends AppCompatActivity implements RetrofitResponse, View.OnClickListener
{
    RecyclerView recyclerAccepted;
    ConstraintLayout cc_orders;
    ConstraintLayout cc_logout;
    ConstraintLayout cc_more,cc_menu;
    TextView tvPreparing,tvReady,tvPick,tvMore;
    TextView tvNoData;
    TextView tvLabellll;
    ConstraintLayout options;
    ImageView imageBack;
    ImageView ivMore;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accept_orders);
        findIds();
    }

    public void callService()
    {
        new RetrofitService(this, AcceptedOrders.this, URLHelper.ORDER_LIST ,
                500, 1,"1").callService(true);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        tvPreparing.setBackground(getResources().getDrawable(R.drawable.red_background));
        tvPreparing.setTextColor(getResources().getColor(R.color.red));

        tvReady.setBackground(getResources().getDrawable(R.drawable.gray_fill_background));
        tvReady.setTextColor(getResources().getColor(R.color.chat_text));

        tvPick.setBackground(getResources().getDrawable(R.drawable.gray_fill_background));
        tvPick.setTextColor(getResources().getColor(R.color.chat_text));
        callService();
    }

    ArrayList<OrderList> acceptedList=new ArrayList<>();
    ArrayList<OrderList> readyList=new ArrayList<>();
    ArrayList<OrderList> DeliveredList=new ArrayList<>();

    @Override
    public void onResponse(int RequestCode, String response)
    {
        switch (RequestCode)
        {
                case 500:
                Log.e("ResponseRestaurantList ",response);
                try {
                    JSONObject jo=new JSONObject(response);
                    if (jo.getString("status").equalsIgnoreCase("success"))
                    {
                        tvNoData.setVisibility(View.GONE);
                        recyclerAccepted.setVisibility(View.VISIBLE);

                        acceptedList.clear();
                        readyList.clear();
                        DeliveredList.clear();

                        JSONArray jsonArray=jo.getJSONArray("result");

                        for (int i = 0; i <jsonArray.length() ; i++)
                        {
                            JSONObject jsonObject=jsonArray.getJSONObject(i);
                            OrderList restaurantsList=new OrderList();

                            restaurantsList.setId(jsonObject.getString("id"));
//                            restaurantsList.setUser_name(jsonObject.getString("title"));
                            restaurantsList.setOrder_id(jsonObject.getString("order_id"));
                            restaurantsList.setLatitude(jsonObject.getString("latitude"));
                            restaurantsList.setLongitude(jsonObject.getString("longitude"));
                            restaurantsList.setLocation(jsonObject.getString("location"));
                            restaurantsList.setPrice(jsonObject.getString("price"));
                            restaurantsList.setItems(jsonObject.getString("item"));
                            restaurantsList.setStatus(jsonObject.getString("status"));
                            restaurantsList.setHours(jsonObject.getString("hours"));
                            restaurantsList.setMinutes(jsonObject.getString("minutes"));
                            //todo to set timer functionality
                            restaurantsList.setValueTimer("1");

                            JSONObject user=jsonObject.getJSONObject("user");

                            restaurantsList.setUser_name(user.getString("first_name")+ " "+user.getString("last_name"));

                            if (jsonObject.has("delivery_boy"))
                            {
                                Object o=jsonObject.get("delivery_boy");
                                if (o instanceof JSONObject)
                                {
                                    JSONObject delivery_boy=jsonObject.getJSONObject("delivery_boy");
                                    restaurantsList.setDeliveryId(delivery_boy.getString("id"));
                                    restaurantsList.setDeliveryImage(delivery_boy.getString("picture"));
                                    restaurantsList.setDeliveryPhone(delivery_boy.getString("mobile"));
                                    restaurantsList.setDeliveryName(delivery_boy.getString("first_name"));
//                                    restaurantsList.setDeliveryName(delivery_boy.getString("first_name")+ " "+delivery_boy.getString("last_name"));
                                }
                            }

                            JSONArray item=jsonObject.getJSONArray("item");
                            ArrayList<FoodItemModel> list=new ArrayList<>();

                            if (item.length()>0)
                            {
                                for (int j = 0; j < item.length(); j++)
                                {
                                    JSONObject object=item.getJSONObject(j);
                                    FoodItemModel foodItemModel=new FoodItemModel();
                                    foodItemModel.setMenu_id(object.getString("menu_id"));
                                    foodItemModel.setQuantity(object.getString("quantity"));
                                    foodItemModel.setItemName(object.getString("item_name"));
                                    foodItemModel.setPrice(object.getString("price"));
                                    list.add(foodItemModel);
                                }
                            }

                            restaurantsList.setList(list);

                            //delivered//pickup
                            if (jsonObject.getString("status").equalsIgnoreCase("accept"))
                            {
                                acceptedList.add(restaurantsList);
                                tvPreparing.setText("PREPARING("+acceptedList.size()+")");
                            }
                            if (jsonObject.getString("status").equalsIgnoreCase("prepared"))
                            {
                                readyList.add(restaurantsList);
                                tvReady.setText("READY("+readyList.size()+")");
                            }
                            if (jsonObject.getString("status").equalsIgnoreCase("delivered"))
                            {
                                DeliveredList.add(restaurantsList);
                                tvPick.setText("PICK UP("+DeliveredList.size()+")");
                            }


                            if (acceptedList.size()==0)
                            {
                                tvPreparing.setText("PREPARING)");
                                tvPreparing.setBackground(getResources().getDrawable(R.drawable.red_background));
                                tvPreparing.setTextColor(getResources().getColor(R.color.red));
                            }
                        }
                        if (acceptedList.size()>0)
                        {
                            setAdapter("accept");
                        }
                        else
                        {
                            tvNoData.setVisibility(View.VISIBLE);
                            recyclerAccepted.setVisibility(View.GONE);

                            tvPreparing.setText("PREPARING)");
                            tvPreparing.setBackground(getResources().getDrawable(R.drawable.gray_fill_background));
                            tvPreparing.setTextColor(getResources().getColor(R.color.chat_text));

                            tvReady.setBackground(getResources().getDrawable(R.drawable.gray_fill_background));
                            tvReady.setTextColor(getResources().getColor(R.color.chat_text));

                            tvPick.setBackground(getResources().getDrawable(R.drawable.gray_fill_background));
                            tvPick.setTextColor(getResources().getColor(R.color.chat_text));
                        }
                    }

                    else
                    {
                        tvNoData.setVisibility(View.VISIBLE);
                        recyclerAccepted.setVisibility(View.GONE);
                    }
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
                break;

                case 100:
                Log.e("ResponsePrepare ",response);
                callService();
                break;
        }
    }

    public void findIds()
    {
        recyclerAccepted=(RecyclerView)findViewById(R.id.recyclerAccepted);
        tvNoData=(TextView) findViewById(R.id.tvNoData);
        tvLabellll=(TextView) findViewById(R.id.tvLabellll);
        options=(ConstraintLayout) findViewById(R.id.options);
        tvPreparing=(TextView) findViewById(R.id.tvPreparing);
        tvReady=(TextView) findViewById(R.id.tvReady);
        tvPick=(TextView) findViewById(R.id.tvPick);
        imageBack=(ImageView) findViewById(R.id.imageBack);
        ivMore=(ImageView) findViewById(R.id.ivMore);
        tvMore=(TextView) findViewById(R.id.tvMore);


        tvMore.setTextColor(getResources().getColor(R.color.darkest_blue));
        ivMore.setImageDrawable(getResources().getDrawable(R.drawable.more_blue));

        recyclerAccepted.setVisibility(View.VISIBLE);
        recyclerAccepted.setLayoutManager(new LinearLayoutManager(this));

        cc_orders=(ConstraintLayout) findViewById(R.id.cc_orders);
        cc_logout=(ConstraintLayout) findViewById(R.id.cc_logout);
        cc_more=(ConstraintLayout) findViewById(R.id.cc_more);
        cc_menu=(ConstraintLayout) findViewById(R.id.cc_menu);
        tvPreparing.setOnClickListener(this);

        tvReady.setOnClickListener(this);
        tvPick.setOnClickListener(this);
        tvLabellll.setText("Accepted Orders");
        options.setVisibility(View.VISIBLE);

        imageBack.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        cc_menu.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent=new Intent(AcceptedOrders.this, Menu.class);
                startActivity(intent);
            }
        });

        cc_orders.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent=new Intent(AcceptedOrders.this,HomeScreen.class);
                startActivity(intent);
            }
        });

        cc_logout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                SharedHelper.Companion.putKey(AcceptedOrders.this, "current_status", "");
                SharedHelper.Companion.putKey(AcceptedOrders.this, "loggedIn", "false");

                Intent intent=new Intent(AcceptedOrders.this, Login.class);
                startActivity(intent);
            }
        });

        cc_more.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent=new Intent(AcceptedOrders.this, AcceptedOrders.class);
                startActivity(intent);
            }
        });
    }

    public void callAction(String id,String status)
    {
        try
        {
            JSONObject jo=new JSONObject();
            jo.put("id", id);
            jo.put("status",status);
            Log.e("paramAction ",jo.toString());
            new RetrofitService(this, AcceptedOrders.this, URLHelper.CHANGE_STATUS ,jo,
                    100, 2,"1").callService(true);
        }
        catch(Exception exx)
        {
            exx.printStackTrace();
        }
    }

    public void setAdapter(String type)
    {
        AcceptedOrdersAdapter homeAdapter=null;
        if (type.equalsIgnoreCase("accept"))
        {
            if (acceptedList.size()>0)
            {

                recyclerAccepted.setVisibility(View.VISIBLE);
                tvNoData.setVisibility(View.GONE);
                homeAdapter=new AcceptedOrdersAdapter(this,acceptedList);
                recyclerAccepted.setAdapter(homeAdapter);

                homeAdapter.onItemSelectedListener(new AcceptedOrdersAdapter.myclick() {
                    @Override
                    public void onButtonClick(int layoutPosition, View view)
                    {
                        callAction(acceptedList.get(layoutPosition).getId(),"prepared");
                    }
                });
            }
            else {
                recyclerAccepted.setVisibility(View.GONE);
                tvNoData.setVisibility(View.VISIBLE);
            }
        }
        else if (type.equalsIgnoreCase("pickup"))
        {
            if (readyList.size()>0)
            {
                recyclerAccepted.setVisibility(View.VISIBLE);
                tvNoData.setVisibility(View.GONE);
                homeAdapter=new AcceptedOrdersAdapter(this,readyList);
                recyclerAccepted.setAdapter(homeAdapter);

                /*homeAdapter.onItemSelectedListener(new AcceptedOrdersAdapter.myclick() {
                    @Override
                    public void onButtonClick(int layoutPosition, View view)
                    {
                        callAction(acceptedList.get(layoutPosition).getId(),"prepared");
                    }
                });*/
            }
            else
            {
                recyclerAccepted.setVisibility(View.GONE);
                tvNoData.setVisibility(View.VISIBLE);
            }

        }
        else if (type.equalsIgnoreCase("delivered"))
        {
             homeAdapter=new AcceptedOrdersAdapter(this,DeliveredList);

            if (DeliveredList.size()>0)
            {
                recyclerAccepted.setVisibility(View.VISIBLE);
                tvNoData.setVisibility(View.GONE);
                homeAdapter=new AcceptedOrdersAdapter(this,DeliveredList);
                recyclerAccepted.setAdapter(homeAdapter);

                /*

                homeAdapter.onItemSelectedListener(new AcceptedOrdersAdapter.myclick() {
                    @Override
                    public void onButtonClick(int layoutPosition, View view)
                    {
                        callAction(acceptedList.get(layoutPosition).getId(),"prepared");
                    }
                });

                */
            }
            else
            {
                recyclerAccepted.setVisibility(View.GONE);
                tvNoData.setVisibility(View.VISIBLE);
            }

        }
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.tvPreparing:

                tvPreparing.setBackground(getResources().getDrawable(R.drawable.red_background));
                tvPreparing.setTextColor(getResources().getColor(R.color.red));

                tvReady.setBackground(getResources().getDrawable(R.drawable.gray_fill_background));
                tvReady.setTextColor(getResources().getColor(R.color.chat_text));

                tvPick.setBackground(getResources().getDrawable(R.drawable.gray_fill_background));
                tvPick.setTextColor(getResources().getColor(R.color.chat_text));

                setAdapter("accept");

                break;

            case R.id.tvReady:

                tvReady.setBackground(getResources().getDrawable(R.drawable.red_background));
                tvReady.setTextColor(getResources().getColor(R.color.red));

                tvPreparing.setBackground(getResources().getDrawable(R.drawable.gray_fill_background));
                tvPreparing.setTextColor(getResources().getColor(R.color.chat_text));

                tvPick.setBackground(getResources().getDrawable(R.drawable.gray_fill_background));
                tvPick.setTextColor(getResources().getColor(R.color.chat_text));

                setAdapter("pickup");

                break;

            case R.id.tvPick:
                tvPick.setBackground(getResources().getDrawable(R.drawable.red_background));
                tvPick.setTextColor(getResources().getColor(R.color.red));

                tvPreparing.setBackground(getResources().getDrawable(R.drawable.gray_fill_background));
                tvPreparing.setTextColor(getResources().getColor(R.color.chat_text));

                tvReady.setBackground(getResources().getDrawable(R.drawable.gray_fill_background));
                tvReady.setTextColor(getResources().getColor(R.color.chat_text));

                setAdapter("delivered");


                break;
        }
    }
}
