package com.oodi.jingoo.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.oodi.jingoo.R;
import com.oodi.jingoo.adapter.BannerAdapter1;
import com.oodi.jingoo.adapter.ExpandableListAdapter;
import com.oodi.jingoo.pojo.Banners;
import com.oodi.jingoo.pojo.NotificationFooter;
import com.oodi.jingoo.pojo.NotificationHeader;
import com.oodi.jingoo.utility.AppUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;

public class NotificationsActivity extends AppCompatActivity {

    ViewPager pager ;
    List<Banners> bannersList = new ArrayList<>();
    BannerAdapter1 bannerAdapter ;
    private SectionedRecyclerViewAdapter sectionAdapter;
    RecyclerView recyclerView ;
    TextView mTxtHome ;
    ArrayList<NotificationFooter> dietFooterList = new ArrayList<>();
    List<NotificationHeader> dietHeaderList = new ArrayList<>();
    int currentPage;
    ImageView mImgBack ;
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    AppUtils appUtils ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        appUtils = new AppUtils(this);

        LoginActivity.notification = false ;

        init();

        mImgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mTxtHome.setOnClickListener(new View.OnClickListener() {
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
            offers();

            pagerList();
        }
    }

    public void init(){

        pager = (ViewPager) findViewById(R.id.pager);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        mTxtHome = (TextView) findViewById(R.id.textView);
        mImgBack = (ImageView) findViewById(R.id.imgBack);
        expListView = (ExpandableListView) findViewById(R.id.lvExp);

    }

    class ContactsSection extends StatelessSection {

        List<NotificationHeader> headerList;
        List<NotificationFooter> footerList;
        int i ;

        public ContactsSection(List<NotificationHeader> workoutExercise , int i , List<NotificationFooter> list ) {
            super(R.layout.diet_section_ex1_header, R.layout.diet_footer_item);

            this.headerList = workoutExercise;
            this.footerList = list;
            this.i = i ;
        }

        @Override
        public int getContentItemsTotal() {
            return footerList.size();
        }

        @Override
        public RecyclerView.ViewHolder getItemViewHolder(View view) {
            return new ItemViewHolder(view);
        }

        @Override
        public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
            final ItemViewHolder itemHolder = (ItemViewHolder) holder;

            final NotificationFooter dietFooter = footerList.get(position);

            itemHolder.txtTitle.setText(dietFooter.getNotification_title());
            itemHolder.txtDescription.setText(dietFooter.getNotification_desc());

        }

