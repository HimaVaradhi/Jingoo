package com.oodi.jingoo.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.crashlytics.android.Crashlytics;
import com.onesignal.OSNotificationAction;
import com.onesignal.OSNotificationOpenResult;
import com.onesignal.OneSignal;
import com.oodi.jingoo.R;
import com.oodi.jingoo.utility.AppUtils;
import com.oodi.jingoo.utility.Session;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.fabric.sdk.android.Fabric;

public class LoginActivity extends AppCompatActivity {

    Button mBtnSignIn;
    TextView mTxtForgotPassword, mTxtHeaderName, mTxtTaC, p;
    EditText mEdtUsername, mEdtPassword, mEdtReferral;
    Session session;
    AppUtils appUtils;
    CheckBox mCbTaC;
    public static boolean notification = false;
    String device_token = "", referral_code = "";
    public static Activity a;
    private static final int PERMISSION_REQUEST_CODE = 1;

    private class ExampleNotificationOpenedHandler implements OneSignal.NotificationOpenedHandler {
        // This fires when a notification is opened by tapping on it.
        @Override
        public void notificationOpened(OSNotificationOpenResult result) {
            OSNotificationAction.ActionType actionType = result.action.type;
            JSONObject data = result.notification.payload.additionalData;
            String customKey;

            notification = true;

            if (data != null) {
                customKey = data.optString("customkey", null);
                if (customKey != null)
                    Log.i("OneSignalExample", "customkey set with value: " + customKey);
            }

            if (actionType == OSNotificationAction.ActionType.ActionTaken) {

                Log.i("OneSignalExample", "Button pressed with id: " + result.action.actionID);

            }

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        OneSignal.startInit(this)
                .setNotificationOpenedHandler(new ExampleNotificationOpenedHandler())
                .init();
        setContentView(R.layout.activity_login);

        a = this;

        init();

        p = (TextView) findViewById(R.id.p);

        p.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignuUpActivity.class);
                startActivity(intent);
            }
        });

        if (Build.VERSION.SDK_INT >= 23) {

//Check whether your app has access to the READ permission//

            if (checkPermission()) {

//If your app has access to the device’s storage, then print the following message to Android Studio’s Logcat//

                Log.e("permission", "Permission already granted.");
            } else {

//If your app doesn’t have permission to access external storage, then call requestPermission//

                requestPermission();
            }
        }

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
        } catch (Exception e) {

        }

        appUtils = new AppUtils(this);

        session = new Session(LoginActivity.this);

        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        mTxtTaC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tac();
               /* String url = "http://oodiproject.com/jingoo/terms-and-conditions/";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);*/
            }
        });

        mTxtHeaderName.setText("Sign In");

        mBtnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (appUtils.isOnLine()) {
                    referral_code = mEdtReferral.getText().toString();
                    if (isValide()) {
                        if (referral_code.equals("")) {
                            signup_otp();
                        } else {
                            check_referral_code();
                        }
                    }
                } else {
                    Intent intent = new Intent(LoginActivity.this, NoInternetActivity.class);
                    startActivity(intent);
                    //appUtils.showToast(R.string.offline);
                }

            }
        });

        mTxtForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent intent = new Intent(LoginActivity.this , ForgotPasswordActivity.class);
                startActivity(intent);*/
            }
        });

    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_SMS}, PERMISSION_REQUEST_CODE);

    }

    private boolean checkPermission() {

//Check for READ_EXTERNAL_STORAGE access, using ContextCompat.checkSelfPermission()//

        int result = ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);

