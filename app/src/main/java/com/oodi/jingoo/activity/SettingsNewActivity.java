package com.oodi.jingoo.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.oodi.jingoo.R;
import com.oodi.jingoo.utility.AppUtils;
import com.oodi.jingoo.utility.Session;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SettingsNewActivity extends AppCompatActivity {

    TextView mTxtHeaderName , mTxtHome , Home , mTxtTaC , mTxtVersion;
    AppUtils appUtils ;
    ImageView mImgBack ;
    LinearLayout mLnrOtherSettings , mLnrProfileSetings , lnrTaC;
    Button mBtnSignOut ;
    Session session ;
    public static Activity settingNew ;
    String token ;

    TextView  mtxtSupplier , mtxtSelling , mtxtTransfer ;
    LinearLayout mlnrSelling , mLnrSupplier , mLnrTransferStock , mLnrShareRefferalCode , mLnrHelp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appUtils = new AppUtils(this);
        SharedPreferences prefs1 = getSharedPreferences("language", MODE_PRIVATE);
        String lang = prefs1.getString("lang", "");
        if (lang.equals("hi")){
            appUtils.setLocale("hi");
        }else {
            appUtils.setLocale("en");
        }
        session = new Session(this);

        settingNew = this ;

    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences pr = this.getSharedPreferences("Login", Context.MODE_PRIVATE);
        token = pr.getString("token" , "");

        appUtils = new AppUtils(this);
        SharedPreferences prefs1 = getSharedPreferences("language", MODE_PRIVATE);
        String lang = prefs1.getString("lang", "");
        if (lang.equals("hi")){
            appUtils.setLocale("hi");
        }else {
            appUtils.setLocale("en");
        }
        setContentView(R.layout.activity_settings_new);

        init();

        String versionName = "";
        int versionCode = -1;
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            versionName = packageInfo.versionName;
            versionCode = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        mTxtVersion.setText(String.format("Version %s ", versionName));

        get_my_profile();

        ImageView imgHelp = (ImageView) findViewById(R.id.imgHelp);
        imgHelp.setVisibility(View.GONE);
        mTxtHome.setVisibility(View.VISIBLE);
        Home.setVisibility(View.VISIBLE);

        mTxtHeaderName.setText(getResources().getString(R.string.settings));
        mTxtHome.setText(getResources().getString(R.string.settings));

        mImgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mLnrProfileSetings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsNewActivity.this , SettingsActivity.class);
                startActivity(intent);
            }
        });

        mLnrOtherSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsNewActivity.this , OtherSettingsActivity.class);
                startActivity(intent);
            }
        });

        lnrTaC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://yoofy.in/terms-and-conditions/";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                //tac();
            }
        });

        mBtnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session.setLogin(false);

                SharedPreferences s = getSharedPreferences("Login" , Context.MODE_PRIVATE);
                s.edit().clear().commit();

                SharedPreferences s1 = getSharedPreferences("language", Context.MODE_PRIVATE);
                s1.edit().clear().commit();

                Intent intent = new Intent(SettingsNewActivity.this , LoginActivity.class);
                startActivity(intent);

                MainActivity.mainActivity.finish();
                finish();
            }
        });

        Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //=====================================================================================================================

        mlnrSelling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsNewActivity.this , SellingPriseActivity.class);
                startActivity(intent);
            }
        });

        mLnrSupplier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsNewActivity.this , ChooseSupliersActivity.class);
                intent.putExtra("type" , "setting");
                startActivity(intent);
            }
        });

        mLnrTransferStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsNewActivity.this , TransferStockActivity.class);
                startActivity(intent);
            }
        });


    }

    public void init(){

        mImgBack = (ImageView) findViewById(R.id.imgBack);
        mTxtHeaderName = (TextView) findViewById(R.id.txtHeaderName);
        mTxtHome = (TextView) findViewById(R.id.txtHome);
        Home = (TextView) findViewById(R.id.Home);
        mLnrOtherSettings = (LinearLayout) findViewById(R.id.lnrOtherSettings);
        mLnrProfileSetings = (LinearLayout) findViewById(R.id.lnrProfileSetings);
        mTxtTaC = (TextView) findViewById(R.id.txtTaC);
        mBtnSignOut = (Button) findViewById(R.id.btnSignOut);


        mLnrSupplier = (LinearLayout) findViewById(R.id.lnrSupplier);
        mlnrSelling = (LinearLayout) findViewById(R.id.lnrSelling);
        mLnrShareRefferalCode = (LinearLayout) findViewById(R.id.lnrShareRefferalCode);

        mLnrTransferStock = (LinearLayout) findViewById(R.id.lnrTransferStock);

        mtxtSupplier = (TextView) findViewById(R.id.txtSupplier);
        mtxtSelling = (TextView) findViewById(R.id.txtSelling);
        mtxtTransfer = (TextView) findViewById(R.id.txtTransfer);
        mTxtVersion = (TextView) findViewById(R.id.txtVersion);

        lnrTaC = (LinearLayout) findViewById(R.id.lnrTaC);

    }

    public void tac(){

        LayoutInflater layoutInflater = LayoutInflater.from(SettingsNewActivity.this);
        View promptsView = layoutInflater.inflate(R.layout.tac, null);

        final android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(SettingsNewActivity.this);
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

    private void get_my_profile(){

        appUtils.showProgressBarLoading();

        String REGISTER_URL = SettingsNewActivity.this.getResources().getString(R.string.base_url) + "v1/index.php/appshopboth/get_my_profile";

        final String KEY_USERNAME = "token";

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

                        if(status.equals("1"))
                        {

                            try {
                                JSONObject object =jsonObject.getJSONObject("data");

                                String role_selling_price = object.getString("role_selling_price");
                                String role_transfer_stock = object.getString("role_transfer_stock");
                                String type = object.getString("type");

                                if (type.equals("csm")){

                                    mLnrSupplier.setVisibility(View.GONE);
                                    mtxtSupplier.setVisibility(View.GONE);
                                    mLnrShareRefferalCode.setVisibility(View.GONE);

                                    if (role_selling_price.equals("1")){
                                        mlnrSelling.setVisibility(View.VISIBLE);
                                        mtxtSelling.setVisibility(View.VISIBLE);
                                    }else {
                                        mlnrSelling.setVisibility(View.GONE);
                                        mtxtSelling.setVisibility(View.GONE);

                                    }

                                    if (role_transfer_stock.equals("1")){
                                        mLnrTransferStock.setVisibility(View.VISIBLE);
                                        mtxtTransfer.setVisibility(View.VISIBLE);
                                    }else {
                                        mLnrTransferStock.setVisibility(View.GONE);
                                        mtxtTransfer.setVisibility(View.GONE);

                                    }

                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        appUtils.dismissProgressBar();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        appUtils.dismissProgressBar();

                        //Toast.makeText(SettingsActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put(KEY_USERNAME,token);

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


}
