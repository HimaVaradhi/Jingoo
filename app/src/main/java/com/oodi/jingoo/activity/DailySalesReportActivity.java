package com.oodi.jingoo.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.oodi.jingoo.R;
import com.oodi.jingoo.pojo.DailySales;
import com.oodi.jingoo.utility.AppUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DailySalesReportActivity extends AppCompatActivity {

    TextView mTxtHeaderName , mTxtHome , Home;
    ImageView mImgBack;
    AppUtils appUtils ;
    private PieChart mChart;
    String token = "";
    List<DailySales> mDailySalesList = new ArrayList<>();
    ImageView mImgQty , mImgValue ;
    String type = "q";


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
        setContentView(R.layout.activity_daily_sales_report);

        init();

        ImageView imgHelp = (ImageView) findViewById(R.id.imgHelp);
        imgHelp.setVisibility(View.GONE);
        mTxtHome.setVisibility(View.VISIBLE);
        Home.setVisibility(View.VISIBLE);

        SharedPreferences prefs = this.getSharedPreferences("Login", Context.MODE_PRIVATE);
        token = prefs.getString("token" , "");

        mTxtHeaderName.setText(getResources().getString(R.string.Daily_Sales_Report));
        mTxtHome.setText(getResources().getString(R.string.Daily_Sales_Report));

        Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    ReportsActivity.report.finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                finish();
            }
        });

        mTxtHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mImgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mImgQty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mImgQty.setImageResource(R.drawable.ic_qty_selected);
                mImgValue.setImageResource(R.drawable.ic_value_unselected);

                type = "q";

                daily_sales_report();

            }
        });

        mImgValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mImgQty.setImageResource(R.drawable.ic_qty_unselected);
                mImgValue.setImageResource(R.drawable.ic_value_selected);

                type = "p";

                daily_sales_report();

            }
        });

        daily_sales_report();
    }

    public void init(){

        mTxtHeaderName = (TextView) findViewById(R.id.txtHeaderName);
        mTxtHome = (TextView) findViewById(R.id.txtHome);
        mImgBack = (ImageView) findViewById(R.id.imgBack);
        Home = (TextView) findViewById(R.id.Home);
        mChart = (PieChart) findViewById(R.id.chart1);
        mImgValue = (ImageView) findViewById(R.id.imgValue);
        mImgQty = (ImageView) findViewById(R.id.imgQty);

    }

    private void daily_sales_report() {

        appUtils.showProgressBarLoading();

        mDailySalesList.clear();

        String REGISTER_URL = DailySalesReportActivity.this.getResources().getString(R.string.base_url) + "v1/index.php/appshopboth/daily_sales_report";

        final String KEY_TOKEN = "token";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        String success = jsonObject.optString("success");

                        if (success.equals("1")){

                            JSONArray jsonArray = jsonObject.optJSONArray("row_array_dailysales_chart");

                            for (int i = 0 ; i < jsonArray.length() ; i++){

                                JSONObject jObject = jsonArray.optJSONObject(i);

                                String selling_qty = jObject.optString("selling_qty");
                                String category_name = jObject.optString("category_name");

                                DailySales dailySales = new DailySales();

                                dailySales.setSelling_qty(selling_qty);
                                dailySales.setCategory_name(category_name);

                                mDailySalesList.add(dailySales);

                            }
                        }

                        mChart.setUsePercentValues(false);

                        ArrayList<Entry> yvalues = new ArrayList<Entry>();
                        ArrayList<String> xVals = new ArrayList<String>();

                        int totalValue = 0 ;

                        for (int total = 0 ; total < mDailySalesList.size() ; total++){

                            totalValue = totalValue + Integer.parseInt(mDailySalesList.get(total).getSelling_qty());

                        }

                        for (int j = 0 ; j < mDailySalesList.size() ; j++){

                            if (!mDailySalesList.get(j).getSelling_qty().equals("0")) {

                                yvalues.add(new Entry(Float.parseFloat(mDailySalesList.get(j).getSelling_qty()), j, mDailySalesList.get(j).getCategory_name()));

                                xVals.add(String.format("%.2f", (((Float.parseFloat(mDailySalesList.get(j).getSelling_qty())) / totalValue) * 100)) + "% \n" + mDailySalesList.get(j).getCategory_name());
                            }
                        }

                        PieDataSet dataSet = new PieDataSet(yvalues, "");

                        ArrayList<Integer> colors = new ArrayList<Integer>();

                        for (int c : ColorTemplate.VORDIPLOM_COLORS)
                            colors.add(c);

                        for (int c : ColorTemplate.JOYFUL_COLORS)
                            colors.add(c);

                        for (int c : ColorTemplate.COLORFUL_COLORS)
                            colors.add(c);

                        for (int c : ColorTemplate.LIBERTY_COLORS)
                            colors.add(c);

                        for (int c : ColorTemplate.PASTEL_COLORS)
                            colors.add(c);

                        colors.add(ColorTemplate.getHoloBlue());

                        dataSet.setColors(colors);

                        PieData data = new PieData(xVals, dataSet);
                        data.setDrawValues(false);
                        data.setValueFormatter(new PercentFormatter());
                        mChart.setDescription("");
                        mChart.setData(data);
                        mChart.getLegend().setEnabled(false);
                        //data.setValueTextColor(Color.WHITE);


                        mChart.setDrawHoleEnabled(true);
                        mChart.setTransparentCircleRadius(60f);
                        mChart.setHoleRadius(60f);

                        data.setValueTextSize(12f);

                        mChart.animateXY(1400, 1400);

                        appUtils.dismissProgressBar();

                    }
                },
                new Response.ErrorListener()

                {
                    @Override
                    public void onErrorResponse (VolleyError error){
                        appUtils.dismissProgressBar();
                    }
                })

        {
            @Override
            protected Map<String, String> getParams () {
                Map<String, String> params = new HashMap<String, String>();
                params.put(KEY_TOKEN, token);
                params.put("type",type);

                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
