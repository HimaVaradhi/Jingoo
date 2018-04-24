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
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.oodi.jingoo.adapter.SellingPriseShopAdapter;
import com.oodi.jingoo.pojo.ChildReport;
import com.oodi.jingoo.pojo.Group;
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

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;

public class TopSellingVariantReportActivity extends AppCompatActivity {

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

    EditText mEdtSearch ;

    public String type = "" , csm = "";

    ArrayList<Group> group_list = new ArrayList<Group>();
    ArrayList<ChildReport> child_list;

    private SectionedRecyclerViewAdapter sectionAdapter;
    RecyclerView recyclerView ;

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
        setContentView(R.layout.activity_top_selling_variant_report);

        init();

        ImageView imageView3 = (ImageView) findViewById(R.id.imageView3);

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

        ImageView imgHelp = (ImageView) findViewById(R.id.imgHelp);
        imgHelp.setVisibility(View.GONE);
        mTxtHome.setVisibility(View.VISIBLE);
        Home.setVisibility(View.VISIBLE);

        Intent intent = getIntent();
        type = intent.getStringExtra("type");

        SharedPreferences prefs = this.getSharedPreferences("Login", Context.MODE_PRIVATE);
        token = prefs.getString("token" , "");
        store_id = prefs.getString("store_id" , "");
        csm = prefs.getString("type", "");

        mTxtHome.setText(getResources().getString(R.string.Top_Selling_Variant));
        mTxtHeaderName.setText(getResources().getString(R.string.Top_Selling_Variant));

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
                top_selling_report();
            }else {
                store_list();
            }
        }


        mTxtStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(TopSellingVariantReportActivity.this,
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
                                    top_selling_report();
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

                DatePickerDialog datePickerDialog = new DatePickerDialog(TopSellingVariantReportActivity.this,
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
                                    top_selling_report();
                                }

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
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
        mRecShopList = (RecyclerView) findViewById(R.id.recShopList);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);

        mEdtSearch = (EditText) findViewById(R.id.edtSearch);
        mTxtCancle = (TextView) findViewById(R.id.txtCancle);
        mTxtPurchase = (TextView) findViewById(R.id.txtPurchase);

    }

    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (!intent.hasExtra("item_category")) {
            } else {
                try{
                    storeId = intent.getStringExtra("item_category");
                    Log.e("storeID", storeId);

                    top_selling_report();

                }catch (Exception e){
                }
            }
        }
    };

    private void store_list() {

        appUtils.showProgressBarLoading();

        String REGISTER_URL = TopSellingVariantReportActivity.this.getResources().getString(R.string.base_url) + "v1/index.php/appshopboth/store_list";

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

                            mSellingPriseShopAdapter = new SellingPriseShopAdapter(TopSellingVariantReportActivity.this, mStockList);
                            mRecShopList.setAdapter(mSellingPriseShopAdapter);

                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(TopSellingVariantReportActivity.this, LinearLayoutManager.HORIZONTAL, false);
                            mRecShopList.setLayoutManager(mLayoutManager);
                            mRecShopList.setItemAnimator(new DefaultItemAnimator());

                        }

                        appUtils.dismissProgressBar();

                        try {
                            storeId = mStockList.get(0).getStore();

                            top_selling_report();
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
                params.put("type" , "all");
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void top_selling_report(){

        appUtils.showProgressBarLoading();

        if (csm.equals("csm")){

            storeId = store_id ;

        }

        group_list = new ArrayList<>();
        child_list = new ArrayList<>();

        SharedPreferences prefs = TopSellingVariantReportActivity.this.getSharedPreferences("Login", Context.MODE_PRIVATE);
        final String token = prefs.getString("token" , "");

        String REGISTER_URL = TopSellingVariantReportActivity.this.getResources().getString(R.string.base_url) + "v1/index.php/appshopboth/top_selling_report";

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
                                    array = jsonObject.getJSONArray("row_array_topsell");

                                    Group analyticsHeader ;

                                    for(int i=0; i< array.length(); i++)
                                    {
                                        JSONObject c = array.getJSONObject(i);

                                        String category = c.getString("category");

                                        analyticsHeader = new Group();

                                        analyticsHeader.setCategory(category);

                                        JSONArray jExercise = c.getJSONArray("products");

                                        child_list = new ArrayList<>();

                                        for(int j=0; j< jExercise.length(); j++)
                                        {
                                            JSONObject jExerciseObject = jExercise.getJSONObject(j);

                                            String product_id = jExerciseObject.getString("product_id");
                                            String products_name = jExerciseObject.getString("products_name");
                                            String products_size = jExerciseObject.getString("products_size");
                                            String category_name = jExerciseObject.getString("category_name");
                                            String selling_qty = jExerciseObject.getString("selling_qty");

                                            ChildReport analyticsFooter = new ChildReport();

                                            analyticsFooter.setProduct_id(product_id);
                                            analyticsFooter.setProducts_name(products_name);
                                            analyticsFooter.setProducts_size(products_size);
                                            analyticsFooter.setCategory_name(category_name);
                                            analyticsFooter.setSelling_qty(selling_qty);

                                            child_list.add(analyticsFooter);

                                            analyticsHeader.setChildReports(child_list);

                                        }

                                        group_list.add(analyticsHeader);

                                    }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }else {
                            Toast.makeText(TopSellingVariantReportActivity.this,jsonObject.optString("msg"),Toast.LENGTH_LONG).show();
                        }

                        sectionAdapter = new SectionedRecyclerViewAdapter();

                        for (int i = 0 ; i < group_list.size() ; i++){

                            sectionAdapter.addSection(new ContactsSection(group_list , i , group_list.get(i).getChildReports()));

                        }

                        recyclerView.setLayoutManager(new LinearLayoutManager(TopSellingVariantReportActivity.this));
                        recyclerView.setAdapter(sectionAdapter);

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
                params.put("store_id",storeId);
                params.put("from_date",startDate);
                params.put("to_date",endDate);
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(TopSellingVariantReportActivity.this);
        requestQueue.add(stringRequest);
    }

    class ContactsSection extends StatelessSection {

        List<Group> headerList;
        List<ChildReport> footerList;
        int i ;

        public ContactsSection(List<Group> workoutExercise , int i , List<ChildReport> list ) {
            super(R.layout.view_header, R.layout.view_top_report);

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

            final ChildReport analyticsFooter = footerList.get(position);

            itemHolder.txtName.setText(analyticsFooter.getProducts_name());
            itemHolder.txtWinner.setText(analyticsFooter.getSelling_qty() + " Bottles sold");

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

        public TextView txtName , txtWinner;

        public ItemViewHolder(View view) {
            super(view);

            txtName = (TextView) view.findViewById(R.id.txtName);
            txtWinner = (TextView) view.findViewById(R.id.txtWinner);

        }
    }

}