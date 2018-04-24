package com.oodi.jingoo.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.oodi.jingoo.R;
import com.oodi.jingoo.pojo.TakeStock;

import java.util.List;

/**
 * Created by pc on 2/23/17.
 */

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.MyViewHolder>{

    Context mContext ;
    List<TakeStock> storeLists ;

    public ProductListAdapter(FragmentActivity mContext, List<TakeStock> mRewardUserList) {
        this.mContext = mContext ;
        this.storeLists = mRewardUserList ;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtName;

        public MyViewHolder(View itemView) {
            super(itemView);

            txtName = (TextView) itemView.findViewById(R.id.txtName);

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

        final TakeStock rewardUser = storeLists.get(position);

        holder.txtName.setText(rewardUser.getProducts_name());


        holder.txtName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("product");
                intent.putExtra("item_category",rewardUser.getId());
                intent.putExtra("item_name" , rewardUser.getProducts_name());
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return storeLists.size();

    }



}
