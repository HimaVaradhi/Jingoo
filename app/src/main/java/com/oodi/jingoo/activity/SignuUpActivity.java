package com.oodi.jingoo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.HashMap;
import java.util.Map;

public class SignuUpActivity extends AppCompatActivity {

    EditText mEdtName, mEdtPhoneNumber, mEdtStoreName, mEdtState, mEdtCity;
    Button mBtnSignUp;
    CheckBox mCbTaC ;
    AppUtils appUtils ;
    TextView mTxtHeaderName;
    ImageView mImgRegisterd;
    String phone = "" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signu_up);
        appUtils = new AppUtils(this);

        init();

        Intent intent = getIntent();
        phone = intent.getStringExtra("phone");

        mEdtPhoneNumber.setText(phone);

        mTxtHeaderName.setText("Sign Up");

        mBtnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValide()){
                    new_signup_request();
                }
            }
        });

        mImgRegisterd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    public void init(){

        mEdtName = (EditText) findViewById(R.id.edtName);
        mEdtPhoneNumber = (EditText) findViewById(R.id.edtPhoneNumber);
        mEdtStoreName = (EditText) findViewById(R.id.edtStoreName);
        mEdtState = (EditText) findViewById(R.id.edtState);
        mEdtCity = (EditText) findViewById(R.id.edtCity);
        mBtnSignUp = (Button) findViewById(R.id.btnSignUp);
        mCbTaC = (CheckBox) findViewById(R.id.cbTaC);
        mTxtHeaderName = (TextView) findViewById(R.id.txtHeaderName);
        mImgRegisterd = (ImageView) findViewById(R.id.imgRegisterd);

    }

    private void new_signup_request(){

        appUtils.showProgressBarLoading();

        String REGISTER_URL = SignuUpActivity.this.getResources().getString(R.string.base_url) + "v1/index.php/appshopboth/new_signup_request";

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
                            Toast.makeText(SignuUpActivity.this,msg,Toast.LENGTH_LONG).show();
                        }
                        if(status.equals("1"))
                        {
                            mImgRegisterd.setVisibility(View.VISIBLE);

                        }

                        appUtils.dismissProgressBar();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        appUtils.dismissProgressBar();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("token","");
                params.put("phone",mEdtPhoneNumber.getText().toString());
                params.put("store_owner_name" , mEdtName.getText().toString());
                params.put("store_name" , mEdtStoreName.getText().toString());
                params.put("store_state" , mEdtState.getText().toString());
                params.put("store_city" , mEdtCity.getText().toString());

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public boolean isValide() {

        if (mEdtName.getText().toString().equalsIgnoreCase("")) {
            makeToast("Please enter name");
            return false;
        } else if (mEdtPhoneNumber.getText().toString().equals("") && mEdtPhoneNumber.getText().toString().length() != 10 ) {
            makeToast("Please enter 10 digit phone number");
            return false;
        }else if (mEdtStoreName.getText().toString().equals("")) {
            makeToast("Please enter store name");
            return false;
        }else if (mEdtState.getText().toString().equals("")) {
            makeToast("Please enter state");
            return false;
        }else if (mEdtCity.getText().toString().equals("")) {
            makeToast("Please enter city");
            return false;
        }else if (!mCbTaC.isChecked()) {
            makeToast("Please check terms and conditions");
            return false;
        }

        return true;
    }

    public void makeToast(String name) {

        Toast.makeText(SignuUpActivity.this, name, Toast.LENGTH_SHORT).show();

    }

}
