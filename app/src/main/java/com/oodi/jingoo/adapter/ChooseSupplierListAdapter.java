package com.oodi.jingoo.adapter;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.oodi.jingoo.R;
import com.oodi.jingoo.pojo.Supplier;

import java.util.List;

/**
 * Created by pc on 2/23/17.
 */

public class ChooseSupplierListAdapter extends RecyclerView.Adapter<ChooseSupplierListAdapter.MyViewHolder>{

    Activity mContext ;
    List<Supplier> mRewardUserList ;

    public ChooseSupplierListAdapter(Activity mContext, List<Supplier> mRewardUserList) {
        this.mContext = mContext ;
        this.mRewardUserList = mRewardUserList ;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtName ;
        CheckBox cbSelected ;

        public MyViewHolder(View itemView) {
            super(itemView);

            txtName = (TextView) itemView.findViewById(R.id.txtName);
            cbSelected = (CheckBox) itemView.findViewById(R.id.cbSelected);

        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_supplier_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        final Supplier supplier = mRewardUserList.get(position);

        if (supplier.isSelected()){
            holder.cbSelected.setChecked(true);
        }
        else
        {
            holder.cbSelected.setChecked(false);
        }

        holder.cbSelected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isPressed()){
                    if (isChecked){
                        supplier.setSelected(true);
                        holder.cbSelected.setChecked(true);
                    }else if (!isChecked && supplier.getCompany_id().equals("1")){
                        supplier.setSelected(true);
                        holder.cbSelected.setChecked(true);

                        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                        builder.setMessage(mContext.getResources().getString(R.string.no_select)+" "+ ("\uD83D\uDE42"))
                                .setCancelable(false)
                                .setPositiveButton(mContext.getResources().getString(R.string.okay), new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        //do things
                                        dialog.dismiss();
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();

                    }else if (!isChecked && !supplier.getCompany_id().equals("1")){
                        supplier.setSelected(false);
                        holder.cbSelected.setChecked(false);
                    }

                    notifyDataSetChanged();
                }
            }
        });

        holder.txtName.setText(supplier.getBrand_name());

    }

    @Override
    public int getItemCount() {
        return mRewardUserList.size();
    }

}
