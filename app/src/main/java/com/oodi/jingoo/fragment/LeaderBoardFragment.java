package com.oodi.jingoo.fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.oodi.jingoo.R;
import com.oodi.jingoo.activity.NoInternetActivity;
import com.oodi.jingoo.adapter.RewardUserAdapter;
import com.oodi.jingoo.pojo.RewardUser;
import com.oodi.jingoo.utility.AppUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class LeaderBoardFragment extends Fragment {

    View view ;

    ImageView mImgBack , mImgDp , mImgBgDistrict , mImgBgDepot , mImgBgState , mImgWt;
    TextView mTxtPoints ,mTxtHeaderName , mTxtHome
            , mTxtDistrict , mTxtDepot , mTxtState , mTxtDistrictRank , mTxtDepotRank , mTxtStateRank , mTxtTaC , home , txtCityNew , txtStateNew , txtRank;
    FrameLayout mFrameDistrict , mFrameDepot , mFrameState ;
    AppUtils appUtils ;
    String token  , img , name , id = "state_leaderboard" , type = "";
    RecyclerView mRecRewardUserName ;
    RewardUserAdapter mRewardUserAdapter ;
    List<RewardUser> mRewardUserList = new ArrayList<>();
    public List<List<RewardUser>> workoutScheduleList ;
    List<RewardUser> r = new ArrayList<>();

    public LeaderBoardFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        appUtils = new AppUtils(getActivity());
        SharedPreferences prefs1 = getActivity().getSharedPreferences("language", Context.MODE_PRIVATE);
        String lang = prefs1.getString("lang", "");
        if (lang.equals("hi")){
            appUtils.setLocale("hi");
        }else{
            appUtils.setLocale("en");
        }
        view = inflater.inflate(R.layout.fragment_leader_board, container, false);

        init();

        final Bundle bundle = this.getArguments();
        type = bundle.getString("type");

        SharedPreferences pr = getActivity().getSharedPreferences("Login", Context.MODE_PRIVATE);
        token = pr.getString("token" , "");
        img = pr.getString("avatar" , "");
        name = pr.getString("name" , "");

        mFrameDistrict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!id.equals("district_leaderboard")){

                    id = "district_leaderboard";
                    if (type.equals("csm")){
                        leaderboards_csm_new();
                    }else{
                        leaderboard_shopowner_new();
                    }

                    mImgBgDepot.setVisibility(View.GONE);
                    mImgBgState.setVisibility(View.GONE);
                    mImgBgDistrict.setVisibility(View.VISIBLE);

                    mTxtDistrict.setTextColor(getResources().getColor(R.color.white));
                    mTxtDistrictRank.setTextColor(getResources().getColor(R.color.white));

                    mTxtDepot.setTextColor(getResources().getColor(R.color.black));
                    mTxtDepotRank.setTextColor(getResources().getColor(R.color.black));

                    mTxtState.setTextColor(getResources().getColor(R.color.black));
                    mTxtStateRank.setTextColor(getResources().getColor(R.color.black));

                }

            }
        });

        txtCityNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!id.equals("city_leaderboard")){

                    id = "city_leaderboard";
                    if (type.equals("csm")){
                        leaderboards_csm_new();
                    }else{
                        leaderboard_shopowner_new();
                    }

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        txtCityNew.setBackground(getActivity().getResources().getDrawable(R.drawable.state_bg_));
                        txtCityNew.setTextColor(getActivity().getResources().getColor(R.color.white));
                        txtStateNew.setBackground(getActivity().getResources().getDrawable(R.drawable.city_bg_));
                        txtStateNew.setTextColor(getActivity().getResources().getColor(R.color.black));
                    }

                    mImgBgDistrict.setVisibility(View.GONE);
                    mImgBgState.setVisibility(View.GONE);
                    mImgBgDepot.setVisibility(View.VISIBLE);

                    mTxtDepot.setTextColor(getResources().getColor(R.color.white));
                    mTxtDepotRank.setTextColor(getResources().getColor(R.color.white));

                    mTxtDistrict.setTextColor(getResources().getColor(R.color.black));
                    mTxtDistrictRank.setTextColor(getResources().getColor(R.color.black));

                    mTxtState.setTextColor(getResources().getColor(R.color.black));
                    mTxtStateRank.setTextColor(getResources().getColor(R.color.black));

                }

            }
        });

        txtStateNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!id.equals("state_leaderboard")){

                    id = "state_leaderboard";
                    if (type.equals("csm")){
                        leaderboards_csm_new();
                    }else{
                        leaderboard_shopowner_new();
                    }

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        txtCityNew.setTextColor(getActivity().getResources().getColor(R.color.black));
                        txtStateNew.setTextColor(getActivity().getResources().getColor(R.color.white));

                        txtCityNew.setBackground(getActivity().getResources().getDrawable(R.drawable.city_bg));
                        txtStateNew.setBackground(getActivity().getResources().getDrawable(R.drawable.state_bg));
                    }

                    mImgBgDepot.setVisibility(View.GONE);
                    mImgBgDistrict.setVisibility(View.GONE);
                    mImgBgState.setVisibility(View.VISIBLE);

                    mTxtState.setTextColor(getResources().getColor(R.color.white));
                    mTxtStateRank.setTextColor(getResources().getColor(R.color.white));

                    mTxtDistrict.setTextColor(getResources().getColor(R.color.black));
                    mTxtDistrictRank.setTextColor(getResources().getColor(R.color.black));

                    mTxtDepot.setTextColor(getResources().getColor(R.color.black));
                    mTxtDepotRank.setTextColor(getResources().getColor(R.color.black));

                }

            }
        });

        if (!appUtils.isOnLine()){
            Intent intent1 = new Intent(getActivity() , NoInternetActivity.class);
            startActivity(intent1);
            //appUtils.showToast(R.string.offline);
        }else {
            if (type.equals("csm")){
                leaderboards_csm_new();
            }else{
                leaderboard_shopowner_new();
            }

        }

        return view;
    }

    public void init(){

        mTxtDistrictRank = (TextView) view.findViewById(R.id.txtDistrictRank);
        mTxtDepotRank = (TextView) view.findViewById(R.id.txtDepotRank);
        mTxtStateRank = (TextView) view.findViewById(R.id.txtStateRank);
        mTxtDistrict = (TextView) view.findViewById(R.id.txtDistrict);
        mTxtDepot = (TextView) view.findViewById(R.id.txtDepot);
        mTxtState = (TextView) view.findViewById(R.id.txtState);
        mTxtHeaderName = (TextView) view.findViewById(R.id.txtHeaderName);
        mTxtHome = (TextView) view.findViewById(R.id.txtHome);
        mImgBack = (ImageView) view.findViewById(R.id.imgBack);
        mImgDp = (ImageView) view.findViewById(R.id.imgDp);
        mImgBgDistrict = (ImageView) view.findViewById(R.id.imgBgDistrict);
        mImgBgDepot = (ImageView) view.findViewById(R.id.imgBgDepot);
        mImgBgState = (ImageView) view.findViewById(R.id.imgBgState);
        mFrameDistrict = (FrameLayout) view.findViewById(R.id.frameDistrict);
        mFrameDepot = (FrameLayout) view.findViewById(R.id.frameDepot);
        mFrameState = (FrameLayout) view.findViewById(R.id.frameState);
        mRecRewardUserName = (RecyclerView) view.findViewById(R.id.recRewardUserName);
        home = (TextView) view.findViewById(R.id.home);
        mTxtTaC = (TextView) view.findViewById(R.id.txtTaC);
        mTxtPoints = (TextView) view.findViewById(R.id.txtPoints);
        mImgWt = (ImageView) view.findViewById(R.id.imgWt);
        txtCityNew = (TextView) view.findViewById(R.id.txtCityNew);
        txtStateNew = (TextView) view.findViewById(R.id.txtStateNew);
        txtRank = (TextView) view.findViewById(R.id.txtRank);

    }

    private void rewardUsersList(){

        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setMessage("loading");
        pd.setCancelable(false);
        pd.show();

        mRewardUserList.clear();

        String REGISTER_URL = "" ;

        if (type.equals("csm")){
            REGISTER_URL = getActivity().getResources().getString(R.string.base_url) + "v1/index.php/appshopboth/leaderboards_csm_new";
        }else{
            REGISTER_URL = getActivity().getResources().getString(R.string.base_url) + "v1/index.php/appshopboth/leaderboards_shopowners";
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
                            String my_state_rank = jsonObject.optString("my_state_rank");
                            String my_district_rank = jsonObject.optString("my_district_rank");
                            String my_depot_rank = jsonObject.optString("my_city_rank");
                            String total_points = jsonObject.optString("total_points");
                            String my_state = jsonObject.optString("my_state");
                            String my_city = jsonObject.optString("my_city");

                            mTxtDistrictRank.setText(my_district_rank);
                            mTxtDepotRank.setText(my_depot_rank);
                            mTxtStateRank.setText(my_state_rank);
                            //mTxtPoints.setText(total_points + " Points");
                            mTxtState.setText(my_state);
                            mTxtDepot.setText(my_city);

                            if (id.equals("district_leaderboard")){
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
                                        String avatar = c.getString("my_district_rank");

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
                            }else if (id.equals("city_leaderboard")){
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
                                        rewardUser.setAvatar(my_city);


                                        mRewardUserList.add(rewardUser);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            }else if (id.equals("state_leaderboard")){
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
                                        rewardUser.setAvatar(my_state);


                                        mRewardUserList.add(rewardUser);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            }


                        }

                        mRewardUserAdapter = new RewardUserAdapter(getActivity() , mRewardUserList);
                        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                        mRecRewardUserName.setLayoutManager(mLayoutManager);
                        mRecRewardUserName.setItemAnimator(new DefaultItemAnimator());
                        mRecRewardUserName.setAdapter(mRewardUserAdapter);
                        mRecRewardUserName.setNestedScrollingEnabled(false);

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

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    private void leaderboards_csm_new(){

        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setMessage("loading");
        pd.setCancelable(false);
        pd.show();

        mRewardUserList.clear();

        String REGISTER_URL = getActivity().getResources().getString(R.string.base_url) + "v1/index.php/appshopboth/leaderboards_csm_new";

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
                            String my_state_rank = jsonObject.optString("my_state_rank");
                           // String my_district_rank = jsonObject.optString("my_district_rank");
                            String my_depot_rank = jsonObject.optString("my_city_rank");
                            //String total_points = jsonObject.optString("total_points");
                            String my_state = jsonObject.optString("my_state");
                            String my_city = jsonObject.optString("my_city");

                            //mTxtDistrictRank.setText(my_district_rank);
                            mTxtDepotRank.setText(my_depot_rank);
                            mTxtStateRank.setText(my_state_rank);
                            //mTxtPoints.setText(total_points + " Points");
                            txtStateNew.setText(my_state);
                            txtCityNew.setText(my_city);

                            if (id.equals("city_leaderboard")){
                                txtRank.setText("Your Rank: " + my_depot_rank);

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
                                        String username = c.getString("name");
                                        String points = c.getString("points");
                                        String rank = c.getString("rank");
                                        String avatar = c.getString("city");


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
                            }else if (id.equals("state_leaderboard")){
                                txtRank.setText("Your Rank: " + my_state_rank);

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
                                        String username = c.getString("name");
                                        String points = c.getString("points");
                                        String rank = c.getString("rank");
                                        String avatar = c.getString("city");

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

                        //workoutScheduleList = Lists.partition(mRewardUserList, 7);

                        //r = workoutScheduleList.get(0);

                        mRewardUserAdapter = new RewardUserAdapter(getActivity() , mRewardUserList);
                        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                        mRecRewardUserName.setLayoutManager(mLayoutManager);
                        mRecRewardUserName.setItemAnimator(new DefaultItemAnimator());
                        mRecRewardUserName.setAdapter(mRewardUserAdapter);
                        mRecRewardUserName.setNestedScrollingEnabled(false);

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

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    private void leaderboard_shopowner_new(){

        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setMessage("loading");
        pd.setCancelable(false);
        pd.show();

        mRewardUserList.clear();

        String REGISTER_URL = getActivity().getResources().getString(R.string.base_url) + "v1/index.php/appshopboth/leaderboard_shopowner_new";

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
                            String my_state_rank = jsonObject.optString("my_state_rank");
                            // String my_district_rank = jsonObject.optString("my_district_rank");
                            String my_depot_rank = jsonObject.optString("my_city_rank");
                            //String total_points = jsonObject.optString("total_points");
                            String my_state = jsonObject.optString("my_state");
                            String my_city = jsonObject.optString("my_city");

                            //mTxtDistrictRank.setText(my_district_rank);
                            mTxtDepotRank.setText(my_depot_rank);
                            mTxtStateRank.setText(my_state_rank);
                            //mTxtPoints.setText(total_points + " Points");
                            txtStateNew.setText(my_state);
                            txtCityNew.setText(my_city);

                            if (id.equals("city_leaderboard")){

                                txtRank.setText("Your Rank: " + my_depot_rank);

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
                                        String username = c.getString("name");
                                        String points = c.getString("points");
                                        String rank = c.getString("rank");
                                        String avatar = c.getString("city");


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
                            }else if (id.equals("state_leaderboard")){
                                txtRank.setText("Your Rank: " + my_state_rank);

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
                                        String username = c.getString("name");
                                        String points = c.getString("points");
                                        String rank = c.getString("rank");
                                        String avatar = c.getString("city");

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

                        //workoutScheduleList = Lists.partition(mRewardUserList, 7);

                        //r = workoutScheduleList.get(0);

                        mRewardUserAdapter = new RewardUserAdapter(getActivity() , mRewardUserList);
                        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                        mRecRewardUserName.setLayoutManager(mLayoutManager);
                        mRecRewardUserName.setItemAnimator(new DefaultItemAnimator());
                        mRecRewardUserName.setAdapter(mRewardUserAdapter);
                        mRecRewardUserName.setNestedScrollingEnabled(false);

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

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

}
