package com.ibring_restaurantowner.utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.ibring_restaurantowner.App
import android.R.id.edit






 class SharedHelper{



    val PREFS_NAME = "JustDail"


     companion object {
         fun a() : Int = 1

         lateinit var sharedPreferences: SharedPreferences
         lateinit var editor: SharedPreferences.Editor

         private var instance: SharedHelper? = null

         fun  getKey(contextGetKey: Context, Key: String): String
         {
             var  value:String
             sharedPreferences = contextGetKey.getSharedPreferences("Cache", Context.MODE_PRIVATE)
             value= sharedPreferences.getString(Key, "").toString()
             return value
         }

         fun putKey(context: Context, Key: String, Value: String)
         {
             sharedPreferences = context.getSharedPreferences("Cache", Context.MODE_PRIVATE)
             editor = sharedPreferences.edit()
             editor.putString(Key, Value)
             editor.commit()
         }

         fun saveBoolean(context: Context, Key: String, value: Boolean?)
         {
             val editor = sharedPreferences.edit()
             editor.putBoolean(Key, value!!)
             editor.commit()
         }

         fun getBoolean(context: Context, Key: String, `val`: Boolean?): Boolean
         {
             sharedPreferences = context.getSharedPreferences("Cache", Context.MODE_PRIVATE)
             return sharedPreferences.getBoolean(Key, `val`!!)
         }

         fun logout()
         {
             App.getGlobalPrefs().edit().clear().commit()
             instance = null
         }
     }




 }