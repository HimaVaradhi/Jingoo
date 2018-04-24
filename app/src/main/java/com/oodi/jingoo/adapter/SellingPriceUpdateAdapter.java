package com.oodi.jingoo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.oodi.jingoo.R;
import com.oodi.jingoo.pojo.Child;

import java.util.List;

/**
 * Created by pc on 2/23/17.
 */

public class SellingPriceUpdateAdapter extends RecyclerView.Adapter<SellingPriceUpdateAdapter.MyViewHolder>{

    Context mContext ;
    List<Child> mStockList ;

    public SellingPriceUpdateAdapter(Context mContext, List<Child> mStockList) {
        this.mStockList = mStockList ;
        this.mContext = mContext ;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtName ;
        EditText  txtValue ;

        public MyViewHolder(View itemView) {
            super(itemView);

            txtName = (TextView) itemView.findViewById(R.id.txtName);
            txtValue = (EditText) itemView.findViewById(R.id.txtValue);

        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_selling_update_new, parent, false);

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

        try {

            if (stock.getValue2().equals(stock.getValue3())){
                holder.txtValue.setText(stock.getValue2());
            }else {
                holder.txtValue.setText(stock.getValue3());
            }

        }catch (Exception e){

            Log.e("e" , e+"");

        }

        holder.txtValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    stock.setValue3(String.valueOf(s));
                    stock.setNew_price(String.valueOf(s));
                }catch (Exception e){}
                // notifyDataSetChanged();

                // TODO Auto-generated method stub
            }
        });

    }

    @Override
    public int getItemCount() {
        return mStockList.size();
    }

}
