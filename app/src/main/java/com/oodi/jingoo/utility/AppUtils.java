package com.oodi.jingoo.utility;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.oodi.jingoo.R;
import com.wooplr.spotlight.SpotlightView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by pc on 6/27/17.
 */

public class AppUtils {

    Activity mContext;
    ProgressDialog dialog;
    SpotlightView spotLight ;
    public String success = "" ;

    public AppUtils(Activity mContext) {
        this.mContext = mContext;
        dialog  = new ProgressDialog(mContext);
        dialog.setMessage("loading...");
    }

    public boolean isOnLine() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void showIntro(View view, String usageId , String msg) {
        spotLight = new SpotlightView.Builder(mContext)
                .introAnimationDuration(400)
                .enableRevealAnimation(true)
                .performClick(true)
                .fadeinTextDuration(400)
                //.setTypeface(FontUtil.get(this, "RemachineScript_Personal_Use"))
                .headingTvColor(Color.parseColor("#eb273f"))
                .headingTvSize(32)
                .headingTvText("Love")
                .subHeadingTvColor(Color.parseColor("#ffffff"))
                .subHeadingTvSize(16)
                .subHeadingTvText(msg)
                .maskColor(Color.parseColor("#dc000000"))
                .target(view)
                .lineAnimDuration(400)
                .lineAndArcColor(Color.parseColor("#eb273f"))
                .dismissOnTouch(true)
                .dismissOnBackPress(true)
                .enableDismissAfterShown(true)
                .usageId(usageId) //UNIQUE ID
                .show();
    }

    public  void showToast(int msg){

        Toast.makeText(mContext , msg , Toast.LENGTH_LONG).show();

    }

    public void showProgressBarLoading(){

        if (dialog != null){

            if (!dialog.isShowing()) {
                dialog.show();
                dialog.setCancelable(false);
            }
        }
    }

    public void dismissProgressBar(){
        if(dialog!=null)
        {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }

    public void setLocale(String lang) {

        Locale myLocale = new Locale(lang);
        Resources res = mContext.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
    }

    public void products_ccare(final String type , final String product_list , final String customer_note){

        showProgressBarLoading();

        SharedPreferences prefs = mContext.getSharedPreferences("Login", Context.MODE_PRIVATE);
        final String token = prefs.getString("token" , "");

        String REGISTER_URL = mContext.getResources().getString(R.string.base_url) + "v1/index.php/appshopboth/submit_ccare";

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

                        success = jsonObject.optString("success");

                        if (success.equals("1")){

                            Toast.makeText(mContext , jsonObject.optString("msg") , Toast.LENGTH_LONG).show();
                        }

                        dismissProgressBar();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dismissProgressBar();

                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();

                params.put("token" , token);
                params.put("type" , type);
                params.put("product_list" , product_list);
                params.put("customer_note" , customer_note);

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        requestQueue.add(stringRequest);

    }

}