//If the app does have this permission, then return true//

        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {

//If the app doesn’t have this permission, then return false//

            return false;
        }
    }

    public void tac() {

        LayoutInflater layoutInflater = LayoutInflater.from(LoginActivity.this);
        View promptsView = layoutInflater.inflate(R.layout.tac, null);

        final android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(LoginActivity.this);
        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder.setCancelable(true);
        final android.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCanceledOnTouchOutside(true);

        final TextView Update = (TextView) promptsView.findViewById(R.id.btn_confirm);

        Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                    // Toast.makeText(LoginActivity.this,
                    //       "Permission accepted", Toast.LENGTH_LONG).show();
                } else {
                    //Toast.makeText(LoginActivity.this,
                    //      "Permission denied", Toast.LENGTH_LONG).show();

                }
                break;
        }
    }

    public void init() {

        mBtnSignIn = (Button) findViewById(R.id.btnSignIn);
        mTxtForgotPassword = (TextView) findViewById(R.id.txtForgotPassword);
        mTxtHeaderName = (TextView) findViewById(R.id.txtHeaderName);
        mEdtUsername = (EditText) findViewById(R.id.edtUsername);
        mEdtPassword = (EditText) findViewById(R.id.edtPassword);
        mCbTaC = (CheckBox) findViewById(R.id.cbTaC);
        mTxtTaC = (TextView) findViewById(R.id.txtTaC);
        mEdtReferral = (EditText) findViewById(R.id.edtReferral);

    }

    private void signup_otp() {

        appUtils.showProgressBarLoading();

        String REGISTER_URL = LoginActivity.this.getResources().getString(R.string.base_url) + "v1/index.php/appshopboth/signup_otp";

        final String KEY_USERNAME = "phone";

        final String username = mEdtUsername.getText().toString().trim();

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

                        if (!msg.equals("")) {
                            Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_LONG).show();
                        }
                        if (status.equals("1")) {
                            Intent intent = new Intent(LoginActivity.this, OTPActivity.class);
                            intent.putExtra("otp", jsonObject.optString("OTP"));
                            intent.putExtra("referral_code", referral_code);
                            intent.putExtra("name", mEdtUsername.getText().toString());
                            startActivity(intent);
                        }else {
                            Intent intent = new Intent(LoginActivity.this, SignuUpActivity.class);
                            intent.putExtra("phone" , mEdtUsername.getText().toString());
                            startActivity(intent);
                        }

                        appUtils.dismissProgressBar();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        appUtils.dismissProgressBar();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(KEY_USERNAME, username);

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void login_with_referral() {

        appUtils.showProgressBarLoading();

        String REGISTER_URL = LoginActivity.this.getResources().getString(R.string.base_url) + "v1/index.php/appshopboth/login_with_referral";

        final String KEY_USERNAME = "phone";
        final String KEY_PASSWORD = "password";

        final String username = mEdtUsername.getText().toString().trim();
        final String password = mEdtPassword.getText().toString().trim();

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

                        if (!msg.equals("")) {
                            Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_LONG).show();
                        }
                        if (status.equals("1")) {
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

                                if (first.equals("1")) {
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Intent intent = new Intent(LoginActivity.this, CompleteProfileActivity.class);
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
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(KEY_USERNAME, username);
                params.put(KEY_PASSWORD, password);
                params.put("device_type", "a");
                params.put("p_id", device_token);
                params.put("referral_code", referral_code);

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void check_referral_code() {

        appUtils.showProgressBarLoading();

        String REGISTER_URL = LoginActivity.this.getResources().getString(R.string.base_url) + "v1/index.php/appshopboth/check_referral_code";

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

                        if (status.equals("1")) {
                            signup_otp();
                        } else {
                            Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_LONG).show();
                        }

                        appUtils.dismissProgressBar();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        appUtils.dismissProgressBar();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("referral_code", referral_code);

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public boolean isValide() {

        if (mEdtUsername.getText().toString().equalsIgnoreCase("")) {
            makeToast("Please enter phone number");
            return false;
        } else if (mEdtReferral.getText().toString().length() != 10 && !mEdtReferral.getText().toString().equals("")) {
            makeToast("Please enter 10 digit referral code");
            return false;
        } else if (!mCbTaC.isChecked()) {
            makeToast("Please check terms and conditions");
            return false;
        }

        return true;
    }

    public void makeToast(String name) {

        Toast.makeText(LoginActivity.this, name, Toast.LENGTH_SHORT).show();

    }

}
