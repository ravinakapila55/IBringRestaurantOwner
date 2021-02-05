package com.ibring_restaurantowner.model;

import java.io.Serializable;
import java.util.ArrayList;

public class MenuModel implements Serializable
{
    String menu_type,id,isStarterFlag,isMainCourseFlag,isDesertsFlag,noOfItems;

    public String getNoOfItems() {
        return noOfItems;
    }

    public void setNoOfItems(String noOfItems) {
        this.noOfItems = noOfItems;
    }

    public String getIsStarterFlag()
    {
        return isStarterFlag;
    }

    public void setIsStarterFlag(String isStarterFlag) {
        this.isStarterFlag = isStarterFlag;
    }

    public String getIsMainCourseFlag() {
        return isMainCourseFlag;
    }

    public void setIsMainCourseFlag(String isMainCourseFlag)
    {
        this.isMainCourseFlag = isMainCourseFlag;
    }

    public String getIsDesertsFlag() {
        return isDesertsFlag;
    }

    public void setIsDesertsFlag(String isDesertsFlag) {
        this.isDesertsFlag = isDesertsFlag;
    }

    ArrayList<MenuItemModel> starterItemList;
    ArrayList<MenuItemModel> desertItemList;
    ArrayList<MenuItemModel> MainCourdeItemList;

    public ArrayList<MenuItemModel> getStarterItemList() {
        return starterItemList;
    }

    public void setStarterItemList(ArrayList<MenuItemModel> starterItemList) {
        this.starterItemList = starterItemList;
    }

    public ArrayList<MenuItemModel> getDesertItemList()
    {
        return desertItemList;
    }

    public void setDesertItemList(ArrayList<MenuItemModel> desertItemList) {
        this.desertItemList = desertItemList;
    }

    public ArrayList<MenuItemModel> getMainCourdeItemList() {
        return MainCourdeItemList;
    }

    public void setMainCourdeItemList(ArrayList<MenuItemModel> mainCourdeItemList) {
        MainCourdeItemList = mainCourdeItemList;
    }

    public String getMenu_type() {
        return menu_type;
    }

    public void setMenu_type(String menu_type) {
        this.menu_type = menu_type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }


}
