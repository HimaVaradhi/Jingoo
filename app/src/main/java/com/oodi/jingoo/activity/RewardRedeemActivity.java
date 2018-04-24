package com.oodi.jingoo.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.oodi.jingoo.R;
import com.oodi.jingoo.utility.AppUtils;

public class RewardRedeemActivity extends AppCompatActivity {

    ImageView mImgBack ;
    TextView mTxtHeaderName , mTxtHome , Home , txtPoints , txtName;
    AppUtils appUtils ;
    String  token ;
    String points = "";
    String item_name = "";
    Button btnDone ;

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
        setContentView(R.layout.activity_reward_redeem);

        init();

        Intent intent = getIntent();
        points = intent.getStringExtra("points");
        item_name = intent.getStringExtra("item_name");

        txtName.setText(item_name);
        txtPoints.setText(points + " coins ");

        SharedPreferences prefs = this.getSharedPreferences("Login", Context.MODE_PRIVATE);
        token = prefs.getString("token" , "");

        mTxtHome.setText(getResources().getString(R.string.rewards));

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

        mTxtHeaderName.setText(getResources().getString(R.string.rewards));

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent();
        setResult(2,intent);
        finish();
    }

    public void init(){

        mImgBack = (ImageView) findViewById(R.id.imgBack);
        mTxtHeaderName = (TextView) findViewById(R.id.txtHeaderName);
        mTxtHome = (TextView) findViewById(R.id.txtHome);
        txtPoints = (TextView) findViewById(R.id.txtPoints);
        txtName = (TextView) findViewById(R.id.txtName);
        Home = (TextView) findViewById(R.id.Home);
        btnDone = (Button) findViewById(R.id.btnDone);

    }
}
