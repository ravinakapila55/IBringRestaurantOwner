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
import com.ibring_restaurantowner.model.MenuItemModel;
import com.ibring_restaurantowner.model.MenuModel;
import com.ibring_restaurantowner.retrofit.RetrofitResponse;
import com.ibring_restaurantowner.retrofit.RetrofitService;
import com.ibring_restaurantowner.utils.SharedHelper;
import com.ibring_restaurantowner.utils.URLHelper;
import com.ibring_restaurantowner.views.Login;
import com.ibring_restaurantowner.views.home.adapter.MenuAdapter;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;

public class Menu extends AppCompatActivity implements View.OnClickListener, RetrofitResponse
{
    ImageView imageBack;
    TextView tvLabellll;
    RecyclerView recycler;
    ConstraintLayout cc_orders;
    ConstraintLayout cc_logout;
    ConstraintLayout cc_more,cc_menu;

    ArrayList<MenuModel> menuModelList=new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);
        findIds();
    }


    ImageView ivMenu;
    TextView tvMenu;
    public void findIds()
    {
        imageBack=(ImageView)findViewById(R.id.imageBack);
        tvLabellll=(TextView) findViewById(R.id.tvLabellll);
        recycler=(RecyclerView) findViewById(R.id.recycler);
        tvLabellll.setText("Menu");
        imageBack.setOnClickListener(this);

        cc_orders=(ConstraintLayout) findViewById(R.id.cc_orders);
        cc_logout=(ConstraintLayout) findViewById(R.id.cc_logout);
        cc_more=(ConstraintLayout) findViewById(R.id.cc_more);
        cc_menu=(ConstraintLayout) findViewById(R.id.cc_menu);

        ivMenu=(ImageView) findViewById(R.id.ivMenu);
        tvMenu=(TextView) findViewById(R.id.tvMenu);


        tvMenu.setTextColor(getResources().getColor(R.color.darkest_blue));
        ivMenu.setImageDrawable(getResources().getDrawable(R.drawable.menu_bottom));

        cc_logout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                SharedHelper.Companion.putKey(Menu.this, "current_status", "");
                SharedHelper.Companion.putKey(Menu.this, "loggedIn", "false");

//                SharedHelper.Companion.logout();

                Intent intent=new Intent(Menu.this, Login.class);
                startActivity(intent);
            }
        });

        cc_orders.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent=new Intent(Menu.this, HomeScreen.class);
                startActivity(intent);
            }
        });

        cc_menu.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent=new Intent(Menu.this, Menu.class);
                startActivity(intent);
            }
        });

        cc_more.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent=new Intent(Menu.this, AcceptedOrders.class);
                startActivity(intent);
            }
        });

        nameList.clear();
        nameList.add("Starters");
        nameList.add("Main Course");
        nameList.add("Deserts");

        callService();
