package com.oodi.jingoo.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import com.oodi.jingoo.adapter.HistoryListAdapter;
import com.oodi.jingoo.pojo.RewardHistory;
import com.oodi.jingoo.utility.AppUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RewardHistoryActivity extends AppCompatActivity {

    ImageView mImgBack , imgHelp;
    TextView mTxtHeaderName , mTxtHome , Home , txtName , txtPoints;
    AppUtils appUtils ;
    String  token , type = "all" , store = "" , point = "" , type_ = "";
    RecyclerView recRedeemHistory ;
    List<RewardHistory> rewardHistoryList = new ArrayList<>();
    HistoryListAdapter historyListAdapter ;

    LinearLayout lnrEH , lnrL30D ;
    TextView txtEHLine , txtL30D ;

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
        setContentView(R.layout.activity_reward_history);

        init();

        imgHelp.setVisibility(View.GONE);

        Intent intent = getIntent();
        store = intent.getStringExtra("store");
        point = intent.getStringExtra("point");
        type_ = intent.getStringExtra("type");

        txtName.setText("Score: ");
        txtPoints.setText(point + " coins");

        SharedPreferences prefs = this.getSharedPreferences("Login", Context.MODE_PRIVATE);
        token = prefs.getString("token" , "");

        Home.setVisibility(View.VISIBLE);
        mTxtHome.setVisibility(View.VISIBLE);
        mTxtHome.setText(getResources().getString(R.string.History));

        Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RewardActivity.mContext.finish();
                onBackPressed();
            }
        });

        mImgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        mTxtHeaderName.setText(getResources().getString(R.string.History));

        lnrEH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                txtL30D.setVisibility(View.GONE);
                txtEHLine.setVisibility(View.VISIBLE);

                type = "all";

                redeem_history();

            }
        });

        lnrL30D.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                txtL30D.setVisibility(View.VISIBLE);
                txtEHLine.setVisibility(View.GONE);

                type = "30days";

                redeem_history();

            }
        });

        redeem_history();

    }

    public void init(){

        mImgBack = (ImageView) findViewById(R.id.imgBack);
        imgHelp = (ImageView) findViewById(R.id.imgHelp);
        mTxtHeaderName = (TextView) findViewById(R.id.txtHeaderName);
        mTxtHome = (TextView) findViewById(R.id.txtHome);
        txtName = (TextView) findViewById(R.id.txtName);
        txtPoints = (TextView) findViewById(R.id.txtPoints);
        Home = (TextView) findViewById(R.id.Home);
        recRedeemHistory = (RecyclerView) findViewById(R.id.recRedeemHistory);

        lnrEH = (LinearLayout) findViewById(R.id.lnrEH);
        lnrL30D = (LinearLayout) findViewById(R.id.lnrL30D);
        txtEHLine = (TextView) findViewById(R.id.txtEHLine);
        txtL30D = (TextView) findViewById(R.id.txtL30D);

    }

    private void redeem_history(){

        final ProgressDialog pd = new ProgressDialog(RewardHistoryActivity.this);
        pd.setMessage("loading");
        pd.setCancelable(false);
        pd.show();

        SharedPreferences pr = RewardHistoryActivity.this.getSharedPreferences("Login", Context.MODE_PRIVATE);
        final String token = pr.getString("token" , "");

        rewardHistoryList.clear();

        String REGISTER_URL = "";

        if (type_.equals("csm")){
            REGISTER_URL = RewardHistoryActivity.this.getResources().getString(R.string.base_url) + "v1/index.php/appshopboth/redeem_history";
        }else {
            REGISTER_URL = RewardHistoryActivity.this.getResources().getString(R.string.base_url) + "v1/index.php/appshopboth/redeem_history_shopowner";
        }


        final String KEY_USERNAME = "token";

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

                            JSONArray history_list = jsonObject.optJSONArray("history_list");

                            for (int i = 0 ; i < history_list.length() ; i++){

                                JSONObject object = history_list.optJSONObject(i);

                                String id = object.optString("id");
                                String type = object.optString("type");
                                String points = object.optString("points");
                                String note = object.optString("note");
                                String created_at = object.optString("created_at");

                                RewardHistory history = new RewardHistory();

                                history.setId(id);
                                history.setType(type);
                                history.setPoints(points);
                                history.setNote(note);
                                history.setCreated_at(created_at);

                                rewardHistoryList.add(history);

                            }

                            historyListAdapter = new HistoryListAdapter(RewardHistoryActivity.this , rewardHistoryList);
                            final LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                            recRedeemHistory.setLayoutManager(mLayoutManager);
                            recRedeemHistory.setAdapter(historyListAdapter);

                        }else {

                            Toast.makeText(RewardHistoryActivity.this , jsonObject.optString("msg") , Toast.LENGTH_LONG).show();

                        }

                        pd.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(LoginActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                        pd.dismiss();

                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put(KEY_USERNAME,token);
                params.put("type" , type);

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(RewardHistoryActivity.this);
        requestQueue.add(stringRequest);
    }


}
