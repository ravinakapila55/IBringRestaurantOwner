package com.ibring_restaurantowner.views.tutorial

import android.content.Context
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.ibring_restaurantowner.R
import com.ibring_restaurantowner.model.WelcomeModel
import java.util.ArrayList

 class WelcomeScreenAdapter(val context: Context,val list: ArrayList<WelcomeModel>): PagerAdapter()
{
//    private lateinit var inflater: LayoutInflater


    internal var array = arrayOf<Int>(R.drawable.first, R.drawable.second, R.drawable.third)
    private val inflater: LayoutInflater


    init {
        inflater = LayoutInflater.from(context)
    }



    override fun getCount(): Int {
        return list.size
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun instantiateItem(view: ViewGroup, position: Int): Any
    {
        val imageLayout = inflater.inflate(R.layout.custom_welcome, view, false)!!

        val slideimg = imageLayout!!.findViewById(R.id.ivSlide) as ImageView
        val tvTitle = imageLayout!!.findViewById(R.id.tvTitle) as TextView
        val tvDesc = imageLayout!!.findViewById(R.id.tvDesc) as TextView

        /* Picasso.get().load(Constant.SPLASHIMAGEURL + list.get(position).getImage())
                .placeholder(R.drawable.noimageplaceholder).
                into(slideimg);*/

        tvTitle.setText(list[position].getTitle())
        tvDesc.setText(list[position].getDescription())

        for (i in array.indices)
        {
            slideimg.setImageResource(array[position])
        }
        view.addView(imageLayout, 0);
        return imageLayout
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean
    {
        return view == `object`
    }

    override fun restoreState(state: Parcelable?, loader: ClassLoader?)
    {

    }

    override fun saveState(): Parcelable?
    {
        return null
    }

}