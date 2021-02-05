package com.ibring_restaurantowner.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public   class  URLHelper
{
    public static final String BASE_URL="http://178.128.116.149/ibring/public";
    public static final String BASE_URL1="http://178.128.116.149/ibring/public/";
    public static final String BASE_URL_CHAT="http://178.128.116.149:7003/";
    public static final int    CLIENT_ID = 5;

    public static final String  CLIENT_SECRET_KEY = "gehZMcyHIkaSuxQado1EzmGyDSAi3NqzHwEqTp9N";
    public static final String  APP_LINK = "https://play.google.com/store/apps/details?id=com.taxicabs_sp.provider";

   //Help
    public static final String HELP_REDIRECT_URL = BASE_URL + "";
    public final static String PLACES_API="AIzaSyAK5It4p1CiJ2gFzWRbfs24Cibo2QTcPRU";

   // Image load options URL
  //  public static final String BASE_IMAGE_LOAD_URL_WITH_STORAGE = BASE_URL +"/storage/";
    public static final String BASE_IMAGE_LOAD_URL_WITH_STORAGE ="http://178.128.116.149/ibring/storage/app/public/";

// http://178.128.116.149/ibring/storage/app/public/provider//profile//e7cc26b093b3941f3fcb651062d8e38a.jpeg

// http://178.128.116.149/ibring/api/provider/status-order

//status
//prepared or pickup or delivered
//id
//Order id column

//  public static final String CHAT_SERVER_URL ="http://178.128.116.149:7003";
//  public static final String CHAT_SERVER_URL = "http://192.168.1.49:7003" ;
    public static final String CHAT_SERVER_URL ="http://13.235.235.100:8090";

     //  public final static String SOCKET_URL = "http://165.22.215.99:9002/";
    //  public final static String SOCKET_URL_LIVE_TRACK="http://192.168.1.49:9002/";
   //  public final static String SOCKET_URL_LIVE_TRACK="http://68.183.91.242:9002/";

    //http://178.128.116.149/ibring/public/api/provider/reset/password
    // {"password":"12345678@Rk","password_confirmation":"12345678@Rk","id":"171"}
    //{"password":"12345678@Rk","password_confirmation":"12345678@Rk","id":"96"}

    //http://178.128.116.149/ibring/public

    public final static String SOCKET_URL_LIVE_TRACK="http://13.235.235.100:9002/";

    // WEB API LIST
    public static final String LOGIN = BASE_URL + "/api/provider/oauth/token";
    public static final String REGISTER = BASE_URL + "/api/provider/register";
    public static final String PROVIDER_PROFILE = BASE_URL + "/api/provider/profile";
    public static final String ORDER_LIST = BASE_URL + "/api/provider/order-list";
    public static final String MAKE_ACTION = BASE_URL + "/api/provider/accept-reject-order";
    public static final String CHANGE_STATUS = BASE_URL + "/api/provider/status-order";
    public static final String NEARBY_DRIVERS = BASE_URL + "/api/provider/get-delivery-boy";
    public static final String ALL_STOCK_MENU = BASE_URL + "/api/provider/in-stock";
    public static final String DELETE_MENU_API = BASE_URL + "/api/provider/delete-menu";
    public static final String CHAT_TIME_FORMAT ="dd-mm-yyyy HH:mm a";
    public static final String RESET_PASSWORD = BASE_URL + "/api/provider/reset/password";
    public static final String FORGET_PASSWORD = BASE_URL + "/api/provider/forgot/password";

    public static String getFormatedTime(String dateStr, String strReadFormat, String strWriteFormat)
    {
        try
        {
            String formattedDate = dateStr;
            DateFormat readFormat = new SimpleDateFormat(strReadFormat);
            readFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            DateFormat writeFormat = new SimpleDateFormat(strWriteFormat);
            writeFormat.setTimeZone(TimeZone.getDefault());
            Date date = null;

            try
            {
                date = readFormat.parse(dateStr);
            }
            catch (ParseException e)
            {
                e.printStackTrace();
            }

            if (date != null)
            {
                formattedDate = writeFormat.format(date);
            }

            return formattedDate;
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            return dateStr;
        }
    }

    public static String getFormatedDate(String dateStr, String strReadFormat, String strWriteFormat)
    {
        String formattedDate = dateStr;

        DateFormat readFormat = new SimpleDateFormat(strReadFormat);
        DateFormat writeFormat = new SimpleDateFormat(strWriteFormat);

        Date date = null;

        try
        {
            date = readFormat.parse(dateStr);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }

        if (date != null)
        {
            formattedDate = writeFormat.format(date);
        }

        return formattedDate;
    }
}
