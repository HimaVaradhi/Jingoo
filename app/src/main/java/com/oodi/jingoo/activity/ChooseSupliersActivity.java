package com.oodi.jingoo.activity;

import android.app.ProgressDialog;
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
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CheckBox;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.oodi.jingoo.R;
import com.oodi.jingoo.adapter.ChooseSuplierAdapter;
import com.oodi.jingoo.adapter.ChooseSupplierListAdapter;
import com.oodi.jingoo.adapter.SellingPriseShopAdapter;
import com.oodi.jingoo.adapter.SupplierStoreAdapter;
import com.oodi.jingoo.pojo.Selectsupplier;
import com.oodi.jingoo.pojo.Store;
import com.oodi.jingoo.pojo.Supplier;
import com.oodi.jingoo.utility.AppUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChooseSupliersActivity extends AppCompatActivity {
    private CheckBox chk_select_all;
    private static int count = 0;
    private static boolean isNotAdded = true;
    TextView mTxtHeaderName , mTxtHome , mTxtListViewLine , mTxtGridViewline , Home , mTxtDoItLater , mTxtCopy;
    ImageView mImgBack ,  mImgList , mImgGrid , mImgWt;
    AppUtils appUtils ;
    RecyclerView mRecSuppliers , mRecShopList ;
    LinearLayout mLnrGridView , mLnrListView ;
    ChooseSuplierAdapter mChooseSuplierAdapter ;
    List<Supplier> mSupplierList = new ArrayList<>();
    ChooseSupplierListAdapter mChooseSupplierListAdapter ;
    String store_id = "" , token , store_id_temp = "";
    public static String type = "" ;
    Button mBtnUpdate ;
    JSONArray jsonArray ;
    List<Store> mStockList;
    SellingPriseShopAdapter mSellingPriseShopAdapter;
    android.app.AlertDialog alertDialog ;
    SparseBooleanArray mChecked = new SparseBooleanArray();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appUtils = new AppUtils(this);
        SharedPreferences prefs1 = getSharedPreferences("language", MODE_PRIVATE);
        String lang = prefs1.getString("lang", "");
        if (lang.equals("hi")){
            appUtils.setLocale("hi");
        }else{
            appUtils.setLocale("en");
        }
        setContentView(R.layout.activity_choose_supliers);

        init();

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

        ImageView imgHelp = (ImageView) findViewById(R.id.imgHelp);
        imgHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mBtnUpdate.setVisibility(View.INVISIBLE);
                mImgWt.setVisibility(View.VISIBLE);

            }
        });

        //=====================================================================================================================

        if (lang.equals("hi")){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                mImgWt.setBackground(getResources().getDrawable(R.drawable.hi_chosse_brands));
            }
        }

        SharedPreferences wt = getSharedPreferences("wt", MODE_PRIVATE);
        String first = wt.getString("choose_suplier", "");

        if (first.equals("1")){

            mImgWt.setVisibility(View.GONE);
            mBtnUpdate.setVisibility(View.VISIBLE);

        }

        mImgWt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mImgWt.setVisibility(View.GONE);
                mBtnUpdate.setVisibility(View.VISIBLE);

                SharedPreferences.Editor editor = getSharedPreferences("wt", MODE_PRIVATE).edit();
                editor.putString("choose_suplier", "1");

                editor.commit();

            }
        });

        //=====================================================================================================================

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
            //type = "" ;
        }

        SharedPreferences prefs = this.getSharedPreferences("Login", Context.MODE_PRIVATE);
        token = prefs.getString("token" , "");

        if (type.equals("new")) {
            mTxtHeaderName.setText(getResources().getString(R.string.Choose_Your_Brands));
            mTxtHome.setText(getResources().getString(R.string.Choose_Your_Brands));
        }else {
            mTxtHeaderName.setText(getResources().getString(R.string.Choose_Your_Brands_));
            mTxtHome.setText(getResources().getString(R.string.Choose_Your_Brands_));
        }

        mChooseSupplierListAdapter = new ChooseSupplierListAdapter(ChooseSupliersActivity.this , mSupplierList);
        mChooseSuplierAdapter = new ChooseSuplierAdapter(ChooseSupliersActivity.this , mSupplierList);

        mImgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingsNewActivity.settingNew.finish();
                OtherSettingsActivity.otherSetting.finish();
