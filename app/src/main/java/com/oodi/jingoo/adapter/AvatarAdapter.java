package com.oodi.jingoo.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.oodi.jingoo.R;
import com.oodi.jingoo.pojo.Avatar;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by pc on 2/23/17.
 */

public class AvatarAdapter extends RecyclerView.Adapter<AvatarAdapter.MyViewHolder>{

    Activity mContext ;
    List<Avatar> avatarList ;
    int last_position = 0;


    public AvatarAdapter(Activity mContext, List<Avatar> avatarList) {
        this.mContext = mContext ;
        this.avatarList = avatarList ;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imgAvatar , imgAvatarSelected;

        public MyViewHolder(View itemView) {
            super(itemView);

            imgAvatar = (ImageView) itemView.findViewById(R.id.imgAvatar);
            imgAvatarSelected = (ImageView) itemView.findViewById(R.id.imgAvatarSelected);

        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_avatar, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        final Avatar avatar = avatarList.get(position);

        Picasso.with(mContext)
                .load(avatar.getAvatar())
                .fit()
                .into(holder.imgAvatar);

        if (avatar.isSelected()){
            holder.imgAvatarSelected.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.imgAvatarSelected.setVisibility(View.INVISIBLE);

        }

        holder.imgAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                holder.imgAvatarSelected.setVisibility(View.VISIBLE);

                if(holder.getAdapterPosition() != last_position)
                {
                    avatarList.get(last_position).setSelected(false);
                    holder.imgAvatarSelected.setVisibility(View.INVISIBLE);
                }

                if (!avatar.isSelected()){
                    avatar.setSelected(true);
                    holder.imgAvatarSelected.setVisibility(View.VISIBLE);
                    Intent intent = new Intent("custom-message");
                    intent.putExtra("avatar",avatar.getAvatar());
                    intent.putExtra("id" , avatar.getId());
                    LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                }else {
                    avatar.setSelected(false);
                    holder.imgAvatarSelected.setVisibility(View.INVISIBLE);
                }

                //avatar.setSelected(true);

                last_position = holder.getAdapterPosition();

                notifyDataSetChanged();

            }
        });

    }

    @Override
    public int getItemCount() {
        return avatarList.size();

    }



}
