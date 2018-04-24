package com.oodi.jingoo.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.oodi.jingoo.R;
import com.oodi.jingoo.utility.AppUtils;

public class ShareReferralInstructionActivity extends AppCompatActivity {

    TextView mTxtHeaderName , mTxtHome , Home  , mTxtHowWorks;
    ImageView mImgBack ;
    AppUtils appUtils ;

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
        setContentView(R.layout.activity_share_referral_instruction);

        init();

        ImageView imageView3 = (ImageView) findViewById(R.id.imageView3);

        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                try {
                    ShareRefferalCodeActivity.shareRefferalCode.finish();
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        mImgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingsNewActivity.settingNew.finish();
                OtherSettingsActivity.otherSetting.finish();
                ShareRefferalCodeActivity.shareRefferalCode.finish();
                onBackPressed();
            }
        });

        mTxtHeaderName.setText(getResources().getString(R.string.refferal_code));
        mTxtHome.setText(getResources().getString(R.string.refferal_code));

    }

    public void init(){

        mTxtHeaderName = (TextView) findViewById(R.id.txtHeaderName);
        mImgBack = (ImageView) findViewById(R.id.imgBack);
        mTxtHome = (TextView) findViewById(R.id.txtHome);
        Home = (TextView) findViewById(R.id.Home);
        mTxtHowWorks = (TextView) findViewById(R.id.txtHowWorks);

    }

}
