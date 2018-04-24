package com.oodi.jingoo.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.onesignal.OneSignal;
import com.oodi.jingoo.R;
import com.oodi.jingoo.utility.AppUtils;
import com.oodi.jingoo.utility.Session;
import com.stfalcon.smsverifycatcher.OnSmsCatchListener;
import com.stfalcon.smsverifycatcher.SmsVerifyCatcher;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OTPActivity extends AppCompatActivity {

    String otp = "" , referral_code = "" , device_token = "" , name = "";
    Button mBtnSignIn ;
    EditText mEdtUsername;
    AppUtils appUtils ;
    Session session;

    private SmsVerifyCatcher smsVerifyCatcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        appUtils = new AppUtils(this);
        session = new Session(this);

        try {
            OneSignal.enableSound(true);
            OneSignal.idsAvailable(new OneSignal.IdsAvailableHandler() {
                @Override
                public void idsAvailable(String userId, String registrationId) {
                    if (registrationId != null) {
                        device_token = userId;
                    }
                }
            });
        }catch (Exception e){

        }

        mEdtUsername = (EditText) findViewById(R.id.edtUsername);
        mBtnSignIn = (Button) findViewById(R.id.btnSignIn);

        Intent intent = getIntent();
        otp = intent.getStringExtra("otp");
        referral_code = intent.getStringExtra("referral_code");
        name = intent.getStringExtra("name");

        mBtnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEdtUsername.getText().toString().equals("")){
                    Toast.makeText(OTPActivity.this , "Please enter OTP" , Toast.LENGTH_LONG).show();
                }else {
                    if (mEdtUsername.getText().toString().equals("1")){
                        login_with_referral();
                    }else{
                        Toast.makeText(OTPActivity.this , "Incorrect OTP" , Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        //init SmsVerifyCatcher
        smsVerifyCatcher = new SmsVerifyCatcher(this, new OnSmsCatchListener<String>() {
            @Override
            public void onSmsCatch(String message) {
                String code = parseCode(message);//Parse verification code
                mEdtUsername.setText(code);//set code in edit text
                //then you can send verification code to server
            }
        });

        //set phone number filter if needed
        //smsVerifyCatcher.setPhoneNumberFilter("HP-JINGOO");

    }

    private String parseCode(String message) {
        Pattern p = Pattern.compile("\\b\\d{4}\\b");
        Matcher m = p.matcher(message);
        String code = "";
        while (m.find()) {
            code = m.group(0);
        }
        return code;
    }

    @Override
    protected void onStart() {
        super.onStart();
        smsVerifyCatcher.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        smsVerifyCatcher.onStop();
    }

    /**
     * need for Android 6 real time permissions
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        smsVerifyCatcher.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void login_with_referral(){

        appUtils.showProgressBarLoading();

        String REGISTER_URL = OTPActivity.this.getResources().getString(R.string.base_url) + "v1/index.php/appshopboth/login_with_referral";

        final String KEY_USERNAME = "phone";
        final String KEY_PASSWORD = "password";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }

                        Log.d("Json Array", "doInBackground: " + jsonObject);

                        String status = jsonObject.optString("success");
                        String msg = jsonObject.optString("msg");

                        if (!msg.equals("")){
                            Toast.makeText(OTPActivity.this,msg,Toast.LENGTH_LONG).show();
                        }
                        if(status.equals("1"))
                        {
                            session.setLogin(true);

                            JSONObject object = null;
                            try {
                                object = jsonObject.getJSONObject("customer_data");
                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }

                            try {
                                String token = object.getString("token");
                                String name = object.getString("name");
                                String email = object.getString("email");
                                String username = object.getString("username");
                                String type = object.getString("type");
                                String phone = object.getString("phone");
                                String avatar = object.getString("avatar");
                                String avatar_id = object.getString("avatar_id");
                                String store_id = object.getString("store_id");

                                SharedPreferences.Editor editor = getSharedPreferences("Login", MODE_PRIVATE).edit();
                                editor.putString("token", token);
                                editor.putString("name", name);
                                editor.putString("email", email);
                                editor.putString("username", username);
                                editor.putString("type", type);
                                editor.putString("phone", phone);
                                editor.putString("avatar", avatar);
                                editor.putString("avatar_id", avatar_id);
                                editor.putString("store_id", store_id);

                                editor.commit();

                                SharedPreferences prefs1 = getSharedPreferences("wt", MODE_PRIVATE);
                                String first = prefs1.getString("first", "");

                                if (first.equals("1")){
                                    LoginActivity.a.finish();
                                    Intent intent = new Intent(OTPActivity.this , MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }else {
                                    LoginActivity.a.finish();
                                    Intent intent = new Intent(OTPActivity.this , CompleteProfileActivity.class);
                                    intent.putExtra("token", token);
                                    intent.putExtra("name", name);
                                    intent.putExtra("phone", phone);
                                    intent.putExtra("avatar", avatar);
                                    intent.putExtra("avatar_id", avatar_id);
                                    startActivity(intent);
                                    finish();
                                }


                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }
                        }

                        appUtils.dismissProgressBar();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        appUtils.dismissProgressBar();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put(KEY_USERNAME,name);
                params.put(KEY_PASSWORD,"123");
                params.put("device_type" , "a");
                params.put("p_id" , device_token);
                params.put("referral_code" , referral_code);

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


}
