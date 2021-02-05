package com.ibring_restaurantowner.views

import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.iid.FirebaseInstanceId
import com.ibring_restaurantowner.R
import com.ibring_restaurantowner.retrofit.RetrofitResponse
import com.ibring_restaurantowner.retrofit.RetrofitService
import com.ibring_restaurantowner.utils.AppHelper
import com.ibring_restaurantowner.utils.SharedHelper
import com.ibring_restaurantowner.utils.URLHelper
import com.ibring_restaurantowner.views.home.HomeScreen
import kotlinx.android.synthetic.main.login.*
import org.json.JSONObject
import java.util.regex.Pattern

class Login : AppCompatActivity(), View.OnClickListener, RetrofitResponse {
//{"status":"error","message":"The email address or password you entered is incorrect.","result":[]}

    override fun onResponse(RequestCode: Int, response: String?)
{
        when (RequestCode)
        {
            100 ->
            {
                Log.e("LoginResponse ", response)
                try
                {
                    val jsonObject = JSONObject(response)

                    if(jsonObject.getString("status").equals("error",ignoreCase = true))
                    {
                        Toast.makeText(this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show()
                    }
                    else
                    {
                        SharedHelper.putKey(this, "access_token", jsonObject.optString("access_token"))
                        SharedHelper.putKey(this, "refresh_token", jsonObject.optString("refresh_token"))
                        SharedHelper.putKey(this, "token_type", "Bearer")
                        SharedHelper.putKey(this, "first_name", jsonObject.optString("first_name"))
                        SharedHelper.putKey(this, "last_name", jsonObject.optString("last_name"))
                        SharedHelper.putKey(this, "email", jsonObject.optString("email"))
                        SharedHelper.putKey(this, "picture", AppHelper.getImageUrl(jsonObject.optString("picture")))
                        SharedHelper.putKey(this, "gender", jsonObject.optString("gender"))
                        SharedHelper.putKey(this, "mobile", jsonObject.optString("mobile"))
                        SharedHelper.putKey(this, "description", jsonObject.optString("description"))
                        SharedHelper.putKey(this, "wallet_balance", jsonObject.optString("wallet_balance"))
                        SharedHelper.putKey(this, "payment_mode", jsonObject.optString("payment_mode"))

                        val  restaurant=jsonObject.getJSONObject("restaurant")
                        SharedHelper.putKey(this, "id", restaurant.optString("id"))


                        if (!jsonObject.optString("currency").equals("", ignoreCase = true) ||
                            !jsonObject.optString("currency").equals("null", ignoreCase = true))
                        {
                            SharedHelper.putKey(this, "currency", jsonObject.optString("currency"))
                        }
                        else
                        {
                            SharedHelper.putKey(this, "currency", "$")
                        }
                        SharedHelper.putKey(this, "loggedIn", "true")
                        getProfile()
                    }
                }
                catch (ex: Exception)
                {
                    ex.printStackTrace()
                }
            }

            500 ->
            {
                Log.e("ProfileResponse ", response)
                try
                {
                    val jsonObject = JSONObject(response)
                    //  SharedHelper.putKey(this, "id", jsonObject.getJSONObject("restaurant").optString("id"))
                    val mainIntent = Intent(this, HomeScreen::class.java)
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(mainIntent)
                    this.finish()
                }
                catch (ex: Exception)
                {
                    ex.printStackTrace()
                }
            }
        }
    }



    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)
        GetToken()
        rlLogin.setOnClickListener(this)
        lblforgotpassword.setOnClickListener(this)


        txtpassword.setCustomSelectionActionModeCallback(object : ActionMode.Callback {

            override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean {
                return false
            }

            override fun onDestroyActionMode(mode: ActionMode) {}

            override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
                return false
            }

            override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
                return false
            }
        })



        ivEye.setOnClickListener {
            if (txtpassword.transformationMethod == PasswordTransformationMethod.getInstance()) {
                ivEye.setImageResource(R.drawable.eye_hide)
                txtpassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
                txtpassword.setSelection(txtpassword.length())
            } else {
                ivEye.setImageResource(R.drawable.eye_visible)
                txtpassword.transformationMethod = PasswordTransformationMethod.getInstance()
                txtpassword.setSelection(txtpassword.length())

            }
        }


    }

    fun getProfile() {
        RetrofitService(this, this, URLHelper.PROVIDER_PROFILE, 500,
                1, "1").callService(true)
    }

    override fun onClick(v: View?) {
        when (v!!.getId()) {
            R.id.rlLogin -> {
                if (checkValidations()) {
                    /*val intent = Intent(this, HomeScreen::class.java)
                    startActivity(intent)*/

                    callLogin()
                }
            }

            R.id.lblforgotpassword -> {
                val intent = Intent(this, ForgetPassword::class.java)
                startActivity(intent)
            }
        }
    }

    private fun isValidEmail(email: String): Boolean {
        val EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
        val pattern = Pattern.compile(EMAIL_PATTERN)
        val matcher = pattern.matcher(email)
        return matcher.matches()
    }

    fun checkValidations(): Boolean {
        if (txtemail.text.toString().trim().equals("")) {
            Toast.makeText(this, "Enter your email address", Toast.LENGTH_SHORT).show()
            return false
        } else if (!isValidEmail(txtemail.text.toString()))
        {
            Toast.makeText(this, "Enter valid email address", Toast.LENGTH_SHORT).show()
            return false
        }
        else if (txtpassword.text.toString().trim().equals("")) {
            Toast.makeText(this, "Enter your password", Toast.LENGTH_SHORT).show()
            return false
        } else if (txtpassword.getText().toString().length < 6) {
            Toast.makeText(this, "Password must be minimum 6 characters", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    internal var device_token: String = ""
    internal var device_UDID: String = ""

    fun GetToken() {
        device_token = FirebaseInstanceId.getInstance().token.toString()
        Log.e("device_token  ", device_token)
        try {
            device_UDID = android.provider.Settings.Secure.getString(contentResolver, android.provider.Settings.Secure.ANDROID_ID)
            Log.e("UDID  ", device_UDID)
        } catch (e: Exception) {
            device_UDID = "COULD NOT GET UDID"
            e.printStackTrace()
            Log.e("failed", "Failed to complete device UDID")
        }
    }

    fun callLogin() {
        try {
            val jsonObject = JSONObject()
            jsonObject.put("email", txtemail.text.toString().trim())
            jsonObject.put("password", txtpassword.text.toString().trim())
            jsonObject.put("device_type", "android")
            jsonObject.put("device_id", device_UDID)
            jsonObject.put("device_token", device_token)

            Log.e("Params ", jsonObject.toString())

            RetrofitService(this@Login, this@Login, URLHelper.LOGIN, jsonObject,
                100, 2, "2").callService(true)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

}