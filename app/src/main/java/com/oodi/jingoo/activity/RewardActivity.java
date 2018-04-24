package com.oodi.jingoo.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.common.collect.Lists;
import com.oodi.jingoo.R;
import com.oodi.jingoo.adapter.RewardUserAdapter;
import com.oodi.jingoo.fragment.LeaderBoardFragment;
import com.oodi.jingoo.fragment.YourPointsFragment;
import com.oodi.jingoo.pojo.RewardUser;
import com.oodi.jingoo.utility.AppUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RewardActivity extends AppCompatActivity {

    public static Activity mContext;

    ImageView mImgBack, mImgDp, mImgBgDistrict, mImgBgDepot, mImgBgState, mImgWt;
    TextView mTxtPoints, mTxtHeaderName, mTxtHome, mTxtDistrictLine, mTxtDepotLine, mTxtStateLine,
            mTxtDistrict, mTxtDepot, mTxtState, mTxtDistrictRank, mTxtDepotRank, mTxtStateRank, mTxtTaC, home;
    FrameLayout mFrameDistrict, mFrameDepot, mFrameState;
    AppUtils appUtils;
    String token, img, name, id = "state_leaderboard", type = "";
    RecyclerView mRecRewardUserName;
    RewardUserAdapter mRewardUserAdapter;
    List<RewardUser> mRewardUserList = new ArrayList<>();
    public List<List<RewardUser>> workoutScheduleList;
    List<RewardUser> r = new ArrayList<>();

    LinearLayout lnrLeaderboard, lnrYP;
    TextView txtLbLine, txtYPLine;

    String selectedScreen = "lb" , lang = "" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appUtils = new AppUtils(this);
        SharedPreferences prefs1 = getSharedPreferences("language", MODE_PRIVATE);
        lang = prefs1.getString("lang", "");
        if (lang.equals("")){
            lang="en";
        }
        if (lang.equals("hi")) {
            appUtils.setLocale("hi");
        } else {
            appUtils.setLocale("en");
        }
        setContentView(R.layout.activity_reward);
        mContext = this;

        init();

        ImageView imageView3 = (ImageView) findViewById(R.id.imageView);

        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                try {
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

                setWT();

                mImgWt.setVisibility(View.VISIBLE);

            }
        });

        Intent intent = getIntent();
        type = intent.getStringExtra("type");

        if (type.equals("csm")) {
            mTxtHome.setText(getResources().getString(R.string.csm_leader));
        } else {
            mTxtHome.setText(getResources().getString(R.string.referral_leader));
        }

        //=====================================================================================================================

        setWT();

        SharedPreferences wt = getSharedPreferences("wt", MODE_PRIVATE);
        String first = wt.getString("lb", "");

        if (first.equals("1")) {
            mImgWt.setVisibility(View.GONE);
        } else {
            mImgWt.setVisibility(View.VISIBLE);
        }

        //=====================================================================================================================

        mTxtTaC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://yoofy.in/terms-and-conditions/";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                //tac();
            }
        });

        SharedPreferences pr = this.getSharedPreferences("Login", Context.MODE_PRIVATE);
        token = pr.getString("token", "");
        img = pr.getString("avatar", "");
        name = pr.getString("name", "");

        mTxtHeaderName.setText(name);

        mImgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        lnrLeaderboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedScreen = "lb";

                txtYPLine.setVisibility(View.GONE);
                txtLbLine.setVisibility(View.VISIBLE);

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                LeaderBoardFragment fragment = new LeaderBoardFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("type", type);

                fragment.setArguments(bundle);

                transaction.replace(R.id.root_acc, fragment, "");

                transaction.commit();

            }
        });

        lnrYP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                selectedScreen = "yp";

                setWT();

                SharedPreferences wt = getSharedPreferences("wt", MODE_PRIVATE);
                String first = wt.getString("yp", "");

                if (first.equals("1")) {
                    mImgWt.setVisibility(View.GONE);
                } else {
                    mImgWt.setVisibility(View.VISIBLE);
                }


                txtYPLine.setVisibility(View.VISIBLE);
                txtLbLine.setVisibility(View.GONE);

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                YourPointsFragment fragment = new YourPointsFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("type", type);

                fragment.setArguments(bundle);

                transaction.replace(R.id.root_acc, fragment, "");

                transaction.commit();

            }
        });

        mImgWt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mImgWt.setVisibility(View.GONE);

                if (selectedScreen.equals("lb")){
                    SharedPreferences.Editor editor = getSharedPreferences("wt", MODE_PRIVATE).edit();
                    editor.putString("lb", "1");

                    editor.commit();
                }else {
                    SharedPreferences.Editor editor = getSharedPreferences("wt", MODE_PRIVATE).edit();
                    editor.putString("yp", "1");

                    editor.commit();
                }



            }
        });


        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        LeaderBoardFragment fragment = new LeaderBoardFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("type", type);
        fragment.setArguments(bundle);

        transaction.replace(R.id.root_acc, fragment, "");

        transaction.commit();

    }

    public void setWT(){

        if (selectedScreen.equals("yp")){
            if (lang.equals("hi") && !type.equals("csm")) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    mImgWt.setBackground(getResources().getDrawable(R.drawable.hso_yp));
                }
            } else if (lang.equals("en") && !type.equals("csm")) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    mImgWt.setBackground(getResources().getDrawable(R.drawable.eso_yp));
                }
            } else if (lang.equals("hi") && type.equals("csm")) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    mImgWt.setBackground(getResources().getDrawable(R.drawable.hcsm_yp));
                }
            } else if (lang.equals("en") && type.equals("csm")) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    mImgWt.setBackground(getResources().getDrawable(R.drawable.ecsm_yp));
                }
            }
        }else {
            if (lang.equals("hi")){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    mImgWt.setBackground(getResources().getDrawable(R.drawable.hlb));
                }
            } else if (lang.equals("en")){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    mImgWt.setBackground(getResources().getDrawable(R.drawable.elb));
                }
            }
        }


    }

    public void tac() {

        LayoutInflater layoutInflater = LayoutInflater.from(RewardActivity.this);
        View promptsView = layoutInflater.inflate(R.layout.tac, null);

        final android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(RewardActivity.this);
        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder.setCancelable(true);
        final android.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCanceledOnTouchOutside(true);

        final TextView Update = (TextView) promptsView.findViewById(R.id.btn_confirm);

        Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    public void init() {

        mTxtDistrictRank = (TextView) findViewById(R.id.txtDistrictRank);
        mTxtDepotRank = (TextView) findViewById(R.id.txtDepotRank);
        mTxtStateRank = (TextView) findViewById(R.id.txtStateRank);
        mTxtDistrict = (TextView) findViewById(R.id.txtDistrict);
        mTxtDepot = (TextView) findViewById(R.id.txtDepot);
        mTxtState = (TextView) findViewById(R.id.txtState);
        mTxtHeaderName = (TextView) findViewById(R.id.txtHeaderName);
        mTxtHome = (TextView) findViewById(R.id.txtHome);
        mImgBack = (ImageView) findViewById(R.id.imgBack);
        mImgDp = (ImageView) findViewById(R.id.imgDp);
        mImgBgDistrict = (ImageView) findViewById(R.id.imgBgDistrict);
        mImgBgDepot = (ImageView) findViewById(R.id.imgBgDepot);
        mImgBgState = (ImageView) findViewById(R.id.imgBgState);
        mTxtDistrictLine = (TextView) findViewById(R.id.txtDistrictLine);
        mTxtDepotLine = (TextView) findViewById(R.id.txtDepotLine);
        mTxtStateLine = (TextView) findViewById(R.id.txtStateLine);
        mFrameDistrict = (FrameLayout) findViewById(R.id.frameDistrict);
        mFrameDepot = (FrameLayout) findViewById(R.id.frameDepot);
        mFrameState = (FrameLayout) findViewById(R.id.frameState);
        mRecRewardUserName = (RecyclerView) findViewById(R.id.recRewardUserName);
        home = (TextView) findViewById(R.id.home);
        mTxtTaC = (TextView) findViewById(R.id.txtTaC);
        mTxtPoints = (TextView) findViewById(R.id.txtPoints);
        mImgWt = (ImageView) findViewById(R.id.imgWt);

        lnrLeaderboard = (LinearLayout) findViewById(R.id.lnrLeaderboard);
        lnrYP = (LinearLayout) findViewById(R.id.lnrYP);
        txtLbLine = (TextView) findViewById(R.id.txtLbLine);
        txtYPLine = (TextView) findViewById(R.id.txtYPLine);


    }

    private void rewardUsersList() {

        final ProgressDialog pd = new ProgressDialog(RewardActivity.this);
        pd.setMessage("loading");
        pd.setCancelable(false);
        pd.show();

        mRewardUserList.clear();

        String REGISTER_URL = "";

        if (type.equals("csm")) {
            REGISTER_URL = RewardActivity.this.getResources().getString(R.string.base_url) + "v1/index.php/appshopboth/leaderboards";
        } else {
            REGISTER_URL = RewardActivity.this.getResources().getString(R.string.base_url) + "v1/index.php/appshopboth/leaderboards_shopowners";
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

                        if (status.equals("1")) {
                            String my_state_rank = jsonObject.optString("my_state_rank");
                            String my_district_rank = jsonObject.optString("my_district_rank");
                            String my_depot_rank = jsonObject.optString("my_city_rank");
                            String total_points = jsonObject.optString("total_points");
                            String my_state = jsonObject.optString("my_state");
                            String my_city = jsonObject.optString("my_city");


                            mTxtDistrictRank.setText(my_district_rank);
                            mTxtDepotRank.setText(my_depot_rank);
                            mTxtStateRank.setText(my_state_rank);
                            mTxtPoints.setText(total_points + " Points");
                            mTxtState.setText(my_state);
                            mTxtDepot.setText(my_city);

                            if (id.equals("district_leaderboard")) {
                                JSONArray array = null;
                                try {
                                    array = jsonObject.getJSONArray("district_leaderboard");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                for (int i = 0; i < array.length(); i++) {

                                    JSONObject c = null;
                                    try {
                                        c = array.getJSONObject(i);

                                        String id = c.getString("id");
                                        String username = c.getString("username");
                                        String points = c.getString("points");
                                        String rank = c.getString("rank");
                                        String avatar = c.getString("avatar");

                                        RewardUser rewardUser = new RewardUser();

                                        rewardUser.setName(username);
                                        rewardUser.setPoints(points);
                                        rewardUser.setRank(rank);
                                        rewardUser.setAvatar(avatar);

                                        mRewardUserList.add(rewardUser);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            } else if (id.equals("city_leaderboard")) {
                                JSONArray array = null;
                                try {
                                    array = jsonObject.getJSONArray("city_leaderboard");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                for (int i = 0; i < array.length(); i++) {

                                    JSONObject c = null;
                                    try {
                                        c = array.getJSONObject(i);

                                        String id = c.getString("id");
                                        String username = c.getString("username");
                                        String points = c.getString("points");
                                        String rank = c.getString("rank");
                                        String avatar = c.getString("avatar");


                                        RewardUser rewardUser = new RewardUser();

                                        rewardUser.setName(username);
                                        rewardUser.setPoints(points);
                                        rewardUser.setRank(rank);
                                        rewardUser.setAvatar(avatar);


                                        mRewardUserList.add(rewardUser);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            } else if (id.equals("state_leaderboard")) {
                                JSONArray array = null;
                                try {
                                    array = jsonObject.getJSONArray("state_leaderboard");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                for (int i = 0; i < array.length(); i++) {

                                    JSONObject c = null;
                                    try {
                                        c = array.getJSONObject(i);

                                        String id = c.getString("id");
                                        String username = c.getString("username");
                                        String points = c.getString("points");
                                        String rank = c.getString("rank");
                                        String avatar = c.getString("avatar");


                                        RewardUser rewardUser = new RewardUser();

                                        rewardUser.setName(username);
                                        rewardUser.setPoints(points);
                                        rewardUser.setRank(rank);
                                        rewardUser.setAvatar(avatar);


                                        mRewardUserList.add(rewardUser);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            }


                        }

                        workoutScheduleList = Lists.partition(mRewardUserList, 7);

                        r = workoutScheduleList.get(0);

                        mRewardUserAdapter = new RewardUserAdapter(RewardActivity.this, mRewardUserList);
                        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(RewardActivity.this);
                        mRecRewardUserName.setLayoutManager(mLayoutManager);
                        mRecRewardUserName.setItemAnimator(new DefaultItemAnimator());
                        mRecRewardUserName.setAdapter(mRewardUserAdapter);
                        mRecRewardUserName.setNestedScrollingEnabled(false);


                       /* mRecRewardUserName.addOnScrollListener(new RecyclerView.OnScrollListener() {
                            @Override
                            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                                super.onScrollStateChanged(recyclerView, newState);
                            }

                            @Override
                            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                                super.onScrolled(recyclerView, dx, dy);

                                if (mLayoutManager.findLastVisibleItemPosition() == workoutScheduleList.get(0).size()-1)
                                    r.addAll(workoutScheduleList.get(1));

                                Log.e("r" , r+"");
                                    mRewardUserAdapter.notifyDataSetChanged();

                            }
                        });*/


                        pd.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(LoginActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                        pd.dismiss();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(KEY_USERNAME, token);

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(RewardActivity.this);
        requestQueue.add(stringRequest);
    }

}
