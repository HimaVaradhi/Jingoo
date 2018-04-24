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
import com.oodi.jingoo.activity.CCProductQualityIssueSubmitActivity;
import com.oodi.jingoo.activity.TakeStockListActivity;
import com.oodi.jingoo.pojo.Stock;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.List;

/**
 * Created by pc on 2/23/17.
 */

public class StockListAdapter extends RecyclerView.Adapter<StockListAdapter.MyViewHolder>{

    Context mContext ;
    List<Stock> mStockList ;
    public static String id ;
    int i ;

    public StockListAdapter(Activity mContext, List<Stock> mStockList , int i) {
        this.mStockList = mStockList ;
        this.mContext = mContext ;
        this.i = i ;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        LinearLayout lnrStock ;
        ImageView imgBg ;
        TextView txtCategoryName ;

        public MyViewHolder(View itemView) {
            super(itemView);

            lnrStock = (LinearLayout) itemView.findViewById(R.id.lnrStock);
            imgBg = (ImageView) itemView.findViewById(R.id.imgBg);
            txtCategoryName = (TextView) itemView.findViewById(R.id.txtCategoryName);

        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_stock, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        final Stock stock = mStockList.get(position);

        holder.txtCategoryName.setText(stock.getCategory_name());

        Picasso.with(mContext)
                .load(stock.getCategory_image())
                .fit()
                .centerInside()
                .placeholder(R.drawable.cat_holder)
                .into(holder.imgBg);

            holder.lnrStock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (i == 2){
                        id = stock.getId();
                        Intent intent = new Intent(mContext , TakeStockListActivity.class);
                        intent.putExtra("cat_list" , (Serializable) mStockList);
                        intent.putExtra("position" , position);
                        intent.putExtra("cat_name" , stock.getCategory_name());
                        mContext.startActivity(intent);
                    }else if(i == 3){
                        Intent intent = new Intent(mContext , CCProductQualityIssueSubmitActivity.class);
                        intent.putExtra("cat_name" , stock.getCategory_name());
                        mContext.startActivity(intent);
                    }else{
                        Intent intent = new Intent(mContext , TakeStockListActivity.class);
                        intent.putExtra("id" , stock.getId());
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
