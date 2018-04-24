package com.oodi.jingoo.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.oodi.jingoo.R;
import com.oodi.jingoo.pojo.RewardHistory;

import java.util.List;

/**
 * Created by pc on 2/23/17.
 */

public class HistoryListAdapter extends RecyclerView.Adapter<HistoryListAdapter.MyViewHolder>{

    Context mContext ;
    List<RewardHistory> mRewardUserList ;

    public HistoryListAdapter(Activity mContext, List<RewardHistory> mRewardUserList) {
        this.mContext = mContext ;
        this.mRewardUserList = mRewardUserList ;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

       LinearLayout lnrRoot ;
       TextView txtDate , txtName , txtPoints ;

        public MyViewHolder(View itemView) {
            super(itemView);

            lnrRoot = (LinearLayout) itemView.findViewById(R.id.lnrRoot);
            txtPoints = (TextView) itemView.findViewById(R.id.txtPoints);
            txtName = (TextView) itemView.findViewById(R.id.txtName);
            txtDate = (TextView) itemView.findViewById(R.id.txtDate);

        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_history, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        RewardHistory history = mRewardUserList.get(position);

        if (position%2 == 0) {
            holder.itemView.setBackgroundColor(mContext.getResources().getColor(R.color.l1));
        }
        else {
            holder.itemView.setBackgroundColor(mContext.getResources().getColor(R.color.l2));
        }

        holder.txtName.setText(history.getNote());
        holder.txtDate.setText(history.getCreated_at());

        if (history.getType().equals("cr")){
            String type = "Cr";
            holder.txtPoints.setTextColor(Color.rgb(0,128,0));
            holder.txtPoints.setText("+"+history.getPoints() + " "+  type);
        }else {
            String type = "Dr";
            holder.txtPoints.setTextColor(Color.RED);
            holder.txtPoints.setText("-"+history.getPoints() + " "+  type);
        }


    }

    @Override
    public int getItemCount() {
        return mRewardUserList.size();
    }

}
