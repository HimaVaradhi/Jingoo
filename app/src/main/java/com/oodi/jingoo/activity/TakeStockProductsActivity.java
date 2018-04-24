package com.oodi.jingoo.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
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
import com.oodi.jingoo.adapter.StartingStoreListAdapter;
import com.oodi.jingoo.adapter.StockListAdapter;
import com.oodi.jingoo.pojo.Stock;
import com.oodi.jingoo.utility.AppUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TakeStockProductsActivity extends AppCompatActivity {

    RecyclerView mRecStockList ;
    StockListAdapter mStockListAdapter ;
    List<Stock> mStockList = new ArrayList<>();
    ArrayList<Stock> matches = new ArrayList<>();
    ImageView mImgBack , mImgWt;
    TextView mTxtHeaderName , mTxtHome , Home , mTxtCancle;
    public static Activity a ;
    AppUtils appUtils ;
    public String token , type = "" , csm_Storeid = "" ;
    EditText mEdtSearch ;
    public static String store_id = "" ;
    LinearLayout mLnrNoResult ;

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
        setContentView(R.layout.activity_take_stock_products);

        SharedPreferences prefs = this.getSharedPreferences("Login", Context.MODE_PRIVATE);
        token = prefs.getString("token" , "");
        type = prefs.getString("type" , "");
        csm_Storeid = prefs.getString("store_id" , "");

        a = this ;

        init();

        ImageView imgHelp = (ImageView) findViewById(R.id.imgHelp);
        imgHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mImgWt.setVisibility(View.VISIBLE);

            }
        });

        //=====================================================================================================================

        if (lang.equals("hi") && type.equals("csm")){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                mImgWt.setBackground(getResources().getDrawable(R.drawable.en_csm_select_brand));
            }
        }else if (lang.equals("en") && type.equals("csm")){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                mImgWt.setBackground(getResources().getDrawable(R.drawable.en_csm_select_brand));
            }
        }

        SharedPreferences wt = getSharedPreferences("wt", MODE_PRIVATE);
        String first = wt.getString("select_brand1", "");

        if (first.equals("1")){

            mImgWt.setVisibility(View.GONE);

        }

        mImgWt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mImgWt.setVisibility(View.GONE);

                SharedPreferences.Editor editor = getSharedPreferences("wt", MODE_PRIVATE).edit();
                editor.putString("select_brand1", "1");

                editor.commit();

            }
        });

        //=====================================================================================================================

        Intent intent = getIntent();
        store_id = intent.getStringExtra("id");

        mEdtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener(){

            @Override
            public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
                if(arg1 == EditorInfo.IME_ACTION_DONE)
                {

                    String text = mEdtSearch.getText().toString().toLowerCase();

                    findMemberByName(text);
                    mTxtCancle.setVisibility(View.VISIBLE);

                    if (matches.isEmpty()){
                        mRecStockList.setVisibility(View.GONE);
                        mLnrNoResult.setVisibility(View.VISIBLE);
                    }else {
                        mStockListAdapter = new StockListAdapter(TakeStockProductsActivity.this , matches , 2);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(TakeStockProductsActivity.this);
                        mRecStockList.setLayoutManager(mLayoutManager);
                        mRecStockList.setItemAnimator(new DefaultItemAnimator());
                        mRecStockList.setAdapter(mStockListAdapter);
                        mRecStockList.setNestedScrollingEnabled(false);
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
                mRecStockList.setVisibility(View.VISIBLE);
                mLnrNoResult.setVisibility(View.GONE);
                //s = "";

                mEdtSearch.setText("");
                mEdtSearch.clearFocus();

                mStockListAdapter = new StockListAdapter(TakeStockProductsActivity.this , mStockList , 2);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(TakeStockProductsActivity.this);
                mRecStockList.setLayoutManager(mLayoutManager);
                mRecStockList.setItemAnimator(new DefaultItemAnimator());
                mRecStockList.setAdapter(mStockListAdapter);
                mRecStockList.setNestedScrollingEnabled(false);

                //categorieList();

            }
        });

        if (MainActivity.idx == 1){
            mTxtHome.setText(getResources().getString(R.string.select_brand));
            mTxtHeaderName.setText(getResources().getString(R.string.select_brand));

        }else {
            mTxtHome.setText(getResources().getString(R.string.select_brand));
            mTxtHeaderName.setText(getResources().getString(R.string.select_brand));
        }


        Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    //StoreActivity.storeActivity.finish();
                    TakeStockActivity.tackStock.finish();
                    TakeStockListActivity.tackStockList.finish();
                    a.finish();
                }catch (Exception e){
                    a.finish();

                }
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
            brand_list();
        }
    }

    public ArrayList<Stock> findMemberByName(String name) {
        //ArrayList<Stock> matches = new ArrayList<Stock>();
        // go through list of members and compare name with given name

        matches.clear();

        for(Stock member : mStockList) {

            if(member.getCategory_name().toLowerCase().contains(name)){
                matches.add(member);
            }

        }
        return matches; // return the matches, which is empty when no member with the given name was found
    }

    public void init(){

        mRecStockList = (RecyclerView) findViewById(R.id.recStockList);
        mImgBack = (ImageView) findViewById(R.id.imgBack);
        mTxtHeaderName = (TextView) findViewById(R.id.txtHeaderName);
        mTxtHome = (TextView) findViewById(R.id.txtHome);
        Home = (TextView) findViewById(R.id.Home);
        mEdtSearch = (EditText) findViewById(R.id.edtSearch);
        mTxtCancle = (TextView) findViewById(R.id.txtCancle);
        mLnrNoResult = (LinearLayout) findViewById(R.id.lnrNoResult);
        mImgWt = (ImageView) findViewById(R.id.imgWt);

    }

    private void categorieList(){

        appUtils.showProgressBarLoading();

        String REGISTER_URL = TakeStockProductsActivity.this.getResources().getString(R.string.base_url) + "v1/index.php/appshopboth/categories";

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

                                    stock.setCategory_image(TakeStockProductsActivity.this.getResources().getString(R.string.base_url) +category_image);
                                    stock.setCategory_name(category_name);
                                    stock.setId(id);

                                    mStockList.add(stock);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }

                        mStockListAdapter = new StockListAdapter(TakeStockProductsActivity.this , mStockList , 2);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(TakeStockProductsActivity.this);
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

    private void brand_list(){

        mStockList.clear();

        if (type.equals("csm")){
            StartingStoreListAdapter.id = csm_Storeid ;
        }

        final ProgressDialog pd = new ProgressDialog(TakeStockProductsActivity.this);
        pd.setMessage("loading");
        pd.show();

        String REGISTER_URL = TakeStockProductsActivity.this.getResources().getString(R.string.base_url) + "v1/index.php/appshopboth/brand_list_for_products";

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

                            JSONArray object = null;
                            try {
                                object = jsonObject.getJSONArray("data");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            for (int i = 0; i < object.length(); i++) {

                                try {
                                    JSONObject jsonObject1 = object.getJSONObject(i);

                                    String id = jsonObject1.getString("id");
                                    String brand_name = jsonObject1.getString("brand_name");
                                    String brand_image = jsonObject1.getString("brand_image");
                                    String is_selected = jsonObject1.getString("is_selected");

                                    Stock stock = new Stock();

                                    stock.setCategory_image(TakeStockProductsActivity.this.getResources().getString(R.string.base_url) +brand_image);
                                    stock.setCategory_name(brand_name);
                                    stock.setId(id);

                                    mStockList.add(stock);

                                  /*  Supplier supplier = new Supplier();

                                    supplier.setId(id);
                                    supplier.setBrand_name(brand_name);
                                    supplier.setBrand_image(getResources().getString(R.string.base_url)+brand_image);
                                    supplier.setIs_selected(is_selected);

                                    if (is_selected.equals("0")){
                                        supplier.setSelected(false);
                                    }else {
                                        supplier.setSelected(true);
                                    }

                                    mSupplierList.add(supplier);*/

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }



                            mStockListAdapter = new StockListAdapter(TakeStockProductsActivity.this , mStockList , 2);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(TakeStockProductsActivity.this);
                            mRecStockList.setLayoutManager(mLayoutManager);
                            mRecStockList.setItemAnimator(new DefaultItemAnimator());
                            mRecStockList.setAdapter(mStockListAdapter);
                            mRecStockList.setNestedScrollingEnabled(false);
                        }

                        if (mStockList.size() == 0){

                            Toast.makeText(TakeStockProductsActivity.this , "Oops! It looks like you have not yet assigned any brands! Go to settings -> other settings to assign the brands you stock!" , Toast.LENGTH_LONG).show();

                        }

                        pd.dismiss();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(MainActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                        pd.dismiss();

                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();

                params.put("token" , token);
                params.put("store_id" , StartingStoreListAdapter.id);
                params.put("category_id", TakeStockProductsActivity.store_id);

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

}
