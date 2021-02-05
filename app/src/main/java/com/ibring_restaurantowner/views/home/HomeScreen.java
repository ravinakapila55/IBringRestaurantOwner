package com.ibring_restaurantowner.views.home;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.ibring_restaurantowner.R;
import com.ibring_restaurantowner.model.FoodItemModel;
import com.ibring_restaurantowner.model.OrderList;
import com.ibring_restaurantowner.retrofit.RetrofitResponse;
import com.ibring_restaurantowner.retrofit.RetrofitService;
import com.ibring_restaurantowner.utils.SharedHelper;
import com.ibring_restaurantowner.utils.URLHelper;
import com.ibring_restaurantowner.utils.permission.Permission;
import com.ibring_restaurantowner.utils.permission.PermissionGranted;
import com.ibring_restaurantowner.views.Login;
import com.ibring_restaurantowner.views.home.adapter.HomeAdapter;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;

public class HomeScreen extends AppCompatActivity implements RetrofitResponse,GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, PermissionGranted
{
    RecyclerView recycler,recyclerAccepted;
    TextView tvNoData;
    ConstraintLayout cc_orders;
    ConstraintLayout cc_menu;
    ConstraintLayout cc_logout;
    ConstraintLayout options;
    ConstraintLayout cc_more;
    ImageView imageBack;
    TextView tvLabellll;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        Permission.checkPermissionLocation(this, this);


        if (mGoogleApiClient == null)
        {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        findIds();
    }

    public void callService()
    {
        new RetrofitService(this, HomeScreen.this, URLHelper.ORDER_LIST ,
                500, 1,"1").callService(true);
    }

    ArrayList<OrderList> pendingList=new ArrayList<>();
    ArrayList<OrderList> mainList=new ArrayList<>();

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
                        recycler.setVisibility(View.VISIBLE);
                        recyclerAccepted.setVisibility(View.GONE);

                        pendingList.clear();
                        mainList.clear();
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
                                else {
                                    restaurantsList.setDeliveryId("abc");
                                }
                            }


                            JSONObject user=jsonObject.getJSONObject("user");

                            restaurantsList.setUser_name(user.getString("first_name")+ " "+user.getString("last_name"));

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

                            mainList.add(restaurantsList);
                            if (jsonObject.getString("status").equalsIgnoreCase("pending"))
                            {
                                pendingList.add(restaurantsList);
                            }
                        }

                        if (pendingList.size()>0)
                        {
                            setAdapter();
                        }

                        else {
                            tvNoData.setVisibility(View.VISIBLE);
                            recycler.setVisibility(View.GONE);
                            recyclerAccepted.setVisibility(View.GONE);
                        }
                    }

                    else {
                        tvNoData.setVisibility(View.VISIBLE);
                        recycler.setVisibility(View.GONE);
                        recyclerAccepted.setVisibility(View.GONE);
                    }
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
                break;

            case 100:
                try
                {
                    Log.e("ActionResponse ",response);

                    JSONObject jsonObject=new JSONObject(response);
                    callService();

                   /* if (jsonObject.getString("status").equalsIgnoreCase("cancel"))
                    {
                        callService();
                    }
                    else
                    {
                        Intent intent=new Intent(HomeScreen.this, AcceptedOrders.class);
                        startActivity(intent);
                    }*/
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }

                break;
        }
    }

    ImageView ivorders;
    TextView tvOrders;

    public void findIds()
    {
        recycler=(RecyclerView)findViewById(R.id.recycler);
        recyclerAccepted=(RecyclerView)findViewById(R.id.recyclerAccepted);
        cc_orders=(ConstraintLayout) findViewById(R.id.cc_orders);
        cc_menu=(ConstraintLayout) findViewById(R.id.cc_menu);
        cc_logout=(ConstraintLayout) findViewById(R.id.cc_logout);
        options=(ConstraintLayout) findViewById(R.id.options);
        cc_more=(ConstraintLayout) findViewById(R.id.cc_more);
        tvNoData=(TextView) findViewById(R.id.tvNoData);
        tvLabellll=(TextView) findViewById(R.id.tvLabellll);
        imageBack=(ImageView) findViewById(R.id.imageBack);
        tvLabellll.setText("New Orders");
        recycler.setVisibility(View.GONE);
        recyclerAccepted.setVisibility(View.GONE);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        options.setVisibility(View.GONE);
        imageBack.setVisibility(View.GONE);


        ivorders=(ImageView) findViewById(R.id.ivorders);
        tvOrders=(TextView) findViewById(R.id.tvOrders);


        tvOrders.setTextColor(getResources().getColor(R.color.darkest_blue));
        ivorders.setImageDrawable(getResources().getDrawable(R.drawable.order_blue));

        cc_logout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                SharedHelper.Companion.putKey(HomeScreen.this, "current_status", "");
                SharedHelper.Companion.putKey(HomeScreen.this, "loggedIn", "false");

//                SharedHelper.Companion.logout();

                Intent intent=new Intent(HomeScreen.this, Login.class);
                startActivity(intent);
            }
        });

        cc_orders.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent=new Intent(HomeScreen.this, HomeScreen.class);
                startActivity(intent);
            }
        });

        cc_menu.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent=new Intent(HomeScreen.this, Menu.class);
                startActivity(intent);
            }
        });

        cc_more.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent=new Intent(HomeScreen.this, AcceptedOrders.class);
                startActivity(intent);
            }
        });

