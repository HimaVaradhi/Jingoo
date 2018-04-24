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
import android.view.View;
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
import com.oodi.jingoo.adapter.ManageTeamAdapter;
import com.oodi.jingoo.pojo.Team;
import com.oodi.jingoo.utility.AppUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ManageTeamActivity extends AppCompatActivity {

    RecyclerView mRecStockList ;
    List<Team> mStockList = new ArrayList<>();
    ManageTeamAdapter mStockListAdapter ;
    ImageView mImgBack ;
    LinearLayout mLnrAdd ;
    TextView mTxtHeaderName , mTxtHome , Home;
    public static Activity manageTeam ;
    String token , type = "";
    AppUtils appUtils ;

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
        setContentView(R.layout.activity_manage_team);

        init();

        ImageView imgHelp = (ImageView) findViewById(R.id.imgHelp);
        imgHelp.setVisibility(View.GONE);
        mTxtHome.setVisibility(View.VISIBLE);
        Home.setVisibility(View.VISIBLE);

        SharedPreferences prefs = this.getSharedPreferences("Login", Context.MODE_PRIVATE);
        token = prefs.getString("token" , "");

        mTxtHome.setText(getResources().getString(R.string.manage_team));

        Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        manageTeam = this ;

        mImgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        mLnrAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManageTeamActivity.this , AddTeamActivity.class);
                intent.putExtra("id" , "add");
                startActivity(intent);
            }
        });

        mTxtHeaderName.setText(getResources().getString(R.string.manage_team));


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!appUtils.isOnLine()){
            Intent intent = new Intent(getApplicationContext() , NoInternetActivity.class);
            startActivity(intent);
            //appUtils.showToast(R.string.offline);
        }else {
            getCSMList();
        }
    }

    public void init(){

        mRecStockList = (RecyclerView) findViewById(R.id.recStockList);
        mImgBack = (ImageView) findViewById(R.id.imgBack);
        mLnrAdd = (LinearLayout) findViewById(R.id.lnrAdd);
        mTxtHeaderName = (TextView) findViewById(R.id.txtHeaderName);
        mTxtHome = (TextView) findViewById(R.id.txtHome);
        Home = (TextView) findViewById(R.id.Home);

    }

    public void getCSMList(){

        final ProgressDialog pd = new ProgressDialog(ManageTeamActivity.this);
        pd.setMessage("loading");
        pd.show();

        mStockList.clear();

        String REGISTER_URL = ManageTeamActivity.this.getResources().getString(R.string.base_url) + "v1/index.php/appshopboth/listcsm";

        final String KEY_USERNAME = "token";

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
                        String msg = jsonObject.optString("msg");

                        if (!msg.equals("")){
                            Toast.makeText(ManageTeamActivity.this,msg,Toast.LENGTH_LONG).show();
                        }
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
                                    String username = c.getString("username");
                                    String phone = c.getString("phone");
                                    String email = c.getString("email");
                                    String store_name = c.getString("store_name");
                                    String avatar = c.getString("avatar");
                                    String store_id = c.getString("store_id");
                                    String role_selling_price = c.getString("role_selling_price");
                                    String role_transfer_stock = c.getString("role_transfer_stock");
                                    String role_reports = c.getString("role_reports");

                                    Team csm = new Team();

                                    csm.setId(id);
                                    csm.setUsername(username);
                                    csm.setPhone(phone);
                                    csm.setEmail(email);
                                    csm.setStore_name(store_name);
                                    csm.setAvatar(avatar);
                                    csm.setStore_id(store_id);
                                    csm.setRole_selling_price(role_selling_price);
                                    csm.setRole_transfer_stock(role_transfer_stock);
                                    csm.setRole_reports(role_reports);

                                    mStockList.add(csm);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }

                        }

                        mStockListAdapter = new ManageTeamAdapter(ManageTeamActivity.this,  mStockList);

                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(ManageTeamActivity.this);
                        mRecStockList.setLayoutManager(mLayoutManager);
                        mRecStockList.setItemAnimator(new DefaultItemAnimator());
                        mRecStockList.setAdapter(mStockListAdapter);
                        mRecStockList.setNestedScrollingEnabled(false);

                        pd.dismiss();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pd.dismiss();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put(KEY_USERNAME,token);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


}
