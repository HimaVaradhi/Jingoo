package com.oodi.jingoo.activity;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.oodi.jingoo.R;
import com.oodi.jingoo.utility.AppUtils;

public class CCTechAppIssueActivity extends AppCompatActivity {

    TextView mTxtHeaderName , mTxtHome  , Home ;
    ImageView mImgBack ;
    AppUtils appUtils ;
    Button mBtnSubmit ;
    EditText mEdtIssue ;
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
        setContentView(R.layout.activity_cctech_app_issue);

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
                CustomerCareActivity.CC.finish();
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

                String product_list = "" ;

                if (r1.isChecked()){
                    product_list = "The app is hanging and is buggy";
                }else if (r2.isChecked()){
                    product_list = "Unable to update product information";
                }else if (r3.isChecked()){
                    product_list = "Unable to view reports";
                }else {
                    product_list = mEdtIssue.getText().toString();
                }

                if (product_list.equals("")){
                    Toast.makeText(CCTechAppIssueActivity.this , "Please select atleast one point" , Toast.LENGTH_LONG).show();
                }else {
                    appUtils.products_ccare("Technical App Issue" , "" , product_list);

                    CustomerCareActivity.CC.finish();
                    onBackPressed();
                }

            }
        });
    }

    public void init(){

        mTxtHeaderName = (TextView) findViewById(R.id.txtHeaderName);
        mImgBack = (ImageView) findViewById(R.id.imgBack);
        mTxtHome = (TextView) findViewById(R.id.txtHome);
        Home = (TextView) findViewById(R.id.Home);
        mBtnSubmit = (Button) findViewById(R.id.btnSubmit);
        mEdtIssue = (EditText) findViewById(R.id.edtIssue);
        r1 = (RadioButton) findViewById(R.id.r1);
        r2 = (RadioButton) findViewById(R.id.r2);
        r3 = (RadioButton) findViewById(R.id.r3);

    }
}