//        setAdapter();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        callService();
    }

    public void setAdapter()
    {
        HomeAdapter homeAdapter=new HomeAdapter(this,pendingList);
        recycler.setAdapter(homeAdapter);



        homeAdapter.onItemSelectedListener(new HomeAdapter.onClickListener()
        {
            @Override
            public void onAcceptClick(int layoutPosition, View view)
            {
                Intent intent=new Intent(HomeScreen.this,NearbyDrivers.class);
                intent.putExtra("list",pendingList.get(layoutPosition));
                startActivity(intent);
               /* callAction(pendingList.get(layoutPosition).getId(),"accept",pendingList.get(layoutPosition).getHours(),
                        pendingList.get(layoutPosition).getMinutes());*/
            }

            @Override
            public void onRejectClick(int layoutPositin, View view)
            {
                callAction(pendingList.get(layoutPositin).getId(),"cancel",pendingList.get(layoutPositin).getHours(),
                        pendingList.get(layoutPositin).getMinutes());
            }

            @Override
            public void onAddClick(int layoutPositin, View view) {


                count=Integer.parseInt(pendingList.get(layoutPositin).getMinutes());
                count++;

                pendingList.get(layoutPositin).setMinutes(String.valueOf(count));
                pendingList.get(layoutPositin).setHours(pendingList.get(layoutPositin).getHours());
                pendingList.get(layoutPositin).setValueTimer("0");
                homeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onSubtractClick(int layoutPositin, View view) {
                count=Integer.parseInt(pendingList.get(layoutPositin).getMinutes());
                count-=1;


                pendingList.get(layoutPositin).setMinutes(String.valueOf(count));
                pendingList.get(layoutPositin).setHours(pendingList.get(layoutPositin).getHours());
                pendingList.get(layoutPositin).setValueTimer("0");
                homeAdapter. notifyDataSetChanged();
            }
        });
    }

    int count;
    String current_lat = "", current_lng = "";

    public void callAction(String id,String status,String hours,String minutes)
    {
        try
        {
            JSONObject jo=new JSONObject();
            jo.put("id", id);
            jo.put("status",status);

            if (status.equalsIgnoreCase("accept"))
            {
                jo.put("hours",hours);
                jo.put("minutes",minutes);
            }

            Log.e("paramAction ",jo.toString());

            new RetrofitService(this, HomeScreen.this, URLHelper.MAKE_ACTION ,jo,
                    100, 2,"1").callService(true);

        }
        catch(Exception exx)
        {
            exx.printStackTrace();
        }
    }
    GoogleApiClient mGoogleApiClient;

    @Override
    public void onConnected(@Nullable Bundle bundle)
    {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED)
        {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location mLastLocation=LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);


        if (mLastLocation != null)
        {
            current_lat = ""+String.valueOf(mLastLocation.getLatitude());
            current_lng = ""+String.valueOf(mLastLocation.getLongitude());
            SharedHelper.Companion.putKey(this, "current_lat", current_lat);
            SharedHelper.Companion.putKey(this, "current_lng", current_lng);

            Log.e("HomeLattt ",current_lat);
            Log.e("HomeLnggg ",current_lng);
        }
    }

    protected void onStart()
    {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void showPermissionAlert(ArrayList<String> permissionList, int code)
    {
        Log.e( "showPermissionAlert: ","Popup" );
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            Log.e( "showPermissionAlert: ","Inside" );
            requestPermissions(permissionList.toArray(new String[permissionList.size()]), code);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        switch (requestCode)
        {

            case 2:

                for (int i = 0; i < permissions.length; i++)
                {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED)
                    {
                        // Toast.makeText(this, "Permitions Allow", Toast.LENGTH_SHORT).show();
                        //  getLocation();
                    } else if (grantResults[i] == PackageManager.PERMISSION_DENIED)
                    {
                        // checkPermissionLocation(context);
                        //   Toast.makeText(this, "Permitions Denied", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            default:
            {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }
    }
}
