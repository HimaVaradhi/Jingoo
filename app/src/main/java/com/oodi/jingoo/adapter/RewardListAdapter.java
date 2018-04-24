package com.oodi.jingoo.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.oodi.jingoo.R;
import com.oodi.jingoo.activity.RewardRedeemActivity;
import com.oodi.jingoo.fragment.YourPointsFragment;
import com.oodi.jingoo.pojo.Reward;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by pc on 2/23/17.
 */

public class RewardListAdapter extends RecyclerView.Adapter<RewardListAdapter.MyViewHolder>{

    YourPointsFragment mContext ;
    List<Reward> mRewardUserList ;
    int progressStatus = 0;
    String reward_id = "";

    public RewardListAdapter(YourPointsFragment mContext, List<Reward> mRewardUserList) {
        this.mContext = mContext ;
        this.mRewardUserList = mRewardUserList ;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ProgressBar pb ;
        LinearLayout lnr1 , lnr2 ;
        Button btnRedeem ;
        ImageView img;
        TextView txtStock , txtName , txtPoints , txtMyPoint , txtCurrentPoints , txtTargetPoints , txtPercentage;

        public MyViewHolder(View itemView) {
            super(itemView);

            pb = (ProgressBar) itemView.findViewById(R.id.pb);
            lnr1 = (LinearLayout) itemView.findViewById(R.id.lnr1);
            lnr2 = (LinearLayout) itemView.findViewById(R.id.lnr2);
            btnRedeem = (Button) itemView.findViewById(R.id.btnRedeem);
            img = (ImageView) itemView.findViewById(R.id.img);
            txtStock = (TextView) itemView.findViewById(R.id.txtStock);
            txtName = (TextView) itemView.findViewById(R.id.txtName);
            txtPoints = (TextView) itemView.findViewById(R.id.txtPoints);
            txtMyPoint = (TextView) itemView.findViewById(R.id.txtMyPoint);
            txtCurrentPoints = (TextView) itemView.findViewById(R.id.txtCurrentPoints);
            txtTargetPoints = (TextView) itemView.findViewById(R.id.txtTargetPoints);
            txtPercentage = (TextView) itemView.findViewById(R.id.txtPercentage);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_reward, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        final Reward reward = mRewardUserList.get(position);

        if (Integer.parseInt(reward.getItem_points()) < Integer.parseInt(mContext.points)){
            holder.lnr2.setVisibility(View.GONE);
            holder.lnr1.setVisibility(View.VISIBLE);
            holder.btnRedeem.setVisibility(View.VISIBLE);
        }else {
            holder.btnRedeem.setVisibility(View.GONE);
            holder.lnr2.setVisibility(View.VISIBLE);
            holder.lnr1.setVisibility(View.GONE);
        }

        holder.txtName.setText(reward.getItem_name());
        holder.txtPoints.setText("Price : "+ reward.getItem_points() + " coins");
        if (reward.getItem_stock().equals("0")){
            holder.txtStock.setText("All redeemed");

        }else {
            holder.txtStock.setText(reward.getItem_stock() +" "+ mContext.getResources().getString(R.string.PCS_LEFT));

        }
        holder.txtMyPoint.setText(mContext.getResources().getString(R.string.Your_Points) +" "+ mContext.points+" coins");
        holder.txtCurrentPoints.setText(mContext.getResources().getString(R.string.Current_Points) +" "+ mContext.points+" coins");

        String targetPoints = String.valueOf(Integer.parseInt(reward.getItem_points()) - Integer.parseInt(mContext.points));

        holder.txtTargetPoints.setText(mContext.getResources().getString(R.string.Points_to_reach) +" "+ targetPoints+" coins");

        Picasso.with(mContext.getActivity())
                .load(reward.getItem_image())
                .placeholder(R.mipmap.ic_logo)
                .into(holder.img);

        final int progress = (Integer.parseInt(targetPoints) * 100) / Integer.parseInt(reward.getItem_points());

        holder.txtPercentage.setText(progress + mContext.getResources().getString(R.string.reach_your_Goal));

        holder.btnRedeem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(mContext.getActivity());
                builder.setMessage("Are you sure you want to redeem?")
                        .setCancelable(false)
                        .setNegativeButton("Cancel" , new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //do things
                                dialog.dismiss();
                                reward_id = reward.getId();
                                redeem_rewards_submit();

                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();


            }
        });

        final Handler handler = new Handler();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(progressStatus < (100-progress)){
                    // Update the progress status
                    progressStatus +=1;

                    // Try to sleep the thread for 20 milliseconds
                    try{
                        Thread.sleep(20);
                    }catch(InterruptedException e){
                        e.printStackTrace();
                    }

                    // Update the progress bar
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            holder.pb.setProgress(progressStatus);
                            // Show the progress on TextView
                        }
                    });
                }
            }
        }).start();


    }

    @Override
    public int getItemCount() {
        return mRewardUserList.size();
    }

    private void redeem_rewards_submit(){

        final ProgressDialog pd = new ProgressDialog(mContext.getActivity());
        pd.setMessage("loading");
        pd.setCancelable(false);
        pd.show();

        SharedPreferences pr = mContext.getActivity().getSharedPreferences("Login", Context.MODE_PRIVATE);
        final String token = pr.getString("token" , "");

        mRewardUserList.clear();

        String REGISTER_URL = "" ;

        if (mContext.type.equals("csm")){
            REGISTER_URL = mContext.getActivity().getResources().getString(R.string.base_url) + "v1/index.php/appshopboth/redeem_rewards_submit";
        }else {
            REGISTER_URL = mContext.getActivity().getResources().getString(R.string.base_url) + "v1/index.php/appshopboth/shopower_redeem_rewards_submit";
        }


        final String KEY_USERNAME = "token";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(LoginActivity.this,response,Toast.LENGTH_LONG).show();

                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }

                        Log.d("Json Array", "doInBackground: " + jsonObject);

                        String status = jsonObject.optString("success");

                        if(status.equals("1"))
                        {

                            String points = jsonObject.optString("points");
                            String item_name = jsonObject.optString("item_name");

                            Intent intent = new Intent(mContext.getActivity() , RewardRedeemActivity.class);
                            intent.putExtra("points", points);
                            intent.putExtra("item_name", item_name);
                            mContext.startActivityForResult(intent, 2);

                        }else {

                            Toast.makeText(mContext.getActivity() , jsonObject.optString("msg") , Toast.LENGTH_LONG).show();

                        }

                        pd.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(LoginActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                        pd.dismiss();

                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put(KEY_USERNAME,token);
                params.put("reward_id" , reward_id);

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(mContext.getActivity());
        requestQueue.add(stringRequest);
    }

}
