package com.oodi.jingoo.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.oodi.jingoo.R;
import com.oodi.jingoo.activity.StoreAnalyticsActivity;
import com.oodi.jingoo.pojo.Store;

import java.util.List;

/**
 * Created by pc on 2/23/17.
 */

public class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.MyViewHolder>{

    Context mContext ;
    List<Store> mStockList ;

    public StoreAdapter(Activity mContext, List<Store> mStockList) {
        this.mStockList = mStockList ;
        this.mContext = mContext ;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        LinearLayout lnrStock ;
        TextView txtCategoryName ;

        public MyViewHolder(View itemView) {
            super(itemView);

            lnrStock = (LinearLayout) itemView.findViewById(R.id.lnrStock);
            txtCategoryName = (TextView) itemView.findViewById(R.id.txtCategoryName);

        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout._view_manage_store, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        final Store stock = mStockList.get(position);

        holder.txtCategoryName.setText(stock.getName());

        holder.lnrStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext , StoreAnalyticsActivity.class);
                intent.putExtra("store_id",stock.getStore());
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mStockList.size();
    }

}
