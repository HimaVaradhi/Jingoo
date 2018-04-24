package com.oodi.jingoo.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.oodi.jingoo.adapter.TakeStockAdapter;
import com.oodi.jingoo.pojo.SellingPrise;
import com.oodi.jingoo.pojo.Stock;
import com.oodi.jingoo.pojo.TakeStock;
import com.oodi.jingoo.utility.AppUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TakeStockListActivity extends AppCompatActivity {

    RecyclerView mRecTackStock ;
    TakeStockAdapter mTakeStockAdapter ;
    List<TakeStock> mTakeStockList = new ArrayList<>();
    List<SellingPrise> list = new ArrayList<>();
    ImageView mBtnBack , mImgNext , mImgPrevious , Home , mImgWt;
    Button mBtnSubmit ;
    TextView mTxtHome , mTxtCatName ;
    EditText mEdtSearch ;
    public static Activity tackStockList ;
    String token;
    int position ;
    String cat_name;
    String type ,  csm_Storeid = "" ;
    AppUtils appUtils ;
    static List<Stock> mCatList = new ArrayList<>();
    long currentVisiblePosition = 0;
    JSONArray jsonArray ;
    ArrayList<TakeStock> matches = new ArrayList<>();
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appUtils = new AppUtils(this);
        SharedPreferences prefs1 = getSharedPreferences("language", MODE_PRIVATE);
        final String lang = prefs1.getString("lang", "en");
        if (lang.equals("hi")){
            appUtils.setLocale("hi");
        }else {
            appUtils.setLocale("en");
        }
        setContentView(R.layout.activity_take_stock_list);

        init();

        ImageView imageView3 = (ImageView) findViewById(R.id.imageView3);

        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //onBackPressed();
                try {
                    TakeStockActivity.tackStock.finish();
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        mEdtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener(){

            @Override
            public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
                if(arg1 == EditorInfo.IME_ACTION_DONE)
                {

                    findMemberByName(mEdtSearch.getText().toString().toLowerCase());
                  //  mTxtCancle.setVisibility(View.VISIBLE);

                    if (matches.isEmpty()){
                        Toast.makeText(TakeStockListActivity.this , "Please try again",Toast.LENGTH_LONG).show();
                    }else {
                        mTakeStockAdapter = new TakeStockAdapter(TakeStockListActivity.this , matches , 2);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(TakeStockListActivity.this);
                        mRecTackStock.setLayoutManager(mLayoutManager);
                        mRecTackStock.setItemAnimator(new DefaultItemAnimator());
                        mRecTackStock.setAdapter(mTakeStockAdapter);
                    }



                    //s = mEdtSearch.getText().toString();

                    //categories();

                }
                return false;
            }

        });
       /* mTxtCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTxtCancle.setVisibility(View.GONE);
                //s = "";

                mEdtSearch.setText("");
                mEdtSearch.clearFocus();
                mTakeStockAdapter = new TakeStockAdapter(TakeStockListActivity.this , mTakeStockList , 2);

                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(TakeStockListActivity.this);
                mRecTackStock.setLayoutManager(mLayoutManager);
                mRecTackStock.setItemAnimator(new DefaultItemAnimator());
                mRecTackStock.setAdapter(mTakeStockAdapter);

                //categorieList();

            }
        });
*/
        SharedPreferences wt = getSharedPreferences("wt", MODE_PRIVATE);
        final String closing_stock_wt = wt.getString("closing_stock_wt", "");
        final String purchase_wt = wt.getString("purchase_wt", "");
        SharedPreferences prefs = this.getSharedPreferences("Login", Context.MODE_PRIVATE);
        token = prefs.getString("token" , "");
        type = prefs.getString("type" , "");
        String store_id = prefs.getString("store_id" , "");
        csm_Storeid = prefs.getString("store_id" , "");
        ImageView imgHelp = (ImageView) findViewById(R.id.imgHelp);

        imgHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (MainActivity.idx == 1){
                    if (lang.equals("hi") && !type.equals("csm")){
                        mImgWt.setBackground(getResources().getDrawable(R.drawable.closing_stock_anroid));
                    }else if (lang.equals("hi") && type.equals("csm")){
                        mImgWt.setBackground(getResources().getDrawable(R.drawable.closing_stock_anroid));
                    }else if (lang.equals("en") && !type.equals("csm")){
                        mImgWt.setBackground(getResources().getDrawable(R.drawable.cswt));
                    }else if (lang.equals("en") && type.equals("csm")){
                        mImgWt.setBackground(getResources().getDrawable(R.drawable.cswt));
                    }
                }else if (MainActivity.idx == 2){
                    if (lang.equals("hi") && !type.equals("csm")){
                        mImgWt.setBackground(getResources().getDrawable(R.drawable.purchase_anroid));
                    }else if (lang.equals("hi") && type.equals("csm")){
                        mImgWt.setBackground(getResources().getDrawable(R.drawable.purchase_anroid));
                    }else if (lang.equals("en") && !type.equals("csm")){
                        mImgWt.setBackground(getResources().getDrawable(R.drawable.purchasewt1));
                    }else if (lang.equals("en") && type.equals("csm")){
                        mImgWt.setBackground(getResources().getDrawable(R.drawable.purchasewt1));
                    }
                }

                mBtnSubmit.setVisibility(View.INVISIBLE);
                mImgWt.setVisibility(View.VISIBLE);

            }
        });


        if (MainActivity.idx == 1 && !closing_stock_wt.equals("1")){
            if (lang.equals("hi") && !type.equals("csm")){
                mImgWt.setVisibility(View.VISIBLE);
                mImgWt.setBackground(getResources().getDrawable(R.drawable.closing_stock_anroid));
            }else if (lang.equals("hi") && type.equals("csm")){
                mImgWt.setVisibility(View.VISIBLE);
                //mImgWt.setBackground(getResources().getDrawable(R.drawable.cswt1));
            }else if (lang.equals("en") && !type.equals("csm")){
                mImgWt.setVisibility(View.VISIBLE);
                mImgWt.setBackground(getResources().getDrawable(R.drawable.cswt));
            }else if (lang.equals("en") && type.equals("csm")){
                mImgWt.setVisibility(View.VISIBLE);
                mImgWt.setBackground(getResources().getDrawable(R.drawable.en_csm_take_stock));
            }
        }else if (MainActivity.idx == 2 && !purchase_wt.equals("1")){
            if (lang.equals("hi") && !type.equals("csm")){
                mImgWt.setVisibility(View.VISIBLE);
                mImgWt.setBackground(getResources().getDrawable(R.drawable.purchase_anroid));
            }else if (lang.equals("hi") && type.equals("csm")){
                mImgWt.setVisibility(View.VISIBLE);
                //mImgWt.setBackground(getResources().getDrawable(R.drawable.purchasewt1));
            }else if (lang.equals("en") && !type.equals("csm")){
                mImgWt.setVisibility(View.VISIBLE);
                mImgWt.setBackground(getResources().getDrawable(R.drawable.purchasewt1));
            }else if (lang.equals("en") && type.equals("csm")){
                mImgWt.setVisibility(View.VISIBLE);
                mImgWt.setBackground(getResources().getDrawable(R.drawable.en_csm_take_stock));
            }
       }else {
            mImgWt.setVisibility(View.GONE);
           mBtnSubmit.setVisibility(View.VISIBLE);
          }

        if (type.equals("csm")){
            StartingStoreListAdapter.id = store_id ;
        }

        if (!appUtils.isOnLine()){
            Intent intent = new Intent(getApplicationContext() , NoInternetActivity.class);
            startActivity(intent);
            //appUtils.showToast(R.string.offline);
        }else {
            brand_list_for_products_with_all();
        }

        Intent intent = getIntent();
        TakeStockProductsActivity.store_id = intent.getStringExtra("id");

        tackStockList = this;

        if (MainActivity.idx == 1){
            mTxtHome.setText(getResources().getString(R.string.select_product));

        }else {
            mTxtHome.setText(getResources().getString(R.string.select_product));
        }

        mTxtHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //StoreActivity.storeActivity.finish();
                    TakeStockActivity.tackStock.finish();
                    TakeStockProductsActivity.a.finish();
                    tackStockList.finish();
                }catch (Exception e){
                }

            }
        });

        /*Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //StoreActivity.storeActivity.finish();
                    TakeStockActivity.tackStock.finish();
                    TakeStockProductsActivity.a.finish();
                    tackStockList.finish();
                }catch (Exception e){
                }
            }
        });*/

        //mTxtCatName.setText(cat_name);

        mImgNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    if (position < (mCatList.size()-1)){
                        position = position + 1;
                        StockListAdapter.id = mCatList.get(position).getId();
                        mTxtCatName.setText(mCatList.get(position).getCategory_name());
                        productList();
                    }


            }
        });

        mImgPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    if (position < mCatList.size() && (position > 0)){
                        position = position - 1;
                        StockListAdapter.id = mCatList.get(position).getId();
                        mTxtCatName.setText(mCatList.get(position).getCategory_name());
                        productList();
                    }


            }
        });

        mBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mBtnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(TakeStockListActivity.this);
                builder.setMessage(getResources().getString(R.string.submit_))
                        .setCancelable(false)
                        .setNegativeButton("Cancel" , new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //do things
                                dialog.dismiss();
                                final MediaPlayer mp = MediaPlayer.create(TakeStockListActivity.this,R.raw.glass_toast_ting);
                                mp.start();

                                currentVisiblePosition = ((LinearLayoutManager)mRecTackStock.getLayoutManager()).findFirstCompletelyVisibleItemPosition();

                                if (MainActivity.idx == 1){
                                    take();
                                    //products_stock_update();
                                }else {
                                    products_stock_update_refill();
                                }
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();

            }
        });

       mImgWt.setOnClickListener(new View.OnClickListener() {
        @Override
           public void onClick(View v) {

               mImgWt.setVisibility(View.GONE);
               mBtnSubmit.setVisibility(View.VISIBLE);
            SharedPreferences.Editor editor = getSharedPreferences("wt", MODE_PRIVATE).edit();

             if (MainActivity.idx == 1){

               editor.putString("closing_stock_wt", "1");

              editor.commit();

              } else {

                 editor.putString("purchase_wt", "1");

                  editor.commit();

              }
          }
        });

    }

    public void init(){

        mRecTackStock = (RecyclerView) findViewById(R.id.recTakeStock);
        mBtnBack = (ImageView) findViewById(R.id.imgBack);
        mBtnSubmit = (Button) findViewById(R.id.btnSubmit);
        mTxtHome = (TextView) findViewById(R.id.txtHome);
        mEdtSearch = (EditText) findViewById(R.id.edtSearch);
        //mTxtCancle = (TextView) findViewById(R.id.txtCancle);
        mImgNext = (ImageView) findViewById(R.id.imgNext);
        mImgPrevious = (ImageView) findViewById(R.id.imgPrevious);
        mTxtCatName = (TextView) findViewById(R.id.textView);
        Home = (ImageView) findViewById(R.id.imageView3);
        mImgWt = (ImageView) findViewById(R.id.imgWt);

    }
    public ArrayList<TakeStock> findMemberByName(String name) {
        //ArrayList<Stock> matches = new ArrayList<Stock>();
        // go through list of members and compare name with given name

        matches.clear();

        for(TakeStock member : mTakeStockList) {

            if(member.getProducts_name().toLowerCase().contains(name)){
                matches.add(member);
            }

        }
        return matches; // return the matches, which is empty when no member with the given name was found
    }

    private void brand_list_for_products_with_all(){

        mCatList.clear();

        if (type.equals("csm")){
            StartingStoreListAdapter.id = csm_Storeid ;
        }

        final ProgressDialog pd = new ProgressDialog(TakeStockListActivity.this);
        pd.setMessage("loading");
        pd.show();

        String REGISTER_URL = TakeStockListActivity.this.getResources().getString(R.string.base_url) + "v1/index.php/appshopboth/brand_list_for_products_with_all";

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

                                    stock.setCategory_image(TakeStockListActivity.this.getResources().getString(R.string.base_url) +brand_image);
                                    stock.setCategory_name(brand_name);
                                    stock.setId(id);

                                    mCatList.add(stock);

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

                            mTxtCatName.setText(object.optJSONObject(0).optString("brand_name"));

                        }else {
                            Toast.makeText(TakeStockListActivity.this , jsonObject.optString("msg"),Toast.LENGTH_LONG).show();
                        }

                        try {
                            StockListAdapter.id = mCatList.get(0).getId();
                            productList();
                        } catch (Exception e) {
                            e.printStackTrace();
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


    private void productList(){

        appUtils.showProgressBarLoading();

        mTakeStockList.clear();

        String REGISTER_URL = TakeStockListActivity.this.getResources().getString(R.string.base_url) + "v1/index.php/appshopboth/products";

        final String KEY_USERNAME = "token";
        final String KEY_PASSWORD = "category_id";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(LoginActivity.this,response,Toast.LENGTH_LONG).show();

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
                                    String category_id = c.getString("category_id");
                                    String products_name = c.getString("products_name");
                                    String products_desc = c.getString("products_desc");
                                    String products_image = c.getString("products_image");
                                    String keywords = c.getString("keywords");
                                    String sort_order = c.getString("sort_order");
                                    String is_active = c.getString("is_active");
                                    String created_at = c.getString("created_at");
                                    String last_activity_at = c.getString("last_activity_at");
                                    String products_size = c.getString("products_size");
                                    String stock_qty = c.getString("stock_qty");
                                    String last_updated = c.getString("last_updated");

                                    TakeStock takeStock = new TakeStock();

                                    takeStock.setId(id);
                                    takeStock.setProducts_name(products_name);
                                    takeStock.setProducts_image(TakeStockListActivity.this.getResources().getString(R.string.base_url) +products_image);
                                    takeStock.setProducts_size(products_size);
                                    takeStock.setSort_order(stock_qty);
                                    takeStock.setStock_qty(stock_qty);
                                    takeStock.setLast_updated(last_updated);
                                    takeStock.setIs_active("0");

                                    mTakeStockList.add(takeStock);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }

                        if (MainActivity.idx == 1){
                            mTakeStockAdapter = new TakeStockAdapter(TakeStockListActivity.this,mTakeStockList , 1 );
                        }else {
                            mTakeStockAdapter = new TakeStockAdapter(TakeStockListActivity.this,mTakeStockList , 2 );
                        }
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(TakeStockListActivity.this);
                        mRecTackStock.setLayoutManager(mLayoutManager);
                        mRecTackStock.setItemAnimator(new DefaultItemAnimator());
                        mRecTackStock.setAdapter(mTakeStockAdapter);
                        mRecTackStock.setNestedScrollingEnabled(false);
                        ((LinearLayoutManager) mRecTackStock.getLayoutManager()).scrollToPosition((int) currentVisiblePosition);

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
                params.put(KEY_USERNAME,token);
                params.put(KEY_PASSWORD, TakeStockProductsActivity.store_id);
                params.put("store_id" , StartingStoreListAdapter.id);
                params.put("brand_id", StockListAdapter.id);

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(TakeStockListActivity.this);
        requestQueue.add(stringRequest);
    }

    public void take(){

        JSONObject student1 = null;
        jsonArray = new JSONArray();

        boolean flag = false;

        try {

            for (int i = 0; i < mTakeStockList.size(); i++) {

                student1 = new JSONObject();
                try {
                    student1.put("id", mTakeStockList.get(i).getId());

                    if (String.valueOf(Integer.parseInt(mTakeStockList.get(i).getStock_qty()) - Integer.parseInt(mTakeStockList.get(i).getSort_order())).equals("0")){
                        student1.put("qty" , "0");

                    }
                    else if ((Integer.parseInt(mTakeStockList.get(i).getStock_qty()) - Integer.parseInt(mTakeStockList.get(i).getSort_order()) < 0)){
                        //Toast.makeText(TakeStockListActivity.this , "The quantity you have entered is greater than current stock value for one or more items. Please try again." , Toast.LENGTH_SHORT).show();
                        flag = true;
                        final AlertDialog.Builder builder = new AlertDialog.Builder(TakeStockListActivity.this);
                        builder.setMessage(getResources().getString(R.string.quantity_))
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        //do things
                                        dialog.dismiss();
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                        break;
                    }
                    else {
                        student1.put("qty" , String.valueOf(Integer.parseInt(mTakeStockList.get(i).getStock_qty()) - Integer.parseInt(mTakeStockList.get(i).getSort_order())));
                    }

                    jsonArray.put(student1);
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

        }catch (Exception e){

        }
        if(!flag) {
            products_stock_update();
        }

    }

    private void products_stock_update(){

        final ProgressDialog pd = new ProgressDialog(TakeStockListActivity.this);
        pd.setMessage("loading");
        pd.show();

        SharedPreferences prefs = TakeStockListActivity.this.getSharedPreferences("Login", Context.MODE_PRIVATE);
        final String token = prefs.getString("token" , "");

        String REGISTER_URL = this.getResources().getString(R.string.base_url) + "v1/index.php/appshopboth/products_stock_update_outward";

        final String KEY_USERNAME = "token";
        final String KEY_PASSWORD = "products_qty";

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

                            Toast.makeText(TakeStockListActivity.this,getResources().getString(R.string.update),Toast.LENGTH_LONG).show();
                            finish();

                            //productList();
                            //getActivity().finish();
                        }

                        pd.dismiss();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(TakeStockListActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                        pd.dismiss();

                    }
                }){
            @Override
            protected Map<String,String> getParams(){

                Map<String,String> params = new HashMap<String, String>();
                params.put(KEY_USERNAME,token);
                params.put(KEY_PASSWORD, jsonArray.toString());
                params.put("store_id" , StartingStoreListAdapter.id);

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(TakeStockListActivity.this);
        requestQueue.add(stringRequest);
    }

    private void products_stock_update_refill(){

        final ProgressDialog pd = new ProgressDialog(TakeStockListActivity.this);
        pd.setMessage("loading");
        pd.show();

        SharedPreferences prefs = TakeStockListActivity.this.getSharedPreferences("Login", Context.MODE_PRIVATE);
        final String token = prefs.getString("token" , "");

        String REGISTER_URL = this.getResources().getString(R.string.base_url) + "v1/index.php/appshopboth/products_stock_update_inward";

        final String KEY_USERNAME = "token";
        final String KEY_PASSWORD = "products_qty";

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

                            final AlertDialog.Builder builder = new AlertDialog.Builder(TakeStockListActivity.this);
                            builder.setMessage(getResources().getString(R.string.update))
                                    .setCancelable(false)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            //do things
                                            dialog.dismiss();
                                            finish();
                                            //productList();
                                        }
                                    });
                            AlertDialog alert = builder.create();
                            alert.show();

                            //getActivity().finish();

                        }

                        pd.dismiss();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(TakeStockListActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                        pd.dismiss();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){

                JSONObject student1 = null;
                JSONArray jsonArray = new JSONArray();

                for (int i=0; i < mTakeStockList.size(); i++) {

                    student1 = new JSONObject();
                    try {
                        student1.put("id", mTakeStockList.get(i).getId());
                        student1.put("qty" , mTakeStockList.get(i).getIs_active());

                        jsonArray.put(student1);
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

                Map<String,String> params = new HashMap<String, String>();
                params.put(KEY_USERNAME,token);
                params.put(KEY_PASSWORD, jsonArray.toString());
                params.put("store_id" , StartingStoreListAdapter.id);

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(TakeStockListActivity.this);
        requestQueue.add(stringRequest);
    }

}
