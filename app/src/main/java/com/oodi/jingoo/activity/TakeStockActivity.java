package com.oodi.jingoo.activity;

import android.app.Activity;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.oodi.jingoo.R;
import com.oodi.jingoo.adapter.SellingPriseShopAdapter;
import com.oodi.jingoo.adapter.StartingStoreListAdapter;
import com.oodi.jingoo.adapter.StockListAdapter;
import com.oodi.jingoo.pojo.Stock;
import com.oodi.jingoo.pojo.Store;
import com.oodi.jingoo.utility.AppUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TakeStockActivity extends AppCompatActivity {

    RecyclerView mRecStockList , mRecShopList;
    StockListAdapter mStockListAdapter ;
    List<Stock> mStockList = new ArrayList<>();
    ImageView mImgBack, mImgWt;
    TextView mTxtHeaderName , mTxtHome , Home;
    public static Activity tackStock ;
    AppUtils appUtils ;
    String token , type = "" , store_id = "" ;
    SellingPriseShopAdapter mSellingPriseShopAdapter;
    List<Store> mStockList1;

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
        setContentView(R.layout.activity_take_stock);

        init();

        ImageView imgHelp = (ImageView) findViewById(R.id.imgHelp);

        ImageView imageView3 = (ImageView) findViewById(R.id.imageView3);

        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //OtherSettingsActivity.otherSetting.finish();
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        imgHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mImgWt.setVisibility(View.VISIBLE);

            }
        });

        if (lang.equals("hi")){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                mImgWt.setBackground(getResources().getDrawable(R.drawable.select_cat_hindi));
            }
        }

        SharedPreferences wt = getSharedPreferences("wt", MODE_PRIVATE);
        String first = wt.getString("select_cat_wt", "");

        if (first.equals("1")){

            mImgWt.setVisibility(View.GONE);

        }

        mImgWt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mImgWt.setVisibility(View.GONE);

                SharedPreferences.Editor editor = getSharedPreferences("wt", MODE_PRIVATE).edit();
                editor.putString("select_cat_wt", "1");

                editor.commit();

            }
        });

        SharedPreferences prefs = this.getSharedPreferences("Login", Context.MODE_PRIVATE);
        token = prefs.getString("token" , "");
        type = prefs.getString("type" , "");

        tackStock = this ;

        if (MainActivity.idx == 1){
            mTxtHome.setText(getResources().getString(R.string.select_category));
            mTxtHeaderName.setText(getResources().getString(R.string.select_category));

        }else {
            mTxtHome.setText(getResources().getString(R.string.select_category));
            mTxtHeaderName.setText(getResources().getString(R.string.select_category));
        }


        Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    //StoreActivity.storeActivity.finish();
                }catch (Exception e){
                }
                tackStock.finish();
            }
        });

        mImgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        if (!appUtils.isOnLine()){
            Intent intent = new Intent(getApplicationContext() , NoInternetActivity.class);
            startActivity(intent);
            //appUtils.showToast(R.string.offline);
        }else {
            if (type.equals("csm")){
                categorieList();
            }else {
                store_list();
            }
        }

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("custom-message"));

    }

    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (!intent.hasExtra("item_category")) {
            } else {
                try{
                    StartingStoreListAdapter.id = intent.getStringExtra("item_category");
                }catch (Exception e){
                }
            }
        }
    };

    public void init(){

        mRecStockList = (RecyclerView) findViewById(R.id.recStockList);
        mImgWt = (ImageView) findViewById(R.id.imgWt);
        mImgBack = (ImageView) findViewById(R.id.imgBack);
        mTxtHeaderName = (TextView) findViewById(R.id.txtHeaderName);
        mTxtHome = (TextView) findViewById(R.id.txtHome);
        Home = (TextView) findViewById(R.id.Home);
        mRecShopList = (RecyclerView) findViewById(R.id.recShopList);

    }

    private void store_list() {

        appUtils.showProgressBarLoading();

        String REGISTER_URL = TakeStockActivity.this.getResources().getString(R.string.base_url) + "v1/index.php/appshopboth/store_list";

        final String KEY_TOKEN = "token";

        mStockList1 = new ArrayList<>();

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

                                    mStockList1.add(store);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }

                        Log.e("list", mStockList1 + "");

                        if (mStockList1.size() != 0){

                            mSellingPriseShopAdapter = new SellingPriseShopAdapter(TakeStockActivity.this, mStockList1);
                            mRecShopList.setAdapter(mSellingPriseShopAdapter);

                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(TakeStockActivity.this, LinearLayoutManager.HORIZONTAL, false);
                            mRecShopList.setLayoutManager(mLayoutManager);
                            mRecShopList.setItemAnimator(new DefaultItemAnimator());

                        }

                        try {
                            store_id = mStockList1.get(0).getStore();

                            StartingStoreListAdapter.id = mStockList1.get(0).getStore();

                            categorieList();
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
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void categorieList(){

        appUtils.showProgressBarLoading();

        String REGISTER_URL = TakeStockActivity.this.getResources().getString(R.string.base_url) + "v1/index.php/appshopboth/categories";

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

                        if(status.equals("1"))
                        {

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

                                    String id = c.getString("id");
                                    String category_name = c.getString("category_name");
                                    String category_image = c.getString("category_image");
                                    String sort_order = c.getString("sort_order");
                                    String is_active = c.getString("is_active");
                                    String created_at = c.getString("created_at");
                                    String last_activity_at = c.getString("last_activity_at");

                                    Stock stock = new Stock();

                                    stock.setCategory_image(TakeStockActivity.this.getResources().getString(R.string.base_url) +category_image);
                                    stock.setCategory_name(category_name);
                                    stock.setId(id);

                                    mStockList.add(stock);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }

                        mStockListAdapter = new StockListAdapter(TakeStockActivity.this , mStockList , 1);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(TakeStockActivity.this);
                        mRecStockList.setLayoutManager(mLayoutManager);
                        mRecStockList.setItemAnimator(new DefaultItemAnimator());
                        mRecStockList.setAdapter(mStockListAdapter);
                        mRecStockList.setNestedScrollingEnabled(false);

                        appUtils.dismissProgressBar();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(MainActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                        appUtils.dismissProgressBar();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put(KEY_TOKEN,token);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

}
