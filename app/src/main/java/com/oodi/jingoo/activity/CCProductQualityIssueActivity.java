package com.oodi.jingoo.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.oodi.jingoo.R;
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

public class CCProductQualityIssueActivity extends AppCompatActivity {

    TextView mTxtHeaderName , mTxtHome  , Home , mTxtCancle ;
    ImageView mImgBack ;
    AppUtils appUtils ;
    RecyclerView mRecStockList ;
    List<Stock> mStockList = new ArrayList<>();
    ArrayList<Stock> matches = new ArrayList<>();
    String token ;
    StockListAdapter mStockListAdapter ;
    EditText mEdtSearch ;
    public static Activity aProductQuality ;

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
        setContentView(R.layout.activity_ccproduct_quality_issue);
        aProductQuality = this ;

        SharedPreferences prefs = this.getSharedPreferences("Login", Context.MODE_PRIVATE);
        token = prefs.getString("token" , "");

        init();

        ImageView imgHelp = (ImageView) findViewById(R.id.imgHelp);
        imgHelp.setVisibility(View.GONE);
        mTxtHome.setVisibility(View.VISIBLE);
        Home.setVisibility(View.VISIBLE);

        mTxtHeaderName.setText(getResources().getString(R.string.customer_care));
        mTxtHome.setText(getResources().getString(R.string.customer_care));

        Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomerCareActivity.CC.finish();
                onBackPressed();
            }
        });

        mImgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

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
                    }else {
                        mStockListAdapter = new StockListAdapter(CCProductQualityIssueActivity.this , matches , 3);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(CCProductQualityIssueActivity.this);
                        mRecStockList.setLayoutManager(mLayoutManager);
                        mRecStockList.setItemAnimator(new DefaultItemAnimator());
                        mRecStockList.setAdapter(mStockListAdapter);
                        mRecStockList.setNestedScrollingEnabled(false);
                    }

                }
                return false;
            }

        });

        mTxtCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTxtCancle.setVisibility(View.GONE);
                mRecStockList.setVisibility(View.VISIBLE);

                mEdtSearch.setText("");
                mEdtSearch.clearFocus();

                mStockListAdapter = new StockListAdapter(CCProductQualityIssueActivity.this , mStockList , 3);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(CCProductQualityIssueActivity.this);
                mRecStockList.setLayoutManager(mLayoutManager);
                mRecStockList.setItemAnimator(new DefaultItemAnimator());
                mRecStockList.setAdapter(mStockListAdapter);
                mRecStockList.setNestedScrollingEnabled(false);

            }
        });

        if (!appUtils.isOnLine()){
            Intent intent1 = new Intent(getApplicationContext() , NoInternetActivity.class);
            startActivity(intent1);
        }else {
            products_ccare();
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

        mTxtHeaderName = (TextView) findViewById(R.id.txtHeaderName);
        mImgBack = (ImageView) findViewById(R.id.imgBack);
        mTxtHome = (TextView) findViewById(R.id.txtHome);
        Home = (TextView) findViewById(R.id.Home);
        mRecStockList = (RecyclerView) findViewById(R.id.recStockList);
        mEdtSearch = (EditText) findViewById(R.id.edtSearch);
        mTxtCancle = (TextView) findViewById(R.id.txtCancle);

    }

    private void products_ccare(){

        mStockList.clear();

        final ProgressDialog pd = new ProgressDialog(CCProductQualityIssueActivity.this);
        pd.setMessage("loading");
        pd.show();

        String REGISTER_URL = CCProductQualityIssueActivity.this.getResources().getString(R.string.base_url) + "v1/index.php/appshopboth/products_ccare";

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

                                    /*"id": "3",
		"": "Ac Black Luxury Pure Grain Whisky",
		"products_size": "180 ML",
		"": "files\/products\/21be43710c4c208db84d1e2aaa436af8.png"*/

                                    String id = jsonObject1.getString("id");
                                    String brand_name = jsonObject1.getString("products_name");
                                    String brand_image = jsonObject1.getString("products_image");

                                    Stock stock = new Stock();

                                    stock.setCategory_image(CCProductQualityIssueActivity.this.getResources().getString(R.string.base_url) +brand_image);
                                    stock.setCategory_name(brand_name);
                                    stock.setId(id);

                                    mStockList.add(stock);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }

                            mStockListAdapter = new StockListAdapter(CCProductQualityIssueActivity.this , mStockList ,3);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(CCProductQualityIssueActivity.this);
                            mRecStockList.setLayoutManager(mLayoutManager);
                            mRecStockList.setItemAnimator(new DefaultItemAnimator());
                            mRecStockList.setAdapter(mStockListAdapter);
                            mRecStockList.setNestedScrollingEnabled(false);
                        }

                        if (mStockList.size() == 0){

                            Toast.makeText(CCProductQualityIssueActivity.this , "Oops! It looks like you have not yet assigned any brands! Go to settings -> other settings to assign the brands you stock!" , Toast.LENGTH_LONG).show();

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


}
