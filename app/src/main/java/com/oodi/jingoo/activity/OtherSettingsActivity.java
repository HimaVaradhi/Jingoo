package com.oodi.jingoo.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.oodi.jingoo.R;
import com.oodi.jingoo.utility.AppUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class OtherSettingsActivity extends AppCompatActivity {

    TextView mTxtHeaderName , mTxtHome , Home , mtxtSupplier , mtxtSelling , mtxtTransfer , mTxtPhone , mTxtTaC;
    AppUtils appUtils ;
    ImageView mImgBack , mImgEditSellingPrise , mImgEditSupplier , mImgEditTransferStock , mImgShareRefferalCode;
    LinearLayout mlnrSelling , mLnrSupplier , mLnrTransferStock , mLnrShareRefferalCode , mLnrHelp;
    String token ;
    static final Integer CALL = 0x2;
    public static Activity otherSetting ;

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
        setContentView(R.layout.activity_other_settings);

        otherSetting = this ;

        init();

        ImageView imgHelp = (ImageView) findViewById(R.id.imgHelp);
        imgHelp.setVisibility(View.GONE);
        mTxtHome.setVisibility(View.VISIBLE);
        Home.setVisibility(View.VISIBLE);

        mTxtTaC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://oodiproject.com/jingoo/terms-and-conditions/";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        mTxtPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                askForPermission(android.Manifest.permission.CALL_PHONE,CALL);
            }
        });

        mLnrSupplier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mImgEditSupplier.performClick();
            }
        });

        mLnrTransferStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mImgEditTransferStock.performClick();

            }
        });

        mlnrSelling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mImgEditSellingPrise.performClick();

            }
        });

        mLnrShareRefferalCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mImgShareRefferalCode.performClick();
            }
        });

        SharedPreferences pr = this.getSharedPreferences("Login", Context.MODE_PRIVATE);
        token = pr.getString("token" , "");

        mTxtHeaderName.setText(getResources().getString(R.string.settings));
        mTxtHome.setText(getResources().getString(R.string.settings));

        mImgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if (!appUtils.isOnLine()){
            Intent intent = new Intent(getApplicationContext() , NoInternetActivity.class);
            startActivity(intent);
            //appUtils.showToast(R.string.offline);
        }else {
            get_my_profile();
        }

        mImgEditSellingPrise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OtherSettingsActivity.this , SellingPriseActivity.class);
                startActivity(intent);
            }
        });

        mImgEditSupplier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OtherSettingsActivity.this , ChooseSupliersActivity.class);
                intent.putExtra("type" , "setting");
                startActivity(intent);
            }
        });

        mLnrTransferStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OtherSettingsActivity.this , TransferStockActivity.class);
                startActivity(intent);
            }
        });

        mImgShareRefferalCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(OtherSettingsActivity.this , ShareRefferalCodeActivity.class);
                startActivity(intent);
            }
        });

        mLnrHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(OtherSettingsActivity.this , HelpActivity.class);
                startActivity(intent);
            }
        });

        Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingsNewActivity.settingNew.finish();
                onBackPressed();
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(ActivityCompat.checkSelfPermission(this, permissions[0]) == PackageManager.PERMISSION_GRANTED){

            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + mTxtPhone.getText().toString()));
            if (ActivityCompat.checkSelfPermission(OtherSettingsActivity.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                startActivity(callIntent);
            }


            Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
        }
    }

    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(OtherSettingsActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(OtherSettingsActivity.this, permission)) {

                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(OtherSettingsActivity.this, new String[]{permission}, requestCode);

            } else {

                ActivityCompat.requestPermissions(OtherSettingsActivity.this, new String[]{permission}, requestCode);
            }
        } else {
            Intent in = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" +mTxtPhone.getText().toString()));
            startActivity(in);
            //Toast.makeText(this, "" + permission + " is already granted.", Toast.LENGTH_SHORT).show();
        }
    }

    public void init(){

        mImgBack = (ImageView) findViewById(R.id.imgBack);
        mTxtHeaderName = (TextView) findViewById(R.id.txtHeaderName);
        mTxtHome = (TextView) findViewById(R.id.txtHome);
        Home = (TextView) findViewById(R.id.Home);
        mTxtTaC = (TextView) findViewById(R.id.txtTaC);

        mImgEditSellingPrise = (ImageView) findViewById(R.id.imgEditSellingPrise);
        mImgEditSupplier = (ImageView) findViewById(R.id.imgEditSupplier);

        mtxtSupplier = (TextView) findViewById(R.id.txtSupplier);
        mtxtSelling = (TextView) findViewById(R.id.txtSelling);
        mtxtTransfer = (TextView) findViewById(R.id.txtTransfer);
        mLnrSupplier = (LinearLayout) findViewById(R.id.lnrSupplier);
        mlnrSelling = (LinearLayout) findViewById(R.id.lnrSelling);
        mLnrShareRefferalCode = (LinearLayout) findViewById(R.id.lnrShareRefferalCode);

        mLnrTransferStock = (LinearLayout) findViewById(R.id.lnrTransferStock);
        mImgEditTransferStock = (ImageView) findViewById(R.id.imgEditTransferStock);
        mImgShareRefferalCode = (ImageView) findViewById(R.id.imgShareRefferalCode);
        mTxtPhone = (TextView) findViewById(R.id.txtPhone);
        mLnrHelp = (LinearLayout) findViewById(R.id.lnrHelp);


    }

    private void get_my_profile(){

        appUtils.showProgressBarLoading();

        String REGISTER_URL = OtherSettingsActivity.this.getResources().getString(R.string.base_url) + "v1/index.php/appshopboth/get_my_profile";

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
