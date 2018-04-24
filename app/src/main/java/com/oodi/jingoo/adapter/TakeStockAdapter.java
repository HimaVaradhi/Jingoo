package com.oodi.jingoo.adapter;

import android.content.Context;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.oodi.jingoo.R;
import com.oodi.jingoo.pojo.TakeStock;
import com.oodi.jingoo.utility.RepeatListener;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
/**
 * Created by pc on 2/23/17.
 */
public class TakeStockAdapter extends RecyclerView.Adapter<TakeStockAdapter.MyViewHolder> {

    Context mContext;
    List<TakeStock> mTakeStockList;
    int i;
    public TakeStockAdapter(FragmentActivity mContext, List<TakeStock> mTakeStockList, int i) {
        this.mTakeStockList = mTakeStockList;
        this.mContext = mContext;
        this.i = i;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        LinearLayout lnrStock;
        ImageView imgMinus, imgPlus, imgProductImage;
        TextView txtProductName, txtProductSize, txtCurrentStock;
        EditText edtQuantity;
        Handler mHandler;
        MotionEvent event;
        boolean isMoving = false;

        public MyViewHolder(View itemView) {
            super(itemView);

            lnrStock = (LinearLayout) itemView.findViewById(R.id.lnrStock);
            edtQuantity = (EditText) itemView.findViewById(R.id.edtQuantity);
            imgMinus = (ImageView) itemView.findViewById(R.id.imgMinus);
            imgPlus = (ImageView) itemView.findViewById(R.id.imgPlus);
            imgProductImage = (ImageView) itemView.findViewById(R.id.imgProductImage);
            txtProductName = (TextView) itemView.findViewById(R.id.txtProductName);
            txtProductSize = (TextView) itemView.findViewById(R.id.txtProductSize);
            txtCurrentStock = (TextView) itemView.findViewById(R.id.txtCurrentStock);

        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_take_stock, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        final TakeStock takeStock = mTakeStockList.get(position);

        final int minValue = Integer.parseInt(takeStock.getStock_qty());

        if (!takeStock.getLast_updated().equals("") && i == 2) {
            String time = takeStock.getLast_updated();
            String setTime = null;

            try {
                final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd H:mm:ss");
                final Date dateObj = sdf.parse(time);
                setTime = new SimpleDateFormat("dd/MM, K:mm a").format(dateObj);
            } catch (final ParseException e) {
                e.printStackTrace();
            }
            holder.txtCurrentStock.setText("Last refill updated at " + setTime);
        } else if (!takeStock.getLast_updated().equals("") && i == 1) {
            String time = takeStock.getLast_updated();
            String setTime = null;

            try {
                final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd H:mm:ss");
                final Date dateObj = sdf.parse(time);
                setTime = new SimpleDateFormat("dd/MM, K:mm a").format(dateObj);
            } catch (final ParseException e) {
                e.printStackTrace();
            }
            holder.txtCurrentStock.setText("Last stock updated at " + setTime);
            holder.txtCurrentStock.setVisibility(View.GONE);
        } else {
            holder.txtCurrentStock.setVisibility(View.GONE);
        }

        holder.imgPlus.setOnTouchListener(new RepeatListener(400, 100, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.imgPlus.performClick();
                Log.e("TAP", "YESSssssssss");
                // the code to execute repeatedly
            }
        }));

        holder.imgMinus.setOnTouchListener(new RepeatListener(400, 100, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.imgMinus.performClick();
                Log.e("TAP", "YESSssssssss");
            }
        }));

        holder.imgPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (i == 1) {

                    int i = Integer.parseInt(holder.edtQuantity.getText().toString());
                    i++;

                    if (i > minValue) {
                        i = minValue;
                    }
                    holder.edtQuantity.setText(i + "");
                    takeStock.setSort_order(holder.edtQuantity.getText().toString());
                    //notifyDataSetChanged();

                } else {

                    int i = Integer.parseInt(holder.edtQuantity.getText().toString());
                    i++;

                    holder.edtQuantity.setText(i + "");
                    takeStock.setIs_active(holder.edtQuantity.getText().toString());
                    //notifyDataSetChanged();

                }
            }
        });

        holder.imgMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int i = Integer.parseInt(holder.edtQuantity.getText().toString());
                i--;

                if (i < 0) {
                    i = 0;
                }

                holder.edtQuantity.setText(i + "");
                takeStock.setSort_order(holder.edtQuantity.getText().toString());
                //notifyDataSetChanged();

            }
        });


        holder.edtQuantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                if (i == 1) {
                    takeStock.setSort_order(holder.edtQuantity.getText().toString());
                } else {
                    takeStock.setIs_active(holder.edtQuantity.getText().toString());
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (i == 1) {
                    takeStock.setSort_order(holder.edtQuantity.getText().toString());
                } else {
                    takeStock.setIs_active(holder.edtQuantity.getText().toString());
                }
            }
        });

        Picasso.with(mContext)
                .load(takeStock.getProducts_image())
                .fit().centerInside()
                .placeholder(R.drawable.product_holder)
                .into(holder.imgProductImage);

        if (i == 1) {
            holder.edtQuantity.setText(takeStock.getSort_order());
        }

        holder.txtProductName.setText(takeStock.getProducts_name());
        holder.txtProductSize.setText(takeStock.getProducts_size());

    }

    @Override
    public int getItemCount() {
        return mTakeStockList.size();
    }

}
