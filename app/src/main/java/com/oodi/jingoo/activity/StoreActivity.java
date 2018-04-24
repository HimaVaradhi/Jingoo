package com.oodi.jingoo.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.oodi.jingoo.R;
import com.oodi.jingoo.adapter.StartingStoreListAdapter;
import com.oodi.jingoo.pojo.Store;
import com.oodi.jingoo.utility.AppUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StoreActivity extends AppCompatActivity {

    RecyclerView mRecStockList ;
    StartingStoreListAdapter mStockListAdapter ;
    List<Store> mStockList = new ArrayList<>();
    ImageView mImgBack ;
    Button mBtnManageStore ;
    TextView mTxtHeaderName , mTxtHome , Home;
    public static Activity storeActivity ;
    AppUtils appUtils ;
    public String token , type;

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
        setContentView(R.layout.activity_store);

        Intent intent = getIntent();
        type = intent.getStringExtra("type");

        init();

        SharedPreferences prefs = this.getSharedPreferences("Login", Context.MODE_PRIVATE);
        token = prefs.getString("token" , "");

        if (MainActivity.idx == 1){
            mTxtHome.setText(getResources().getString(R.string.take_stock));

        }else {
            mTxtHome.setText(getResources().getString(R.string.ANALYTICS));
        }

        if (type.equals("setting")){
            mTxtHome.setText(getResources().getString(R.string.settings));

        }
        storeActivity = this ;

        Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    SettingsNewActivity.settingNew.finish();
                    OtherSettingsActivity.otherSetting.finish();
                }catch (Exception e){

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

        mBtnManageStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StoreActivity.this , ManageStoreActivity.class);
                startActivity(intent);
            }
        });

        mTxtHeaderName.setText(getResources().getString(R.string.choose_store));

        if (!appUtils.isOnLine()){
            Intent intent1 = new Intent(getApplicationContext() , NoInternetActivity.class);
            startActivity(intent1);
            //appUtils.showToast(R.string.offline);
        }else {
            store_list();
        }

    }

    public void init(){

        mRecStockList = (RecyclerView) findViewById(R.id.recStockList);
        mImgBack = (ImageView) findViewById(R.id.imgBack);
        mBtnManageStore = (Button) findViewById(R.id.btnManageStore);
        mTxtHeaderName = (TextView) findViewById(R.id.txtHeaderName);
        mTxtHome = (TextView) findViewById(R.id.txtHome);
        Home = (TextView) findViewById(R.id.Home);

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void store_list(){

       appUtils.showProgressBarLoading();

        String REGISTER_URL = StoreActivity.this.getResources().getString(R.string.base_url) + "v1/index.php/appshopboth/store_list";

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

                            mStockList = new ArrayList<>();

                            for (int i = 0; i < array.length(); i++) {

                                JSONObject c = null;
                                try {

                                    c = array.getJSONObject(i);

                                    String name = c.getString("name");
                                    String id = c.getString("id");

                                    Store store = new Store();

                                    store.setName(name);
                                    store.setStore(id);

                                    mStockList.add(store);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }

                        mStockListAdapter = new StartingStoreListAdapter(StoreActivity.this , mStockList);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(StoreActivity.this);
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
