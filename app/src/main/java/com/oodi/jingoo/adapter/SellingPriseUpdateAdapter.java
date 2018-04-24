package com.oodi.jingoo.adapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
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
import com.oodi.jingoo.pojo.SellingProductName;

import java.util.List;

/**
 * Created by pc on 2/23/17.
 */

public class SellingPriseUpdateAdapter extends RecyclerView.Adapter<SellingPriseUpdateAdapter.MyViewHolder>{

    Context mContext ;
    List<SellingProductName> mRewardUserList ;

    public SellingPriseUpdateAdapter(FragmentActivity mContext, List<SellingProductName> mRewardUserList) {
        this.mContext = mContext ;
        this.mRewardUserList = mRewardUserList ;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView txtName ;
        EditText edt750 , edt375 ,  edt180 ;
        TextView txt750 , txt375 , txt180 ;

        public MyViewHolder(View view) {
            super(view);

            txtName = (TextView) view.findViewById(R.id.txtName);
            edt750 = (EditText) view.findViewById(R.id.edt750);
            edt375 = (EditText) view.findViewById(R.id.edt375);
            edt180 = (EditText) view.findViewById(R.id.edt180);

            txt180 = (TextView) view.findViewById(R.id.txt180);
            txt375 = (TextView) view.findViewById(R.id.txt375);
            txt750 = (TextView) view.findViewById(R.id.txt750);

        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_selling_prise_update, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.setIsRecyclable(false);

        final SellingProductName sellingPrise = mRewardUserList.get(position);

        holder.txtName.setText(sellingPrise.getName());

        try {

            if (sellingPrise.getSellingPriseList().get(0).getProducts_size().equals("750 ML")){
                holder.edt750.setText(sellingPrise.getSellingPriseList().get(0).getProduct_price());
            }else if (sellingPrise.getSellingPriseList().get(0).getProducts_size().equals("375 ML")){
                holder.edt375.setText(sellingPrise.getSellingPriseList().get(0).getProduct_price());
            }else {
                holder.edt180.setText(sellingPrise.getSellingPriseList().get(0).getProduct_price());
            }

        }catch (Exception e){

            Log.e("e" , e+"");

        }

        try {
            if (sellingPrise.getSellingPriseList().get(1).getProducts_size().equals("375 ML")){
                holder.edt375.setText(sellingPrise.getSellingPriseList().get(1).getProduct_price());
            }else if (sellingPrise.getSellingPriseList().get(1).getProducts_size().equals("750 ML")){
                holder.edt750.setText(sellingPrise.getSellingPriseList().get(1).getProduct_price());
            }else {
                holder.edt180.setText(sellingPrise.getSellingPriseList().get(1).getProduct_price());
            }
        }catch (Exception e){
            Log.e("e" , e+"");
        }

        try {
            if (sellingPrise.getSellingPriseList().get(2).getProducts_size().equals("180 ML")) {
            holder.edt180.setText(sellingPrise.getSellingPriseList().get(2).getProduct_price());
        }else if (sellingPrise.getSellingPriseList().get(2).getProducts_size().equals("750 ML")){
                holder.edt750.setText(sellingPrise.getSellingPriseList().get(2).getProduct_price());
            }else {
                holder.edt375.setText(sellingPrise.getSellingPriseList().get(2).getProduct_price());
            }
        }catch (Exception e){
            Log.e("e" , e+"");
        }

        try {
            if (!mRewardUserList.get(position).getSellingPriseList().get(0).getProduct_price()
                    .equals(mRewardUserList.get(position).getSellingPriseList().get(0).getNew_price())){

                holder.edt750.setText(mRewardUserList.get(position).getSellingPriseList().get(0).getNew_price());

            }
        }catch (Exception e){

        }

        try {
            if (!mRewardUserList.get(position).getSellingPriseList().get(1).getProduct_price()
                    .equals(mRewardUserList.get(position).getSellingPriseList().get(1).getNew_price())){

                holder.edt375.setText(mRewardUserList.get(position).getSellingPriseList().get(1).getNew_price());

            }
        }catch (Exception e){

        }

        try {
            if (!mRewardUserList.get(position).getSellingPriseList().get(2).getProduct_price()
                    .equals(mRewardUserList.get(position).getSellingPriseList().get(2).getNew_price())){

                holder.edt180.setText(mRewardUserList.get(position).getSellingPriseList().get(2).getNew_price());

            }
        }catch (Exception e){

        }

        holder.edt750.addTextChangedListener(new TextWatcher() {
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
                    sellingPrise.getSellingPriseList().get(0).setNew_price(String.valueOf(s));
                }catch (Exception e){}
               // notifyDataSetChanged();

                // TODO Auto-generated method stub
            }
        });

        holder.edt375.addTextChangedListener(new TextWatcher() {
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
                    sellingPrise.getSellingPriseList().get(1).setNew_price(String.valueOf(s));

                }catch (Exception e){

                }

                // TODO Auto-generated method stub
            }
        });

        holder.edt180.addTextChangedListener(new TextWatcher() {
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
                    sellingPrise.getSellingPriseList().get(2).setNew_price(String.valueOf(s));

                }catch (Exception e){

                }

                // TODO Auto-generated method stub
            }
        });

        if (holder.edt180.getText().toString().equals("t")){
            holder.edt180.setVisibility(View.INVISIBLE);
            holder.txt180.setVisibility(View.INVISIBLE);
        }

        if (holder.edt750.getText().toString().equals("t")){
            holder.edt750.setVisibility(View.INVISIBLE);
            holder.txt750.setVisibility(View.INVISIBLE);
        }

        if (holder.edt375.getText().toString().equals("t")){
            holder.edt375.setVisibility(View.INVISIBLE);
            holder.txt375.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return mRewardUserList.size();
    }
}