        @Override
        public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
            return new HeaderViewHolder(view);
        }

        @Override
        public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
            final HeaderViewHolder headerHolder = (HeaderViewHolder) holder;

            final NotificationHeader dietHeader = headerList.get(i) ;

            headerHolder.txtDate.setText(dietHeader.getDate());


        }
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder {

        TextView txtDate ;

        public HeaderViewHolder(View view) {
            super(view);

            txtDate = (TextView) view.findViewById(R.id.txtDate);

        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView  txtTitle , txtDescription ;

        public ItemViewHolder(View view) {
            super(view);

            txtTitle = (TextView) view.findViewById(R.id.txtTitle);
            txtDescription = (TextView) view.findViewById(R.id.txtDescription);

        }
    }

    private void offers(){

        dietHeaderList.clear();

        SharedPreferences prefs = NotificationsActivity.this.getSharedPreferences("Login", Context.MODE_PRIVATE);
        final String token = prefs.getString("token" , "");

        String REGISTER_URL = NotificationsActivity.this.getResources().getString(R.string.base_url) + "v1/index.php/appshopboth/offers";

        final String KEY_TOKEN = "token";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                            String msg = jsonObject.getString("msg");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Log.d("Json Array", "doInBackground: " + jsonObject);

                        String status = jsonObject.optString("success");

                        if(status.equals("1"))
                        {
                            JSONArray array = null;

                            try {

                                array = jsonObject.getJSONArray("data");

                                NotificationHeader analyticsHeader ;

                                for(int i=0; i< array.length(); i++)
                                {
                                    JSONObject c = array.getJSONObject(i);

                                    String date = c.getString("date");

                                    analyticsHeader = new NotificationHeader();

                                    analyticsHeader.setDate(date);

                                    JSONArray jExercise = c.getJSONArray("notifications");

                                    dietFooterList = new ArrayList<>();

                                    for(int j=0; j< jExercise.length(); j++)
                                    {
                                        JSONObject jExerciseObject = jExercise.getJSONObject(j);

                                        String notification_id = jExerciseObject.getString("notification_id");
                                        String notification_title = jExerciseObject.getString("notification_title");
                                        String notification_date = jExerciseObject.getString("notification_date");
                                        String notification_desc = jExerciseObject.getString("notification_desc");
                                        String customer_id = jExerciseObject.getString("customer_id");

                                        NotificationFooter analyticsFooter = new NotificationFooter();

                                        analyticsFooter.setNotification_title(notification_title);
                                        analyticsFooter.setNotification_desc(notification_desc);

                                        dietFooterList.add(analyticsFooter);

                                        analyticsHeader.setNotificationFooterList(dietFooterList);

                                    }

                                    dietHeaderList.add(analyticsHeader);

                                }

                                sectionAdapter = new SectionedRecyclerViewAdapter();

                                for (int i = 0 ; i < dietHeaderList.size() ; i++){

                                    sectionAdapter.addSection(new ContactsSection(dietHeaderList , i , dietHeaderList.get(i).getNotificationFooterList()));

                                }

                                recyclerView.setLayoutManager(new LinearLayoutManager(NotificationsActivity.this));
                                recyclerView.setAdapter(sectionAdapter);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                      /*  if (status.equals("0")){

                            if (dietHeaderList.size() == 0){
                                sectionAdapter = new SectionedRecyclerViewAdapter();

                                for (int i = 0 ; i < dietHeaderList.size() ; i++){

                                    sectionAdapter.addSection(new ContactsSection(dietHeaderList , i , dietHeaderList.get(i).getAnalyticsFooters()));

                                }

                                recyclerView.setLayoutManager(new LinearLayoutManager(NotificationsActivity.this));
                                recyclerView.setAdapter(sectionAdapter);
                            }

                        }*/

                        listAdapter = new ExpandableListAdapter(NotificationsActivity.this, dietHeaderList, dietFooterList);
                        expListView.setAdapter(listAdapter);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put(KEY_TOKEN,token);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(NotificationsActivity.this);
        requestQueue.add(stringRequest);
    }

    private void pagerList(){

        SharedPreferences prefs = NotificationsActivity.this.getSharedPreferences("Login", Context.MODE_PRIVATE);
        final String token = prefs.getString("token" , "");

        String REGISTER_URL = NotificationsActivity.this.getResources().getString(R.string.base_url) + "v1/index.php/appshopboth/banners";

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

                                    String id = c.getString("id") ;
                                    String banner_name = c.getString("banner_name")  ;
                                    String banner_image = c.getString("banner_image")  ;
                                    String link = c.getString("link");

                                    Banners banners = new Banners();

                                    banners.setId(id);
                                    banners.setBanner_image(NotificationsActivity.this.getResources().getString(R.string.base_url) +banner_image);
                                    banners.setBanner_name(banner_name);
                                    banners.setLink(link);

                                    bannersList.add(banners);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }

                        bannerAdapter = new BannerAdapter1(NotificationsActivity.this , bannersList);
                        pager.setAdapter(bannerAdapter);
                        //indicator.setViewPager(pager);

                        final Handler handler = new Handler();

                        final Runnable update = new Runnable() {
                            public void run() {
                                if (currentPage == bannersList.size()) {
                                    currentPage = 0;
                                }
                                pager.setCurrentItem(currentPage++, true);
                            }
                        };

                        new Timer().schedule(new TimerTask() {

                            @Override
                            public void run() {
                                handler.post(update);
                            }
                        }, 4000, 4000);


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put(KEY_USERNAME,token);
                params.put("type" , "OFFERS");

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(NotificationsActivity.this);
        requestQueue.add(stringRequest);
    }

}
