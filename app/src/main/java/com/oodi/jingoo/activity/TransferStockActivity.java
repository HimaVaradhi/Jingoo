package com.oodi.jingoo.activity;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
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
import com.oodi.jingoo.adapter.ProductListAdapter;
import com.oodi.jingoo.adapter.StoreListAdapter;
import com.oodi.jingoo.adapter.StoreListAdapter_;
import com.oodi.jingoo.pojo.StoreList;
import com.oodi.jingoo.pojo.TakeStock;
import com.oodi.jingoo.utility.AppUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransferStockActivity extends AppCompatActivity {

    TextView mTxtHeaderName , mTxtHome , Home , mTxtFrom , mTxtTo , mTxtWhat;
    ImageView mImgBack , mImgWt ;
    AppUtils appUtils ;
    String token ;
    ArrayList<TakeStock> matches = new ArrayList<>();
    List<StoreList> storeLists = new ArrayList<>();
    List<TakeStock> productList = new ArrayList<>();
    android.app.AlertDialog alertDialog , alertDialog1 , alertDialog2;
    String stateName = "" , storeTo = "" , productName = "";
    LinearLayout mLnrFrom ;
    EditText mEdtProduct;
    String from_store_id = "" , to_store_id = "" , product_id = "" ;
    Button mBtnTransfer ;

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
        setContentView(R.layout.activity_transfer_stock);

        SharedPreferences prefs = this.getSharedPreferences("Login", Context.MODE_PRIVATE);
        token = prefs.getString("token" , "");

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

        init();

        ImageView imgHelp = (ImageView) findViewById(R.id.imgHelp);
        imgHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mBtnTransfer.setVisibility(View.INVISIBLE);
                mImgWt.setVisibility(View.VISIBLE);

            }
        });

        if (lang.equals("hi")){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                mImgWt.setBackground(getResources().getDrawable(R.drawable.hi_transfer_wt));
            }
        }

        SharedPreferences wt = getSharedPreferences("wt", MODE_PRIVATE);
        String first = wt.getString("transfer_wt", "");

        if (first.equals("1")){

            mImgWt.setVisibility(View.GONE);
            mBtnTransfer.setVisibility(View.VISIBLE);

        }

        if (!appUtils.isOnLine()){
            Intent intent = new Intent(getApplicationContext() , NoInternetActivity.class);
            startActivity(intent);
            //appUtils.showToast(R.string.offline);
        }else {
            transfer_stock_data();
        }

        Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingsNewActivity.settingNew.finish();
                OtherSettingsActivity.otherSetting.finish();
                finish();
            }
        });

        mLnrFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storeFrom();
            }
        });

        mTxtTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storeTo();
            }
        });

        mTxtWhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!from_store_id.equals("")  && !to_store_id.equals("")){
                    transfer_stock_product_list();
                }else {
                    makeToast("Please select store1 and store2");
                }
            }
        });

        mBtnTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTxtFrom.getText().toString().equals(mTxtTo.getText().toString())){
                    Toast.makeText(TransferStockActivity.this , "Please select diffrent stores for transfer" , Toast.LENGTH_LONG).show();
                }else {
                    if (isValide()){
                        transfer_stock_submit();
                    }
                }
            }
        });

        mImgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mTxtHeaderName.setText(getResources().getString(R.string.transfer_stock));
        mTxtHome.setText(getResources().getString(R.string.transfer_stock));

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("custom-message"));

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver1,
                new IntentFilter("msgto"));

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver2,
                new IntentFilter("product"));

        mImgWt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mImgWt.setVisibility(View.GONE);
                mBtnTransfer.setVisibility(View.VISIBLE);

                SharedPreferences.Editor editor = getSharedPreferences("wt", MODE_PRIVATE).edit();
                editor.putString("transfer_wt", "1");

                editor.commit();

            }
        });

    }

    public void init(){

        mTxtHeaderName = (TextView) findViewById(R.id.txtHeaderName);
        mTxtHome = (TextView) findViewById(R.id.txtHome);
        mImgBack = (ImageView) findViewById(R.id.imgBack);
        Home = (TextView) findViewById(R.id.Home);
        mLnrFrom = (LinearLayout) findViewById(R.id.lnrFrom);
        mTxtFrom = (TextView) findViewById(R.id.txtFrom);
        mTxtTo = (TextView) findViewById(R.id.txtTo);
        mTxtWhat = (TextView) findViewById(R.id.txtWhat);
        mEdtProduct = (EditText) findViewById(R.id.edtProduct);
        mBtnTransfer = (Button) findViewById(R.id.btnTransfer);
        mImgWt = (ImageView) findViewById(R.id.imgWt);


    }

    private void transfer_stock_data(){

        final ProgressDialog pd = new ProgressDialog(TransferStockActivity.this);
        pd.setMessage("loading");
        pd.show();

        String REGISTER_URL = TransferStockActivity.this.getResources().getString(R.string.base_url) + "v1/index.php/appshopboth/transfer_stock_data";

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
                            JSONArray jArrayProduct_list = null ;
                            try {
                                object = jsonObject.getJSONArray("store_list");
                                jArrayProduct_list = jsonObject.getJSONArray("product_list");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            for (int i = 0; i < object.length(); i++) {

                                try {
                                    JSONObject jsonObject1 = object.getJSONObject(i);

                                    String id = jsonObject1.getString("id");
                                    String name = jsonObject1.getString("name");

                                    StoreList storeList = new StoreList();

                                    storeList.setId(id);
                                    storeList.setName(name);

                                    storeLists.add(storeList);


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }

                            for (int i = 0; i < jArrayProduct_list.length(); i++) {

                                try {
                                    JSONObject jsonObject1 = jArrayProduct_list.getJSONObject(i);

                                    String id = jsonObject1.getString("id");
                                    String products_name = jsonObject1.getString("products_name");

                                    TakeStock takeStock = new TakeStock();

                                    takeStock.setId(id);
                                    takeStock.setProducts_name(products_name);

                                    productList.add(takeStock);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

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

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void transfer_stock_product_list(){

        productList.clear();

        final ProgressDialog pd = new ProgressDialog(TransferStockActivity.this);
        pd.setMessage("loading");
        pd.show();

        String REGISTER_URL = TransferStockActivity.this.getResources().getString(R.string.base_url) + "v1/index.php/appshopboth/transfer_stock_product_list";

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

                            JSONArray jArrayProduct_list = null ;
                            try {
                                jArrayProduct_list = jsonObject.getJSONArray("product_list");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            for (int i = 0; i < jArrayProduct_list.length(); i++) {

                                try {
                                    JSONObject jsonObject1 = jArrayProduct_list.getJSONObject(i);

                                    String id = jsonObject1.getString("id");
                                    String products_name = jsonObject1.getString("products_name");

                                    TakeStock takeStock = new TakeStock();

                                    takeStock.setId(id);
                                    takeStock.setProducts_name(products_name);

                                    productList.add(takeStock);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }

                            productList();

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
                params.put("from_store" , from_store_id);
                params.put("to_store" , to_store_id);

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void transfer_stock_submit(){

        final ProgressDialog pd = new ProgressDialog(TransferStockActivity.this);
        pd.setMessage("loading");
        pd.show();

        String REGISTER_URL = TransferStockActivity.this.getResources().getString(R.string.base_url) + "v1/index.php/appshopboth/transfer_stock_submit";

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

                            makeToast(jsonObject.optString("msg"));
                            finish();

                        }else {

                            final AlertDialog.Builder builder = new AlertDialog.Builder(TransferStockActivity.this);
                            builder.setMessage(jsonObject.optString("msg"))
                                    .setCancelable(false)
                                    .setPositiveButton(getResources().getString(R.string.okay), new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            //do things
                                            dialog.dismiss();
                                        }
                                    });
                            AlertDialog alert = builder.create();
                            alert.show();

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
                params.put("from_store_id" , from_store_id);
                params.put("to_store_id" , to_store_id);
                params.put("product_id" , product_id);
                params.put("product_qty" , mEdtProduct.getText().toString());

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void storeFrom(){

        LayoutInflater layoutInflater = LayoutInflater.from(TransferStockActivity.this);
        View promptsView = layoutInflater.inflate(R.layout.custom_version_dialogbox, null);

        final android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(TransferStockActivity.this);
        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder.setCancelable(true);
        alertDialog = alertDialogBuilder.create();
        alertDialog.setCanceledOnTouchOutside(true);

        final TextView Update = (TextView) promptsView.findViewById(R.id.btn_confirm);

        final RecyclerView recyclerView = (RecyclerView) promptsView.findViewById(R.id.list);

        StoreListAdapter adapter = new StoreListAdapter(this,  storeLists);

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

    public ArrayList<TakeStock> findMemberByName(String name) {
        //ArrayList<Stock> matches = new ArrayList<Stock>();
        // go through list of members and compare name with given name

        matches.clear();

        for(TakeStock member : productList) {

            if(member.getProducts_name().toLowerCase().contains(name)){
                matches.add(member);
            }

        }
        return matches; // return the matches, which is empty when no member with the given name was found
    }

    public void storeTo(){

        LayoutInflater layoutInflater = LayoutInflater.from(TransferStockActivity.this);
        View promptsView = layoutInflater.inflate(R.layout.custom_version_dialogbox, null);

        final android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(TransferStockActivity.this);
        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder.setCancelable(true);
        alertDialog1 = alertDialogBuilder.create();
        alertDialog1.setCanceledOnTouchOutside(true);

        final TextView Update = (TextView) promptsView.findViewById(R.id.btn_confirm);

        final RecyclerView recyclerView = (RecyclerView) promptsView.findViewById(R.id.list);

        StoreListAdapter_ adapter = new StoreListAdapter_(this,  storeLists);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);

        Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog1.dismiss();
            }
        });
        alertDialog1.show();
    }

    public void productList(){

        LayoutInflater layoutInflater = LayoutInflater.from(TransferStockActivity.this);
        View promptsView = layoutInflater.inflate(R.layout.custom_version_dialogbox, null);

        final android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(TransferStockActivity.this);
        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder.setCancelable(true);
        alertDialog2 = alertDialogBuilder.create();
        alertDialog2.setCanceledOnTouchOutside(true);

        final TextView Update = (TextView) promptsView.findViewById(R.id.btn_confirm);
        final TextView txtName = (TextView) promptsView.findViewById(R.id.txtName);

        final RecyclerView recyclerView = (RecyclerView) promptsView.findViewById(R.id.list);

        final EditText mEdtSearch = (EditText) promptsView.findViewById(R.id.edtSearch);

        mEdtSearch.setVisibility(View.VISIBLE);

        txtName.setText(getResources().getString(R.string.select_product));

        ProductListAdapter adapter = new ProductListAdapter(this,  productList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);

        mEdtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                findMemberByName(String.valueOf(s));

                if (matches.isEmpty()){
                    Toast.makeText(TransferStockActivity.this , "Please try again",Toast.LENGTH_LONG).show();
                    ProductListAdapter adapter = new ProductListAdapter(TransferStockActivity.this,  matches);

                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(TransferStockActivity.this);
                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(adapter);
                    recyclerView.setNestedScrollingEnabled(false);
                }else{
                    ProductListAdapter adapter = new ProductListAdapter(TransferStockActivity.this,  matches);

                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(TransferStockActivity.this);
                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(adapter);
                    recyclerView.setNestedScrollingEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        mEdtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener(){

            @Override
            public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
                if(arg1 == EditorInfo.IME_ACTION_DONE)
                {
                    findMemberByName(mEdtSearch.getText().toString().toLowerCase());

                    if (matches.isEmpty()){

                        ProductListAdapter adapter = new ProductListAdapter(TransferStockActivity.this,  matches);

                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(TransferStockActivity.this);
                        recyclerView.setLayoutManager(mLayoutManager);
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        recyclerView.setAdapter(adapter);
                        recyclerView.setNestedScrollingEnabled(false);

                    }else{
                        ProductListAdapter adapter = new ProductListAdapter(TransferStockActivity.this,  matches);

                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(TransferStockActivity.this);
                        recyclerView.setLayoutManager(mLayoutManager);
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        recyclerView.setAdapter(adapter);
                        recyclerView.setNestedScrollingEnabled(false);
                    }

                }
                return false;
            }

        });

        Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog2.dismiss();
            }
        });
        alertDialog2.show();
    }

    public boolean isValide() {

        if (mTxtFrom.getText().toString().equalsIgnoreCase("")) {
            makeToast("Please select store1");
            return false;
        } else if (mTxtTo.getText().toString().equalsIgnoreCase("")) {
            makeToast("Please select store2");
            return false;
        }else if (mTxtWhat.getText().toString().equalsIgnoreCase("")) {
            makeToast("Please select product");
            return false;
        }else if (mEdtProduct.getText().toString().equalsIgnoreCase("")) {
            makeToast("Please enter number of bottles");
            return false;
        }

        return true;
    }

    public void makeToast(String name) {

        Toast.makeText(TransferStockActivity.this, name, Toast.LENGTH_SHORT).show();

    }

    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (!intent.hasExtra("from")){
            }
            else {

                try {
                    alertDialog.dismiss();
                }catch (Exception e){
                }
                from_store_id = intent.getStringExtra("from");

                stateName = intent.getStringExtra("item_name");

                mTxtFrom.setText(stateName);


            }
        }
    };

    public BroadcastReceiver mMessageReceiver1 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (!intent.hasExtra("to")){
            }
            else {
                try {
                    alertDialog1.dismiss();
                }catch (Exception e){

                }
                to_store_id = intent.getStringExtra("to");

                storeTo = intent.getStringExtra("item_name");

                mTxtTo.setText(storeTo);



            }
        }
    };

    public BroadcastReceiver mMessageReceiver2 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (!intent.hasExtra("item_category")){
            }
            else {
                try {
                    alertDialog2.dismiss();
                }catch (Exception e){
                }
                product_id = intent.getStringExtra("item_category");

                productName = intent.getStringExtra("item_name");

                mTxtWhat.setText(productName);



            }
        }
    };
}
