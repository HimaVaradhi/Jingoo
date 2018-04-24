package com.oodi.jingoo.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.oodi.jingoo.R;
import com.oodi.jingoo.adapter.BannerAdapter;
import com.oodi.jingoo.pojo.Banners;
import com.oodi.jingoo.utility.AppUtils;

import java.util.ArrayList;
import java.util.List;

public class HelpActivity extends AppCompatActivity {

    ViewPager mStartingPager;
    List<Banners> bannersList = new ArrayList<>();
    BannerAdapter bannerAdapter ;
    AppUtils appUtils ;
    TextView mTxtClose ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        appUtils = new AppUtils(this);

        SharedPreferences prefs1 = getSharedPreferences("language", MODE_PRIVATE);
        String lang = prefs1.getString("lang", "en");

        SharedPreferences login = getSharedPreferences("Login", MODE_PRIVATE);
        String type = login.getString("type", "");

        init();

        if (lang.equals("hi") && type.equals("csm")){
            Banners b = new Banners();
            b.setW1(R.drawable.hi_complete_profile);
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
        }else if (lang.equals("en") && type.equals("csm")){
            Banners b = new Banners();
            b.setW1(R.drawable.en_complete_profile);
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
            b.setW1(R.drawable.hi_complete_profile);
            bannersList.add(b);

            b = new Banners();
            b.setW1(R.drawable.hi_chosse_brands);
            bannersList.add(b);

            b = new Banners();
            b.setW1(R.drawable.hi_selling_price_new);
            bannersList.add(b);

            b = new Banners();
            b.setW1(R.drawable.hi_create_team);
            bannersList.add(b);

            b = new Banners();
            b.setW1(R.drawable.hi_main_menu);
            bannersList.add(b);

            b = new Banners();
            b.setW1(R.drawable.purchase_anroid);
            bannersList.add(b);

            b = new Banners();
            b.setW1(R.drawable.closing_stock_anroid);
            bannersList.add(b);

            b = new Banners();
            b.setW1(R.drawable.transfer_stock_anroid);
            bannersList.add(b);
        }else if (lang.equals("en") && !type.equals("csm")){
            Banners b = new Banners();
            b.setW1(R.drawable.en_complete_profile);
            bannersList.add(b);

            b = new Banners();
            b.setW1(R.drawable.en_choose_your_brands);
            bannersList.add(b);

            b = new Banners();
            b.setW1(R.drawable.en_selling_price_new);
            bannersList.add(b);

            b = new Banners();
            b.setW1(R.drawable.en_create_team);
            bannersList.add(b);

            b = new Banners();
            b.setW1(R.drawable.en_main_menu);
            bannersList.add(b);

            b = new Banners();
            b.setW1(R.drawable.purchasewt1);
            bannersList.add(b);

            b = new Banners();
            b.setW1(R.drawable.cswt1);
            bannersList.add(b);

            b = new Banners();
            b.setW1(R.drawable.transfer_wt);
            bannersList.add(b);

        }

        bannerAdapter = new BannerAdapter(this , bannersList);
        mStartingPager.setAdapter(bannerAdapter);

        mTxtClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    public void init(){

        mStartingPager = (ViewPager) findViewById(R.id.startingPager);
        mTxtClose = (TextView) findViewById(R.id.txtClose);

    }

}
