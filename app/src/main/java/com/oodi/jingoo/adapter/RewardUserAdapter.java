package com.oodi.jingoo.adapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.oodi.jingoo.R;
import com.oodi.jingoo.pojo.RewardUser;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by pc on 2/23/17.
 */

public class RewardUserAdapter extends RecyclerView.Adapter<RewardUserAdapter.MyViewHolder>{

    Context mContext ;
    List<RewardUser> mRewardUserList ;

    public RewardUserAdapter(FragmentActivity mContext, List<RewardUser> mRewardUserList) {
        this.mContext = mContext ;
        this.mRewardUserList = mRewardUserList ;
    }

    public void add(List<RewardUser> items) {
        int previousDataSize = this.mRewardUserList.size();
       // this.mRewardUserList.addAll(items);

        for (int i = 0 ; i < mRewardUserList.size() ; i++){

            items.add(mRewardUserList.get(i));

        }

        notifyItemRangeInserted(previousDataSize, items.size());
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        LinearLayout lnrUserReward ;
        TextView txtUserName , txtPoints , txtRank , imgPrize;
        ImageView  imgAvatar ;

        public MyViewHolder(View itemView) {
            super(itemView);

            lnrUserReward = (LinearLayout) itemView.findViewById(R.id.lnrUserReward);
            txtPoints = (TextView) itemView.findViewById(R.id.txtPoints);
            txtUserName = (TextView) itemView.findViewById(R.id.txtUserName);
            txtRank = (TextView) itemView.findViewById(R.id.txtRank);
            imgPrize = (TextView) itemView.findViewById(R.id.imgPrize);
            imgAvatar = (ImageView) itemView.findViewById(R.id.imgAvatar);

        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_reward_user, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        RewardUser rewardUser = mRewardUserList.get(position);

        holder.txtRank.setText(rewardUser.getRank());
        holder.txtUserName.setText(rewardUser.getName());
        holder.txtPoints.setText(rewardUser.getAvatar());

        holder.imgPrize.setText(rewardUser.getPoints() + " coins");

        Picasso.with(mContext)
                .load(rewardUser.getAvatar())
                .fit()
                .placeholder(R.drawable.j1)
                .into(holder.imgAvatar);

        if (position%2 == 0) {
            holder.itemView.setBackgroundColor(mContext.getResources().getColor(R.color.l1));
        }
        else {
            holder.itemView.setBackgroundColor(mContext.getResources().getColor(R.color.l2));

        }

    }

    @Override
    public int getItemCount() {
        return mRewardUserList.size();
    }

}
