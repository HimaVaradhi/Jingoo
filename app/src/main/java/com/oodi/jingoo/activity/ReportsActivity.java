package com.oodi.jingoo.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.oodi.jingoo.R;
import com.oodi.jingoo.utility.AppUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ReportsActivity extends AppCompatActivity {

    LinearLayout mLnrDailySalesReport, mLnrTopSellingVarient, mLnrLowStockReport, mLnrSalesValuationReport, mLnrAnalytics;
    TextView mTxtHeaderName, mTxtHome, Home;
    ImageView mImgBack;
    AppUtils appUtils;
    public static Activity report;
    private int mYear, mMonth, mDay, mHour, mMinute;
    String startDate = "", endDate = "";
    android.app.AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appUtils = new AppUtils(this);
        SharedPreferences prefs1 = getSharedPreferences("language", MODE_PRIVATE);
        String lang = prefs1.getString("lang", "");
        if (lang.equals("hi")) {
            appUtils.setLocale("hi");
        } else {
            appUtils.setLocale("en");
        }
        setContentView(R.layout.activity_reports);

        report = this;

        init();

        ImageView imgHelp = (ImageView) findViewById(R.id.imgHelp);
        imgHelp.setVisibility(View.GONE);
        mTxtHome.setVisibility(View.VISIBLE);
        Home.setVisibility(View.VISIBLE);

        mTxtHome.setText(getResources().getString(R.string.reports));

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

        mTxtHeaderName.setText(getResources().getString(R.string.reports));

        mLnrAnalytics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ReportsActivity.this, StoreAnalyticsActivity.class);
                startActivity(intent);

            }
        });

        mLnrDailySalesReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReportsActivity.this, DailySalesReportActivity.class);
                intent.putExtra("type", "report");
                startActivity(intent);
            }
        });

        mLnrTopSellingVarient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReportsActivity.this, TopSellingVariantReportActivity.class);
                intent.putExtra("type", "dailyTop");
                startActivity(intent);
            }
        });

        mLnrLowStockReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Intent intent = new Intent(ReportsActivity.this , CashOnHandActivity.class);
                intent.putExtra("type" , "lowStock");
                startActivity(intent);*/
                tac();
            }
        });

        mLnrSalesValuationReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReportsActivity.this, SalesValuationReportActivity.class);
                intent.putExtra("type", "lowStock");
                startActivity(intent);
            }
        });

    }

    public void init() {

        mLnrAnalytics = (LinearLayout) findViewById(R.id.lnrAnalytics);
        mLnrDailySalesReport = (LinearLayout) findViewById(R.id.lnrDailySalesReport);
        mLnrTopSellingVarient = (LinearLayout) findViewById(R.id.lnrTopSellingVarient);
        mLnrLowStockReport = (LinearLayout) findViewById(R.id.lnrLowStockReport);
        mLnrSalesValuationReport = (LinearLayout) findViewById(R.id.lnrSalesValuationReport);
        mImgBack = (ImageView) findViewById(R.id.imgBack);
        mTxtHeaderName = (TextView) findViewById(R.id.txtHeaderName);
        mTxtHome = (TextView) findViewById(R.id.txtHome);
        Home = (TextView) findViewById(R.id.Home);

    }

    public void tac() {

        LayoutInflater layoutInflater = LayoutInflater.from(ReportsActivity.this);
        View promptsView = layoutInflater.inflate(R.layout.email_report, null);

        final android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(ReportsActivity.this);
        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder.setCancelable(true);
        alertDialog = alertDialogBuilder.create();
        alertDialog.setCanceledOnTouchOutside(true);

        final Button btnDone = (Button) promptsView.findViewById(R.id.btnDone);
        final TextView txtStartDate = (TextView) promptsView.findViewById(R.id.txtStartDate);
        final TextView txtEndDate = (TextView) promptsView.findViewById(R.id.txtEndDate);
        final TextView txtClose = (TextView) promptsView.findViewById(R.id.txtClose);

        SimpleDateFormat dfDate_day1 = new SimpleDateFormat("dd-MM-yyyy");
        Calendar c1 = Calendar.getInstance();
        final String data1 = dfDate_day1.format(c1.getTime());
        txtStartDate.setText(data1);
        txtEndDate.setText(data1);

        SimpleDateFormat dfDate_day = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        final String _data = dfDate_day.format(c.getTime());

        startDate = _data;
        endDate = _data;

        txtStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(ReportsActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                String day = "";
                                String month = "";

                                if (dayOfMonth == 1) {
                                    day = String.valueOf("01");
                                } else if (dayOfMonth == 2) {
                                    day = String.valueOf("02");
                                } else if (dayOfMonth == 3) {
                                    day = String.valueOf("03");
                                } else if (dayOfMonth == 4) {
                                    day = String.valueOf("04");
                                } else if (dayOfMonth == 5) {
                                    day = String.valueOf("05");
                                } else if (dayOfMonth == 6) {
                                    day = String.valueOf("06");
                                } else if (dayOfMonth == 7) {
                                    day = String.valueOf("07");
                                } else if (dayOfMonth == 8) {
                                    day = String.valueOf("08");
                                } else if (dayOfMonth == 9) {
                                    day = String.valueOf("09");
                                } else {
                                    day = String.valueOf(dayOfMonth);
                                }

                                if (monthOfYear == 0) {
                                    month = String.valueOf("01");
                                } else if (monthOfYear == 1) {
                                    month = String.valueOf("02");
                                } else if (monthOfYear == 2) {
                                    month = String.valueOf("03");
                                } else if (monthOfYear == 3) {
                                    month = String.valueOf("04");
                                } else if (monthOfYear == 4) {
                                    month = String.valueOf("05");
                                } else if (monthOfYear == 5) {
                                    month = String.valueOf("06");
                                } else if (monthOfYear == 6) {
                                    month = String.valueOf("07");
                                } else if (monthOfYear == 7) {
                                    month = String.valueOf("08");
                                } else if (monthOfYear == 8) {
                                    month = String.valueOf("09");
                                } else {
                                    month = String.valueOf(monthOfYear + 1);
                                }

                                startDate = year + "-" + month + "-" + day;
                                txtStartDate.setText(day + "-" + month + "-" + year);


                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }

        });

        txtEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(ReportsActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                String day = "";
                                String month = "";

                                if (dayOfMonth == 1) {
                                    day = String.valueOf("01");
                                } else if (dayOfMonth == 2) {
                                    day = String.valueOf("02");
                                } else if (dayOfMonth == 3) {
                                    day = String.valueOf("03");
                                } else if (dayOfMonth == 4) {
                                    day = String.valueOf("04");
                                } else if (dayOfMonth == 5) {
                                    day = String.valueOf("05");
                                } else if (dayOfMonth == 6) {
                                    day = String.valueOf("06");
                                } else if (dayOfMonth == 7) {
                                    day = String.valueOf("07");
                                } else if (dayOfMonth == 8) {
                                    day = String.valueOf("08");
                                } else if (dayOfMonth == 9) {
                                    day = String.valueOf("09");
                                } else {
                                    day = String.valueOf(dayOfMonth);
                                }

                                if (monthOfYear == 0) {
                                    month = String.valueOf("01");
                                } else if (monthOfYear == 1) {
                                    month = String.valueOf("02");
                                } else if (monthOfYear == 2) {
                                    month = String.valueOf("03");
                                } else if (monthOfYear == 3) {
                                    month = String.valueOf("04");
                                } else if (monthOfYear == 4) {
                                    month = String.valueOf("05");
                                } else if (monthOfYear == 5) {
                                    month = String.valueOf("06");
                                } else if (monthOfYear == 6) {
                                    month = String.valueOf("07");
                                } else if (monthOfYear == 7) {
                                    month = String.valueOf("08");
                                } else if (monthOfYear == 8) {
                                    month = String.valueOf("09");
                                } else {
                                    month = String.valueOf(monthOfYear + 1);
                                }

                                endDate = year + "-" + month + "-" + day;
                                txtEndDate.setText(day + "-" + month + "-" + year);


                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }

        });

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!startDate.equals("") && !endDate.equals("")) {
                    email_report_shopowner();
                }
                //email_report_shopowner
            }
        });

        txtClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    private void email_report_shopowner() {

       /* if (csm.equals("csm")){
            store_id = csm_Storeid ;
        }*/

        appUtils.showProgressBarLoading();

        SharedPreferences prefs = ReportsActivity.this.getSharedPreferences("Login", Context.MODE_PRIVATE);
        final String token = prefs.getString("token", "");

        String REGISTER_URL = ReportsActivity.this.getResources().getString(R.string.base_url) + "v1/index.php/appshopboth/email_report_shopowner";

        final String KEY_TOKEN = "token";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                            //String msg = jsonObject.getString("msg");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Log.d("Json Array", "doInBackground: " + jsonObject);

                        String status = jsonObject.optString("success");

                        Toast.makeText(ReportsActivity.this, jsonObject.optString("msg"), Toast.LENGTH_LONG).show();
                        alertDialog.dismiss();

                        appUtils.dismissProgressBar();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        appUtils.dismissProgressBar();
                        //Toast.makeText(MainActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(KEY_TOKEN, token);
                params.put("from_date", startDate);
                params.put("to_date", endDate);
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(ReportsActivity.this);
        requestQueue.add(stringRequest);
    }

}
