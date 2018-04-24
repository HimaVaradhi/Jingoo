package com.oodi.jingoo.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.oodi.jingoo.R;
import com.oodi.jingoo.activity.AddStoreActivity;
import com.oodi.jingoo.pojo.Store;

import java.util.List;

/**
 * Created by pc on 2/23/17.
 */

public class ManageStoreListAdapter extends RecyclerView.Adapter<ManageStoreListAdapter.MyViewHolder>{

    Context mContext ;
    List<Store> mStockList ;

    public ManageStoreListAdapter(Activity mContext, List<Store> mStockList) {
        this.mStockList = mStockList ;
        this.mContext = mContext ;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        LinearLayout lnrStock ;
        TextView txtCategoryName ;
        ImageView imgEdit ;

        public MyViewHolder(View itemView) {
            super(itemView);

            lnrStock = (LinearLayout) itemView.findViewById(R.id.lnrStock);
            txtCategoryName = (TextView) itemView.findViewById(R.id.txtCategoryName);
            imgEdit = (ImageView) itemView.findViewById(R.id.imgEdit);

        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_manage_store, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        final Store stock = mStockList.get(position);

        holder.imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext , AddStoreActivity.class);
                intent.putExtra("type" , "edit");
                intent.putExtra("name" , stock.getName());
                intent.putExtra("store" , stock.getStore());
                intent.putExtra("depot" , stock.getDepot());
                intent.putExtra("dist" , stock.getDistrict());
                intent.putExtra("lat" , stock.getLat());
                intent.putExtra("lng" , stock.getLng());
                intent.putExtra("license" , stock.getLicense_no());
                intent.putExtra("state" , stock.getState());
                mContext.startActivity(intent);
            }
        });


        holder.txtCategoryName.setText(stock.getName());

    }

    @Override
    public int getItemCount() {
        return mStockList.size();
    }

}
