package com.ibring_restaurantowner.model;

import java.io.Serializable;
import java.util.ArrayList;

public class OrderList implements Serializable
{
    String id;
    String restaurant_id;
    String location;
    String latitude;
    String longitude;
    ArrayList<FoodItemModel> list;

    String user_name;

    String items;
    String price,hours,minutes;
    String order_id,status,valueTimer;
    String deliveryName,deliveryImage,deliveryId,deliveryVehicleNumber,deliveryVehicleName,deliveryPhone;

    public ArrayList<FoodItemModel> getList()
    {
        return list;
    }

    public void setList(ArrayList<FoodItemModel> list)
    {
        this.list = list;
    }

    public String getItems()
    {
        return items;
    }

    public void setItems(String items)
    {
        this.items = items;
    }

    public String getDeliveryPhone()
    {
        return deliveryPhone;
    }

    public void setDeliveryPhone(String deliveryPhone)
    {
        this.deliveryPhone = deliveryPhone;
    }

    public String getDeliveryName()
    {
        return deliveryName;
    }

    public void setDeliveryName(String deliveryName) {
        this.deliveryName = deliveryName;
    }

    public String getDeliveryImage() {
        return deliveryImage;
    }

    public void setDeliveryImage(String deliveryImage) {
        this.deliveryImage = deliveryImage;
    }

    public String getDeliveryId() {
        return deliveryId;
    }

    public void setDeliveryId(String deliveryId) {
        this.deliveryId = deliveryId;
    }

    public String getDeliveryVehicleNumber() {
        return deliveryVehicleNumber;
    }

    public void setDeliveryVehicleNumber(String deliveryVehicleNumber) {
        this.deliveryVehicleNumber = deliveryVehicleNumber;
    }

    public String getDeliveryVehicleName() {
        return deliveryVehicleName;
    }

    public void setDeliveryVehicleName(String deliveryVehicleName) {
        this.deliveryVehicleName = deliveryVehicleName;
    }

    public String getValueTimer() {
        return valueTimer;
    }

    public void setValueTimer(String valueTimer) {
        this.valueTimer = valueTimer;
    }

    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }

    public String getMinutes() {
        return minutes;
    }

    public void setMinutes(String minutes) {
        this.minutes = minutes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRestaurant_id() {
        return restaurant_id;
    }

    public void setRestaurant_id(String restaurant_id) {
        this.restaurant_id = restaurant_id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

}
