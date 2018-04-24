package com.oodi.jingoo.fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.oodi.jingoo.R;
import com.oodi.jingoo.activity.NoInternetActivity;
import com.oodi.jingoo.activity.RewardHistoryActivity;
import com.oodi.jingoo.adapter.CSMListAdapter;
import com.oodi.jingoo.adapter.RewardListAdapter;
import com.oodi.jingoo.pojo.CSM;
import com.oodi.jingoo.pojo.Reward;
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
public class YourPointsFragment extends Fragment {

    View view ;

    ImageView imgExpand ;
    LinearLayout lnrExpand , lnrHistory , lnrSO;
    TextView txtStoreName , txtStorePoint ;

    RecyclerView recCsmList , recRewards;
    List<CSM> csmList = new ArrayList<>();
    CSMListAdapter csmListAdapter ;

    List<Reward> rewardList = new ArrayList<>();
    RewardListAdapter rewardListAdapter ;

    public String expand = "true" , type = "" , token , store = "";
    public String points = "" ;

    AppUtils appUtils ;

    public YourPointsFragment() {
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
        view = inflater.inflate(R.layout.fragment_your_points, container, false);

        init();

        final Bundle bundle = this.getArguments();
        type = bundle.getString("type");

        SharedPreferences pr = getActivity().getSharedPreferences("Login", Context.MODE_PRIVATE);
        token = pr.getString("token" , "");

        if (!type.equals("csm")){
            lnrSO.setVisibility(View.GONE);
            txtStoreName.setText("Earnings: ");

        }

        lnrExpand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (expand.equals("false")){
                    expand = "true" ;
                    imgExpand.setImageResource(R.drawable.expand);
                    recCsmList.setVisibility(View.VISIBLE);
                }else {
                    expand = "false" ;
                    imgExpand.setImageResource(R.drawable.collaps);
                    recCsmList.setVisibility(View.GONE);
                }
            }
        });

        lnrHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity() , RewardHistoryActivity.class);
                intent.putExtra("store" , store);
                intent.putExtra("point" , points);
                intent.putExtra("type" , type);
                startActivity(intent);
            }
        });

        if (!appUtils.isOnLine()){
            Intent intent1 = new Intent(getActivity() , NoInternetActivity.class);
            startActivity(intent1);
            //appUtils.showToast(R.string.offline);
        }else {
            if (type.equals("csm")){
                csm_store_points();
            }else{
                shopowner_points();
            }

        }

        return view;
    }

    public void init(){

        recCsmList = (RecyclerView) view.findViewById(R.id.recCsmList);
        recRewards = (RecyclerView) view.findViewById(R.id.recRewards);
        imgExpand = (ImageView) view.findViewById(R.id.imgExpand);
        lnrExpand = (LinearLayout) view.findViewById(R.id.lnrExpand);
        lnrHistory = (LinearLayout) view.findViewById(R.id.lnrHistory);
        txtStoreName = (TextView) view.findViewById(R.id.txtStoreName);
        txtStorePoint = (TextView) view.findViewById(R.id.txtStorePoint);
        lnrSO = (LinearLayout) view.findViewById(R.id.lnrSO);

    }

    private void csm_store_points(){

        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setMessage("loading");
        pd.setCancelable(false);
        pd.show();

        csmList.clear();

        String REGISTER_URL = "" ;

        REGISTER_URL = getActivity().getResources().getString(R.string.base_url) + "v1/index.php/appshopboth/csm_store_points";

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
                            csmList.clear();
                            rewardList.clear();

                            JSONObject store_details = jsonObject.optJSONObject("store_details");

                            String id = store_details.optString("id");
                            points = store_details.optString("points");
                            store = store_details.optString("name");
                            String city = store_details.optString("city");

                            String note = "<font color='#000000'>" + store+": "+ "</font>" + "<font color='#CA282B'>" + points+ " coins" + "</font>";
                            //holder.txtUpdates.setText(Html.fromHtml(note));

                            txtStoreName.setText(Html.fromHtml(note));
                            txtStorePoint.setVisibility(View.GONE);

                            JSONArray csm_list = jsonObject.optJSONArray("csm_list");

                            for (int i = 0 ; i < csm_list.length() ; i++){

                                JSONObject object = csm_list.optJSONObject(i);

                                String id_ = object.optString("id");
                                String points_ = object.optString("points");
                                String name_ = object.optString("name");

                                CSM csm = new CSM();

                                csm.setId(id_);
                                csm.setPoints(points_);
                                csm.setName(name_);

                                csmList.add(csm);

                            }

                            JSONArray items_list = jsonObject.optJSONArray("items_list");

                            for (int i = 0 ; i < items_list.length() ; i++){

                                JSONObject object = items_list.optJSONObject(i);

                                String id_ = object.optString("id");
                                String item_name = object.optString("item_name");
                                String item_points = object.optString("item_points");
                                String item_image = object.optString("item_image");
                                String item_stock = object.optString("item_stock");

                                Reward reward = new Reward();

                                reward.setId(id_);
                                reward.setItem_name(item_name);
                                reward.setItem_points(item_points);
                                reward.setItem_image(item_image);
                                reward.setItem_stock(item_stock);

                                rewardList.add(reward);

                            }

                        }

                        csmListAdapter = new CSMListAdapter(getActivity() , csmList);
                        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                        recCsmList.setLayoutManager(mLayoutManager);
                        recCsmList.setAdapter(csmListAdapter);
                        recCsmList.setNestedScrollingEnabled(false);

                        rewardListAdapter = new RewardListAdapter(YourPointsFragment.this , rewardList);
                        final LinearLayoutManager mLayoutManager1 = new LinearLayoutManager(getActivity());
                        recRewards.setLayoutManager(mLayoutManager1);
                        recRewards.setAdapter(rewardListAdapter);
                        recRewards.setNestedScrollingEnabled(false);

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

    private void shopowner_points(){

        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setMessage("loading");
        pd.setCancelable(false);
        pd.show();

        csmList.clear();

        String REGISTER_URL = "" ;

        REGISTER_URL = getActivity().getResources().getString(R.string.base_url) + "v1/index.php/appshopboth/shopowner_points";

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
                            csmList.clear();
                            rewardList.clear();

                            JSONObject store_details = jsonObject.optJSONObject("store_details");

                            String id = store_details.optString("id");
                            points = store_details.optString("points");
                            store = store_details.optString("name");

                            String note = "<font color='#000000'>" + "Score: "+ "</font>" + "<font color='#CA282B'>" + points + " coins"+ "</font>" ;
                            //holder.txtUpdates.setText(Html.fromHtml(note));

                            txtStoreName.setText(Html.fromHtml(note));
                            //txtStoreName.setText(store+": ");
                            //txtStorePoint.setText(points);

                            JSONArray items_list = jsonObject.optJSONArray("items_list");

                            for (int i = 0 ; i < items_list.length() ; i++){

                                JSONObject object = items_list.optJSONObject(i);

                                String id_ = object.optString("id");
                                String item_name = object.optString("item_name");
                                String item_points = object.optString("item_points");
                                String item_image = object.optString("item_image");
                                String item_stock = object.optString("item_stock");

                                Reward reward = new Reward();

                                reward.setId(id_);
                                reward.setItem_name(item_name);
                                reward.setItem_points(item_points);
                                reward.setItem_image(item_image);
                                reward.setItem_stock(item_stock);

                                rewardList.add(reward);

                            }

                        }

                        rewardListAdapter = new RewardListAdapter(YourPointsFragment.this , rewardList);
                        final LinearLayoutManager mLayoutManager1 = new LinearLayoutManager(getActivity());
                        recRewards.setLayoutManager(mLayoutManager1);
                        recRewards.setAdapter(rewardListAdapter);
                        recRewards.setNestedScrollingEnabled(false);

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==2)
        {
            if (!appUtils.isOnLine()){
                Intent intent1 = new Intent(getActivity() , NoInternetActivity.class);
                startActivity(intent1);
                //appUtils.showToast(R.string.offline);
            }else {
                if (type.equals("csm")){
                    csm_store_points();
                }else{
                    shopowner_points();
                }

            }
        }
    }
}
