package com.oodi.jingoo.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.oodi.jingoo.R;
import com.oodi.jingoo.utility.AppUtils;

public class ShareRefferalCodeActivity extends AppCompatActivity {

    TextView mTxtHeaderName , mTxtHome , Home , mTxtRefer , mTxtCode, mTxtHistory;
    ImageView mImgBack , mImgShare , imgHelp;
    public static Activity shareRefferalCode ;
    AppUtils appUtils ;
    Button mBtnWhatsapp ;
    LinearLayout mTxtHowWorks, mLnrHistory;

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
        setContentView(R.layout.activity_share_refferal_code);
        shareRefferalCode = this ;

        init();

        ImageView imageView3 = (ImageView) findViewById(R.id.imageView3);

        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                try {
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        SharedPreferences pr = this.getSharedPreferences("Login", Context.MODE_PRIVATE);
        final String phone = pr.getString("phone" , "");
        final String name = pr.getString("name" , "");

        mTxtCode.setText(phone);

        mTxtHeaderName.setText(getResources().getString(R.string.referral_friend));
        mTxtHome.setText(getResources().getString(R.string.referral_friend));
        mTxtRefer.setText(getResources().getString(R.string.referral_friend)+" "+
                getResources().getString(R.string.and)+"\n"+
                getResources().getString(R.string.win_reward));

        mImgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //SettingsNewActivity.settingNew.finish();
                //OtherSettingsActivity.otherSetting.finish();
                finish();
            }
        });

        mTxtHowWorks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShareRefferalCodeActivity.this , ShareReferralInstructionActivity.class);
                startActivity(intent);
            }
        });

        mImgShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               /* Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Hi, " +name+ " has invited you to join JINGOO! Sign up with referral code "+ phone + " to get exciting prizes and revolutionize your stock taking! Download here - " + "https://play.google.com/store/apps/details?id=com.oodi.jingoo&hl=en");
                sendIntent.setType("text/plain");
                startActivity(sendIntent);*/

            }
        });

        mBtnWhatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ShareRefferalCodeActivity.this , ReferalCodeActivity.class);
                startActivity(intent);

                /*Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Hi, " +name+ " has invited you to join JINGOO! Sign up with referral code "+ phone + " to get exciting prizes and revolutionize your stock taking! Download here - " + "https://play.google.com/store/apps/details?id=com.oodi.jingoo&hl=en");
                sendIntent.setType("text/plain");
                startActivity(sendIntent);*/
                /*PackageManager pm=getPackageManager();
                try {

                    Intent waIntent = new Intent(Intent.ACTION_SEND);
                    waIntent.setType("text/plain");
                    String text = "Hi, " +name+ " has invited you to join JINGOO! Sign up with referral code "+ phone + " to get exciting prizes and revolutionize your stock taking! Download here - " + "https://play.google.com/store/apps/details?id=com.oodi.jingoo&hl=en";

                    PackageInfo info=pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
                    //Check if package exists or not. If not then code
                    //in catch block will be called
                    waIntent.setPackage("com.whatsapp");

                    waIntent.putExtra(Intent.EXTRA_TEXT, text);
                    startActivity(Intent.createChooser(waIntent, "Share with"));

                } catch (Exception e) {
                    Toast.makeText(ShareRefferalCodeActivity.this, "WhatsApp not Installed", Toast.LENGTH_SHORT)
                            .show();
                }*/
            }
        });

        mLnrHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShareRefferalCodeActivity.this , RefferalHistoryActivity.class);
                startActivity(intent);
            }
        });

    }

    public void init(){

        mTxtHeaderName = (TextView) findViewById(R.id.txtHeaderName);
        mImgBack = (ImageView) findViewById(R.id.imgBack);
        mTxtHome = (TextView) findViewById(R.id.txtHome);
        Home = (TextView) findViewById(R.id.Home);
        mTxtRefer = (TextView) findViewById(R.id.txtRefer);
        mTxtHowWorks = (LinearLayout) findViewById(R.id.txtHowWorks);
        mTxtCode = (TextView) findViewById(R.id.txtCode);
        mBtnWhatsapp = (Button) findViewById(R.id.btnWhatsapp);
        mImgShare = (ImageView) findViewById(R.id.imgShare);
        imgHelp = (ImageView) findViewById(R.id.imgHelp);
        mLnrHistory = (LinearLayout) findViewById(R.id.lnrHistory);
        mTxtHistory = (TextView) findViewById(R.id.txtHistory);

        imgHelp.setVisibility(View.GONE);
        Home.setVisibility(View.VISIBLE);
        mTxtHome.setVisibility(View.VISIBLE);

    }

}
