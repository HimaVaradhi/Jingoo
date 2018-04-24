package com.oodi.jingoo.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.oodi.jingoo.R;
import com.oodi.jingoo.activity.AddTeamActivity;
import com.oodi.jingoo.pojo.Team;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by pc on 2/23/17.
 */

public class ManageTeamAdapter extends RecyclerView.Adapter<ManageTeamAdapter.MyViewHolder>{

    Context mContext ;
    List<Team> mStockList ;
    String csm_id = "";
    int position_ ;

    public ManageTeamAdapter(Activity mContext, List<Team> mStockList) {
        this.mStockList = mStockList ;
        this.mContext = mContext ;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        LinearLayout lnrStock ;
        TextView txtCategoryName , txtStoreName , txtBadge;
        ImageView imgEdit , imgDp , imgDelete;

        public MyViewHolder(View itemView) {
            super(itemView);

            lnrStock = (LinearLayout) itemView.findViewById(R.id.lnrStock);
            txtCategoryName = (TextView) itemView.findViewById(R.id.txtCategoryName);
            txtStoreName = (TextView) itemView.findViewById(R.id.txtStoreName);
            imgEdit = (ImageView) itemView.findViewById(R.id.imgEdit);
            imgDp = (ImageView) itemView.findViewById(R.id.imgDp);
            imgDelete = (ImageView) itemView.findViewById(R.id.imgDelete);
            txtBadge = (TextView) itemView.findViewById(R.id.txtBadge);

        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_manage_team, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        final Team stock = mStockList.get(position);

        if (stock.getRole_selling_price().equals("1")  ||
                stock.getRole_transfer_stock().equals("1") ||
                stock.getRole_reports().equals("1")){
            holder.txtBadge.setVisibility(View.VISIBLE);
        }else {
            holder.txtBadge.setVisibility(View.INVISIBLE);
        }

        holder.imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext , AddTeamActivity.class);
                intent.putExtra("id" , "edit");
                intent.putExtra("name" , stock.getUsername());
                intent.putExtra("email",stock.getEmail());
                intent.putExtra("store_name",stock.getStore_name());
                intent.putExtra("csm_id", stock.getId());
                intent.putExtra("phone", stock.getPhone());
                intent.putExtra("store_id", stock.getStore_id());
                intent.putExtra("selling", stock.getRole_selling_price());
                intent.putExtra("transfer", stock.getRole_transfer_stock());
                intent.putExtra("role_reports", stock.getRole_reports());

                mContext.startActivity(intent);
            }
        });

        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setMessage(mContext.getResources().getString(R.string.delete))
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
                                csm_id = stock.getId() ;
                                position_ = position ;
                                deletecsm();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();

            }
        });

        Picasso.with(mContext)
                .load(stock.getAvatar())
                .fit()
                .placeholder(R.drawable.j1)
                .into(holder.imgDp);

        holder.txtCategoryName.setText(stock.getUsername());
        holder.txtStoreName.setText(stock.getStore_name());

    }

    @Override
    public int getItemCount() {
        return mStockList.size();
    }

    public void deletecsm(){

        final ProgressDialog pd = new ProgressDialog(mContext);
        pd.setMessage("loading");
        pd.show();

        SharedPreferences prefs = mContext.getSharedPreferences("Login", Context.MODE_PRIVATE);
        final String token = prefs.getString("token" , "");

        String REGISTER_URL = mContext.getResources().getString(R.string.base_url) + "v1/index.php/appshopboth/deletecsm";

        final String KEY_USERNAME = "token";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }

                        Log.d("Json Array", "doInBackground: " + jsonObject);

                        String status = "";
                        try {
                            status = jsonObject.getString("success");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        String msg = "";
                        try {
                            msg = jsonObject.getString("msg");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if (!msg.equals("")){
                            Toast.makeText(mContext,msg,Toast.LENGTH_LONG).show();
                        }
                        if(status.equals("1"))
                        {
                            pd.dismiss();
                            mStockList.remove(position_);

                            notifyDataSetChanged();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(AddTeamActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                        pd.dismiss();

                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put(KEY_USERNAME,token);
                params.put("csm_id" , csm_id );

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        requestQueue.add(stringRequest);
    }


}
