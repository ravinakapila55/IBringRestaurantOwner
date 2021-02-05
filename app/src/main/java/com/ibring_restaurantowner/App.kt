package com.ibring_restaurantowner

import android.app.Activity
import android.app.Application
import android.content.Context
import android.util.Log
import androidx.multidex.MultiDex
import com.ibring_restaurantowner.fonts.FontsOverride
import org.json.JSONException
import org.json.JSONObject
import android.content.SharedPreferences


public class App:Application()
{
    private var mInstance: App? = null
    private var mActivity: Activity? = null

    override fun attachBaseContext(base: Context)
    {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate()
    {
        super.onCreate()
        mInstance = this
        context =getApplicationContext();

//      initCalligraphyConfig()

        FontsOverride.setDefaultFont(this, "DEFAULT", "ClanPro-News.otf")
        FontsOverride.setDefaultFont(this, "MONOSPACE", "ClanPro-News.otf")
        FontsOverride.setDefaultFont(this, "SERIF", "ClanPro-News.otf")
        FontsOverride.setDefaultFont(this, "SANS_SERIF", "ClanPro-News.otf")

    }

   /* private fun initCalligraphyConfig()
    {
        CalligraphyConfig.initDefault(CalligraphyConfig.Builder().
        setDefaultFontPath(resources.getString(R.string.bariol)).setFontAttrId(R.attr.fontPath)----------
                .build())
    }*/

    companion object
    {
        private var context:Context?=null

        fun getContext(): Context?
        {
            return context
        }

        fun getGlobalPrefs(): SharedPreferences
        {
            return getContext()!!.getSharedPreferences("Restaurant", 0)
        }

    }


    fun setActivity(act: Activity)
    {
        mActivity = act
    }


    fun trimMessage(json: String): String?
    {
        var trimmedString = ""

        try
        {
            val jsonObject = JSONObject(json)
            val iter = jsonObject.keys()
            while (iter.hasNext())
            {
                val key = iter.next()
                try {
                    val value = jsonObject.getJSONArray(key)
                    var i = 0
                    val size = value.length()
                    while (i < size) {
                        Log.e("Errors in Form", "" + value.getString(i))
                        trimmedString += value.getString(i)
                        if (i < size - 1) {
                            trimmedString += '\n'.toString()
                        }
                        i++
                    }
                } catch (e: JSONException)
                {
                    trimmedString += jsonObject.optString(key)
                }
            }
        }
        catch (e: JSONException)
        {
            e.printStackTrace()
            return null
        }

        Log.e("Trimmed ", "" + trimmedString)
        return trimmedString
    }


}