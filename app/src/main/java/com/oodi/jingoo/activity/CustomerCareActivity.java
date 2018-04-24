package com.oodi.jingoo.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.oodi.jingoo.R;
import com.oodi.jingoo.utility.AppUtils;

public class CustomerCareActivity extends AppCompatActivity {

    TextView mTxtHeaderName , mTxtHome  , Home ;
    ImageView mImgBack ;
    AppUtils appUtils ;
    public static Activity CC ;
    RadioButton r1 , r2 , r3;

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
        setContentView(R.layout.activity_customer_care);
        CC = this ;

        init();

        ImageView imgHelp = (ImageView) findViewById(R.id.imgHelp);
        imgHelp.setVisibility(View.GONE);
        mTxtHome.setVisibility(View.VISIBLE);
        Home.setVisibility(View.VISIBLE);

        mTxtHeaderName.setText(getResources().getString(R.string.customer_care));
        mTxtHome.setText(getResources().getString(R.string.customer_care));

        Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        mImgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        r1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(CustomerCareActivity.this , CCTechAppIssueActivity.class);
                startActivity(intent);

            }
        });

        r2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(CustomerCareActivity.this , CCUnderstandRewardPointActivity.class);
                startActivity(intent);

            }
        });

        r3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(CustomerCareActivity.this , CCProductQualityIssueActivity.class);
                startActivity(intent);

            }
        });
    }

    public void init(){

        mTxtHeaderName = (TextView) findViewById(R.id.txtHeaderName);
        mImgBack = (ImageView) findViewById(R.id.imgBack);
        mTxtHome = (TextView) findViewById(R.id.txtHome);
        Home = (TextView) findViewById(R.id.Home);
        r1 = (RadioButton) findViewById(R.id.r1);
        r2 = (RadioButton) findViewById(R.id.r2);
        r3 = (RadioButton) findViewById(R.id.r3);

    }
}
