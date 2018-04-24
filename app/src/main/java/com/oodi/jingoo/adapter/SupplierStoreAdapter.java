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

public class SupplierStoreAdapter extends RecyclerView.Adapter<SupplierStoreAdapter.MyViewHolder>{

    Context mContext ;
    List<Store> storeLists ;
    String store_id ;

    public SupplierStoreAdapter(FragmentActivity mContext, List<Store> mRewardUserList, String store_id) {
        this.mContext = mContext ;
        this.storeLists = mRewardUserList ;
        this.store_id = store_id ;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtName;
        LinearLayout lnr ;

        public MyViewHolder(View itemView) {
            super(itemView);

            txtName = (TextView) itemView.findViewById(R.id.txtName);
            lnr = (LinearLayout) itemView.findViewById(R.id.lnr);

        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_store_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        final Store rewardUser = storeLists.get(position);

        holder.txtName.setText(rewardUser.getName());

        holder.txtName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("msgto");
                intent.putExtra("id",rewardUser.getStore());
                intent.putExtra("item_name" , rewardUser.getName());
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);

            }
        });

        if (store_id.equals(rewardUser.getStore())){
            holder.lnr.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return storeLists.size();

    }



}
