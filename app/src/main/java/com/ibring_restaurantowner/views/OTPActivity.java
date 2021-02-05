package com.ibring_restaurantowner.views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;


import com.google.android.material.snackbar.Snackbar;
import com.ibring_restaurantowner.R;
import com.ibring_restaurantowner.retrofit.RetrofitResponse;
import com.ibring_restaurantowner.retrofit.RetrofitService;
import com.ibring_restaurantowner.utils.SharedHelper;
import com.ibring_restaurantowner.utils.URLHelper;


import org.json.JSONException;
import org.json.JSONObject;




public class OTPActivity extends Activity implements RetrofitResponse
{

    EditText txtOTP;
    TextView lblClickToResend;
    Button btnSendOTP;
    ImageView imgBack;
    Context context;
    String strOTP = "";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.otp_screen);
        context = this;
        init();
    }

    private void init()
    {
        txtOTP = (EditText) findViewById(R.id.txtOTP);
        lblClickToResend = (TextView) findViewById(R.id.lblClickToResend);
        btnSendOTP = (Button) findViewById(R.id.btnSendOTP);
        imgBack = (ImageView) findViewById(R.id.imgBack);

        txtOTP.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

            }

            @Override
            public void afterTextChanged(Editable s)
            {

            }
        });

        btnSendOTP.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.e("", "onClick: otp " + SharedHelper.Companion.getKey(context, "otp"));
                if (txtOTP.getText().toString().equalsIgnoreCase(""))
                {
                    Toast.makeText(context, "Please enter OTP!", Toast.LENGTH_SHORT).show();
                }
                else if (txtOTP.getText().toString().length()<6)
                {
                    Toast.makeText(context, "Please enter valid OTP", Toast.LENGTH_SHORT).show();
                }

                else if (!txtOTP.getText().toString().equalsIgnoreCase(SharedHelper.Companion.getKey(context, "otp")))
                {
                    Toast.makeText(context, "Your OTP is incorrect!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Intent resetIntent = new Intent(context, ChangePassword.class);
                    startActivity(resetIntent);
                    finish();
                }
            }
        });

        lblClickToResend.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                callOTp();
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });
    }

    public void callOTp()
    {
        {
            try
            {
                JSONObject jo=new JSONObject();

                jo.put("email", SharedHelper.Companion.getKey(context, "email"));
                Log.e("paramAction ",jo.toString());

                new RetrofitService(this, OTPActivity.this, URLHelper.FORGET_PASSWORD ,jo,
                        100, 2,"1").callService(true);

            }
            catch(Exception exx)
            {
                exx.printStackTrace();
            }
        }
    }


    public void displayMessage(String toastString)
    {
        Snackbar snackbar = Snackbar.make(getCurrentFocus(), toastString, Snackbar.LENGTH_SHORT);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(ContextCompat.getColor(this, R.color.black));
        /*TextView textView = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(ContextCompat.getColor(this, R.color.white));*/
        snackbar.show();
    }

    @Override
    public void onResponse(int RequestCode, String response)
    {
        switch (RequestCode)
        {
            case 100:

                try {
                    JSONObject jsonObject=new JSONObject(response);

                    JSONObject userObj = jsonObject.optJSONObject("provider");

                    SharedHelper.Companion.putKey(context, "otp", ""+userObj.optString("otp"));
                }catch (Exception ex)
                {
                    ex.printStackTrace();
                }

                break;
        }
    }
}
