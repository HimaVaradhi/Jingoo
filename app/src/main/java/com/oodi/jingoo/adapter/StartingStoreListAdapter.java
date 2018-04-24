package com.oodi.jingoo.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.oodi.jingoo.R;
import com.oodi.jingoo.activity.ChooseSupliersActivity;
import com.oodi.jingoo.activity.StoreActivity;
import com.oodi.jingoo.activity.TakeStockActivity;
import com.oodi.jingoo.pojo.Store;

import java.util.List;

/**
 * Created by pc on 2/23/17.
 */

public class StartingStoreListAdapter extends RecyclerView.Adapter<StartingStoreListAdapter.MyViewHolder>{

    StoreActivity mContext ;
    List<Store> mStockList ;
    public static String id  , name;

    public StartingStoreListAdapter(StoreActivity mContext, List<Store> mStockList) {
        this.mStockList = mStockList ;
        this.mContext = mContext ;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        LinearLayout lnrStock ;
        ImageView imgBg ;
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
                .inflate(R.layout.view_store, parent, false);

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
            public void onClick(View view) {

                if (mContext.type.equals("setting")){
                    Intent intent = new Intent(mContext , ChooseSupliersActivity.class);
                    intent.putExtra("store_id" , stock.getStore());
                    mContext.startActivity(intent);
                }else {
                    id = stock.getStore();

                    Intent intent = new Intent(mContext , TakeStockActivity.class);
                    mContext.startActivity(intent);
                }



            }
        });

    }

    @Override
    public int getItemCount() {
        return mStockList.size();
    }

}
