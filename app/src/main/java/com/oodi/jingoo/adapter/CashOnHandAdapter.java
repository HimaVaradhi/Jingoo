package com.oodi.jingoo.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.oodi.jingoo.R;
import com.oodi.jingoo.activity.CashOnHandActivity;
import com.oodi.jingoo.pojo.CashOnHand;

import java.util.List;

/**
 * Created by pc on 2/23/17.
 */

public class CashOnHandAdapter extends RecyclerView.Adapter<CashOnHandAdapter.MyViewHolder>{

    CashOnHandActivity mContext ;
    List<CashOnHand> storeLists ;

    public CashOnHandAdapter(CashOnHandActivity mContext, List<CashOnHand> mRewardUserList) {
        this.mContext = mContext ;
        this.storeLists = mRewardUserList ;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView txtName ;
        TextView edt750 , edt375 ,  edt180 ;
        TextView txt750 , txt375 , txt180 ;

        public MyViewHolder(View view) {
            super(view);

            txtName = (TextView) view.findViewById(R.id.txtName);
            edt750 = (TextView) view.findViewById(R.id.edt750);
            edt375 = (TextView) view.findViewById(R.id.edt375);
            edt180 = (TextView) view.findViewById(R.id.edt180);

            txt180 = (TextView) view.findViewById(R.id.txt180);
            txt375 = (TextView) view.findViewById(R.id.txt375);
            txt750 = (TextView) view.findViewById(R.id.txt750);

        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_cash_on_hand, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        final CashOnHand rewardUser = storeLists.get(position);

        holder.txtName.setText(rewardUser.getProducts_name());

        if (mContext.type.equals("report") || mContext.type.equals("dailyTop")
                || mContext.type.equals("lowStock")){
            try {
                if (!rewardUser.getML180().equals("")){
                    holder.edt180.setText(rewardUser.getML180());}
                else{
                    holder.txt180.setVisibility(View.INVISIBLE);
                    holder.edt180.setText("");}

            }catch (Exception e){
                holder.txt180.setVisibility(View.INVISIBLE);
                holder.edt180.setText("");

            }
            try {
                if (!rewardUser.getML375().equals("")){
                    holder.edt375.setText(rewardUser.getML375());}
                else{
                    holder.txt375.setVisibility(View.INVISIBLE);
                    holder.edt375.setText("");}

            }catch (Exception e){
                holder.txt375.setVisibility(View.INVISIBLE);
                holder.edt375.setText("");

            }
            try {
                if (!rewardUser.getML750().equals("")){
                    holder.edt750.setText(rewardUser.getML750());}
                else{
                    holder.edt750.setText("");
                    holder.txt750.setVisibility(View.INVISIBLE);}
            }catch (Exception e){
                holder.txt750.setVisibility(View.INVISIBLE);
                holder.edt750.setText("");

            }
        }else {
            holder.edt180.setText(mContext.getResources().getString(R.string.Rs)+rewardUser.getML180());
            holder.edt375.setText(mContext.getResources().getString(R.string.Rs)+rewardUser.getML375());
            holder.edt750.setText(mContext.getResources().getString(R.string.Rs)+rewardUser.getML750());
        }


    }

    @Override
    public int getItemCount() {
        return storeLists.size();

    }


}
