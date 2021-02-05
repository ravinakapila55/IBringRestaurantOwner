package com.ibring_restaurantowner.views.home;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.ibring_restaurantowner.App;
import com.ibring_restaurantowner.R;
import com.ibring_restaurantowner.model.NearbyDriversModel;
import com.ibring_restaurantowner.model.OrderList;
import com.ibring_restaurantowner.retrofit.RetrofitResponse;
import com.ibring_restaurantowner.retrofit.RetrofitService;
import com.ibring_restaurantowner.utils.SharedHelper;
import com.ibring_restaurantowner.utils.URLHelper;
import com.ibring_restaurantowner.views.home.adapter.HomeAdapter;
import com.ibring_restaurantowner.views.home.adapter.NearbyDriversAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class NearbyDrivers extends AppCompatActivity implements RetrofitResponse, View.OnClickListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    ImageView imageBack;
    OrderList orderList;
    RecyclerView recycler;
    TextView tvNoData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nearby_drivers);

        if (mGoogleApiClient == null)
        {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        if (getIntent().hasExtra("list"))
        {
            orderList=(OrderList)getIntent().getSerializableExtra("list");
            Log.e("Idd ",orderList.getId());
        }
        findIds();
    }

    public void findIds()
    {
        imageBack=(ImageView)findViewById(R.id.imageBack);
        recycler=(RecyclerView) findViewById(R.id.recycler);
        tvNoData=(TextView) findViewById(R.id.tvNoData);
        imageBack.setOnClickListener(this);


    }

    ArrayList<NearbyDriversModel> list=new ArrayList<>();
    public void setAdapter()
    {
        recycler.setLayoutManager(new LinearLayoutManager(this));
        NearbyDriversAdapter nearbyDriversAdapter=new NearbyDriversAdapter(list,this);
        recycler.setAdapter(nearbyDriversAdapter);


        nearbyDriversAdapter.onItesmSelectedListener(new NearbyDriversAdapter.onClickListner() {
            @Override
            public void onAssignCLick(int layoutPosition, View view) {
                callAssignJob(list.get(layoutPosition).getId());
            }
        });

    }


    public void callAssignJob(String deliveryBodId)
    {
        try
        {
            JSONObject jo=new JSONObject();

            jo.put("id", orderList.getId());
            jo.put("status","accept");
            jo.put("hours",orderList.getHours());
            jo.put("minutes",orderList.getMinutes());
            jo.put("driver_id",deliveryBodId);

            Log.e("paramAction ",jo.toString());

            new RetrofitService(this, NearbyDrivers.this, URLHelper.MAKE_ACTION ,jo,
                    100, 2,"1").callService(true);

        }
        catch(Exception exx)
        {
            exx.printStackTrace();
        }
    }

    public void callNearByDrivers()
    {
        try
        {
            JSONObject jo=new JSONObject();

//            jo.put("longitude","75.7560");
//            jo.put("latitude","31.7412");

            jo.put("latitude", SharedHelper.Companion.getKey(this,"current_lat"));
            jo.put("longitude", SharedHelper.Companion.getKey(this,"current_lng"));
            jo.put("service_id","18");
            jo.put("order_id", orderList.getOrder_id());

            Log.e("ParamActionsnNearBy ",jo.toString());

            new RetrofitService(this, NearbyDrivers.this, URLHelper.NEARBY_DRIVERS ,jo,
                    900, 2,"1").callService(true);
        }
        catch(Exception exx)
        {
            exx.printStackTrace();
        }
    }

    @Override
    public void onResponse(int RequestCode, String response)
    {
        switch (RequestCode)
        {
            case 900:

                Log.e("NearByDriverList ",response);

                try {
                    JSONObject jsonObject=new JSONObject(response);
                    if (jsonObject.getString("status").equalsIgnoreCase("success"))
                    {
                        list.clear();
                        recycler.setVisibility(View.VISIBLE);
                        tvNoData.setVisibility(View.GONE);
                        JSONArray result=jsonObject.getJSONArray("result");

                        if (result.length()>0)
                        {
                            recycler.setVisibility(View.VISIBLE);
                            tvNoData.setVisibility(View.GONE);
                            for (int i = 0; i <result.length() ; i++) {
                                NearbyDriversModel nearbyDriversModel=new NearbyDriversModel();
                                JSONObject object=result.getJSONObject(i);

                                nearbyDriversModel.setId(object.getString("id"));
                                nearbyDriversModel.setName(object.getString("first_name")+" "+object.getString("last_name"));
                                nearbyDriversModel.setEmail(object.getString("email"));
                                nearbyDriversModel.setMobile(object.getString("mobile"));
                                nearbyDriversModel.setImage(object.getString("avatar"));
                                nearbyDriversModel.setDescription(object.getString("description"));
                                nearbyDriversModel.setRating(object.getString("rating"));

                                list.add(nearbyDriversModel);
                            }

                            if (list.size()>0)
                            {
                                setAdapter();
                            }
                        }
                        else {
                            recycler.setVisibility(View.GONE);
                            tvNoData.setVisibility(View.VISIBLE);
                        }
                    }
                    else {
                        recycler.setVisibility(View.GONE);
                        tvNoData.setVisibility(View.VISIBLE);
                    }
                }catch (Exception ex)
                {
                    ex.printStackTrace();
                }

                break;

            case 100:
                Log.e("AssignDriverResponse ",response);
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    Log.e("jsonObject ",jsonObject.toString());

                    Toast.makeText(this, "Driver Assigned Succesfully", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(NearbyDrivers.this, HomeScreen.class);
                    startActivity(intent);

//                    onBackPressed();

                  /*  if (jsonObject.getString("status").equalsIgnoreCase("cancel"))
                    {
//                        callService();
                    }
                    else
                        {
                        Intent intent=new Intent(NearbyDrivers.this, AcceptedOrders.class);
                        startActivity(intent);
                    }*/
                }catch (Exception ex)
                {
                    ex.printStackTrace();
                }

                break;
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
        Location mLastLocation= LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);


        if (mLastLocation != null)
        {
            current_lat = ""+String.valueOf(mLastLocation.getLatitude());
            current_lng = ""+String.valueOf(mLastLocation.getLongitude());
            SharedHelper.Companion.putKey(this, "current_lat", current_lat);
            SharedHelper.Companion.putKey(this, "current_lng", current_lng);

            Log.e("HomeLattt ",current_lat);
            Log.e("HomeLnggg ",current_lng);
        }

        callNearByDrivers();
    }

    String current_lat = "", current_lng = "";

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
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.imageBack:
                onBackPressed();
                break;
        }

    }



    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
