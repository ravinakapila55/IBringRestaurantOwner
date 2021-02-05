package com.ibring_restaurantowner.views;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;
import com.ibring_restaurantowner.R;
import com.ibring_restaurantowner.retrofit.RetrofitResponse;
import com.ibring_restaurantowner.retrofit.RetrofitService;
import com.ibring_restaurantowner.utils.SharedHelper;
import com.ibring_restaurantowner.utils.URLHelper;
import com.ibring_restaurantowner.views.home.NearbyDrivers;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ForgetPassword extends AppCompatActivity implements RetrofitResponse {

    ImageView backArrow;
    EditText email;
    Button nextICON;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
            getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        }
        setContentView(R.layout.activity_forget_password);
        findViewById();

        if (Build.VERSION.SDK_INT > 15) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        nextICON.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if (email.getText().toString().equals("") )
                {
                    Toast.makeText(ForgetPassword.this, "Please enter email address", Toast.LENGTH_SHORT).show();
                }
                else if((!isValidEmail(email.getText().toString())))
                {
                    Toast.makeText(ForgetPassword.this,"Please enter valid email address", Toast.LENGTH_SHORT).show();
//                    displayMessage(getString(R.string.not_valid_email));
                }
                else
                {
//                    forgetPassword();
                    callForget();
                }
            }
        });

        backArrow.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Intent mainIntent = new Intent(ForgetPassword.this, Login.class);
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(mainIntent);
                ForgetPassword.this.finish();
            }
        });

    }



    public void findViewById() {
        email = (EditText) findViewById(R.id.email);
        nextICON = (Button) findViewById(R.id.nextIcon);
        backArrow = (ImageView) findViewById(R.id.backArrow);
    }


    public void callForget()
    {
        try
        {
            JSONObject jo=new JSONObject();

            jo.put("email", email.getText().toString());
            Log.e("paramAction ",jo.toString());

            new RetrofitService(this, ForgetPassword.this, URLHelper.FORGET_PASSWORD ,jo,
                    100, 2,"1").callService(true);

        }
        catch(Exception exx)
        {
            exx.printStackTrace();
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        Intent mainIntent = new Intent(ForgetPassword.this, Login.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(mainIntent);
        ForgetPassword.this.finish();
    }

    private boolean isValidEmail(String email)
    {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    @Override
    public void onResponse(int RequestCode, String response) {

        switch (RequestCode)
        {
            case 100:

                try {
                    JSONObject jsonObject=new JSONObject(response);
                    //{"status":"error","message":"The selected email is invalid."}
                    if (jsonObject.has("status"))
                    {
                        if (jsonObject.getString("status").equalsIgnoreCase("error"))
                        {
                            Toast.makeText(this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        JSONObject userObj = jsonObject.getJSONObject("provider");
                        SharedHelper.Companion.putKey(ForgetPassword.this, "reset_id", "" + userObj.optInt("id"));
                        SharedHelper.Companion.putKey(ForgetPassword.this, "otp", "" + userObj.optInt("otp"));

                        Intent resetIntent = new Intent(ForgetPassword.this, OTPActivity.class);
                        startActivity(resetIntent);
                        finish();
                    }


                }catch (Exception ex)
                {
                    ex.printStackTrace();
                }

                break;
        }

    }
}
