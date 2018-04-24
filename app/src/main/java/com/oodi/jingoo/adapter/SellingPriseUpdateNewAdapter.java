package com.oodi.jingoo.adapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.oodi.jingoo.R;
import com.oodi.jingoo.pojo.SellingProductName;

import java.util.List;

/**
 * Created by pc on 2/23/17.
 */

public class SellingPriseUpdateNewAdapter extends RecyclerView.Adapter<SellingPriseUpdateNewAdapter.MyViewHolder>{

    Context mContext ;
    List<SellingProductName> mRewardUserList ;

   public SellingPriseUpdateNewAdapter(FragmentActivity mContext, List<SellingProductName> mRewardUserList) {
        this.mContext = mContext ;
        this.mRewardUserList = mRewardUserList ;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView txtName ;
        RecyclerView recAnalyticsFooter ;

        public MyViewHolder(View view) {
            super(view);

            txtName = (TextView) view.findViewById(R.id.txtName);
            recAnalyticsFooter = (RecyclerView) view.findViewById(R.id.recAnalyticsFooter);

        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_selling_prise_update, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.setIsRecyclable(false);

        final SellingProductName sellingPrise = mRewardUserList.get(position);

        holder.txtName.setText(sellingPrise.getName());

        SellingPriceUpdateAdapter startingBrowseCategoryAdapter = new SellingPriceUpdateAdapter(mContext, sellingPrise.getChildList());

        RecyclerView.LayoutManager mLayoutManager1 = new GridLayoutManager(mContext, 3);
        holder.recAnalyticsFooter.setLayoutManager(mLayoutManager1);
        holder.recAnalyticsFooter.setAdapter(startingBrowseCategoryAdapter);


    }

    @Override
    public int getItemCount() {
        return mRewardUserList.size();
    }
}
