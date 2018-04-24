package com.oodi.jingoo.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.oodi.jingoo.R;
import com.oodi.jingoo.utility.AppUtils;

public class ForgotPasswordActivity extends AppCompatActivity {

    Button mBtnProceed ;
    TextView mTxtHeaderName ;
    AppUtils appUtils ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        appUtils = new AppUtils(this);

        init();

        mTxtHeaderName.setText("Forgot Password");

        mBtnProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!appUtils.isOnLine()){
                    Intent intent = new Intent(getApplicationContext() , NoInternetActivity.class);
                    startActivity(intent);
                    //appUtils.showToast(R.string.offline);
                }else {
                    Intent intent = new Intent(ForgotPasswordActivity.this, IntroAppActivity.class);
                    startActivity(intent);
                }
            }
        });

    }

    public void init(){

        mBtnProceed = (Button) findViewById(R.id.btnProceed);
        mTxtHeaderName = (TextView) findViewById(R.id.txtHeaderName);


    }
}
