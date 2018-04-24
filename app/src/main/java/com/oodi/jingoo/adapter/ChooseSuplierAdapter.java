package com.oodi.jingoo.adapter;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.oodi.jingoo.R;
import com.oodi.jingoo.pojo.Supplier;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by pc on 2/23/17.
 */

public class ChooseSuplierAdapter extends RecyclerView.Adapter<ChooseSuplierAdapter.MyViewHolder>{

    Activity mContext ;
    List<Supplier> mSupplierList ;
    int last_position = 0;

    public ChooseSuplierAdapter(Activity mContext, List<Supplier> mSupplierList) {
        this.mContext = mContext ;
        this.mSupplierList = mSupplierList ;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imgAvatar , imgAvatarSelected;
        TextView txtName ;

        public MyViewHolder(View itemView) {
            super(itemView);

            imgAvatar = (ImageView) itemView.findViewById(R.id.imgAvatar);
            imgAvatarSelected = (ImageView) itemView.findViewById(R.id.imgAvatarSelected);
            txtName = (TextView) itemView.findViewById(R.id.txtName);

        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_supplier, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final Supplier supplier = mSupplierList.get(position);

        holder.txtName.setText(supplier.getBrand_name());

        Picasso.with(mContext)
                .load(supplier.getBrand_image())
                .fit().centerInside()
                .placeholder(R.drawable.cat_holder)
                .into(holder.imgAvatar);

        if (supplier.isSelected()){
            holder.imgAvatarSelected.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.imgAvatarSelected.setVisibility(View.INVISIBLE);

        }

        holder.imgAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!supplier.isSelected() && supplier.getCompany_id().equals("1")){
                    supplier.setSelected(true);
                    holder.imgAvatarSelected.setVisibility(View.VISIBLE);
                }else if(!supplier.isSelected()){
                    supplier.setSelected(true);
                    holder.imgAvatarSelected.setVisibility(View.VISIBLE);
                }else if (supplier.isSelected() && !supplier.getCompany_id().equals("1")){
                    supplier.setSelected(false);
                    holder.imgAvatarSelected.setVisibility(View.INVISIBLE);
                }else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setMessage(mContext.getResources().getString(R.string.no_select) +" "+ ("\uD83D\uDE42"))
                            .setCancelable(false)
                            .setPositiveButton(mContext.getResources().getString(R.string.okay), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //do things
                                    dialog.dismiss();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }

                notifyDataSetChanged();

            }
        });

    }

    @Override
    public int getItemCount() {
        return mSupplierList.size();
    }
}