//      setAdapter();
    }

    public void callService()
    {
        try
        {
            JSONObject jo=new JSONObject();

            String Id= SharedHelper.Companion.getKey(Menu.this,"id");
            Log.e("Id  ",Id);
//            jo.put("restaurant_id","5");
            jo.put("restaurant_id",Id);
            Log.e("AllStockParams ",jo.toString());

            new RetrofitService(this, Menu.this, URLHelper.ALL_STOCK_MENU ,jo,
                    900, 2,"1").callService(true);
        }
        catch(Exception exx)
        {
            exx.printStackTrace();
        }
    }

    ArrayList<String> nameList=new ArrayList<>();

    public void callDeleteItem(String menuId)
    {
        try
        {
            JSONObject jo=new JSONObject();
            jo.put("menu_id",menuId);

            Log.e("ParamssDelete ",jo.toString());

            new RetrofitService(this, Menu.this, URLHelper.DELETE_MENU_API ,jo,
                    1200, 2,"1").callService(true);

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public void setAdapter()
    {
        MenuAdapter menuAdapter=new MenuAdapter(this,menuModelList);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(menuAdapter);

        menuAdapter.onItemClickListener(new MenuAdapter.mainItemListener() {
            @Override
            public void onItemClick(int layoutPosition, View view, int subPosition) {


                Log.e("LayoutPosition ",layoutPosition+"");
                Log.e("subPosition ",subPosition+"");
                Log.e("SizeInside1 ",menuModelList.get(layoutPosition).getStarterItemList().size()+"");
                Log.e("InsideId ",menuModelList.get(layoutPosition).getStarterItemList().get(subPosition).getId()+"");



                if (layoutPosition==0)
                {
                    Log.e("SizeInside ",menuModelList.get(layoutPosition).getStarterItemList().size()+"");
                    callDeleteItem(menuModelList.get(layoutPosition).getStarterItemList().get(subPosition).getId());
                    menuModelList.get(layoutPosition).getStarterItemList().remove(subPosition);
                    menuAdapter.notifyDataSetChanged();

                }if (layoutPosition==1)
                {
                    menuModelList.get(layoutPosition).getMainCourdeItemList().remove(subPosition);
                    menuAdapter.notifyDataSetChanged();
                    callDeleteItem(menuModelList.get(layoutPosition).getMainCourdeItemList().get(subPosition).getId());
                }if (layoutPosition==2)
                {
                    menuModelList.get(layoutPosition).getDesertItemList().remove(subPosition);
                    menuAdapter.notifyDataSetChanged();
                    callDeleteItem(menuModelList.get(layoutPosition).getDesertItemList().get(subPosition).getId());
                }
            }
        });
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
    public void onPointerCaptureChanged(boolean hasCapture)
    {

    }

    @Override
    public void onResponse(int RequestCode, String response)
    {
        switch (RequestCode)
        {
            case 900:
            Log.e("AllMenuItems ",response);

            try {
                JSONObject jsonObject=new JSONObject(response);
                Log.e("JSONObject ",jsonObject.toString());

                if (jsonObject.getString("status").equalsIgnoreCase("success"))
                {
                    menuModelList.clear();
                    JSONObject result=jsonObject.getJSONObject("result");

                    JSONArray starter=result.getJSONArray("starter");
                    JSONArray dessert=result.getJSONArray("dessert");
                    JSONArray main_course=result.getJSONArray("main_course");

                    for (int p = 0; p <nameList.size() ; p++)
                    {
                        MenuModel menuModel=new MenuModel();


                        menuModel.setId(String.valueOf(p+1));
                        menuModel.setMenu_type(nameList.get(p));

                        if (p==0)
                        {
                            menuModel.setNoOfItems(String.valueOf(starter.length()));
                        }
                         if (p==1)
                        {
                            menuModel.setNoOfItems(String.valueOf(main_course.length()));
                        }
                         if (p==2)
                        {
                            menuModel.setNoOfItems(String.valueOf(dessert.length()));
                        }

                            if (starter.length()>0)
                            {
                                menuModel.setIsStarterFlag("1");
//                                menuModel.setNoOfItems(String.valueOf(starter.length()));

                                ArrayList<MenuItemModel> starterList=new ArrayList<>();
                                for (int i = 0; i <starter.length() ; i++)
                                {

                                    JSONObject startersObject=starter.getJSONObject(i);
                                    MenuItemModel menuItemModel=new MenuItemModel();

                                    menuItemModel.setId(startersObject.getString("id"));
                                    menuItemModel.setMenu_name(startersObject.getString("name"));
                                    menuItemModel.setItem_name(startersObject.getString("item_name"));
                                    menuItemModel.setImage(startersObject.getString("image"));
                                    menuItemModel.setMenu_type(startersObject.getString("menu_type"));
                                    menuItemModel.setPrice(startersObject.getString("price"));
                                    menuItemModel.setStock_out(startersObject.getString("stock_out"));
                                    menuItemModel.setRestaurant_id(startersObject.getString("restaurant_id"));

                                    starterList.add(menuItemModel);

                                    menuModel.setStarterItemList(starterList);
                                }

                            }
                            else
                            {
                                menuModel.setIsStarterFlag("0");
                            }

                            if (main_course.length()>0)
                            {
                                menuModel.setIsMainCourseFlag("1");
                                menuModel.setNoOfItems(String.valueOf(main_course.length()));

                                ArrayList<MenuItemModel> mainCourseList=new ArrayList<>();
                                for (int i = 0; i <main_course.length() ; i++)
                                {

                                    JSONObject mainCourseObj=main_course.getJSONObject(i);
                                    MenuItemModel menuItemModel=new MenuItemModel();

                                    menuItemModel.setId(mainCourseObj.getString("id"));
                                    menuItemModel.setMenu_name(mainCourseObj.getString("name"));
                                    menuItemModel.setItem_name(mainCourseObj.getString("item_name"));
                                    menuItemModel.setImage(mainCourseObj.getString("image"));
                                    menuItemModel.setMenu_type(mainCourseObj.getString("menu_type"));
                                    menuItemModel.setPrice(mainCourseObj.getString("price"));
                                    menuItemModel.setStock_out(mainCourseObj.getString("stock_out"));
                                    menuItemModel.setRestaurant_id(mainCourseObj.getString("restaurant_id"));

                                    mainCourseList.add(menuItemModel);

                                    menuModel.setMainCourdeItemList(mainCourseList);
                                }

                            }
                            else
                            {
                                menuModel.setIsMainCourseFlag("0");
                            }

                            if (dessert.length()>0)
                            {
                                menuModel.setIsDesertsFlag("1");
                                menuModel.setNoOfItems(String.valueOf(dessert.length()));



                                ArrayList<MenuItemModel> desertList=new ArrayList<>();
                                for (int i = 0; i <dessert.length() ; i++)
                                {

                                    JSONObject desertObj=dessert.getJSONObject(i);
                                    MenuItemModel menuItemModel=new MenuItemModel();

                                    menuItemModel.setId(desertObj.getString("id"));
                                    menuItemModel.setMenu_name(desertObj.getString("name"));
                                    menuItemModel.setItem_name(desertObj.getString("item_name"));
                                    menuItemModel.setImage(desertObj.getString("image"));
                                    menuItemModel.setMenu_type(desertObj.getString("menu_type"));
                                    menuItemModel.setPrice(desertObj.getString("price"));
                                    menuItemModel.setStock_out(desertObj.getString("stock_out"));
                                    menuItemModel.setRestaurant_id(desertObj.getString("restaurant_id"));

                                    desertList.add(menuItemModel);

                                    menuModel.setDesertItemList(desertList);
                                }

                            }
                            else
                            {
                                menuModel.setIsDesertsFlag("0");
                            }

                            menuModelList.add(menuModel);

                       Log.e("MainListSize ",menuModelList.size()+"");
                    }

                    Log.e("MainListSize Outside",menuModelList.size()+"");
                    if (menuModelList.size()>0)
                    {
                        setAdapter();
                    }







                }

            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
            break;

            case 1200:
            Log.e("DeleteItemsResponse ",response);
            try {
                JSONObject jsonObject=new JSONObject(response);
                Log.e("jsonObject ",jsonObject.toString());
                callService();
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
            break;
        }
    }
}
