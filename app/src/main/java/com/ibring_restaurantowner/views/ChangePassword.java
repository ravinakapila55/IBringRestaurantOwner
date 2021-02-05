package com.ibring_restaurantowner.views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
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


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChangePassword extends AppCompatActivity implements RetrofitResponse {
    String TAG = "ChangePasswordActivity";
    public Context context = ChangePassword.this;
    public Activity activity = ChangePassword.this;
    Boolean isInternet;
    Button changePasswordBtn;
    ImageView backArrow;
    EditText new_password, confirm_new_password;

    private static final String PASSWORD_PATTERN = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%*-_]).{6,20})";
    private Pattern pattern;
    private Matcher matcher;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
            getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        }
        setContentView(R.layout.change_password);
        findViewByIdandInitialization();

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        changePasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String new_password_value = new_password.getText().toString();
                String confirm_password_value = confirm_new_password.getText().toString();

                pattern = Pattern.compile(PASSWORD_PATTERN);
                matcher=pattern.matcher(new_password.getText().toString().trim());
                Log.e("matcher",""+matcher+"");


                if (new_password_value == null || new_password_value.equalsIgnoreCase("")) {
                    Toast.makeText(ChangePassword.this, "Please enter password", Toast.LENGTH_SHORT).show();
                }

                else if (!matcher.matches() || new_password.getText().toString().length() < 6)
                {
                    Toast.makeText(ChangePassword.this,
                            "Please Enter atleast one small letter, one capital letter," +
                                    "one digit and one special character with minimum length of 6 characters",
                            Toast.LENGTH_SHORT).show();

                }
                else if (confirm_password_value == null || confirm_password_value.equalsIgnoreCase(""))
                {
                    Toast.makeText(ChangePassword.this, "Enter Confirm Password", Toast.LENGTH_SHORT).show();
                }
                else if (!new_password_value.equals(confirm_password_value))
                {
                    Toast.makeText(ChangePassword.this, "Password does not match", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    resetPassword(new_password_value,confirm_password_value);
                }
            }
        });
    }



    public void findViewByIdandInitialization()
    {
        new_password = (EditText) findViewById(R.id.new_password);
        confirm_new_password = (EditText) findViewById(R.id.confirm_password);
        changePasswordBtn = (Button) findViewById(R.id.changePasswordBtn);
        backArrow = (ImageView) findViewById(R.id.imgBack);

        final ImageView ivNewPswd = (ImageView) findViewById(R.id.ivNewPswd);
        final ImageView ivConfirmPswd = (ImageView) findViewById(R.id.ivConfirmPswd);


        new_password.setCustomSelectionActionModeCallback(new ActionMode.Callback() {

            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            public void onDestroyActionMode(ActionMode mode) {
            }

            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }
        });
        confirm_new_password.setCustomSelectionActionModeCallback(new ActionMode.Callback() {

            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            public void onDestroyActionMode(ActionMode mode) {
            }

            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }
        });


        ivNewPswd.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                if (new_password.getTransformationMethod().equals(PasswordTransformationMethod.getInstance()))
                {
                    ivNewPswd.setImageResource(R.drawable.eye_hide);
                    new_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    new_password.setSelection(new_password.length());
                }
                else
                {
                    ivNewPswd.setImageResource(R.drawable.eye_visible);
                    new_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    new_password.setSelection(new_password.length());
                }
            }
        });

        ivConfirmPswd.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (confirm_new_password.getTransformationMethod().equals(PasswordTransformationMethod.getInstance()))
                {
                    ivConfirmPswd.setImageResource(R.drawable.eye_hide);
                    confirm_new_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    confirm_new_password.setSelection(confirm_new_password.length());
                }
                else {
                    ivConfirmPswd.setImageResource(R.drawable.eye_visible);
                    confirm_new_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    confirm_new_password.setSelection(confirm_new_password.length());
                }
            }
        });

    }

    public void resetPassword(String new_pass, String confirm_new_pass)
    {
        JSONObject object = new JSONObject();
        try {
            object.put("password", new_pass);
            object.put("password_confirmation", confirm_new_pass);
            object.put("id", SharedHelper.Companion.getKey(context, "reset_id"));
            Log.e("ChangePasswordAPI", "" + object);

            Log.e("paramAction ",object.toString());

            new RetrofitService(this, ChangePassword.this, URLHelper.RESET_PASSWORD ,object,
                    100, 2,"1").callService(true);

        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

    }


/*    private void resetPassword(String new_pass, String confirm_new_pass)
    {

        customDialog = new CustomDialog(context);
        customDialog.setCancelable(false);
        customDialog.show();
        JSONObject object = new JSONObject();
        try {
            object.put("password", new_pass);
            object.put("password_confirmation", confirm_new_pass);
            object.put("id", SharedHelper.getKey(context, "reset_id"));
            Log.e("ChangePasswordAPI", "" + object);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URLHelper.RESET_PASSWORD,
                object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                customDialog.dismiss();
                Log.v("SignInResponse", response.toString());
                displayMessage(response.optString("message"));
                Toast.makeText(ChangePassword.this, response.optString("message"), Toast.LENGTH_SHORT).show();
                GoToBeginActivity();
                SharedHelper.putKey(context, "otp", "");
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                customDialog.dismiss();
                String json = null;
                String Message;
                NetworkResponse response = error.networkResponse;
                if (response != null && response.data != null) {
                    try {
                        JSONObject errorObj = new JSONObject(new String(response.data));
                        Log.e("ErrorChangePasswordAPI", "" + errorObj.toString());

                        if (response.statusCode == 400 || response.statusCode == 405 || response.statusCode == 500) {
                            try {
                                displayMessage(errorObj.optString("error"));
                            } catch (Exception e) {
                                displayMessage(getString(R.string.something_went_wrong));
                            }
                        } else if (response.statusCode == 401) {
                            GoToBeginActivity();
                        } else if (response.statusCode == 422) {
                            json = XuberServicesApplication.trimMessage(new String(response.data));
                            if (json != "" && json != null) {
                                displayMessage(json);
                            } else {
                                displayMessage(getString(R.string.please_try_again));
                            }
                        } else {
                            displayMessage(getString(R.string.please_try_again));
                        }

                    } catch (Exception e) {
                        displayMessage(getString(R.string.something_went_wrong));
                    }


                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("X-Requested-With", "XMLHttpRequest");
                headers.put("Authorization", "Bearer " + SharedHelper.getKey(context, "access_token"));
                return headers;
            }
        };

        XuberServicesApplication.getInstance().addToRequestQueue(jsonObjectRequest);

    }*/


 /*   private void changePassword(String current_pass, String new_pass, String confirm_new_pass)
    {

        customDialog = new CustomDialog(context);
        customDialog.setCancelable(false);
        customDialog.show();
        JSONObject object = new JSONObject();
        try {
            object.put("password", new_pass);
            object.put("password_confirmation", confirm_new_pass);
            object.put("old_password", current_pass);
            Log.e("ChangePasswordAPI", "" + object);

        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                URLHelper.CHANGE_PASSWORD_API, object, new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response)
            {
                customDialog.dismiss();
                Log.v("SignInResponse", response.toString());
                displayMessage(response.optString("message"));
            }

        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                customDialog.dismiss();
                String json = null;
                String Message;
                NetworkResponse response = error.networkResponse;
                Log.e("MyTest", "" + error);
                Log.e("MyTestError", "" + error.networkResponse);
                Log.e("MyTestError1", "" + response.statusCode);

                if (response != null && response.data != null) {
                    try
                    {
                        JSONObject errorObj = new JSONObject(new String(response.data));
                        Log.e("ErrorChangePasswordAPI", "" + errorObj.toString());

                        if (response.statusCode == 400 || response.statusCode == 405 || response.statusCode == 500)
                        {
                            try
                            {
                                displayMessage(errorObj.optString("error"));
                            }
                            catch (Exception e)
                            {
                                displayMessage(getString(R.string.something_went_wrong));
                            }
                        }

                        else if (response.statusCode == 401)
                        {
                            GoToBeginActivity();
                        }

                        else if (response.statusCode == 422)
                        {
                            json = XuberServicesApplication.trimMessage(new String(response.data));
                            if (json != "" && json != null)
                            {
                                displayMessage(json);
                            }
                            else
                            {
                                displayMessage(getString(R.string.please_try_again));
                            }
                        }

                        else
                        {
                            displayMessage(getString(R.string.please_try_again));
                        }

                    } catch (Exception e)
                    {
                        displayMessage(getString(R.string.something_went_wrong));
                    }


                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("X-Requested-With", "XMLHttpRequest");
                headers.put("Authorization", "Bearer " + SharedHelper.getKey(context, "access_token"));
                return headers;
            }
        };

        XuberServicesApplication.getInstance().addToRequestQueue(jsonObjectRequest);

    }
*/

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void GoToBeginActivity() {
        SharedHelper.Companion.putKey(activity, "loggedIn","false");
        Intent mainIntent = new Intent(activity, Login.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(mainIntent);
        activity.finish();
    }

    public void displayMessage(String toastString) {
        Snackbar snackbar = Snackbar.make(getCurrentFocus(), toastString, Snackbar.LENGTH_SHORT);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(ContextCompat.getColor(this, R.color.black));
     /*   TextView textView = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);
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
                   /* JSONObject jsonObject=new JSONObject(response);
                    Log.e("JSONObjectValue ",jsonObject.toString());*/
                    Toast.makeText(this, "Password Updated", Toast.LENGTH_SHORT).show();
                    GoToBeginActivity();
                    SharedHelper.Companion.putKey(context, "otp", "");
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
                break;
        }
    }
}
