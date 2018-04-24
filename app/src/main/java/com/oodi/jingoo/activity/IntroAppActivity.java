package com.oodi.jingoo.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.vivchar.viewpagerindicator.ViewPagerIndicator;
import com.oodi.jingoo.R;
import com.oodi.jingoo.adapter.BannerAdapter;
import com.oodi.jingoo.pojo.Banners;
import com.oodi.jingoo.utility.AppUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class IntroAppActivity extends AppCompatActivity {

    ViewPager mStartingPager;
    List<Banners> bannersList = new ArrayList<>();
    BannerAdapter bannerAdapter ;
    TextView mTxtSkip ;
    AppUtils appUtils ;
    Button mBtnDone ;
    File f ;
    String lang ;
    String type ;
    ViewPagerIndicator mIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_app);
        mStartingPager = (ViewPager) findViewById(R.id.startingPager);
        mTxtSkip = (TextView) findViewById(R.id.txtSkip);
        mBtnDone = (Button) findViewById(R.id.btnDone);
        mIndicator = (ViewPagerIndicator)findViewById(R.id.view_pager_indicator);
        appUtils = new AppUtils(this);
        SharedPreferences prefs1 = getSharedPreferences("language", MODE_PRIVATE);
        lang = prefs1.getString("lang", "en");

        SharedPreferences login = getSharedPreferences("Login", MODE_PRIVATE);
        type = login.getString("type", "");

        if (lang.equals("hi") && type.equals("csm")){
            Banners b = new Banners();
            b.setW1(R.drawable.hcsm_wt_01);
            bannersList.add(b);

            b = new Banners();
            b.setW1(R.drawable.hcsm_wt_02);
            bannersList.add(b);

            b = new Banners();
            b.setW1(R.drawable.hcsm_wt_03);
            bannersList.add(b);
        }else if (lang.equals("en") && type.equals("csm")){
            Banners b = new Banners();
            b.setW1(R.drawable.ecsm_wt_01);
            bannersList.add(b);

            b = new Banners();
            b.setW1(R.drawable.ecsm_wt_02);
            bannersList.add(b);

            b = new Banners();
            b.setW1(R.drawable.ecsm_wt_03);
            bannersList.add(b);

            b = new Banners();
            b.setW1(R.drawable.en_csm_main_menu);
            bannersList.add(b);

            b = new Banners();
            b.setW1(R.drawable.en_csm_select_brand);
            bannersList.add(b);

            b = new Banners();
            b.setW1(R.drawable.en_csm_take_stock);
            bannersList.add(b);
        }else if (lang.equals("hi") && !type.equals("csm")){
            Banners b = new Banners();
            b.setW1(R.drawable.hso_wt_02);
            bannersList.add(b);

            b = new Banners();
            b.setW1(R.drawable.hso_wt_01);
            bannersList.add(b);

            b = new Banners();
            b.setW1(R.drawable.hso_wt_03);
            bannersList.add(b);
        }else if (lang.equals("en") && !type.equals("csm")){
            Banners b = new Banners();
            b.setW1(R.drawable.eso_wt_02);
            bannersList.add(b);

            b = new Banners();
            b.setW1(R.drawable.eso_wt_01);
            bannersList.add(b);

            b = new Banners();
            b.setW1(R.drawable.eso_wt_03);
            bannersList.add(b);
        }

        mTxtSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!type.equals("csm")){
                    Intent intent = new Intent(IntroAppActivity.this , ChooseSupliersActivity.class);
                    intent.putExtra("type" , "new");
                    startActivity(intent);
                    finish();
                }else {
                    Intent intent = new Intent(IntroAppActivity.this , MainActivity.class);
                    intent.putExtra("opening_stock" , "1");
                    startActivity(intent);
                    finish();
                }
            }
        });

        mBtnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!type.equals("csm")){
                    Intent intent = new Intent(IntroAppActivity.this , ChooseSupliersActivity.class);
                    intent.putExtra("type" , "new");
                    startActivity(intent);
                    finish();
                }else {
                    Intent intent = new Intent(IntroAppActivity.this , MainActivity.class);
                    intent.putExtra("opening_stock" , "1");
                    startActivity(intent);
                    finish();
                }

            }
        });

        bannerAdapter = new BannerAdapter(this , bannersList);
        mStartingPager.setAdapter(bannerAdapter);
        mIndicator.setupWithViewPager(mStartingPager);


        //  mStartingPager.setOffscreenPageLimit(2);

        mStartingPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {


            }

            @Override
            public void onPageSelected(int position)
            {

                if(position == 2 && !type.equals("csm")){
                    mBtnDone.setVisibility(View.VISIBLE);
                }else if(position == 5 && type.equals("csm")){
                    mBtnDone.setVisibility(View.VISIBLE);
                }else {
                    mBtnDone.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {


            }
        });

    }

}
