package com.oodi.jingoo.adapter;

import android.content.Context;
import android.database.DataSetObserver;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.oodi.jingoo.R;
import com.oodi.jingoo.pojo.Banners;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by pc on 12/26/16.
 */

public class BannerAdapter1 extends PagerAdapter {

    private Context mContext;
    List<Banners> bannersList;

    public BannerAdapter1(Context mContext, List<Banners> bannersList) {
        this.mContext = mContext;
        this.bannersList = bannersList;
    }

    @Override
    public int getCount() {
        return bannersList.size();

    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
        if (observer != null) {
            super.unregisterDataSetObserver(observer);
        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.pager_item, container, false);

        ImageView imageView = (ImageView) itemView.findViewById(R.id.pagerImage);

        //imageView.setImageResource(mResources[position]);

        /*if (!bannersList.get(position).getLink().equals("")){
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url = bannersList.get(position).getLink();
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    mContext.startActivity(i);
                }
            });
        }

        */

        Picasso.with(mContext)
                .load(bannersList.get(position).getBanner_image())
                .fit().centerCrop()
                .into(imageView);

        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((FrameLayout) object);
    }
}