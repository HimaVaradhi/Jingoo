package com.oodi.jingoo.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
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
import com.oodi.jingoo.pojo.AnalyticsFooter;
import com.oodi.jingoo.pojo.AnalyticsHeader;
import com.oodi.jingoo.utility.AppUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;

public class AnalyticsActivity extends AppCompatActivity {

    ImageView mImgBack ;
    TextView mTxtHeaderName , mTxtHome , Home;
    AppUtils appUtils ;
    RecyclerView recyclerView ;
    TextView mTxtStartDate , mTxtEndDate , mTxtTSline , mTxtRefillLine , mTxtCSline ;
    private int mYear, mMonth, mDay, mHour, mMinute;
    String startDate = "" , endDate = "" , token , id = "data_sales";
    LinearLayout mLnrTS , mLnrRefill , mLnrCS ;
    private SectionedRecyclerViewAdapter sectionAdapter;
    List<AnalyticsHeader> dietHeaderList = new ArrayList<>();
    List<AnalyticsFooter> dietFooterList = new ArrayList<>();

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
        setContentView(R.layout.activity_analytics);

        init();

        SimpleDateFormat dfDate_day1= new SimpleDateFormat("dd-MM-yyyy");
        Calendar c1 = Calendar.getInstance();
        final String data1 = dfDate_day1.format(c1.getTime());
        mTxtStartDate.setText(data1);
        mTxtEndDate.setText(data1);

        SimpleDateFormat dfDate_day= new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        final String data=dfDate_day.format(c.getTime());

        startDate = data ;
        endDate = data ;

        SharedPreferences prefs = this.getSharedPreferences("Login", Context.MODE_PRIVATE);
        token = prefs.getString("token" , "");

        mTxtHome.setText(getResources().getString(R.string.a));

        Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mImgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        mTxtHeaderName.setText(getResources().getString(R.string.a));

