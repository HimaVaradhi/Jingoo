package com.oodi.jingoo.activity;

import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.oodi.jingoo.R;
import com.oodi.jingoo.adapter.CashOnHandAdapter;
import com.oodi.jingoo.adapter.SellingPriseShopAdapter;
import com.oodi.jingoo.pojo.CashOnHand;
import com.oodi.jingoo.pojo.Store;
import com.oodi.jingoo.utility.AppUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CashOnHandActivity extends AppCompatActivity {

    ImageView mImgBack ;
    TextView mTxtHeaderName , mTxtHome , Home , mTxtCancle , mTxtPurchase;
    AppUtils appUtils ;
    TextView mTxtStartDate , mTxtEndDate ;
    private int mYear, mMonth, mDay, mHour, mMinute;

    String startDate = "" , endDate = "" , token , id = "data_sales" , store_id = "" , v1 = "" , v2 = "" , v3 = "";
    String storeId = "";

    List<Store> mStockList;
    SellingPriseShopAdapter mSellingPriseShopAdapter;
    RecyclerView mRecShopList , mRecSellingPrise;
    List<CashOnHand> mCashOnHandList = new ArrayList<>();
    CashOnHandAdapter mSellingPriseUpdateAdapter;

    EditText mEdtSearch ;
    ArrayList<CashOnHand> matches = new ArrayList<>();

    public String type = "" , csm = "";
    LinearLayout mL2 ;

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
        setContentView(R.layout.activity_cash_on_hand);

        init();

        SharedPreferences login = getSharedPreferences("Login", MODE_PRIVATE);
        csm = login.getString("type", "");
        store_id = login.getString("store_id" , "");

        Intent intent = getIntent();
        type = intent.getStringExtra("type");

        SimpleDateFormat dfDate_day1= new SimpleDateFormat("dd-MM-yyyy");
        Calendar c1 = Calendar.getInstance();
        final String data1 = dfDate_day1.format(c1.getTime());
        mTxtStartDate.setText(data1);
        mTxtEndDate.setText(data1);

        SimpleDateFormat dfDate_day= new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        final String _data=dfDate_day.format(c.getTime());

        startDate = _data ;
        endDate = _data ;

        mTxtStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(CashOnHandActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                String day = "";
                                String month = "";

                                if (dayOfMonth == 1){
                                    day = String.valueOf("01") ;
                                }else if (dayOfMonth == 2){
                                    day = String.valueOf("02") ;
                                }else if (dayOfMonth == 3){
                                    day = String.valueOf("03") ;
                                }else if (dayOfMonth == 4){
                                    day = String.valueOf("04") ;
                                }else if (dayOfMonth == 5){
                                    day = String.valueOf("05") ;
                                }else if (dayOfMonth == 6){
                                    day = String.valueOf("06") ;
                                }else if (dayOfMonth == 7){
                                    day = String.valueOf("07") ;
                                }else if (dayOfMonth == 8){
                                    day = String.valueOf("08") ;
                                }else if (dayOfMonth == 9){
                                    day = String.valueOf("09") ;
                                }else {
                                    day = String.valueOf(dayOfMonth) ;
                                }

                                if (monthOfYear == 0){
                                    month = String.valueOf("01") ;
                                }else if (monthOfYear == 1){
                                    month = String.valueOf("02") ;
                                }else if (monthOfYear == 2){
                                    month = String.valueOf("03") ;
                                }else if (monthOfYear == 3){
                                    month = String.valueOf("04") ;
                                }else if (monthOfYear == 4){
                                    month = String.valueOf("05") ;
                                }else if (monthOfYear == 5){
                                    month = String.valueOf("06") ;
                                }else if (monthOfYear == 6){
                                    month = String.valueOf("07") ;
                                }else if (monthOfYear == 7){
                                    month = String.valueOf("08") ;
                                }else if (monthOfYear == 8){
                                    month = String.valueOf("09") ;
                                }else {
                                    month = String.valueOf(monthOfYear+1) ;
                                }

                                startDate = year + "-" + month + "-" + day ;
                                mTxtStartDate.setText(day + "-" + month + "-" + year);

                                if (!startDate.equals("") && !endDate.equals("")){
                                    cash_on_hand_report();
                                }

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }

        });

        mTxtEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(CashOnHandActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                String day = "";
                                String month = "";

                                if (dayOfMonth == 1){
                                    day = String.valueOf("01") ;
                                }else if (dayOfMonth == 2){
                                    day = String.valueOf("02") ;
                                }else if (dayOfMonth == 3){
                                    day = String.valueOf("03") ;
                                }else if (dayOfMonth == 4){
                                    day = String.valueOf("04") ;
                                }else if (dayOfMonth == 5){
                                    day = String.valueOf("05") ;
                                }else if (dayOfMonth == 6){
                                    day = String.valueOf("06") ;
                                }else if (dayOfMonth == 7){
                                    day = String.valueOf("07") ;
                                }else if (dayOfMonth == 8){
                                    day = String.valueOf("08") ;
                                }else if (dayOfMonth == 9){
                                    day = String.valueOf("09") ;
                                }else {
                                    day = String.valueOf(dayOfMonth) ;
                                }

                                if (monthOfYear == 0){
                                    month = String.valueOf("01") ;
                                }else if (monthOfYear == 1){
                                    month = String.valueOf("02") ;
                                }else if (monthOfYear == 2){
                                    month = String.valueOf("03") ;
                                }else if (monthOfYear == 3){
                                    month = String.valueOf("04") ;
                                }else if (monthOfYear == 4){
                                    month = String.valueOf("05") ;
                                }else if (monthOfYear == 5){
                                    month = String.valueOf("06") ;
                                }else if (monthOfYear == 6){
                                    month = String.valueOf("07") ;
                                }else if (monthOfYear == 7){
                                    month = String.valueOf("08") ;
                                }else if (monthOfYear == 8){
                                    month = String.valueOf("09") ;
                                }else {
                                    month = String.valueOf(monthOfYear+1) ;
                                }

                                endDate = year + "-" + month + "-" + day ;
                                mTxtEndDate.setText(day + "-" + month + "-" + year);

                                if (!startDate.equals("") && !endDate.equals("")){
                                    cash_on_hand_report();

                                }

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }

        });

        SharedPreferences prefs = this.getSharedPreferences("Login", Context.MODE_PRIVATE);
        token = prefs.getString("token" , "");

        if (type.equals("report")){

            mTxtHome.setText(getResources().getString(R.string.Daily_Sales_Report));
            mTxtHeaderName.setText(getResources().getString(R.string.Daily_Sales_Report));

            mL2.setVisibility(View.GONE);
            mTxtPurchase.setVisibility(View.GONE);

        }else if (type.equals("cash")){
            mTxtHome.setText(getResources().getString(R.string.cash_on_hand));
            mTxtHeaderName.setText(getResources().getString(R.string.cash_on_hand));
        }else if (type.equals("dailyTop")){
            mTxtHome.setText(getResources().getString(R.string.Top_Selling_Variant));
            mTxtHeaderName.setText(getResources().getString(R.string.Top_Selling_Variant));

            mL2.setVisibility(View.GONE);
            mTxtPurchase.setVisibility(View.GONE);
        }else if (type.equals("lowStock")){
            mTxtPurchase.setVisibility(View.GONE);
            mL2.setVisibility(View.GONE);

            mTxtHome.setText(getResources().getString(R.string.Low_Stock_Report));
            mTxtHeaderName.setText(getResources().getString(R.string.Low_Stock_Report));
        }



        Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    ReportsActivity.report.finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                onBackPressed();
            }
        });

        mImgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        if (!appUtils.isOnLine()){
            Intent intent1 = new Intent(getApplicationContext() , NoInternetActivity.class);
            startActivity(intent1);
            //appUtils.showToast(R.string.offline);
        }else {
            if (csm.equals("csm")){
                if (type.equals("cash")){
                    cash_on_hand_report();
                }else if (type.equals("report")){
                    daily_sales_report();
                }else if (type.equals("dailyTop")){
                    top_selling_report();
                }else if (type.equals("lowStock")){
                    low_stock_report();
                }
            }else {
                store_list();
            }
        }

        mEdtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener(){

            @Override
            public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
                if(arg1 == EditorInfo.IME_ACTION_DONE)
                {

                    findMemberByName(mEdtSearch.getText().toString().toLowerCase());
                    mTxtCancle.setVisibility(View.VISIBLE);

                    if (matches.isEmpty()){

                        Toast.makeText(CashOnHandActivity.this , "Please try again",Toast.LENGTH_LONG).show();

                    }else {
                        mSellingPriseUpdateAdapter = new CashOnHandAdapter(CashOnHandActivity.this, matches);

                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(CashOnHandActivity.this);
                        mRecSellingPrise.setLayoutManager(mLayoutManager);
                        mRecSellingPrise.setItemAnimator(new DefaultItemAnimator());
                        mRecSellingPrise.setAdapter(mSellingPriseUpdateAdapter);
                    }



                    //s = mEdtSearch.getText().toString();

                    //categories();

                }
                return false;
            }

        });

        mTxtCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTxtCancle.setVisibility(View.GONE);
                //s = "";

                mEdtSearch.setText("");
                mEdtSearch.clearFocus();

                mSellingPriseUpdateAdapter = new CashOnHandAdapter(CashOnHandActivity.this, mCashOnHandList);

                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(CashOnHandActivity.this);
                mRecSellingPrise.setLayoutManager(mLayoutManager);
                mRecSellingPrise.setItemAnimator(new DefaultItemAnimator());
                mRecSellingPrise.setAdapter(mSellingPriseUpdateAdapter);

                //categorieList();

            }
        });

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("custom-message"));
    }

    public ArrayList<CashOnHand> findMemberByName(String name) {
        //ArrayList<Stock> matches = new ArrayList<Stock>();
        // go through list of members and compare name with given name

        matches.clear();

        for(CashOnHand member : mCashOnHandList) {

            if(member.getProducts_name().toLowerCase().contains(name)){
                matches.add(member);
            }

        }
        return matches;
    }

    public void init(){

        mImgBack = (ImageView) findViewById(R.id.imgBack);
        mTxtHeaderName = (TextView) findViewById(R.id.txtHeaderName);
        mTxtHome = (TextView) findViewById(R.id.txtHome);
        Home = (TextView) findViewById(R.id.Home);

        mTxtStartDate = (TextView) findViewById(R.id.txtStartDate);
        mTxtEndDate = (TextView) findViewById(R.id.txtEndDate);
        mRecShopList = (RecyclerView) findViewById(R.id.recShopList);
        mRecSellingPrise = (RecyclerView) findViewById(R.id.recyclerview);

        mEdtSearch = (EditText) findViewById(R.id.edtSearch);
        mTxtCancle = (TextView) findViewById(R.id.txtCancle);
        mTxtPurchase = (TextView) findViewById(R.id.txtPurchase);

        mL2 = (LinearLayout) findViewById(R.id.l2);

    }

    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (!intent.hasExtra("item_category")) {
            } else {
                try{
                    storeId = intent.getStringExtra("item_category");
                    Log.e("storeID", storeId);
                    if (type.equals("cash")){
                        cash_on_hand_report();
                    }else if (type.equals("report")){
                        daily_sales_report();
                    }else if (type.equals("dailyTop")){
                        top_selling_report();
                    }else if (type.equals("lowStock")){
                        low_stock_report();
                    }
                }catch (Exception e){
                }
            }
        }
    };

    private void store_list() {

        appUtils.showProgressBarLoading();

        String REGISTER_URL = CashOnHandActivity.this.getResources().getString(R.string.base_url) + "v1/index.php/appshopboth/store_list";

        final String KEY_TOKEN = "token";

        mStockList = new ArrayList<>();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(MainActivity.this,response,Toast.LENGTH_LONG).show();

                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Log.d("Json Array", "doInBackground: " + jsonObject);

                        String status = jsonObject.optString("success");

                        if (status.equals("1")) {

                            JSONArray array = null;
                            try {
                                array = jsonObject.getJSONArray("data");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            for (int i = 0; i < array.length(); i++) {

                                JSONObject c = null;
                                try {

                                    c = array.getJSONObject(i);

                                    String name = c.getString("name");
                                    String id = c.getString("id");
                                    String state = c.getString("state");
                                    String district = c.getString("district");
                                    String depot = c.getString("depot");
                                    String lat = c.getString("lat");
                                    String lng = c.getString("lng");
                                    String license_no = c.getString("license_no");

                                    Store store = new Store();

                                    store.setName(name);
                                    store.setStore(id);
                                    store.setState(state);
                                    store.setDistrict(district);
                                    store.setDepot(depot);
                                    store.setLat(lat);
                                    store.setLng(lng);
                                    store.setLicense_no(license_no);

                                    mStockList.add(store);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }

                        Log.e("list", mStockList + "");

                        if (mStockList.size() != 0){

                            mSellingPriseShopAdapter = new SellingPriseShopAdapter(CashOnHandActivity.this, mStockList);
                            mRecShopList.setAdapter(mSellingPriseShopAdapter);

                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(CashOnHandActivity.this, LinearLayoutManager.HORIZONTAL, false);
                            mRecShopList.setLayoutManager(mLayoutManager);
                            mRecShopList.setItemAnimator(new DefaultItemAnimator());

                        }

                        appUtils.dismissProgressBar();

                        try {
                            storeId = mStockList.get(0).getStore();

                            if (type.equals("cash")){
                                cash_on_hand_report();
                            }else if (type.equals("report")){
                                daily_sales_report();
                            }else if (type.equals("dailyTop")){
                                top_selling_report();
                            }else if (type.equals("lowStock")){
                                low_stock_report();
                            }

                        }catch (Exception e){

                        }


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
                params.put(KEY_TOKEN, token);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void cash_on_hand_report() {

        appUtils.showProgressBarLoading();

        if (csm.equals("csm")){

            storeId = store_id ;

        }

        mCashOnHandList.clear();

        String REGISTER_URL = CashOnHandActivity.this.getResources().getString(R.string.base_url) + "v1/index.php/appshopboth/cash_on_hand_report";

        final String KEY_TOKEN = "token";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(MainActivity.this,response,Toast.LENGTH_LONG).show();

                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Log.d("Json Array", "doInBackground: " + jsonObject);

                        String status = jsonObject.optString("success");

                        if (status.equals("1")) {

                            JSONArray array = null;
                            try {
                                array = jsonObject.getJSONArray("row_array_purchase");
                                String total_purchase = jsonObject.getString("total_purchase");

                                mTxtPurchase.setText(getResources().getString(R.string.Rs) + total_purchase);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            mStockList = new ArrayList<>();

                            for (int i = 0; i < array.length(); i++) {

                                JSONObject c = null;
                                try {

                                    c = array.getJSONObject(i);

                                    CashOnHand cashOnHand = new CashOnHand();

                                    String products_name = c.getString("products_name");
                                    try {
                                        String ML750 = c.getString("750 ML");
                                        cashOnHand.setML750(ML750);

                                    }catch (Exception e){
                                        String ML750 = "";
                                        cashOnHand.setML750(ML750);
                                    }
                                    try {
                                        String ML375 = c.getString("375 ML");
                                        cashOnHand.setML375(ML375);

                                    }catch (Exception e){
                                        String ML375 = "";
                                        cashOnHand.setML375(ML375);
                                    }
                                    try {
                                        String ML180 = c.getString("180 ML");
                                        cashOnHand.setML180(ML180);

                                    }catch (Exception e){
                                        String ML180 = "";
                                        cashOnHand.setML180(ML180);
                                    }

                                    cashOnHand.setProducts_name(products_name);

                                    mCashOnHandList.add(cashOnHand);

                                } catch (Exception e) {

                                }

                            }

                            mSellingPriseUpdateAdapter = new CashOnHandAdapter(CashOnHandActivity.this, mCashOnHandList);

                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(CashOnHandActivity.this);
                            mRecSellingPrise.setLayoutManager(mLayoutManager);
                            mRecSellingPrise.setItemAnimator(new DefaultItemAnimator());
                            mRecSellingPrise.setAdapter(mSellingPriseUpdateAdapter);



                        }

                        appUtils.dismissProgressBar();

                    }
                },
                new Response.ErrorListener()

                {
                    @Override
                    public void onErrorResponse (VolleyError error){
                        appUtils.dismissProgressBar();
                    }
                })

        {
            @Override
            protected Map<String, String> getParams () {
                Map<String, String> params = new HashMap<String, String>();
                params.put(KEY_TOKEN, token);
                params.put("store_id", storeId);
                params.put("from_date",startDate);
                params.put("to_date",endDate);
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void daily_sales_report() {

        appUtils.showProgressBarLoading();

        if (csm.equals("csm")){

            storeId = store_id ;

        }


        mCashOnHandList.clear();

        String REGISTER_URL = CashOnHandActivity.this.getResources().getString(R.string.base_url) + "v1/index.php/appshopboth/daily_sales_report";

        final String KEY_TOKEN = "token";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(MainActivity.this,response,Toast.LENGTH_LONG).show();

                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Log.d("Json Array", "doInBackground: " + jsonObject);

                        String status = jsonObject.optString("success");

                        if (status.equals("1")) {

                            JSONArray array = null;
                            try {
                                array = jsonObject.getJSONArray("row_array_dailysales");
                                //String total_purchase = jsonObject.getString("total_purchase");

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            mStockList = new ArrayList<>();

                            for (int i = 0; i < array.length(); i++) {

                                JSONObject c = null;
                                try {

                                    c = array.getJSONObject(i);

                                    CashOnHand cashOnHand = new CashOnHand();

                                    String products_name = c.getString("products_name");
                                    try {
                                        String ML750 = c.getString("750 ML");
                                        cashOnHand.setML750(ML750);

                                    }catch (Exception e){
                                        String ML750 = "";
                                        cashOnHand.setML750(ML750);
                                    }
                                    try {
                                        String ML375 = c.getString("375 ML");
                                        cashOnHand.setML375(ML375);

                                    }catch (Exception e){
                                        String ML375 = "";
                                        cashOnHand.setML375(ML375);
                                    }
                                    try {
                                        String ML180 = c.getString("180 ML");
                                        cashOnHand.setML180(ML180);

                                    }catch (Exception e){
                                        String ML180 = "";
                                        cashOnHand.setML180(ML180);
                                    }

                                    cashOnHand.setProducts_name(products_name);

                                    mCashOnHandList.add(cashOnHand);

                                } catch (Exception e) {

                                }

                            }

                            mSellingPriseUpdateAdapter = new CashOnHandAdapter(CashOnHandActivity.this, mCashOnHandList);

                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(CashOnHandActivity.this);
                            mRecSellingPrise.setLayoutManager(mLayoutManager);
                            mRecSellingPrise.setItemAnimator(new DefaultItemAnimator());
                            mRecSellingPrise.setAdapter(mSellingPriseUpdateAdapter);



                        }

                        appUtils.dismissProgressBar();

                    }
                },
                new Response.ErrorListener()

                {
                    @Override
                    public void onErrorResponse (VolleyError error){
                        appUtils.dismissProgressBar();
                    }
                })

        {
            @Override
            protected Map<String, String> getParams () {
                Map<String, String> params = new HashMap<String, String>();
                params.put(KEY_TOKEN, token);
                params.put("store_id", storeId);
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void top_selling_report() {

        appUtils.showProgressBarLoading();

        if (csm.equals("csm")){

            storeId = store_id ;

        }


        mCashOnHandList.clear();

        String REGISTER_URL = CashOnHandActivity.this.getResources().getString(R.string.base_url) + "v1/index.php/appshopboth/top_selling_report";

        final String KEY_TOKEN = "token";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(MainActivity.this,response,Toast.LENGTH_LONG).show();

                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Log.d("Json Array", "doInBackground: " + jsonObject);

                        String status = jsonObject.optString("success");

                        if (status.equals("1")) {

                            JSONArray array = null;
                            try {
                                array = jsonObject.getJSONArray("row_array_topsell");
                                //String total_purchase = jsonObject.getString("total_purchase");

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            mStockList = new ArrayList<>();

                            for (int i = 0; i < array.length(); i++) {

                                JSONObject c = null;
                                try {

                                    c = array.getJSONObject(i);

                                    CashOnHand cashOnHand = new CashOnHand();

                                    String products_name = c.getString("products_name");
                                    try {
                                        String ML750 = c.getString("750 ML");
                                        cashOnHand.setML750(ML750);

                                    }catch (Exception e){
                                        String ML750 = "";
                                        cashOnHand.setML750(ML750);
                                    }
                                    try {
                                        String ML375 = c.getString("375 ML");
                                        cashOnHand.setML375(ML375);

                                    }catch (Exception e){
                                        String ML375 = "";
                                        cashOnHand.setML375(ML375);
                                    }
                                    try {
                                        String ML180 = c.getString("180 ML");
                                        cashOnHand.setML180(ML180);

                                    }catch (Exception e){
                                        String ML180 = "";
                                        cashOnHand.setML180(ML180);
                                    }

                                    cashOnHand.setProducts_name(products_name);

                                    mCashOnHandList.add(cashOnHand);

                                } catch (Exception e) {

                                }

                            }

                            mSellingPriseUpdateAdapter = new CashOnHandAdapter(CashOnHandActivity.this, mCashOnHandList);

                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(CashOnHandActivity.this);
                            mRecSellingPrise.setLayoutManager(mLayoutManager);
                            mRecSellingPrise.setItemAnimator(new DefaultItemAnimator());
                            mRecSellingPrise.setAdapter(mSellingPriseUpdateAdapter);

                        }

                        appUtils.dismissProgressBar();

                    }
                },
                new Response.ErrorListener()

                {
                    @Override
                    public void onErrorResponse (VolleyError error){
                        appUtils.dismissProgressBar();
                    }
                })

        {
            @Override
            protected Map<String, String> getParams () {
                Map<String, String> params = new HashMap<String, String>();
                params.put(KEY_TOKEN, token);
                params.put("store_id", storeId);
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void low_stock_report() {

        appUtils.showProgressBarLoading();

        if (csm.equals("csm")){

            storeId = store_id ;

        }


        mCashOnHandList.clear();

        String REGISTER_URL = CashOnHandActivity.this.getResources().getString(R.string.base_url) + "v1/index.php/appshopboth/low_stock_report";

        final String KEY_TOKEN = "token";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(MainActivity.this,response,Toast.LENGTH_LONG).show();

                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Log.d("Json Array", "doInBackground: " + jsonObject);

                        String status = jsonObject.optString("success");

                        if (status.equals("1")) {

                            JSONArray array = null;
                            try {
                                array = jsonObject.getJSONArray("row_array_lowstock");
                                //String total_purchase = jsonObject.getString("total_purchase");

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            mStockList = new ArrayList<>();

                            for (int i = 0; i < array.length(); i++) {

                                JSONObject c = null;
                                try {

                                    c = array.getJSONObject(i);

                                    CashOnHand cashOnHand = new CashOnHand();

                                    String products_name = c.getString("products_name");
                                    try {
                                        String ML750 = c.getString("750 ML");
                                        cashOnHand.setML750(ML750);

                                    }catch (Exception e){
                                        String ML750 = "";
                                        cashOnHand.setML750(ML750);
                                    }
                                    try {
                                        String ML375 = c.getString("375 ML");
                                        cashOnHand.setML375(ML375);

                                    }catch (Exception e){
                                        String ML375 = "";
                                        cashOnHand.setML375(ML375);
                                    }
                                    try {
                                        String ML180 = c.getString("180 ML");
                                        cashOnHand.setML180(ML180);

                                    }catch (Exception e){
                                        String ML180 = "";
                                        cashOnHand.setML180(ML180);
                                    }

                                    cashOnHand.setProducts_name(products_name);

                                    mCashOnHandList.add(cashOnHand);

                                } catch (Exception e) {

                                }

                            }

                            mSellingPriseUpdateAdapter = new CashOnHandAdapter(CashOnHandActivity.this, mCashOnHandList);

                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(CashOnHandActivity.this);
                            mRecSellingPrise.setLayoutManager(mLayoutManager);
                            mRecSellingPrise.setItemAnimator(new DefaultItemAnimator());
                            mRecSellingPrise.setAdapter(mSellingPriseUpdateAdapter);

                        }

                        appUtils.dismissProgressBar();

                    }
                },
                new Response.ErrorListener()

                {
                    @Override
                    public void onErrorResponse (VolleyError error){
                        appUtils.dismissProgressBar();
                    }
                })

        {
            @Override
            protected Map<String, String> getParams () {
                Map<String, String> params = new HashMap<String, String>();
                params.put(KEY_TOKEN, token);
                params.put("store_id", storeId);
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

}
