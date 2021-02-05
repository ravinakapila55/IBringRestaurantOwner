package com.ibring_restaurantowner.fonts;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatEditText;

/**
 * Created by Freeware Sys on 3/27/2017.
 */

public class ClanProEditText extends AppCompatEditText {


    public ClanProEditText(Context context, AttributeSet attrs) {
        super(context, attrs);

        if(!isInEditMode())
            applyFont(context);
    }

    private void applyFont(Context context){
        setTypeface(Typefaces.get(context, "ClanPro-NarrNews.otf"));
    }


}