        if (!appUtils.isOnLine()){
            Intent intent = new Intent(getApplicationContext() , NoInternetActivity.class);
            startActivity(intent);
            //appUtils.showToast(R.string.offline);
        }else {
            analytics();
        }
        mLnrTS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!id.equals("data_sales")) {

                    id = "data_sales";

                    mTxtTSline.setVisibility(View.VISIBLE);
                    mTxtCSline.setVisibility(View.INVISIBLE);
                    mTxtRefillLine.setVisibility(View.INVISIBLE);

                    //mTxtStartDate.setText(data1);
                    //mTxtEndDate.setText(data1);

                    //startDate = data;
                    //endDate = data;

                    analytics();

                }
            }
        });

        mLnrRefill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!id.equals("data_purchases")) {

                    id = "data_purchases";

                    mTxtTSline.setVisibility(View.INVISIBLE);
                    mTxtCSline.setVisibility(View.INVISIBLE);
                    mTxtRefillLine.setVisibility(View.VISIBLE);

                    //mTxtStartDate.setText(data1);
                    //mTxtEndDate.setText(data1);

                    //startDate = data;
                    //endDate = data;

                    analytics();
                }
            }
        });

        mLnrCS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!id.equals("data_current_stock")) {

                    id = "data_current_stock";

                    mTxtTSline.setVisibility(View.INVISIBLE);
                    mTxtCSline.setVisibility(View.VISIBLE);
                    mTxtRefillLine.setVisibility(View.INVISIBLE);

                    //mTxtStartDate.setText(data1);
                    //mTxtEndDate.setText(data1);

                    //startDate = data;
                    //endDate = data;

                    analytics();
                }
            }
        });

        mTxtStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(AnalyticsActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                String day = "";
                                String month = "";

                                if (dayOfMonth == 1){
                                    day = String.valueOf("01") ;
                                }else if (dayOfMonth == 2){
                                    day = String.valueOf("02") ;
                                }else if (dayOfMonth == 3){
                                    day = String.valueOf("03") ;
                                }else if (dayOfMonth == 4){
                                    day = String.valueOf("04") ;
                                }else if (dayOfMonth == 5){
                                    day = String.valueOf("05") ;
                                }else if (dayOfMonth == 6){
                                    day = String.valueOf("06") ;
                                }else if (dayOfMonth == 7){
                                    day = String.valueOf("07") ;
                                }else if (dayOfMonth == 8){
                                    day = String.valueOf("08") ;
                                }else if (dayOfMonth == 9){
                                    day = String.valueOf("09") ;
                                }else {
                                    day = String.valueOf(dayOfMonth) ;
                                }

                                if (monthOfYear == 0){
                                    month = String.valueOf("01") ;
                                }else if (monthOfYear == 1){
                                    month = String.valueOf("02") ;
                                }else if (monthOfYear == 2){
                                    month = String.valueOf("03") ;
                                }else if (monthOfYear == 3){
                                    month = String.valueOf("04") ;
                                }else if (monthOfYear == 4){
                                    month = String.valueOf("05") ;
                                }else if (monthOfYear == 5){
                                    month = String.valueOf("06") ;
                                }else if (monthOfYear == 6){
                                    month = String.valueOf("07") ;
                                }else if (monthOfYear == 7){
                                    month = String.valueOf("08") ;
                                }else if (monthOfYear == 8){
                                    month = String.valueOf("09") ;
                                }else {
                                    month = String.valueOf(monthOfYear+1) ;
                                }

                                startDate = year + "-" + month + "-" + day ;
                                mTxtStartDate.setText(day + "-" + month + "-" + year);

                                if (!startDate.equals("") && !endDate.equals("")){
                                    analytics();
                                }

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }

        });

        mTxtEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(AnalyticsActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                String day = "";
                                String month = "";

                                if (dayOfMonth == 1){
                                    day = String.valueOf("01") ;
                                }else if (dayOfMonth == 2){
                                    day = String.valueOf("02") ;
                                }else if (dayOfMonth == 3){
                                    day = String.valueOf("03") ;
                                }else if (dayOfMonth == 4){
                                    day = String.valueOf("04") ;
                                }else if (dayOfMonth == 5){
                                    day = String.valueOf("05") ;
                                }else if (dayOfMonth == 6){
                                    day = String.valueOf("06") ;
                                }else if (dayOfMonth == 7){
                                    day = String.valueOf("07") ;
                                }else if (dayOfMonth == 8){
                                    day = String.valueOf("08") ;
                                }else if (dayOfMonth == 9){
                                    day = String.valueOf("09") ;
                                }else {
                                    day = String.valueOf(dayOfMonth) ;
                                }

                                if (monthOfYear == 0){
                                    month = String.valueOf("01") ;
                                }else if (monthOfYear == 1){
                                    month = String.valueOf("02") ;
                                }else if (monthOfYear == 2){
                                    month = String.valueOf("03") ;
                                }else if (monthOfYear == 3){
                                    month = String.valueOf("04") ;
                                }else if (monthOfYear == 4){
                                    month = String.valueOf("05") ;
                                }else if (monthOfYear == 5){
                                    month = String.valueOf("06") ;
                                }else if (monthOfYear == 6){
                                    month = String.valueOf("07") ;
                                }else if (monthOfYear == 7){
                                    month = String.valueOf("08") ;
                                }else if (monthOfYear == 8){
                                    month = String.valueOf("09") ;
                                }else {
                                    month = String.valueOf(monthOfYear+1) ;
                                }

                                endDate = year + "-" + month + "-" + day ;
                                mTxtEndDate.setText(day + "-" + month + "-" + year);

                                if (!startDate.equals("") && !endDate.equals("")){
                                    analytics();

                                }

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }

        });

    }

    public void init(){

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        mImgBack = (ImageView) findViewById(R.id.imgBack);
        mTxtHeaderName = (TextView) findViewById(R.id.txtHeaderName);
        mTxtHome = (TextView) findViewById(R.id.txtHome);
        Home = (TextView) findViewById(R.id.Home);
        mTxtStartDate = (TextView) findViewById(R.id.txtStartDate);
        mTxtEndDate = (TextView) findViewById(R.id.txtEndDate);
        mTxtTSline = (TextView) findViewById(R.id.txtTSline);
        mTxtRefillLine = (TextView) findViewById(R.id.txtRefillLine);
        mTxtCSline = (TextView) findViewById(R.id.txtCSline);
        mLnrTS = (LinearLayout) findViewById(R.id.lnrTS);
        mLnrRefill = (LinearLayout) findViewById(R.id.lnrRefill);
        mLnrCS = (LinearLayout) findViewById(R.id.lnrCS);

    }

    class ContactsSection extends StatelessSection {

        List<AnalyticsHeader> headerList;
        List<AnalyticsFooter> footerList;
        int i ;

        public ContactsSection(List<AnalyticsHeader> workoutExercise , int i , List<AnalyticsFooter> list ) {
            super(R.layout.view_header, R.layout.view_anal_footerlist);

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

            final AnalyticsFooter analyticsFooter = footerList.get(position);

            itemHolder.txtName.setText(analyticsFooter.getProducts_name());
            itemHolder.txt1.setText(analyticsFooter.getValue3());
            itemHolder.txt2.setText(analyticsFooter.getValue2());
            itemHolder.txt3.setText(analyticsFooter.getValue1());

            itemHolder.txt4.setText(String.valueOf(Integer.parseInt(analyticsFooter.getValue1()) + Integer.parseInt(analyticsFooter.getValue2()) + Integer.parseInt(analyticsFooter.getValue3())));

        }

        @Override
        public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
            return new HeaderViewHolder(view);
        }

        @Override
        public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
            final HeaderViewHolder headerHolder = (HeaderViewHolder) holder;

            final AnalyticsHeader dietHeader = headerList.get(i) ;

            headerHolder.tvTitle.setText(dietHeader.getName());

        }
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle  ;

        public HeaderViewHolder(View view) {
            super(view);

            tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        //RecyclerView recAnalyticsFooter ;
        public TextView txtName , txt1 , txt2 , txt3 , txt4;


        public ItemViewHolder(View view) {
            super(view);

            //recAnalyticsFooter = (RecyclerView) view.findViewById(R.id.recAnalyticsFooter);
            txtName = (TextView) view.findViewById(R.id.txtName);
            txt1 = (TextView) view.findViewById(R.id.txt1);
            txt2 = (TextView) view.findViewById(R.id.txt2);
            txt3 = (TextView) view.findViewById(R.id.txt3);
            txt4 = (TextView) view.findViewById(R.id.txt4);

        }
    }

    private void analytics(){

        appUtils.showProgressBarLoading();

        dietHeaderList.clear();

        SharedPreferences prefs = AnalyticsActivity.this.getSharedPreferences("Login", Context.MODE_PRIVATE);
        final String token = prefs.getString("token" , "");

        String REGISTER_URL = AnalyticsActivity.this.getResources().getString(R.string.base_url) + "v1/index.php/appshopboth/stock_report";

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

                                if (id.equals("data_sales")){
                                    array = jsonObject.getJSONArray("data_sales");

                                    AnalyticsHeader analyticsHeader ;

                                    for(int i=0; i< array.length(); i++)
                                    {
                                        JSONObject c = array.getJSONObject(i);

                                        String id = c.getString("id");
                                        String name = c.getString("name");

                                        analyticsHeader = new AnalyticsHeader();

                                        analyticsHeader.setId(id);
                                        analyticsHeader.setName(name);

                                        JSONArray jExercise = c.getJSONArray("products");

                                        dietFooterList = new ArrayList<>();

                                        for(int j=0; j< jExercise.length(); j++)
                                        {
                                            JSONObject jExerciseObject = jExercise.getJSONObject(j);

                                            String products_name = jExerciseObject.getString("products_name");
                                            String value1 = jExerciseObject.getString("180ML");
                                            String value2 = jExerciseObject.getString("375ML");
                                            String value3 = jExerciseObject.getString("750ML");

                                            AnalyticsFooter analyticsFooter = new AnalyticsFooter();

                                            analyticsFooter.setProducts_name(products_name);
                                            analyticsFooter.setValue1(value1);
                                            analyticsFooter.setValue2(value2);
                                            analyticsFooter.setValue3(value3);

                                            dietFooterList.add(analyticsFooter);

                                            analyticsHeader.setAnalyticsFooters(dietFooterList);

                                        }

                                        dietHeaderList.add(analyticsHeader);

                                    }
                                }else if (id.equals("data_purchases")){
                                    array = jsonObject.getJSONArray("data_purchases");

                                    AnalyticsHeader analyticsHeader ;

                                    for(int i=0; i< array.length(); i++)
                                    {
                                        JSONObject c = array.getJSONObject(i);

                                        String id = c.getString("id");
                                        String name = c.getString("name");

                                        analyticsHeader = new AnalyticsHeader();

                                        analyticsHeader.setId(id);
                                        analyticsHeader.setName(name);

                                        JSONArray jExercise = c.getJSONArray("products");

                                        dietFooterList = new ArrayList<>();

                                        for(int j=0; j< jExercise.length(); j++)
                                        {
                                            JSONObject jExerciseObject = jExercise.getJSONObject(j);

                                            String products_name = jExerciseObject.getString("products_name");
                                            String value1 = jExerciseObject.getString("180ML");
                                            String value2 = jExerciseObject.getString("375ML");
                                            String value3 = jExerciseObject.getString("750ML");

                                            AnalyticsFooter analyticsFooter = new AnalyticsFooter();

                                            analyticsFooter.setProducts_name(products_name);
                                            analyticsFooter.setValue1(value1);
                                            analyticsFooter.setValue2(value2);
                                            analyticsFooter.setValue3(value3);

                                            dietFooterList.add(analyticsFooter);

                                            analyticsHeader.setAnalyticsFooters(dietFooterList);

                                        }

                                        dietHeaderList.add(analyticsHeader);

                                    }
                                }else if (id.equals("data_current_stock")){
                                    array = jsonObject.getJSONArray("data_current_stock");

                                    AnalyticsHeader analyticsHeader ;

                                    for(int i=0; i< array.length(); i++)
                                    {
                                        JSONObject c = array.getJSONObject(i);

                                        String id = c.getString("id");
                                        String name = c.getString("name");

                                        analyticsHeader = new AnalyticsHeader();

                                        analyticsHeader.setId(id);
                                        analyticsHeader.setName(name);

                                        JSONArray jExercise = c.getJSONArray("products");

                                        dietFooterList = new ArrayList<>();

                                        for(int j=0; j< jExercise.length(); j++)
                                        {
                                            JSONObject jExerciseObject = jExercise.getJSONObject(j);

                                            String products_name = jExerciseObject.getString("products_name");
                                            String value1 = jExerciseObject.getString("180ML");
                                            String value2 = jExerciseObject.getString("375ML");
                                            String value3 = jExerciseObject.getString("750ML");

                                            AnalyticsFooter analyticsFooter = new AnalyticsFooter();

                                            analyticsFooter.setProducts_name(products_name);
                                            analyticsFooter.setValue1(value1);
                                            analyticsFooter.setValue2(value2);
                                            analyticsFooter.setValue3(value3);

                                            dietFooterList.add(analyticsFooter);

                                            analyticsHeader.setAnalyticsFooters(dietFooterList);

                                        }

                                        dietHeaderList.add(analyticsHeader);

                                    }
                                }




                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            sectionAdapter = new SectionedRecyclerViewAdapter();

                            for (int i = 0 ; i < dietHeaderList.size() ; i++){

                                sectionAdapter.addSection(new ContactsSection(dietHeaderList , i , dietHeaderList.get(i).getAnalyticsFooters()));

                            }

                            recyclerView.setLayoutManager(new LinearLayoutManager(AnalyticsActivity.this));
                            recyclerView.setAdapter(sectionAdapter);
                        }

                      /*  if (status.equals("0")){

                            if (dietHeaderList.size() == 0){
                                sectionAdapter = new SectionedRecyclerViewAdapter();

                                for (int i = 0 ; i < dietHeaderList.size() ; i++){

                                    sectionAdapter.addSection(new ContactsSection(dietHeaderList , i , dietHeaderList.get(i).getAnalyticsFooters()));

                                }

                                recyclerView.setLayoutManager(new LinearLayoutManager(AnalyticsActivity.this));
                                recyclerView.setAdapter(sectionAdapter);
                            }

                        }*/

                        appUtils.dismissProgressBar();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        appUtils.dismissProgressBar();
                        //Toast.makeText(MainActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put(KEY_TOKEN,token);
                params.put("from_date",startDate);
                params.put("to_date",endDate);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(AnalyticsActivity.this);
        requestQueue.add(stringRequest);
    }

}
