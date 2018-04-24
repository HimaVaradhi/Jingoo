package com.oodi.jingoo.activity;

import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.oodi.jingoo.R;
import com.oodi.jingoo.adapter.ExpandListAdapter;
import com.oodi.jingoo.adapter.SellingPriseShopAdapter;
import com.oodi.jingoo.expandrecycleview.ExpandableRecyclerView;
import com.oodi.jingoo.expandrecycleview.ExpandableTestAdapter;
import com.oodi.jingoo.pojo.AnalyticsFooter;
import com.oodi.jingoo.pojo.Child;
import com.oodi.jingoo.pojo.DailySales;
import com.oodi.jingoo.pojo.Group;
import com.oodi.jingoo.pojo.ReportFooter;
import com.oodi.jingoo.pojo.ReportHeader;
import com.oodi.jingoo.pojo.Store;
import com.oodi.jingoo.utility.AppUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class StoreAnalyticsActivity extends AppCompatActivity {

    ImageView mImgBack , mImgWt;
    TextView mTxtHeaderName , mTxtHome , Home;
    AppUtils appUtils ;
    TextView mTxtStartDate , mTxtEndDate , mTxtTSline , mTxtRefillLine , mTxtCSline ;
    LinearLayout mLnrTS , mLnrRefill , mLnrCS , mL2 ;
    private int mYear, mMonth, mDay, mHour, mMinute;
    ExpandableRecyclerView expand_recyclerView ;
    ArrayList<Group> group_list = new ArrayList<Group>();
    public static ArrayList<Child> child_list;
    private ExpandListAdapter ExpAdapter;
    String startDate = "" , endDate = "" , token , id = "data_sales" , store_id = "" , v1 = "" , v2 = "" , v3 = "";
   // private SectionedRecyclerViewAdapter sectionAdapter;
    RecyclerView  mRecShopList;
    ScrollView scroll ;

    List<DailySales> mDailySalesList = new ArrayList<>();
    List<Store> mStockList;
    SellingPriseShopAdapter mSellingPriseShopAdapter;
    String csm = "", csm_Storeid = "" , type = "q" , chart_name = "" , report_type = "s";

    ImageView mImgQty , mImgValue , imageView3;

    List<ReportHeader> reportHeaderList = new ArrayList<>();
    List<ReportFooter> reportFooterList = new ArrayList<>();

    ArrayList<BarEntry> BARENTRY ;
    ArrayList<String> BarEntryLabels ;
    BarDataSet Bardataset ;
    BarData BARDATA ;

    int chart_position = 0 ;

    ArrayList<AnalyticsFooter> analyticsFooterArrayList ;

    WebView webLnC ;

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
        setContentView(R.layout.activity_store_analytics);

        init();

        BARENTRY = new ArrayList<>();

        BarEntryLabels = new ArrayList<String>();

        ImageView imgHelp = (ImageView) findViewById(R.id.imgHelp);
        imgHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mImgWt.setVisibility(View.VISIBLE);

            }
        });

        if (lang.equals("hi")){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                mImgWt.setBackground(getResources().getDrawable(R.drawable.reports_android));
            }
        }

        SharedPreferences wt = getSharedPreferences("wt", MODE_PRIVATE);
        String first = wt.getString("analytics_wt", "");

        if (first.equals("1")){

            mImgWt.setVisibility(View.GONE);

        }

        Intent intent = getIntent();
        store_id = intent.getStringExtra("store_id");

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


        mLnrTS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!id.equals("data_sales")) {

                    id = "data_sales";

                    mL2.setVisibility(View.VISIBLE);
                    mTxtStartDate.setVisibility(View.VISIBLE);


                    mTxtTSline.setVisibility(View.VISIBLE);
                    mTxtCSline.setVisibility(View.INVISIBLE);
                    mTxtRefillLine.setVisibility(View.INVISIBLE);

                    /*mTxtStartDate.setText(data1);
                    mTxtEndDate.setText(data1);

                    startDate = _data;
                    endDate = _data;*/

                    report_type = "s";

                    setWebView();

                    analytics();

                }
            }
        });

        mLnrRefill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!id.equals("data_purchases")) {

                    id = "data_purchases";

                    mL2.setVisibility(View.VISIBLE);
                    mTxtStartDate.setVisibility(View.VISIBLE);


                    mTxtTSline.setVisibility(View.INVISIBLE);
                    mTxtCSline.setVisibility(View.INVISIBLE);
                    mTxtRefillLine.setVisibility(View.VISIBLE);

                    /*mTxtStartDate.setText(data1);
                    mTxtEndDate.setText(data1);

                    startDate = _data;
                    endDate = _data;*/

                    report_type = "p";

                    setWebView();

                    analytics();
                }
            }
        });

        mLnrCS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!id.equals("data_current_stock")) {

                    //mL2.setVisibility(View.GONE);

                    //mTxtEndDate.setVisibility(View.INVISIBLE);
                    mTxtStartDate.setVisibility(View.INVISIBLE);

                    id = "data_current_stock";

                    mTxtTSline.setVisibility(View.INVISIBLE);
                    mTxtCSline.setVisibility(View.VISIBLE);
                    mTxtRefillLine.setVisibility(View.INVISIBLE);

                    /*mTxtStartDate.setText(data1);
                    mTxtEndDate.setText(data1);

                    startDate = _data;
                    endDate = _data;*/

                    report_type = "c";

                    setWebView();

                    analytics();
                }
            }
        });

        mTxtStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(StoreAnalyticsActivity.this,
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
                                    setWebView();

                                    analytics();
                                }

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
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

                final DatePickerDialog datePickerDialog = new DatePickerDialog(StoreAnalyticsActivity.this,
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
                                    setWebView();

                                    analytics();

                                }

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
                datePickerDialog.show();
            }

        });

        SharedPreferences prefs = this.getSharedPreferences("Login", Context.MODE_PRIVATE);
        token = prefs.getString("token" , "");
        csm_Storeid = prefs.getString("store_id" , "");
        csm = prefs.getString("type" , "");

        mTxtHome.setText(getResources().getString(R.string.reports));

        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                try {
                    ReportsActivity.report.finish();
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        mImgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        mTxtHeaderName.setText(getResources().getString(R.string.reports));

        if (!appUtils.isOnLine()){
            Intent intent1 = new Intent(getApplicationContext() , NoInternetActivity.class);
            startActivity(intent1);
            //appUtils.showToast(R.string.offline);
        }else {
            //store_list();
            if (csm.equals("csm")){
                analytics();

            }else {
                store_list();

            }
        }

        mImgQty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mImgQty.setImageResource(R.drawable.ic_qty_selected);
                mImgValue.setImageResource(R.drawable.ic_value_unselected);

                type = "q";

                setWebView();

                analytics();

            }
        });

        mImgValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mImgQty.setImageResource(R.drawable.ic_qty_unselected);
                mImgValue.setImageResource(R.drawable.ic_value_selected);

                type = "p";

                setWebView();

                analytics();

            }
        });

        mImgWt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mImgWt.setVisibility(View.GONE);

                SharedPreferences.Editor editor = getSharedPreferences("wt", MODE_PRIVATE).edit();
                editor.putString("analytics_wt", "1");

                editor.commit();

            }
        });

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("custom-message"));
    }

    public void init(){

        mImgBack = (ImageView) findViewById(R.id.imgBack);
        mTxtHeaderName = (TextView) findViewById(R.id.txtHeaderName);
        mTxtHome = (TextView) findViewById(R.id.txtHome);
        Home = (TextView) findViewById(R.id.Home);
        mTxtStartDate = (TextView) findViewById(R.id.txtStartDate);
        mTxtEndDate = (TextView) findViewById(R.id.txtEndDate);
        mTxtTSline = (TextView) findViewById(R.id.txtTSline);
        mTxtRefillLine = (TextView) findViewById(R.id.txtRefillLine);
        mTxtCSline = (TextView) findViewById(R.id.txtCSline);
        mLnrTS = (LinearLayout) findViewById(R.id.lnrTS);
        mLnrRefill = (LinearLayout) findViewById(R.id.lnrRefill);
        mLnrCS = (LinearLayout) findViewById(R.id.lnrCS);
        expand_recyclerView = (ExpandableRecyclerView) findViewById(R.id.recyclerview);

        scroll = (ScrollView) findViewById(R.id.scroll);
        mImgWt = (ImageView) findViewById(R.id.imgWt);
        mL2 = (LinearLayout) findViewById(R.id.l2);
        mRecShopList = (RecyclerView) findViewById(R.id.recShopList);

        mImgValue = (ImageView) findViewById(R.id.imgValue);
        mImgQty = (ImageView) findViewById(R.id.imgQty);
        imageView3 = (ImageView) findViewById(R.id.imageView3);

        webLnC = (WebView) findViewById(R.id.webLnC);

    }

    public void setWebView(){

        webLnC.getSettings().setPluginState(WebSettings.PluginState.ON);
        webLnC.getSettings().setAllowFileAccess(true);
        webLnC.setWebViewClient(new WebViewClient());
        webLnC.getSettings().setLoadsImagesAutomatically(true);
        webLnC.getSettings().setJavaScriptEnabled(true);
        webLnC.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

        webLnC.loadUrl(StoreAnalyticsActivity.this.getResources().getString(R.string.base_url)+"v1/index.php/appshopboth/report_chart?token="+token+"&store_id="+store_id+"&from_date="+startDate+"&to_date="+endDate+"&type="+type+"&report_type="+report_type);

    }

    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (!intent.hasExtra("item_category")) {
            } else {
                try{
                    store_id = intent.getStringExtra("item_category");
                    Log.e("storeID", store_id);
                    setWebView();
                    analytics();
                }catch (Exception e){
                }
            }
        }
    };

    private void store_list() {

        appUtils.showProgressBarLoading();

        String REGISTER_URL = StoreAnalyticsActivity.this.getResources().getString(R.string.base_url) + "v1/index.php/appshopboth/store_list";

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

                            mSellingPriseShopAdapter = new SellingPriseShopAdapter(StoreAnalyticsActivity.this, mStockList);
                            mRecShopList.setAdapter(mSellingPriseShopAdapter);

                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(StoreAnalyticsActivity.this, LinearLayoutManager.HORIZONTAL, false);
                            mRecShopList.setLayoutManager(mLayoutManager);
                            mRecShopList.setItemAnimator(new DefaultItemAnimator());

                        }

                        try {
                            store_id = mStockList.get(0).getStore();

                            setWebView();

                            analytics();
                        }catch (Exception e){

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
                params.put(KEY_TOKEN, token);
                params.put("type" , "all");
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void analytics(){

        if (csm.equals("csm")){
            store_id = csm_Storeid ;
        }

        appUtils.showProgressBarLoading();

        group_list.clear();
        reportHeaderList.clear();
        reportFooterList.clear();
        chart_position = 0 ;

        SharedPreferences prefs = StoreAnalyticsActivity.this.getSharedPreferences("Login", Context.MODE_PRIVATE);
        final String token = prefs.getString("token" , "");

        String REGISTER_URL = StoreAnalyticsActivity.this.getResources().getString(R.string.base_url) + "v1/index.php/appshopboth/stock_report_chart";

        final String KEY_TOKEN = "token";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                            //String msg = jsonObject.getString("msg");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Log.d("Json Array", "doInBackground: " + jsonObject);

                        String status = jsonObject.optString("success");

                        if(status.equals("1"))
                        {
                            JSONArray array = null;

                            try {

                                if (id.equals("data_sales")){

                                    JSONObject data_sales_total = jsonObject.getJSONObject("data_sales_total");

                                    try {
                                        v1 = data_sales_total.getString("180ML");
                                        v2 = data_sales_total.getString("375ML");
                                        v3 = data_sales_total.getString("750ML");
                                    }catch (Exception e){

                                    }

                                    JSONArray data_sales_brands = jsonObject.optJSONArray("data_sales_brands");

                                    ReportHeader reportHeader;

                                    for (int i = 0 ; i < data_sales_brands.length() ; i++){

                                        JSONObject object = data_sales_brands.getJSONObject(i);

                                        reportHeader = new ReportHeader();

                                        String category_name = object.getString("category_name");

                                        reportHeader.setCategory_name(category_name);

                                        JSONArray jsonArray = object.getJSONArray("category_brands");

                                        reportFooterList = new ArrayList<>();

                                        for (int j = 0 ; j < jsonArray.length() ; j++){

                                            ReportFooter reportFooter = new ReportFooter();

                                            JSONObject object1 = jsonArray.getJSONObject(j);

                                            String name = object1.getString("name");
                                            String qty = object1.getString("qty");

                                            reportFooter.setName(name);
                                            reportFooter.setQty(qty);

                                            reportFooterList.add(reportFooter);
                                            reportHeader.setReportFooters(reportFooterList);

                                        }

                                        reportHeaderList.add(reportHeader);

                                    }

                                    Log.e("report" , reportHeaderList+"");

                                    JSONArray jArray = jsonObject.optJSONArray("data_sales_category");

                                    mDailySalesList.clear();

                                    for (int j = 0 ; j < jArray.length() ; j++ ){

                                        JSONObject jObject = jArray.optJSONObject(j);

                                        String selling_qty = jObject.optString("selling_qty");
                                        String category_name = jObject.optString("category_name");

                                        DailySales dailySales = new DailySales();

                                        dailySales.setSelling_qty(selling_qty);
                                        dailySales.setCategory_name(category_name);

                                        mDailySalesList.add(dailySales);

                                    }

                                    array = jsonObject.getJSONArray("data_sales");

                                    Group analyticsHeader ;

                                    for(int i=0; i< array.length(); i++)
                                    {
                                        JSONObject c = array.getJSONObject(i);

                                        String category = c.getString("category");
                                        String category_img = c.getString("category_img");

                                        analyticsHeader = new Group();

                                        analyticsHeader.setCategory(category);
                                        analyticsHeader.setCategory_img(getResources().getString(R.string.base_url)+category_img);

                                        JSONArray jExercise = c.getJSONArray("products");

                                        analyticsFooterArrayList = new ArrayList<>();


                                        for(int j=0; j< jExercise.length(); j++)
                                        {
                                            AnalyticsFooter analyticsFooterHeader = new AnalyticsFooter();

                                            child_list = new ArrayList<>();

                                            JSONObject jExerciseObject = jExercise.getJSONObject(j);

                                            String products_name = jExerciseObject.getString("products_name");
                                            JSONArray product_keys = jExerciseObject.getJSONArray("product_keys");

                                            analyticsFooterHeader.setProducts_name(products_name);

                                            String total = "0";


                                            for (int k = 0 ; k < product_keys.length() ; k++){

                                                Child analyticsFooter = new Child();

                                                String value = String.valueOf(product_keys.get(k));

                                                String value2 = jExerciseObject.getString(value);

                                                total = String.valueOf(Integer.parseInt(total) + Integer.parseInt(value2));

                                                analyticsFooter.setProducts_name(products_name);
                                                analyticsFooter.setValue1(value);
                                                analyticsFooter.setValue2(value2);
                                                analyticsFooter.setValue3(total);
                                                analyticsFooterHeader.setValue3(total);

                                                child_list.add(analyticsFooter);

                                            }

                                            analyticsFooterHeader.setChild_list(child_list);

                                            analyticsFooterArrayList.add(analyticsFooterHeader);

                                            analyticsHeader.setAnalyticsFooters(analyticsFooterArrayList);

                                        }

                                        group_list.add(analyticsHeader);

                                    }
                                }else if (id.equals("data_purchases")){

                                    JSONObject data_sales_total = jsonObject.getJSONObject("data_purchase_total");

                                    v1 = data_sales_total.getString("180ML");
                                    v2 = data_sales_total.getString("375ML");
                                    v3 = data_sales_total.getString("750ML");

                                    JSONArray jArray = jsonObject.optJSONArray("data_purchase_category");

                                    mDailySalesList.clear();

                                    for (int j = 0 ; j < jArray.length() ; j++ ){

                                        JSONObject jObject = jArray.optJSONObject(j);

                                        String selling_qty = jObject.optString("selling_qty");
                                        String category_name = jObject.optString("category_name");

                                        DailySales dailySales = new DailySales();

                                        dailySales.setSelling_qty(selling_qty);
                                        dailySales.setCategory_name(category_name);

                                        mDailySalesList.add(dailySales);

                                    }

                                    JSONArray data_sales_brands = jsonObject.optJSONArray("data_purchase_brands");

                                    ReportHeader reportHeader;

                                    for (int i = 0 ; i < data_sales_brands.length() ; i++){

                                        JSONObject object = data_sales_brands.getJSONObject(i);

                                        reportHeader = new ReportHeader();

                                        String category_name = object.getString("category_name");

                                        reportHeader.setCategory_name(category_name);

                                        JSONArray jsonArray = object.getJSONArray("category_brands");

                                        reportFooterList = new ArrayList<>();

                                        for (int j = 0 ; j < jsonArray.length() ; j++){

                                            ReportFooter reportFooter = new ReportFooter();

                                            JSONObject object1 = jsonArray.getJSONObject(j);

                                            String name = object1.getString("name");
                                            String qty = object1.getString("qty");

                                            reportFooter.setName(name);
                                            reportFooter.setQty(qty);

                                            reportFooterList.add(reportFooter);
                                            reportHeader.setReportFooters(reportFooterList);

                                        }

                                        reportHeaderList.add(reportHeader);


                                    }

                                    Log.e("report" , reportHeaderList+"");

                                    array = jsonObject.getJSONArray("data_purchase");

                                    Group analyticsHeader ;

                                    for(int i=0; i< array.length(); i++)
                                    {
                                        JSONObject c = array.getJSONObject(i);

                                        String category = c.getString("category");
                                        String category_img = c.getString("category_img");

                                        analyticsHeader = new Group();

                                        analyticsHeader.setCategory(category);
                                        analyticsHeader.setCategory_img(getResources().getString(R.string.base_url)+category_img);

                                        JSONArray jExercise = c.getJSONArray("products");

                                        analyticsFooterArrayList = new ArrayList<>();


                                        for(int j=0; j< jExercise.length(); j++)
                                        {
                                            AnalyticsFooter analyticsFooterHeader = new AnalyticsFooter();

                                            child_list = new ArrayList<>();

                                            JSONObject jExerciseObject = jExercise.getJSONObject(j);

                                            String products_name = jExerciseObject.getString("products_name");
                                            JSONArray product_keys = jExerciseObject.getJSONArray("product_keys");

                                            analyticsFooterHeader.setProducts_name(products_name);

                                            String total = "0";


                                            for (int k = 0 ; k < product_keys.length() ; k++){

                                                Child analyticsFooter = new Child();

                                                String value = String.valueOf(product_keys.get(k));

                                                String value2 = jExerciseObject.getString(value);

                                                total = String.valueOf(Integer.parseInt(total) + Integer.parseInt(value2));

                                                analyticsFooter.setProducts_name(products_name);
                                                analyticsFooter.setValue1(value);
                                                analyticsFooter.setValue2(value2);
                                                analyticsFooter.setValue3(total);
                                                analyticsFooterHeader.setValue3(total);

                                                child_list.add(analyticsFooter);

                                            }

                                            analyticsFooterHeader.setChild_list(child_list);

                                            analyticsFooterArrayList.add(analyticsFooterHeader);

                                            analyticsHeader.setAnalyticsFooters(analyticsFooterArrayList);

                                        }

                                        group_list.add(analyticsHeader);

                                    }
                                }else if (id.equals("data_current_stock")){

                                    JSONObject data_sales_total = jsonObject.getJSONObject("data_current_stock_total");

                                    v1 = data_sales_total.getString("180ML");
                                    v2 = data_sales_total.getString("375ML");
                                    v3 = data_sales_total.getString("750ML");

                                    array = jsonObject.getJSONArray("data_current_stock");

                                    JSONArray jArray = jsonObject.optJSONArray("data_current_category");

                                    JSONArray data_sales_brands = jsonObject.optJSONArray("data_current_brands");

                                    ReportHeader reportHeader;

                                    for (int i = 0 ; i < data_sales_brands.length() ; i++){

                                        JSONObject object = data_sales_brands.getJSONObject(i);

                                        reportHeader = new ReportHeader();

                                        String category_name = object.getString("category_name");

                                        reportHeader.setCategory_name(category_name);

                                        JSONArray jsonArray = object.getJSONArray("category_brands");

                                        reportFooterList = new ArrayList<>();

                                        for (int j = 0 ; j < jsonArray.length() ; j++){

                                            ReportFooter reportFooter = new ReportFooter();

                                            JSONObject object1 = jsonArray.getJSONObject(j);

                                            String name = object1.getString("name");
                                            String qty = object1.getString("qty");

                                            reportFooter.setName(name);
                                            reportFooter.setQty(qty);

                                            reportFooterList.add(reportFooter);
                                            reportHeader.setReportFooters(reportFooterList);

                                        }

                                        reportHeaderList.add(reportHeader);


                                    }

                                    Log.e("report" , reportHeaderList+"");

                                    /*data_sales_category
                                      data_purchase_category
                                      data_current_category*/

                                    mDailySalesList.clear();

                                    for (int j = 0 ; j < jArray.length() ; j++ ){

                                        JSONObject jObject = jArray.optJSONObject(j);

                                        String selling_qty = jObject.optString("selling_qty");
                                        String category_name = jObject.optString("category_name");

                                        DailySales dailySales = new DailySales();

                                        dailySales.setSelling_qty(selling_qty);
                                        dailySales.setCategory_name(category_name);

                                        mDailySalesList.add(dailySales);

                                    }

                                    Group analyticsHeader ;

                                    for(int i=0; i< array.length(); i++)
                                    {
                                        JSONObject c = array.getJSONObject(i);

                                        String category = c.getString("category");
                                        String category_img = c.getString("category_img");

                                        analyticsHeader = new Group();

                                        analyticsHeader.setCategory(category);
                                        analyticsHeader.setCategory_img(getResources().getString(R.string.base_url)+category_img);

                                        JSONArray jExercise = c.getJSONArray("products");

                                        analyticsFooterArrayList = new ArrayList<>();


                                        for(int j=0; j< jExercise.length(); j++)
                                        {
                                            AnalyticsFooter analyticsFooterHeader = new AnalyticsFooter();

                                            child_list = new ArrayList<>();

                                            JSONObject jExerciseObject = jExercise.getJSONObject(j);

                                            String products_name = jExerciseObject.getString("products_name");
                                            JSONArray product_keys = jExerciseObject.getJSONArray("product_keys");

                                            analyticsFooterHeader.setProducts_name(products_name);

                                            String total = "0";


                                            for (int k = 0 ; k < product_keys.length() ; k++){

                                                Child analyticsFooter = new Child();

                                                String value = String.valueOf(product_keys.get(k));

                                                String value2 = jExerciseObject.getString(value);

                                                total = String.valueOf(Integer.parseInt(total) + Integer.parseInt(value2));

                                                analyticsFooter.setProducts_name(products_name);
                                                analyticsFooter.setValue1(value);
                                                analyticsFooter.setValue2(value2);
                                                analyticsFooter.setValue3(total);
                                                analyticsFooterHeader.setValue3(total);

                                                child_list.add(analyticsFooter);

                                            }

                                            analyticsFooterHeader.setChild_list(child_list);

                                            analyticsFooterArrayList.add(analyticsFooterHeader);

                                            analyticsHeader.setAnalyticsFooters(analyticsFooterArrayList);

                                        }

                                        group_list.add(analyticsHeader);

                                    }
                                }


                                if (reportHeaderList.size() != 0){
                                    bar_chart(chart_position);
                                }/*else {
                                    final AlertDialog.Builder builder = new AlertDialog.Builder(StoreAnalyticsActivity.this);
                                    builder.setMessage(getResources().getString(R.string.no_sale))
                                            .setCancelable(false)
                                            .setPositiveButton(getResources().getString(R.string.okay), new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    //do things
                                                    dialog.dismiss();
                                                }
                                            });
                                    AlertDialog alert = builder.create();
                                    alert.show();
                                }*/
                                expand_recyclerView.setHasFixedSize(true);

                                // vertical RecyclerView
                                // keep movie_list_row.xml width to `match_parent`


                                // adding inbuilt divider line

                                assert expand_recyclerView != null;
                                expand_recyclerView.setLayoutManager(new LinearLayoutManager(StoreAnalyticsActivity.this));
                                final ExpandableTestAdapter testAdapter = new ExpandableTestAdapter(group_list,StoreAnalyticsActivity.this);

                                expand_recyclerView.setAdapter(testAdapter);
                             /*   sectionAdapter = new SectionedRecyclerViewAdapter();

                                for (int i = 0 ; i < group_list.size() ; i++){

                                    sectionAdapter.addSection(new ContactsSection(group_list , i , group_list.get(i).getAnalyticsFooters()));

                                }

                                recyclerView.setLayoutManager(new LinearLayoutManager(StoreAnalyticsActivity.this));
                                recyclerView.setAdapter(sectionAdapter);
                                recyclerView.setNestedScrollingEnabled(false);*/

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
                        //Toast.makeText(MainActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put(KEY_TOKEN,token);
                params.put("from_date",startDate);
                params.put("to_date",endDate);
                params.put("store_id",store_id);
                params.put("type",type);
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(StoreAnalyticsActivity.this);
        requestQueue.add(stringRequest);
    }

    public void AddValuesToBARENTRY(){

        BARENTRY.clear();

        for (int i = 0 ; i<reportHeaderList.size() ; i++){

            for (int j =0 ; j < reportHeaderList.get(i).getReportFooters().size() ; j++){
                BARENTRY.add(new BarEntry(Float.parseFloat(reportHeaderList.get(i).getReportFooters().get(j).getQty()) , j));

            }


        }

    }

    public void AddValuesToBarEntryLabels(){

        BarEntryLabels.clear();

        for (int i = 0 ; i<reportHeaderList.size() ; i++){

            for (int j =0 ; j < reportHeaderList.get(i).getReportFooters().size() ; j++){
                BarEntryLabels.add(reportHeaderList.get(i).getReportFooters().get(j).getName());

            }

        }

    }

    public void bar_chart(int chart_position){

        ArrayList<BarEntry> entries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<String>();

       // for (int i = 0 ; i < 1 ; i++){

        try {
            chart_name = reportHeaderList.get(chart_position).getCategory_name();
            for (int j = 0 ; j < reportHeaderList.get(chart_position).getReportFooters().size() ; j++){

                entries.add(new BarEntry(Float.parseFloat(reportHeaderList.get(chart_position).getReportFooters().get(j).getQty()), j));
                labels.add(reportHeaderList.get(chart_position).getReportFooters().get(j).getName().substring(0,7));
            }

            BarDataSet bardataset = new BarDataSet(entries, chart_name);

        } catch (Exception e) {
            e.printStackTrace();
        }



    }

    /*class ContactsSection extends StatelessSection {

        List<Group> headerList;
        List<AnalyticsFooter> footerList;
        int i ;

        public ContactsSection(List<Group> workoutExercise , int i , List<AnalyticsFooter> list ) {
            super(R.layout.view_header, R.layout._view_anal_footerlist);

            this.headerList = workoutExercise;
            this.footerList = list;
            this.i = i ;
        }

        @Override
        public int getContentItemsTotal() {
            return footerList.size();
        }

        @Override
        public RecyclerView.ViewHolder getItemViewHolder(View view) {
            return new ItemViewHolder(view);
        }

        @Override
        public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {

            final ItemViewHolder itemHolder = (ItemViewHolder) holder;

            final AnalyticsFooter analyticsFooter = footerList.get(position);

                itemHolder.txt4.setText(analyticsFooter.getValue3());

                itemHolder.txtName.setText(analyticsFooter.getProducts_name());

                AnalyticsFooterAdapter startingBrowseCategoryAdapter = new AnalyticsFooterAdapter(StoreAnalyticsActivity.this, analyticsFooter.getChild_list());

                RecyclerView.LayoutManager mLayoutManager1 = new GridLayoutManager(StoreAnalyticsActivity.this, 3);
                itemHolder.recAnalyticsFooter.setLayoutManager(mLayoutManager1);
                itemHolder.recAnalyticsFooter.setAdapter(startingBrowseCategoryAdapter);


        }

        @Override
        public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
            return new HeaderViewHolder(view);
        }

        @Override
        public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
            final HeaderViewHolder headerHolder = (HeaderViewHolder) holder;

            final Group dietHeader = headerList.get(i) ;

            headerHolder.tvTitle.setText(dietHeader.getCategory());

        }
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle  ;

        public HeaderViewHolder(View view) {
            super(view);

            tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        RecyclerView recAnalyticsFooter ;
        public TextView txtName , txt1 , txt2 , txt3 , txt4;
        LinearLayout l1 ;

        public ItemViewHolder(View view) {
            super(view);

            recAnalyticsFooter = (RecyclerView) view.findViewById(R.id.recFooterAnalytics);
            txtName = (TextView) view.findViewById(R.id.txtName);
            txt1 = (TextView) view.findViewById(R.id.txt1);
            txt2 = (TextView) view.findViewById(R.id.txt2);
            txt3 = (TextView) view.findViewById(R.id.txt3);
            txt4 = (TextView) view.findViewById(R.id.txt4);
            l1 = (LinearLayout) view.findViewById(R.id.l1);

        }
    }*/

}
