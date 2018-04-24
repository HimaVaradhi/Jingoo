package com.oodi.jingoo.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.oodi.jingoo.R;
import com.oodi.jingoo.utility.AppUtils;

public class CCProductQualityIssueSubmitActivity extends AppCompatActivity {

    TextView mTxtHeaderName , mTxtHome  , Home , txtName;
    ImageView mImgBack ;
    AppUtils appUtils ;
    Button mBtnSubmit ;
    EditText mEdtIssue ;
    String cat_name = "" ;

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
        setContentView(R.layout.activity_ccproduct_quality_issue_submit);

        init();

        ImageView imgHelp = (ImageView) findViewById(R.id.imgHelp);
        imgHelp.setVisibility(View.GONE);
        mTxtHome.setVisibility(View.VISIBLE);
        Home.setVisibility(View.VISIBLE);

        Intent intent = getIntent();
        cat_name = intent.getStringExtra("cat_name");

        txtName.setText(cat_name);

        mTxtHeaderName.setText(getResources().getString(R.string.customer_care));
        mTxtHome.setText(getResources().getString(R.string.customer_care));

        Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomerCareActivity.CC.finish();
                CCProductQualityIssueActivity.aProductQuality.finish();
                onBackPressed();
            }
        });

        mImgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mBtnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appUtils.products_ccare("Product Quality Issue" , cat_name , mEdtIssue.getText().toString());

                CustomerCareActivity.CC.finish();
                CCProductQualityIssueActivity.aProductQuality.finish();
                onBackPressed();

            }
        });

    }

    public void init(){

        mTxtHeaderName = (TextView) findViewById(R.id.txtHeaderName);
        mImgBack = (ImageView) findViewById(R.id.imgBack);
        mTxtHome = (TextView) findViewById(R.id.txtHome);
        txtName = (TextView) findViewById(R.id.txtName);
        Home = (TextView) findViewById(R.id.Home);
        mBtnSubmit = (Button) findViewById(R.id.btnSubmit);
        mEdtIssue = (EditText) findViewById(R.id.edtIssue);

    }
}
