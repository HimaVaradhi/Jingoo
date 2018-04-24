package com.oodi.jingoo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.oodi.jingoo.R;
import com.oodi.jingoo.pojo.Child;

import java.util.List;

/**
 * Created by pc on 2/23/17.
 */

public class AnalyticsFooterAdapter extends RecyclerView.Adapter<AnalyticsFooterAdapter.MyViewHolder>{

    Context mContext ;
    List<Child> mStockList ;

    public AnalyticsFooterAdapter(Context mContext, List<Child> mStockList) {
        this.mStockList = mStockList ;
        this.mContext = mContext ;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtName , txtValue ;

        public MyViewHolder(View itemView) {
            super(itemView);

            txtName = (TextView) itemView.findViewById(R.id.txtName);
            txtValue = (TextView) itemView.findViewById(R.id.txtValue);

        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_analytics_footer, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        final Child stock = mStockList.get(position);

        holder.txtName.setText(stock.getValue1());
        holder.txtValue.setText(stock.getValue2());

    }

    @Override
    public int getItemCount() {
        return mStockList.size();
    }

}
