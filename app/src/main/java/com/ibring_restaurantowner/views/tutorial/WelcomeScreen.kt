package com.ibring_restaurantowner.views.tutorial

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.ibring_restaurantowner.R
import com.ibring_restaurantowner.model.WelcomeModel
import com.ibring_restaurantowner.views.Login
import kotlinx.android.synthetic.main.welcome_screen.*
import java.util.ArrayList

class WelcomeScreen:AppCompatActivity(),View.OnClickListener
{
    override fun onClick(v: View?) {
        when (v!!.getId()) {
            R.id.tvNext -> {

                Log.e("CountNext ", count.toString() + "")


                if (tvNext.text.toString().equals("Get Started", ignoreCase = true)) {
                    val intent = Intent(this, Login::class.java)
                    startActivity(intent)
                } else {
                    viewpager.arrowScroll(View.FOCUS_RIGHT)
                    count = count + 1
                }
            }

            R.id.tvSkip -> {

                Log.e("CountSkip ", count.toString() + "")

                if (tvSkip.text.toString().trim { it <= ' ' }.equals("Skip", ignoreCase = true)) {
                    val intent = Intent(this, Login::class.java)
                    startActivity(intent)
                } else {
                    viewpager.arrowScroll(View.FOCUS_LEFT)
                    count = count - 1
                }
            }
        }/*  if (count==2)
                {
                 //next screen
                    Intent intent=new Intent(this, SignIn.class);
                    startActivity(intent);
                }
                else
                {
                    viewpager.arrowScroll(View.FOCUS_RIGHT);
                    count=count+1;
                }*//*  if (count==2 || count==1)
                {
                    viewpager.arrowScroll(View.FOCUS_LEFT);
                    count=count-1;
                }
                else
                {
                    Intent intent=new Intent(this, SignIn.class);
                    startActivity(intent);
                    //next screen
                }*/
    }

    internal var count = 0


    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.welcome_screen)
//        ButterKnife.bind(this)

        tvNext.setOnClickListener(this)
        tvSkip.setOnClickListener(this)
        setadapter()

    }
    internal var list = ArrayList<WelcomeModel>()
    fun setData() {
        list.clear()

        var sliderModel = WelcomeModel(
            "0", "iBring Courier",
            "Offer Courier services to daily users."
        )
        list.add(sliderModel)

        sliderModel = WelcomeModel(
            "1", "iBring Transport/Taxi",
            "Offer Transport/taxi services to daily users."
        )
        list.add(sliderModel)

        sliderModel = WelcomeModel(
            "2", "iBring Food",
            "Offer Food services to daily users."
        )
        list.add(sliderModel)

    }

    private fun setadapter() {
        setData()

        val welcomeScreenAdapter = WelcomeScreenAdapter(this, list)
        viewpager!!.setAdapter(welcomeScreenAdapter)

        dots!!.attachViewPager(viewpager)

        viewpager!!.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                Log.e("PositionViewPager ", positionOffset.toString() + "")
                Log.e("position ", position.toString() + "")
            }

            override fun onPageSelected(position: Int) {


                if (position == list.size - 1) {
                    tvSkip!!.setVisibility(View.VISIBLE)
                    tvSkip!!.setText("Back")
                    tvNext!!.setText("Get Started")

                } else {

                    if (position == list.size - 2) {
                        tvSkip!!.setText("Back")

                    } else {

                        tvSkip!!.setText("Skip")
                    }
                    tvSkip!!.setVisibility(View.VISIBLE)
                    tvNext!!.setText("Next")

                }

            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })


    }

}