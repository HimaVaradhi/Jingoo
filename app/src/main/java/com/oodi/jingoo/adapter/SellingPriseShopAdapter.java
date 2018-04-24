package com.oodi.jingoo.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.oodi.jingoo.R;
import com.oodi.jingoo.pojo.Store;

import java.util.List;

/**
 * Created by pc on 2/23/17.
 */

public class SellingPriseShopAdapter extends RecyclerView.Adapter<SellingPriseShopAdapter.MyViewHolder>{

    Context mContext ;
    List<Store> mRewardUserList ;
    int lastPosition = 0 ;

    public SellingPriseShopAdapter(FragmentActivity mContext, List<Store> mRewardUserList) {
        this.mContext = mContext ;
        this.mRewardUserList = mRewardUserList ;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtShopName , txtLine ;
        LinearLayout lnrRoot ;

        public MyViewHolder(View itemView) {
            super(itemView);

            txtShopName = (TextView) itemView.findViewById(R.id.txtShopName);
            txtLine = (TextView) itemView.findViewById(R.id.txtLine);
            lnrRoot = (LinearLayout) itemView.findViewById(R.id.lnrRoot);

        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_selling_prise_shop, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        final Store rewardUser = mRewardUserList.get(position);

        holder.txtShopName.setText(rewardUser.getName());

        if (position == lastPosition){
            holder.txtLine.setVisibility(View.VISIBLE);
            holder.txtShopName.setTextColor(mContext.getResources().getColor(R.color.white));
        }else {
            holder.txtLine.setVisibility(View.INVISIBLE);
            holder.txtShopName.setTextColor(mContext.getResources().getColor(R.color.black));
        }

        holder.lnrRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                lastPosition = position ;

                Intent intent = new Intent("custom-message");
                intent.putExtra("item_category",rewardUser.getStore());
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);

                notifyDataSetChanged();

            }
        });

    }

    @Override
    public int getItemCount() {
        return mRewardUserList.size();
    }

}
