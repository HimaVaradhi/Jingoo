package com.oodi.jingoo.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.oodi.jingoo.R;
import com.oodi.jingoo.pojo.RewardHistory;

import java.util.List;

/**
 * Created by pc on 2/23/17.
 */

public class RefferalHistoryListAdapter extends RecyclerView.Adapter<RefferalHistoryListAdapter.MyViewHolder>{

    Context mContext ;
    List<RewardHistory> mRewardUserList ;

    public RefferalHistoryListAdapter(Activity mContext, List<RewardHistory> mRewardUserList) {
        this.mContext = mContext ;
        this.mRewardUserList = mRewardUserList ;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtDate, txtName, txtStatus, txtNumber;

        public MyViewHolder(View itemView) {
            super(itemView);

            txtDate = (TextView) itemView.findViewById(R.id.txtDate);
            txtName = (TextView) itemView.findViewById(R.id.txtName);
            txtStatus = (TextView) itemView.findViewById(R.id.txtStatus);
            txtNumber = (TextView) itemView.findViewById(R.id.txtNumber);

        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_refferal_history, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        RewardHistory r = mRewardUserList.get(position);

        holder.txtDate.setText(r.getCreated_at());
        holder.txtName.setText(r.getName());

        if (r.getRefferel_status().equals("Pending")){
            holder.txtStatus.setTextColor(mContext.getResources().getColor(R.color.grey_text));
        }else if (r.getRefferel_status().equals("Expired") ||
                    r.getRefferel_status().equals("Referred by someone else")){
            holder.txtStatus.setTextColor(mContext.getResources().getColor(R.color.ml250));
        }

        holder.txtStatus.setText(r.getRefferel_status());
        holder.txtNumber.setText(position+1+"");

    }

    @Override
    public int getItemCount() {
        return mRewardUserList.size();
    }

}
