package com.oodi.jingoo.adapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.oodi.jingoo.R;
import com.oodi.jingoo.pojo.CSM;

import java.util.List;

/**
 * Created by pc on 2/23/17.
 */

public class CSMListAdapter extends RecyclerView.Adapter<CSMListAdapter.MyViewHolder>{

    Context mContext ;
    List<CSM> mRewardUserList ;

    public CSMListAdapter(FragmentActivity mContext, List<CSM> mRewardUserList) {
        this.mContext = mContext ;
        this.mRewardUserList = mRewardUserList ;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtName , txtPoints ;

        public MyViewHolder(View itemView) {
            super(itemView);

            txtName = (TextView) itemView.findViewById(R.id.txtName);
            txtPoints = (TextView) itemView.findViewById(R.id.txtPoints);

        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_csmlist, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        CSM csm = mRewardUserList.get(position);

        holder.txtName.setText(csm.getName());
        holder.txtPoints.setText(csm.getPoints());

    }

    @Override
    public int getItemCount() {
        return mRewardUserList.size();
    }

}
