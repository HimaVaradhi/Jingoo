package com.oodi.jingoo.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
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
import com.oodi.jingoo.utility.AppUtility;
import com.oodi.jingoo.utility.AppUtils;
import com.oodi.jingoo.utility.VersionChecker;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    LinearLayout mLnrTakeStock , mLnrRefill , mLnrSettings , mLnrRewards , mLnrTeam , mLnrAnalytics
                , mLnrNotification , mLnrCustomerCare , mLnrTransferStock , mLnrRewardsCSM
                , mLnrCashOnHand , mLnrDailySalesReport , mLnrTopSellingVarient , mLnrLowStockReport , mLnrSalesValuationReport , lnrShareRefferal;
    AppUtils appUtils ;
    public static int idx = 1;
    public static Activity mainActivity ;
    TextView mTxtPosition , mTxtName , mTxtVersion;
    ImageView mImgDp , mImgWt ;
    String type = "" ;
    AppUtility utils ;
    String token ;
    String lang , store_id;
    boolean csmReport = false ;

    @Override
    protected void onResume() {
        super.onResume();
        appUtils = new AppUtils(this);
        SharedPreferences prefs1 = getSharedPreferences("language", MODE_PRIVATE);
        lang = prefs1.getString("lang", "");
        if (lang.equals("hi")){
            appUtils.setLocale("hi");
        }else {
            appUtils.setLocale("en");
        }

        setContentView(R.layout.activity_main);

        SharedPreferences login = getSharedPreferences("Login", MODE_PRIVATE);
        type = login.getString("type", "");
        String avatar = login.getString("avatar", "");
        String username = login.getString("name", "");
        store_id = login.getString("store_id" , "");

        if (LoginActivity.notification == true){
            Intent intent = new Intent(MainActivity.this , NotificationsActivity.class);
            startActivity(intent);
        }

        mainActivity = this ;

        init();

        //=====================================================================================================================

        if (lang.equals("hi") && !type.equals("csm")){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                 mImgWt.setBackground(getResources().getDrawable(R.drawable.hi_main_menu));
            }
        }else if (lang.equals("hi") && type.equals("csm")){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                mImgWt.setBackground(getResources().getDrawable(R.drawable.hi_main_menu));
            }
        }else if (lang.equals("en") && type.equals("csm")){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                mImgWt.setBackground(getResources().getDrawable(R.drawable.hi_main_menu));
            }
        }

        SharedPreferences wt = getSharedPreferences("wt", MODE_PRIVATE);
        String first = wt.getString("main", "");

        if (first.equals("1")){

            mImgWt.setVisibility(View.GONE);

        }

        mImgWt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mImgWt.setVisibility(View.GONE);

                SharedPreferences.Editor editor = getSharedPreferences("wt", MODE_PRIVATE).edit();
                editor.putString("main", "1");

                editor.commit();

            }
        });

        //=====================================================================================================================

        try {
            Picasso.with(this)
                    .load(avatar)
                    .fit()
                    .placeholder(R.drawable.j1)
                    .into(mImgDp);

            mTxtName.setText(username);
        }catch (Exception e){

        }

        if (type.equals("csm")){
            mTxtPosition.setText(getResources().getString(R.string.csm));
            mLnrTeam.setVisibility(View.GONE);
            if (csmReport){
                mLnrAnalytics.setVisibility(View.VISIBLE);
            }else {
                mLnrAnalytics.setVisibility(View.GONE);
            }
            mLnrRewards.setVisibility(View.GONE);
            mLnrCashOnHand.setVisibility(View.GONE);
            mLnrDailySalesReport.setVisibility(View.GONE);
            mLnrTopSellingVarient.setVisibility(View.GONE);
            mLnrLowStockReport.setVisibility(View.GONE);
            lnrShareRefferal.setVisibility(View.GONE);
        }else {
            mLnrRewardsCSM.setVisibility(View.GONE);
        }

        mLnrSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this , SettingsNewActivity.class);
                startActivity(intent);
            }
        });

        mLnrCustomerCare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //onCall();
                Intent intent = new Intent(MainActivity.this , CustomerCareActivity.class);
                startActivity(intent);
                /*String url = "http://yoofy.freshdesk.com";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);*/
            }
        });

        mLnrTakeStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                idx = 1 ;
                if (!type.equals("csm")){
                    Intent intent = new Intent(MainActivity.this , TakeStockActivity.class);
                    intent.putExtra("type" , "main");
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(MainActivity.this , TakeStockActivity.class);
                    startActivity(intent);
                }
            }
        });

        mLnrRefill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                idx = 2 ;
                if (!type.equals("csm")){
                    Intent intent = new Intent(MainActivity.this , TakeStockActivity.class);
                    intent.putExtra("type" , "main");
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(MainActivity.this , TakeStockActivity.class);
                    startActivity(intent);
                }
            }
        });

        mLnrNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this , NotificationsActivity.class);
                startActivity(intent);
            }
        });

        mLnrTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this , ManageTeamActivity.class);
                startActivity(intent);
            }
        });

        mLnrRewards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this , RewardActivity.class);
                intent.putExtra("type" , "shopowner");
                startActivity(intent);
            }
        });

        mLnrRewardsCSM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this , RewardActivity.class);
                intent.putExtra("type" , "csm");
                startActivity(intent);
            }
        });

        mLnrAnalytics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type.equals("csm")){
                    Intent intent = new Intent(MainActivity.this , StoreAnalyticsActivity.class);
                    intent.putExtra("store_id",store_id);
                    startActivity(intent);
                }else {
                    //Intent intent = new Intent(MainActivity.this , AnalyticsNewActivity.class);
                    Intent intent = new Intent(MainActivity.this , ReportsActivity.class);
                    startActivity(intent);
                }
            }
        });

        mLnrTransferStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this , TransferStockActivity.class);
                startActivity(intent);
            }
        });

        mLnrCashOnHand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this , CashOnHandActivity.class);
                intent.putExtra("type" , "cash");
                startActivity(intent);
            }
        });

        mLnrDailySalesReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this , DailySalesReportActivity.class);
                intent.putExtra("type" , "report");
                startActivity(intent);
            }
        });

        mLnrTopSellingVarient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this , TopSellingVariantReportActivity.class);
                intent.putExtra("type" , "dailyTop");
                startActivity(intent);
            }
        });

        mLnrLowStockReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this , CashOnHandActivity.class);
                intent.putExtra("type" , "lowStock");
                startActivity(intent);
            }
        });

        mLnrSalesValuationReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this , SalesValuationReportActivity.class);
                intent.putExtra("type" , "lowStock");
                startActivity(intent);
            }
        });

        lnrShareRefferal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this , ShareRefferalCodeActivity.class);
                startActivity(intent);
            }
        });

    }

    private void checkAppVersion()
    {

        utils = new AppUtility(MainActivity.this);

        String latestVersion = null,CurruntVersion;
        VersionChecker versionChecker = new VersionChecker();
        try {
            latestVersion = versionChecker.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        PackageManager manager = getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        CurruntVersion = info.versionName;

        int str = 0;
        try {
            str = utils.compareVersionNames(CurruntVersion, latestVersion);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(str == -1){
            LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
            View promptsView = layoutInflater.inflate(R.layout.update, null);

            final android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(MainActivity.this);
            alertDialogBuilder.setView(promptsView);
            alertDialogBuilder.setCancelable(false);
            // create alert dialog
            final android.app.AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.setCanceledOnTouchOutside(false);

            final TextView Update = (TextView) promptsView.findViewById(R.id.btn_confirm);

            Update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Update.setText("PLEASE WAIT...");

                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.oodi.jingoo&hl=en"));
                    startActivity(intent);

                    alertDialog.dismiss();
                }
            });
            alertDialog.show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {

            case 123:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    onCall();
                } else {
                    Log.d("TAG", "Call Permission Not Granted");
                }
                break;

            default:
                break;
        }
    }

    public void onCall() {
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.CALL_PHONE},
                    123);
        } else {

            final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage(getResources().getString(R.string.phn_call))
                    .setCancelable(false)
                    .setNegativeButton(getResources().getString(R.string.no) , new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //do things
                            dialog.dismiss();
                            startActivity(new Intent(Intent.ACTION_CALL).setData(Uri.parse("tel: 011 48786060")));

                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();

          /*  AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
            alertDialog.setMessage(getResources().getString(R.string.phn_call));
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getResources().getString(R.string.okay),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            startActivity(new Intent(Intent.ACTION_CALL).setData(Uri.parse("tel: 011 48786060")));
                        }
                    });
            alertDialog.show();*/
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appUtils = new AppUtils(this);
        SharedPreferences prefs1 = getSharedPreferences("language", MODE_PRIVATE);
        lang = prefs1.getString("lang", "");
        if (lang.equals("hi")){
            appUtils.setLocale("hi");
        }else {
            appUtils.setLocale("en");
        }
        SharedPreferences prefs = this.getSharedPreferences("Login", Context.MODE_PRIVATE);
        token = prefs.getString("token" , "");

        set_language();

        try {
            SharedPreferences login = getSharedPreferences("Login", MODE_PRIVATE);
            type = login.getString("type", "");

            if (type.equals("csm")){

                Intent intent = getIntent();
                String opening_stock = intent.getStringExtra("opening_stock");

                if (opening_stock.equals("1")) {

                    idx = 2;

                    Intent in = new Intent(MainActivity.this, TakeStockActivity.class);
                    startActivity(in);
                }
              /*  if (opening_stock.equals("1")){

                    final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage(getResources().getString(R.string.csm_first_time))
                            .setCancelable(false)
                            .setNegativeButton(getResources().getString(R.string.no) , new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            })
                            .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //do things
                                    idx = 2 ;

                                    Intent in = new Intent(MainActivity.this , TakeStockActivity.class);
                                    startActivity(in);
                                    dialog.dismiss();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();

                }*/
            }
        }catch (Exception e){

        }
        if (!appUtils.isOnLine()){
            Intent intent = new Intent(getApplicationContext() , NoInternetActivity.class);
            startActivity(intent);
            //appUtils.showToast(R.string.offline);
        }else {
            checkAppVersion();
            getVersionInfo();
        }

    }

    public void init(){

        mLnrTakeStock = (LinearLayout) findViewById(R.id.lnrTakeStock);
        mLnrRefill = (LinearLayout) findViewById(R.id.lnrRefill);
        mLnrSettings = (LinearLayout) findViewById(R.id.lnrSettings);
        mLnrRewards = (LinearLayout) findViewById(R.id.lnrRewards);
        mLnrTeam = (LinearLayout) findViewById(R.id.lnrTeam);
        mLnrAnalytics = (LinearLayout) findViewById(R.id.lnrAnalytics);
        mLnrNotification = (LinearLayout) findViewById(R.id.lnrNotification);
        mTxtPosition = (TextView) findViewById(R.id.txtPosition);
        mImgDp = (ImageView) findViewById(R.id.imgDp);
        mTxtName = (TextView) findViewById(R.id.txtName);
        mTxtVersion = (TextView) findViewById(R.id.txtVersion);
        mLnrCustomerCare = (LinearLayout) findViewById(R.id.lnrCustomerCare);
        mLnrTransferStock = (LinearLayout) findViewById(R.id.lnrTransferStock);
        mLnrRewardsCSM = (LinearLayout) findViewById(R.id.lnrRewardsCSM);
        mLnrCashOnHand = (LinearLayout) findViewById(R.id.lnrCashOnHand);
        mLnrDailySalesReport = (LinearLayout) findViewById(R.id.lnrDailySalesReport);
        mLnrTopSellingVarient = (LinearLayout) findViewById(R.id.lnrTopSellingVarient);
        mLnrLowStockReport = (LinearLayout) findViewById(R.id.lnrLowStockReport);
        mLnrSalesValuationReport = (LinearLayout) findViewById(R.id.lnrSalesValuationReport);
        lnrShareRefferal = (LinearLayout) findViewById(R.id.lnrShareRefferal);
        mImgWt = (ImageView) findViewById(R.id.imgWt);

    }

    private void getVersionInfo() {
        String versionName = "";
        int versionCode = -1;
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            versionName = packageInfo.versionName;
            versionCode = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        //mTxtVersion.setText(String.format("Version %s ", versionName));
    }

    private void set_language(){

        final String language_used ;

        if (lang.equals("hi")){
            language_used = "1" ;
        }else {
            language_used = "0" ;
        }

        String REGISTER_URL = MainActivity.this.getResources().getString(R.string.base_url) + "v1/index.php/appshopboth/set_language";

        final String KEY_USERNAME = "language_used";
        final String KEY_PASSWORD = "token";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (type.equals("csm")){
                            get_my_profile();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put(KEY_USERNAME,language_used);
                params.put(KEY_PASSWORD,token);

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void get_my_profile(){

        appUtils.showProgressBarLoading();

        String REGISTER_URL = MainActivity.this.getResources().getString(R.string.base_url) + "v1/index.php/appshopboth/get_my_profile";

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
                                String role_reports = object.getString("role_reports");
                                String type = object.getString("type");

                                if (type.equals("csm")){

                                    if (role_reports.equals("1")){
                                        csmReport = true;
                                        mLnrAnalytics.setVisibility(View.VISIBLE);
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