//                StoreActivity.storeActivity.finish();
                finish();
            }
        });

        mLnrGridView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTxtListViewLine.setVisibility(View.GONE);
                mTxtGridViewline.setVisibility(View.VISIBLE);

                RecyclerView.LayoutManager mLayoutManager1 = new GridLayoutManager(ChooseSupliersActivity.this , 3);
                mRecSuppliers.setLayoutManager(mLayoutManager1);
                mRecSuppliers.setAdapter(mChooseSuplierAdapter);
            }
        });

        mLnrListView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTxtListViewLine.setVisibility(View.VISIBLE);
                mTxtGridViewline.setVisibility(View.GONE);

                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(ChooseSupliersActivity.this);
                mRecSuppliers.setLayoutManager(mLayoutManager);
                mRecSuppliers.setAdapter(mChooseSupplierListAdapter);

            }
        });

        mImgList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mImgList.setImageResource(R.drawable.ic_list_selected);
                mImgGrid.setImageResource(R.drawable.ic_grid_unselected);

                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(ChooseSupliersActivity.this);
                mRecSuppliers.setLayoutManager(mLayoutManager);
                mRecSuppliers.setAdapter(mChooseSupplierListAdapter);
            }
        });

        mImgGrid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mImgList.setImageResource(R.drawable.ic_list_unselected);
                mImgGrid.setImageResource(R.drawable.ic_grid_selected);

                RecyclerView.LayoutManager mLayoutManager1 = new GridLayoutManager(ChooseSupliersActivity.this , 3);
                mRecSuppliers.setLayoutManager(mLayoutManager1);
                mRecSuppliers.setAdapter(mChooseSuplierAdapter);

            }
        });

        if (!appUtils.isOnLine()){
            Intent intent1 = new Intent(getApplicationContext() , NoInternetActivity.class);
            startActivity(intent1);
            //appUtils.showToast(R.string.offline);
        }else {
            store_list();
        }

        mTxtDoItLater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChooseSupliersActivity.this , SellingPriseActivity.class);
                intent.putExtra("type" , "new");
                startActivity(intent);
                finish();
            }
        });

        mTxtCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                store();

            }
        });

        mBtnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                JSONObject student1 = null;
                jsonArray = new JSONArray();

                for (int i = 0; i < mSupplierList.size(); i++) {

                    try {
                        student1 = new JSONObject();

                        if (mSupplierList.get(i).isSelected()){
                            student1.put("id", mSupplierList.get(i).getId());
                            jsonArray.put(student1);
                        }


                        Log.e("json" , jsonArray+"");

                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

                if (jsonArray.length() != 0){
                    store_add_brand_products();
                }

            }
        });

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("custom-message"));


        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver1,
                new IntentFilter("msgto"));

    }

    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (!intent.hasExtra("item_category")) {
            } else {
                try{
                    store_id = intent.getStringExtra("item_category");
                    Log.e("storeID", store_id);
                    brand_list();
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
                try {
                    alertDialog.dismiss();
                }catch (Exception e){

                }
                store_id_temp = intent.getStringExtra("id");
                brand_list_temp();


            }
        }
    };

    public void init(){

        mLnrGridView = (LinearLayout) findViewById(R.id.lnrGridView);
        mLnrListView = (LinearLayout) findViewById(R.id.lnrListView);
        mTxtHeaderName = (TextView) findViewById(R.id.txtHeaderName);
        mTxtListViewLine = (TextView) findViewById(R.id.txtListViewLine);
        mTxtGridViewline = (TextView) findViewById(R.id.txtGridViewline);
        mTxtHome = (TextView) findViewById(R.id.txtHome);
        mImgBack = (ImageView) findViewById(R.id.imgBack);
        mRecSuppliers = (RecyclerView) findViewById(R.id.recSuppliers);
        chk_select_all = (CheckBox) findViewById(R.id.filter_select_all);
        Home = (TextView) findViewById(R.id.Home);
        mBtnUpdate = (Button) findViewById(R.id.btnUpdate);
        mRecShopList = (RecyclerView) findViewById(R.id.recShopList);
        mImgList = (ImageView) findViewById(R.id.imgList);
        mImgGrid = (ImageView) findViewById(R.id.imgGrid);
        mTxtDoItLater = (TextView) findViewById(R.id.txtDoItLater);
        mImgWt = (ImageView) findViewById(R.id.imgWt);
        mTxtCopy = (TextView) findViewById(R.id.txtCopy);

    }

    private void brand_list(){

        mSupplierList.clear();

        final ProgressDialog pd = new ProgressDialog(ChooseSupliersActivity.this);
        pd.setMessage("loading");
        pd.show();

        String REGISTER_URL = ChooseSupliersActivity.this.getResources().getString(R.string.base_url) + "v1/index.php/appshopboth/brand_list";

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

                        if(status.equals("1")) {

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
                                    String company_id = jsonObject1.getString("company_id");

                                    Supplier supplier = new Supplier();

                                    supplier.setId(id);
                                    supplier.setBrand_name(brand_name);
                                    supplier.setBrand_image(getResources().getString(R.string.base_url) + brand_image);
                                    supplier.setIs_selected(is_selected);
                                    supplier.setCompany_id(company_id);

                                    if (is_selected.equals("0")) {
                                        supplier.setSelected(false);
                                    } else {
                                        supplier.setSelected(true);
                                    }

                                    if (company_id.equals("1")) {
                                        supplier.setSelected(true);
                                    }

                                    mSupplierList.add(supplier);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }

                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(ChooseSupliersActivity.this);
                            mRecSuppliers.setLayoutManager(mLayoutManager);
                            mRecSuppliers.setAdapter(mChooseSupplierListAdapter);

                                if (chk_select_all.isChecked()) {

                                        for (Supplier supplier : mSupplierList) {
                                            supplier.setSelected(true);
                                        }
                                    } else {

                                        for (Supplier supplier : mSupplierList) {
                                            supplier.setSelected(false);
                                        }
                                    }

                                    mChooseSupplierListAdapter.notifyDataSetChanged();
                                }

                            /*RecyclerView.LayoutManager mLayoutManager1 = new GridLayoutManager(ChooseSupliersActivity.this , 3);
                            mRecSuppliers.setLayoutManager(mLayoutManager1);
                            mRecSuppliers.setAdapter(mChooseSuplierAdapter);*/



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
                params.put("store_id" , store_id);

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void brand_list_temp(){

        mSupplierList.clear();

        final ProgressDialog pd = new ProgressDialog(ChooseSupliersActivity.this);
        pd.setMessage("loading");
        try {
            pd.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

        String REGISTER_URL = ChooseSupliersActivity.this.getResources().getString(R.string.base_url) + "v1/index.php/appshopboth/brand_list";

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
                                    String company_id = jsonObject1.getString("company_id");

                                    Supplier supplier = new Supplier();

                                    supplier.setId(id);
                                    supplier.setBrand_name(brand_name);
                                    supplier.setBrand_image(getResources().getString(R.string.base_url)+brand_image);
                                    supplier.setIs_selected(is_selected);
                                    supplier.setCompany_id(company_id);

                                    if (is_selected.equals("0") ){
                                        supplier.setSelected(false);
                                    }else {
                                        supplier.setSelected(true);
                                    }

                                    if (company_id.equals("1")){
                                        supplier.setSelected(true);
                                    }

                                    mSupplierList.add(supplier);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }

                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(ChooseSupliersActivity.this);
                            mRecSuppliers.setLayoutManager(mLayoutManager);
                            mRecSuppliers.setAdapter(mChooseSupplierListAdapter);


                                   if (chk_select_all.isChecked()) {

                                        for (Supplier supplier : mSupplierList) {
                                            supplier.setSelected(true);
                                        }
                                    } else {

                                        for (Supplier supplier : mSupplierList) {
                                            supplier.setSelected(false);
                                        }
                                    }

                                        mChooseSupplierListAdapter.notifyDataSetChanged();




                            /*RecyclerView.LayoutManager mLayoutManager1 = new GridLayoutManager(ChooseSupliersActivity.this , 3);
                            mRecSuppliers.setLayoutManager(mLayoutManager1);
                            mRecSuppliers.setAdapter(mChooseSuplierAdapter);*/
                        }

                        try {
                            pd.dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

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
                params.put("store_id" , store_id_temp);

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void store_add_brand_products(){

        mSupplierList.clear();

        final ProgressDialog pd = new ProgressDialog(ChooseSupliersActivity.this);
        pd.setMessage("loading");
        pd.show();

        String REGISTER_URL = ChooseSupliersActivity.this.getResources().getString(R.string.base_url) + "v1/index.php/appshopboth/store_add_brand_products";

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

                            Toast.makeText(ChooseSupliersActivity.this , jsonObject.optString("msg") , Toast.LENGTH_LONG).show();
                            if (type.equals("new")){
                                Intent intent = new Intent(ChooseSupliersActivity.this , SellingPriseActivity.class);
                                intent.putExtra("type" , "new");
                                startActivity(intent);
                                finish();
                            }else {
                                finish();
                            }

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
                params.put("store_id" , store_id);
                params.put("brand_id_list" , jsonArray.toString());

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void store_list() {

        appUtils.showProgressBarLoading();

        String REGISTER_URL = ChooseSupliersActivity.this.getResources().getString(R.string.base_url) + "v1/index.php/appshopboth/store_list";

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

                            mSellingPriseShopAdapter = new SellingPriseShopAdapter(ChooseSupliersActivity.this, mStockList);
                            mRecShopList.setAdapter(mSellingPriseShopAdapter);

                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(ChooseSupliersActivity.this, LinearLayoutManager.HORIZONTAL, false);
                            mRecShopList.setLayoutManager(mLayoutManager);
                            mRecShopList.setItemAnimator(new DefaultItemAnimator());

                        }

                        try {
                            store_id = mStockList.get(0).getStore();

                            brand_list();
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

    public void store(){

        LayoutInflater layoutInflater = LayoutInflater.from(ChooseSupliersActivity.this);
        View promptsView = layoutInflater.inflate(R.layout.custom_version_dialogbox, null);

        final android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(ChooseSupliersActivity.this);
        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder.setCancelable(true);
        alertDialog = alertDialogBuilder.create();
        alertDialog.setCanceledOnTouchOutside(true);

        final TextView Update = (TextView) promptsView.findViewById(R.id.btn_confirm);

        final RecyclerView recyclerView = (RecyclerView) promptsView.findViewById(R.id.list);

        SupplierStoreAdapter adapter = new SupplierStoreAdapter(this,  mStockList , store_id);

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
