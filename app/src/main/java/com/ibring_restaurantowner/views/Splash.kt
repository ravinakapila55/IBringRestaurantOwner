package com.ibring_restaurantowner.views

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.ibring_restaurantowner.R
import com.ibring_restaurantowner.utils.SharedHelper
import com.ibring_restaurantowner.views.home.HomeScreen
import com.ibring_restaurantowner.views.tutorial.WelcomeScreen

 class Splash:AppCompatActivity()
{

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash)
        replaceActivity()
    }

    fun replaceActivity()
    {
        Handler().postDelayed({if (SharedHelper.getKey(this, "loggedIn").equals("true"))
                {
                    val intent = Intent(this, HomeScreen::class.java)
                    startActivity(intent)
                    finish()
                }
                else
                {
                    val mainIntent = Intent(this, WelcomeScreen::class.java)
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(mainIntent)
                    finish()
                }}, 3000)
    }


}