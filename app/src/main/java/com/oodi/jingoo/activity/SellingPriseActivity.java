package com.oodi.jingoo.activity;

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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
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
import com.oodi.jingoo.adapter.SellingPriseUpdateAdapter;
import com.oodi.jingoo.adapter.SellingPriseUpdateNewAdapter;
import com.oodi.jingoo.adapter.SupplierStoreAdapter;
import com.oodi.jingoo.pojo.Child;
import com.oodi.jingoo.pojo.SellingPrise;
import com.oodi.jingoo.pojo.SellingProductName;
import com.oodi.jingoo.pojo.Store;
import com.oodi.jingoo.utility.AppUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SellingPriseActivity extends AppCompatActivity {

    TextView mTxtHeaderName, mTxtHome , mTxtCancle , Home , mTxtDoItLater , mTxtCopy;
    ImageView mImgBack , mImgWt;
    AppUtils appUtils;
    RecyclerView mRecShopList, mRecSellingPrise;
    List<Store> mStockList;
    ArrayList<SellingProductName> matches = new ArrayList<>();
    List<SellingProductName> sellingProductNameList = new ArrayList<>();
    String token , csm_Storeid = "" ;
    SellingPriseShopAdapter mSellingPriseShopAdapter;
    SellingPriseUpdateAdapter mSellingPriseUpdateAdapter;
    List<SellingPrise> mSellingPriseList = new ArrayList<>();
    String storeId = "", type = "" , store_id_temp = "";
    String csm = "";
    Button mBtnUpdate ;
    JSONArray jsonArray ;
    EditText mEdtSearch ;

    SellingPriseUpdateNewAdapter sellingPriseUpdateNewAdapter;
    android.app.AlertDialog alertDialog ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appUtils = new AppUtils(this);
        SharedPreferences prefs1 = getSharedPreferences("language", MODE_PRIVATE);
        String lang = prefs1.getString("lang", "");
        if (lang.equals("hi")) {
            appUtils.setLocale("hi");
        } else {
            appUtils.setLocale("en");
        }
        setContentView(R.layout.activity_selling_prise);

        ImageView imageView3 = (ImageView) findViewById(R.id.imageView3);

        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    SettingsNewActivity.settingNew.finish();
                    //OtherSettingsActivity.otherSetting.finish();
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        SharedPreferences prefs = this.getSharedPreferences("Login", Context.MODE_PRIVATE);
        token = prefs.getString("token", "");
        csm_Storeid = prefs.getString("store_id" , "");
        csm = prefs.getString("type" , "");

        init();

        ImageView imgHelp = (ImageView) findViewById(R.id.imgHelp);
        imgHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mBtnUpdate.setVisibility(View.INVISIBLE);
                mImgWt.setVisibility(View.VISIBLE);

            }
        });

        try{
            Intent intent = getIntent();
            type = intent.getStringExtra("type");
            if (type.equals("new")){

                //ImageView imageView3 = (ImageView) findViewById(R.id.imageView3);

                mTxtHome.setVisibility(View.GONE);
                mImgBack.setVisibility(View.GONE);
                imageView3.setVisibility(View.GONE);

                mTxtDoItLater.setVisibility(View.VISIBLE);

            }

        }catch (Exception e){
            type = "" ;
        }

        if (lang.equals("hi")){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                mImgWt.setBackground(getResources().getDrawable(R.drawable.hi_selling_price_new));
            }
        }

        SharedPreferences wt = getSharedPreferences("wt", MODE_PRIVATE);
        String first = wt.getString("selling_wt", "");

        if (first.equals("1")){

            mImgWt.setVisibility(View.GONE);
            mBtnUpdate.setVisibility(View.VISIBLE);

        }

        Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingsNewActivity.settingNew.finish();
                OtherSettingsActivity.otherSetting.finish();
                finish();
            }
        });

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
            if (csm.equals("csm")){
                get_products_selling_price();

            }else {
                store_list();

            }
        }

        if (type.equals("new")){
            mTxtHeaderName.setText(getResources().getString(R.string.Set_Selling_Price));
            mTxtHome.setText(getResources().getString(R.string.Set_Selling_Price));
        }else {
            mTxtHeaderName.setText(getResources().getString(R.string.Set_Selling_Price_));
            mTxtHome.setText(getResources().getString(R.string.Set_Selling_Price_));
        }


        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("custom-message"));

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver1,
                new IntentFilter("msgto"));

        mBtnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                jArray();

            }
        });

        mTxtDoItLater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SellingPriseActivity.this , AddTeamActivity.class);
                intent.putExtra("type" , "new");
                intent.putExtra("id" , "add");
                startActivity(intent);
                finish();
            }
        });

        mEdtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener(){

            @Override
            public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
                if(arg1 == EditorInfo.IME_ACTION_DONE)
                {

                    findMemberByName(mEdtSearch.getText().toString().toLowerCase());
                    mTxtCancle.setVisibility(View.VISIBLE);

                    if (matches.isEmpty()){

                        Toast.makeText(SellingPriseActivity.this , "Please try again",Toast.LENGTH_LONG).show();

                    }else {

                        sellingPriseUpdateNewAdapter = new SellingPriseUpdateNewAdapter(SellingPriseActivity.this, matches);

                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(SellingPriseActivity.this);
                        mRecSellingPrise.setLayoutManager(mLayoutManager);
                        mRecSellingPrise.setItemAnimator(new DefaultItemAnimator());
                        mRecSellingPrise.setAdapter(sellingPriseUpdateNewAdapter);

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

                sellingPriseUpdateNewAdapter = new SellingPriseUpdateNewAdapter(SellingPriseActivity.this, sellingProductNameList);

                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(SellingPriseActivity.this);
                mRecSellingPrise.setLayoutManager(mLayoutManager);
                mRecSellingPrise.setItemAnimator(new DefaultItemAnimator());
                mRecSellingPrise.setAdapter(sellingPriseUpdateNewAdapter);

                //categorieList();

            }
        });

        mImgWt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mImgWt.setVisibility(View.GONE);
                mBtnUpdate.setVisibility(View.VISIBLE);

                SharedPreferences.Editor editor = getSharedPreferences("wt", MODE_PRIVATE).edit();
                editor.putString("selling_wt", "1");

                editor.commit();

            }
        });

        mTxtCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                store();

            }
        });

    }

    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (!intent.hasExtra("item_category")) {
            } else {
                try{
                    storeId = intent.getStringExtra("item_category");
                    Log.e("storeID", storeId);
                    get_products_selling_price();
                }catch (Exception e){
                }
            }
        }
    };

    public BroadcastReceiver mMessageReceiver1 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.hasExtra("msgto")){
            }
            else {
                store_id_temp = intent.getStringExtra("id");
                get_products_selling_price_temp();
                try {
                    alertDialog.dismiss();
                }catch (Exception e){

                }

            }
        }
    };

    public ArrayList<SellingProductName> findMemberByName(String name) {
        //ArrayList<Stock> matches = new ArrayList<Stock>();
        // go through list of members and compare name with given name

        matches.clear();

        for(SellingProductName member : sellingProductNameList) {

            if(member.getName().toLowerCase().contains(name)){
                matches.add(member);
            }

        }
        return matches; // return the matches, which is empty when no member with the given name was found
    }

    public void jArray() {

        JSONObject student1 = null;
        jsonArray = new JSONArray();

        boolean flag = false;

        for (int i = 0; i < sellingProductNameList.size(); i++) {

            for (int j = 0 ; j < sellingProductNameList.get(i).getChildList().size() ; j++){

                try {

                    if (store_id_temp.equals("")) {

                        if (!sellingProductNameList.get(i).getChildList().get(j).getProduct_price()
                                .equals(sellingProductNameList.get(i).getChildList().get(j).getNew_price())) {
                            //Log.e("0", "ID = " + sellingProductNameList.get(i).getName());

                            student1 = new JSONObject();

                            student1.put("id", sellingProductNameList.get(i).getSellingPriseList().get(j).getId());
                            student1.put("category_id", sellingProductNameList.get(i).getSellingPriseList().get(j).getCategory_id());
                            student1.put("company_id", sellingProductNameList.get(i).getSellingPriseList().get(j).getCompany_id());
                            student1.put("brand_id", sellingProductNameList.get(i).getSellingPriseList().get(j).getBrand_id());
                            student1.put("products_name", sellingProductNameList.get(i).getSellingPriseList().get(j).getProducts_name());
                            student1.put("products_size", sellingProductNameList.get(i).getSellingPriseList().get(j).getProducts_size());
                            student1.put("state", sellingProductNameList.get(i).getSellingPriseList().get(j).getState());
                            student1.put("product_price", sellingProductNameList.get(i).getSellingPriseList().get(j).getNew_price());

                            jsonArray.put(student1);

                        }
                    }else {


                        student1 = new JSONObject();

                        student1.put("id", sellingProductNameList.get(i).getSellingPriseList().get(j).getId());
                        student1.put("category_id", sellingProductNameList.get(i).getSellingPriseList().get(j).getCategory_id());
                        student1.put("company_id", sellingProductNameList.get(i).getSellingPriseList().get(j).getCompany_id());
                        student1.put("brand_id", sellingProductNameList.get(i).getSellingPriseList().get(j).getBrand_id());
                        student1.put("products_name", sellingProductNameList.get(i).getSellingPriseList().get(j).getProducts_name());
                        student1.put("products_size", sellingProductNameList.get(i).getSellingPriseList().get(j).getProducts_size());
                        student1.put("state", sellingProductNameList.get(i).getSellingPriseList().get(j).getState());
                        student1.put("product_price", sellingProductNameList.get(i).getSellingPriseList().get(j).getNew_price());

                        jsonArray.put(student1);

                    }

                } catch (Exception e) {

                }

            }

        }

       /* try {

            for (int i = 0; i < sellingProductNameList.size(); i++) {

                try {
                    if (!sellingProductNameList.get(i).getSellingPriseList().get(0).getProduct_price()
                            .equals(sellingProductNameList.get(i).getSellingPriseList().get(0).getNew_price())) {
                        Log.e("0", "ID = " + sellingProductNameList.get(i).getName());

                        student1 = new JSONObject();

                        student1.put("id", sellingProductNameList.get(i).getSellingPriseList().get(0).getId());
                        student1.put("category_id", sellingProductNameList.get(i).getSellingPriseList().get(0).getCategory_id());
                        student1.put("company_id", sellingProductNameList.get(i).getSellingPriseList().get(0).getCompany_id());
                        student1.put("brand_id", sellingProductNameList.get(i).getSellingPriseList().get(0).getBrand_id());
                        student1.put("products_name", sellingProductNameList.get(i).getSellingPriseList().get(0).getProducts_name());
                        student1.put("products_size", sellingProductNameList.get(i).getSellingPriseList().get(0).getProducts_size());
                        student1.put("state", sellingProductNameList.get(i).getSellingPriseList().get(0).getState());
                        student1.put("product_price", sellingProductNameList.get(i).getSellingPriseList().get(0).getNew_price());

                        jsonArray.put(student1);

                    }

                } catch (Exception e) {

                }

                try {
                    if (!sellingProductNameList.get(i).getSellingPriseList().get(1).getProduct_price()
                            .equals(sellingProductNameList.get(i).getSellingPriseList().get(1).getNew_price())) {
                        Log.e("1", "ID = " + sellingProductNameList.get(i).getName());
                        student1 = new JSONObject();

                        student1.put("id", sellingProductNameList.get(i).getSellingPriseList().get(1).getId());
                        student1.put("category_id", sellingProductNameList.get(i).getSellingPriseList().get(1).getCategory_id());
                        student1.put("company_id", sellingProductNameList.get(i).getSellingPriseList().get(1).getCompany_id());
                        student1.put("brand_id", sellingProductNameList.get(i).getSellingPriseList().get(1).getBrand_id());
                        student1.put("products_name", sellingProductNameList.get(i).getSellingPriseList().get(1).getProducts_name());
                        student1.put("products_size", sellingProductNameList.get(i).getSellingPriseList().get(1).getProducts_size());
                        student1.put("state", sellingProductNameList.get(i).getSellingPriseList().get(1).getState());
                        student1.put("product_price", sellingProductNameList.get(i).getSellingPriseList().get(1).getNew_price());

                        jsonArray.put(student1);

                    }

                } catch (Exception e) {

                }

                try {
                    if (!sellingProductNameList.get(i).getSellingPriseList().get(2).getProduct_price()
                            .equals(sellingProductNameList.get(i).getSellingPriseList().get(2).getNew_price())) {
                        Log.e("2", "ID = " + sellingProductNameList.get(i).getName());

                        student1 = new JSONObject();

                        student1.put("id", sellingProductNameList.get(i).getSellingPriseList().get(2).getId());
                        student1.put("category_id", sellingProductNameList.get(i).getSellingPriseList().get(2).getCategory_id());
                        student1.put("company_id", sellingProductNameList.get(i).getSellingPriseList().get(2).getCompany_id());
                        student1.put("brand_id", sellingProductNameList.get(i).getSellingPriseList().get(2).getBrand_id());
                        student1.put("products_name", sellingProductNameList.get(i).getSellingPriseList().get(2).getProducts_name());
                        student1.put("products_size", sellingProductNameList.get(i).getSellingPriseList().get(2).getProducts_size());
                        student1.put("state", sellingProductNameList.get(i).getSellingPriseList().get(2).getState());
                        student1.put("product_price", sellingProductNameList.get(i).getSellingPriseList().get(2).getNew_price());

                        jsonArray.put(student1);
                    }
                } catch (Exception e) {

                }

            }


        } catch (Exception e) {
        }*/

        Log.e("ja" , jsonArray.toString());

        update_products_selling_price();
    }

    public void init() {

        mTxtHeaderName = (TextView) findViewById(R.id.txtHeaderName);
        mTxtHome = (TextView) findViewById(R.id.txtHome);
        mImgBack = (ImageView) findViewById(R.id.imgBack);
        mRecShopList = (RecyclerView) findViewById(R.id.recShopList);
        mRecSellingPrise = (RecyclerView) findViewById(R.id.recSellingPrise);
        mBtnUpdate = (Button) findViewById(R.id.btnUpdate);
        mEdtSearch = (EditText) findViewById(R.id.edtSearch);
        mTxtCancle = (TextView) findViewById(R.id.txtCancle);
        Home = (TextView) findViewById(R.id.Home);
        mImgWt = (ImageView) findViewById(R.id.imgWt);
        mTxtDoItLater = (TextView) findViewById(R.id.txtDoItLater);
        mTxtCopy = (TextView) findViewById(R.id.txtCopy);


    }

    private void store_list() {

        appUtils.showProgressBarLoading();

        String REGISTER_URL = SellingPriseActivity.this.getResources().getString(R.string.base_url) + "v1/index.php/appshopboth/store_list";

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

                            mSellingPriseShopAdapter = new SellingPriseShopAdapter(SellingPriseActivity.this, mStockList);
                            mRecShopList.setAdapter(mSellingPriseShopAdapter);

                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(SellingPriseActivity.this, LinearLayoutManager.HORIZONTAL, false);
                            mRecShopList.setLayoutManager(mLayoutManager);
                            mRecShopList.setItemAnimator(new DefaultItemAnimator());

                        }

                        try {
                            appUtils.dismissProgressBar();

                            storeId = mStockList.get(0).getStore();

                            get_products_selling_price();
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

    private void get_products_selling_price() {

        appUtils.showProgressBarLoading();

        sellingProductNameList.clear();
        mSellingPriseList.clear();

        if (csm.equals("csm")){
            storeId = csm_Storeid ;
        }

        String REGISTER_URL = SellingPriseActivity.this.getResources().getString(R.string.base_url) + "v1/index.php/appshopboth/get_products_selling_price";

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
                                array = jsonObject.getJSONArray("data");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            //mStockList = new ArrayList<>();

                            for (int i = 0; i < array.length(); i++) {

                                JSONObject c = null;
                                try {

                                    c = array.getJSONObject(i);

                                    String products_name = c.getString("products_name");

                                    SellingProductName sellingProductName = new SellingProductName();

                                    sellingProductName.setName(products_name);

                                    SellingPrise sellingPrise = null;

                                    sellingPrise = new SellingPrise();

                                    mSellingPriseList = new ArrayList<>();

                                    JSONArray product_keys = c.getJSONArray("product_keys");

                                    ArrayList<Child> childArrayList = new ArrayList<>();

                                    Child child = new Child();

                                    for (int k = 0 ; k < product_keys.length() ; k++){


                                        String value = String.valueOf(product_keys.get(k));

                                        Log.e("value----------" , value);

                                        try {
                                            JSONObject object = c.getJSONObject(value);

                                            String id = object.getString("id");
                                            String category_id = object.getString("category_id");
                                            String company_id = object.getString("company_id");
                                            String brand_id = object.getString("brand_id");
                                            String _products_name = object.getString("products_name");
                                            String products_size = object.getString("products_size");
                                            String state = object.getString("state");
                                            String product_price = object.getString("product_price");

                                            sellingPrise.setId(id);
                                            sellingPrise.setCategory_id(category_id);
                                            sellingPrise.setCompany_id(company_id);
                                            sellingPrise.setBrand_id(brand_id);
                                            sellingPrise.setProducts_name(_products_name);
                                            sellingPrise.setProducts_size(products_size);
                                            sellingPrise.setState(state);
                                            sellingPrise.setProduct_price(product_price);
                                            sellingPrise.setNew_price(product_price);

                                            child = new Child();

                                            child.setValue1(value);
                                            child.setValue2(product_price);
                                            child.setValue3(product_price);
                                            child.setNew_price(product_price);
                                            child.setId(id);
                                            child.setCategory_id(category_id);
                                            child.setCompany_id(company_id);
                                            child.setBrand_id(brand_id);
                                            child.setProducts_name(_products_name);
                                            child.setProducts_size(products_size);
                                            child.setState(state);
                                            child.setProduct_price(product_price);

                                            childArrayList.add(child);

                                        }catch (Exception e){

                                        }

                                    }

                                    sellingPrise.setChildList(childArrayList);

                                    mSellingPriseList.add(sellingPrise);

                                    sellingProductName.setSellingPriseList(mSellingPriseList);

                                    sellingProductName.setChildList(childArrayList);

                                    sellingProductNameList.add(sellingProductName);

                                 /*   try {
                                        JSONObject object = c.getJSONObject("750 ML");

                                        String id = object.getString("id");
                                        String category_id = object.getString("category_id");
                                        String company_id = object.getString("company_id");
                                        String brand_id = object.getString("brand_id");
                                        String _products_name = object.getString("products_name");
                                        String products_size = object.getString("products_size");
                                        String state = object.getString("state");
                                        String product_price = object.getString("product_price");

                                        sellingPrise = new SellingPrise();

                                        sellingPrise.setId(id);
                                        sellingPrise.setCategory_id(category_id);
                                        sellingPrise.setCompany_id(company_id);
                                        sellingPrise.setBrand_id(brand_id);
                                        sellingPrise.setProducts_name(_products_name);
                                        sellingPrise.setProducts_size(products_size);
                                        sellingPrise.setState(state);
                                        sellingPrise.setProduct_price(product_price);
                                        sellingPrise.setNew_price(product_price);

                                        mSellingPriseList.add(sellingPrise);

                                    }catch (Exception e){

                                    }

                                    try {
                                        JSONObject object = c.getJSONObject("375 ML");

                                        String id = object.getString("id");
                                        String category_id = object.getString("category_id");
                                        String company_id = object.getString("company_id");
                                        String brand_id = object.getString("brand_id");
                                        String _products_name = object.getString("products_name");
                                        String products_size = object.getString("products_size");
                                        String state = object.getString("state");
                                        String product_price = object.getString("product_price");

                                        sellingPrise = new SellingPrise();

                                        sellingPrise.setId(id);
                                        sellingPrise.setCategory_id(category_id);
                                        sellingPrise.setCompany_id(company_id);
                                        sellingPrise.setBrand_id(brand_id);
                                        sellingPrise.setProducts_name(_products_name);
                                        sellingPrise.setProducts_size(products_size);
                                        sellingPrise.setState(state);
                                        sellingPrise.setProduct_price(product_price);
                                        sellingPrise.setNew_price(product_price);

                                        mSellingPriseList.add(sellingPrise);

                                    }catch (Exception e){

                                    }

                                    try {
                                        JSONObject object = c.getJSONObject("180 ML");

                                        String id = object.getString("id");
                                        String category_id = object.getString("category_id");
                                        String company_id = object.getString("company_id");
                                        String brand_id = object.getString("brand_id");
                                        String _products_name = object.getString("products_name");
                                        String products_size = object.getString("products_size");
                                        String state = object.getString("state");
                                        String product_price = object.getString("product_price");

                                        sellingPrise = new SellingPrise();

                                        sellingPrise.setId(id);
                                        sellingPrise.setCategory_id(category_id);
                                        sellingPrise.setCompany_id(company_id);
                                        sellingPrise.setBrand_id(brand_id);
                                        sellingPrise.setProducts_name(_products_name);
                                        sellingPrise.setProducts_size(products_size);
                                        sellingPrise.setState(state);
                                        sellingPrise.setProduct_price(product_price);
                                        sellingPrise.setNew_price(product_price);


                                        mSellingPriseList.add(sellingPrise);

                                    }catch (Exception e){

                                    }*/




                                } catch (Exception e) {

                                }

                        }

                        sellingPriseUpdateNewAdapter = new SellingPriseUpdateNewAdapter(SellingPriseActivity.this, sellingProductNameList);

                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(SellingPriseActivity.this);
                        mRecSellingPrise.setLayoutManager(mLayoutManager);
                        mRecSellingPrise.setItemAnimator(new DefaultItemAnimator());
                        mRecSellingPrise.setAdapter(sellingPriseUpdateNewAdapter);

                        }

                        Log.e("list",sellingProductNameList +"");

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

    private void get_products_selling_price_temp() {

        try {
            appUtils.showProgressBarLoading();
        } catch (Exception e) {
            e.printStackTrace();
        }

        sellingProductNameList.clear();
        mSellingPriseList.clear();

        if (csm.equals("csm")){
            storeId = csm_Storeid ;
        }

        String REGISTER_URL = SellingPriseActivity.this.getResources().getString(R.string.base_url) + "v1/index.php/appshopboth/get_products_selling_price";

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
                                array = jsonObject.getJSONArray("data");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                           // mStockList = new ArrayList<>();

                            for (int i = 0; i < array.length(); i++) {

                                JSONObject c = null;
                                try {

                                    c = array.getJSONObject(i);

                                    String products_name = c.getString("products_name");

                                    SellingProductName sellingProductName = new SellingProductName();

                                    sellingProductName.setName(products_name);

                                    SellingPrise sellingPrise = null;

                                    sellingPrise = new SellingPrise();

                                    mSellingPriseList = new ArrayList<>();

                                    JSONArray product_keys = c.getJSONArray("product_keys");

                                    ArrayList<Child> childArrayList = new ArrayList<>();

                                    Child child = new Child();

                                    for (int k = 0 ; k < product_keys.length() ; k++){


                                        String value = String.valueOf(product_keys.get(k));

                                        Log.e("value----------" , value);

                                        try {
                                            JSONObject object = c.getJSONObject(value);

                                            String id = object.getString("id");
                                            String category_id = object.getString("category_id");
                                            String company_id = object.getString("company_id");
                                            String brand_id = object.getString("brand_id");
                                            String _products_name = object.getString("products_name");
                                            String products_size = object.getString("products_size");
                                            String state = object.getString("state");
                                            String product_price = object.getString("product_price");

                                            sellingPrise.setId(id);
                                            sellingPrise.setCategory_id(category_id);
                                            sellingPrise.setCompany_id(company_id);
                                            sellingPrise.setBrand_id(brand_id);
                                            sellingPrise.setProducts_name(_products_name);
                                            sellingPrise.setProducts_size(products_size);
                                            sellingPrise.setState(state);
                                            sellingPrise.setProduct_price(product_price);
                                            sellingPrise.setNew_price(product_price);

                                            child = new Child();

                                            child.setValue1(value);
                                            child.setValue2(product_price);
                                            child.setValue3(product_price);
                                            child.setNew_price(product_price);
                                            child.setId(id);
                                            child.setCategory_id(category_id);
                                            child.setCompany_id(company_id);
                                            child.setBrand_id(brand_id);
                                            child.setProducts_name(_products_name);
                                            child.setProducts_size(products_size);
                                            child.setState(state);
                                            child.setProduct_price(product_price);

                                            childArrayList.add(child);

                                        }catch (Exception e){

                                        }

                                    }

                                    sellingPrise.setChildList(childArrayList);

                                    mSellingPriseList.add(sellingPrise);

                                    sellingProductName.setSellingPriseList(mSellingPriseList);

                                    sellingProductName.setChildList(childArrayList);

                                    sellingProductNameList.add(sellingProductName);

                                 /*   try {
                                        JSONObject object = c.getJSONObject("750 ML");

                                        String id = object.getString("id");
                                        String category_id = object.getString("category_id");
                                        String company_id = object.getString("company_id");
                                        String brand_id = object.getString("brand_id");
                                        String _products_name = object.getString("products_name");
                                        String products_size = object.getString("products_size");
                                        String state = object.getString("state");
                                        String product_price = object.getString("product_price");

                                        sellingPrise = new SellingPrise();

                                        sellingPrise.setId(id);
                                        sellingPrise.setCategory_id(category_id);
                                        sellingPrise.setCompany_id(company_id);
                                        sellingPrise.setBrand_id(brand_id);
                                        sellingPrise.setProducts_name(_products_name);
                                        sellingPrise.setProducts_size(products_size);
                                        sellingPrise.setState(state);
                                        sellingPrise.setProduct_price(product_price);
                                        sellingPrise.setNew_price(product_price);

                                        mSellingPriseList.add(sellingPrise);

                                    }catch (Exception e){

                                    }

                                    try {
                                        JSONObject object = c.getJSONObject("375 ML");

                                        String id = object.getString("id");
                                        String category_id = object.getString("category_id");
                                        String company_id = object.getString("company_id");
                                        String brand_id = object.getString("brand_id");
                                        String _products_name = object.getString("products_name");
                                        String products_size = object.getString("products_size");
                                        String state = object.getString("state");
                                        String product_price = object.getString("product_price");

                                        sellingPrise = new SellingPrise();

                                        sellingPrise.setId(id);
                                        sellingPrise.setCategory_id(category_id);
                                        sellingPrise.setCompany_id(company_id);
                                        sellingPrise.setBrand_id(brand_id);
                                        sellingPrise.setProducts_name(_products_name);
                                        sellingPrise.setProducts_size(products_size);
                                        sellingPrise.setState(state);
                                        sellingPrise.setProduct_price(product_price);
                                        sellingPrise.setNew_price(product_price);

                                        mSellingPriseList.add(sellingPrise);

                                    }catch (Exception e){

                                    }

                                    try {
                                        JSONObject object = c.getJSONObject("180 ML");

                                        String id = object.getString("id");
                                        String category_id = object.getString("category_id");
                                        String company_id = object.getString("company_id");
                                        String brand_id = object.getString("brand_id");
                                        String _products_name = object.getString("products_name");
                                        String products_size = object.getString("products_size");
                                        String state = object.getString("state");
                                        String product_price = object.getString("product_price");

                                        sellingPrise = new SellingPrise();

                                        sellingPrise.setId(id);
                                        sellingPrise.setCategory_id(category_id);
                                        sellingPrise.setCompany_id(company_id);
                                        sellingPrise.setBrand_id(brand_id);
                                        sellingPrise.setProducts_name(_products_name);
                                        sellingPrise.setProducts_size(products_size);
                                        sellingPrise.setState(state);
                                        sellingPrise.setProduct_price(product_price);
                                        sellingPrise.setNew_price(product_price);


                                        mSellingPriseList.add(sellingPrise);

                                    }catch (Exception e){

                                    }*/




                                } catch (Exception e) {

                                }

                            }

                            sellingPriseUpdateNewAdapter = new SellingPriseUpdateNewAdapter(SellingPriseActivity.this, sellingProductNameList);

                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(SellingPriseActivity.this);
                            mRecSellingPrise.setLayoutManager(mLayoutManager);
                            mRecSellingPrise.setItemAnimator(new DefaultItemAnimator());
                            mRecSellingPrise.setAdapter(sellingPriseUpdateNewAdapter);

                        }

                        Log.e("list",sellingProductNameList +"");

                        try {
                            appUtils.dismissProgressBar();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

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
                params.put("store_id", store_id_temp);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void update_products_selling_price() {

        appUtils.showProgressBarLoading();

        String REGISTER_URL = SellingPriseActivity.this.getResources().getString(R.string.base_url) + "v1/index.php/appshopboth/update_products_selling_price";

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

                            if (type.equals("new")){
                                Intent intent = new Intent(SellingPriseActivity.this , AddTeamActivity.class);
                                intent.putExtra("type" , "new");
                                intent.putExtra("id" , "add");
                                startActivity(intent);
                                finish();
                            }else {
                                store_id_temp = "" ;
                                get_products_selling_price();
                            }

                        }
                        Toast.makeText(SellingPriseActivity.this , jsonObject.optString("msg"),Toast.LENGTH_LONG).show();

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
                params.put("store_id", storeId);
                params.put("product_list", jsonArray.toString());
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void store(){

        LayoutInflater layoutInflater = LayoutInflater.from(SellingPriseActivity.this);
        View promptsView = layoutInflater.inflate(R.layout.custom_version_dialogbox, null);

        final android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(SellingPriseActivity.this);
        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder.setCancelable(true);
        alertDialog = alertDialogBuilder.create();
        alertDialog.setCanceledOnTouchOutside(true);

        final TextView Update = (TextView) promptsView.findViewById(R.id.btn_confirm);

        final RecyclerView recyclerView = (RecyclerView) promptsView.findViewById(R.id.list);

        SupplierStoreAdapter adapter = new SupplierStoreAdapter(this,  mStockList , storeId);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);

        Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }


}
