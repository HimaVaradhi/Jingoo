package com.oodi.jingoo.expandrecycleview;

import android.graphics.Color;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.oodi.jingoo.AnimationUtil;
import com.oodi.jingoo.R;
import com.oodi.jingoo.activity.StoreAnalyticsActivity;
import com.oodi.jingoo.adapter.AnalyticsFooterAdapter;
import com.oodi.jingoo.pojo.AnalyticsFooter;
import com.oodi.jingoo.pojo.AnalyticsHeader;
import com.oodi.jingoo.pojo.Group;

import java.util.ArrayList;
import java.util.List;

public class ExpandableTestAdapter extends ExpandableRecyclerView.Adapter<ExpandableTestAdapter.ChildViewHolder, ExpandableRecyclerView.SimpleGroupViewHolder, String, String> {

    List<Group> headerList;
    StoreAnalyticsActivity context;
    private int previousPosition = 0;
    public ExpandableTestAdapter(List<Group> workoutExercise, StoreAnalyticsActivity _context) {
        this.headerList = workoutExercise;
        this.context = _context;

    }


    @Override
    public int getGroupItemCount() {
        return headerList.size() - 1;
    }

    @Override
    public int getChildItemCount(int group) {
        return headerList.get(group).getAnalyticsFooters().size();
    }

    @Override
    public String getGroupItem(int position) {
        return headerList.get(position).getCategory();
    }

    @Override
    public String getChildItem(int group, int position) {
        return headerList.get(group).getAnalyticsFooters().get(position).getProducts_name();
    }


    @Override
    protected ExpandableRecyclerView.SimpleGroupViewHolder onCreateGroupViewHolder(ViewGroup parent) {
        return new ExpandableRecyclerView.SimpleGroupViewHolder(parent.getContext());
    }

    @Override
    protected ChildViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout._view_anal_footerlist, parent, false);
        return new ChildViewHolder(view);
    }

    @Override
    public int getChildItemViewType(int group, int position) {
        return 1;
    }

    @Override
    public void onBindGroupViewHolder(ExpandableRecyclerView.SimpleGroupViewHolder holder, int group) {
        super.onBindGroupViewHolder(holder, group);
        holder.tvTitle.setText(headerList.get(group).getCategory());

    }

    @Override
    public void onBindChildViewHolder(ChildViewHolder holder, int group, final int position) {
        super.onBindChildViewHolder(holder, group, position);
        holder.txt4.setText(headerList.get(group).getAnalyticsFooters().get(position).getValue3());

        holder.txtName.setText(headerList.get(group).getAnalyticsFooters().get(position).getProducts_name());

        AnalyticsFooterAdapter startingBrowseCategoryAdapter = new AnalyticsFooterAdapter(context, headerList.get(group).getAnalyticsFooters().get(position).getChild_list());

        RecyclerView.LayoutManager mLayoutManager1 = new GridLayoutManager(context, 3);
        holder.recAnalyticsFooter.setLayoutManager(mLayoutManager1);
        holder.recAnalyticsFooter.setAdapter(startingBrowseCategoryAdapter);
    }

    public class ChildViewHolder extends RecyclerView.ViewHolder {
        RecyclerView recAnalyticsFooter ;
        public TextView txtName , txt1 , txt2 , txt3 , txt4;
        LinearLayout l1 ;

        public ChildViewHolder(View view) {
            super(view);
            recAnalyticsFooter = (RecyclerView) view.findViewById(R.id.recFooterAnalytics);
            txtName = (TextView) view.findViewById(R.id.txtName);
            txt1 = (TextView) view.findViewById(R.id.txt1);
            txt2 = (TextView) view.findViewById(R.id.txt2);
            txt3 = (TextView) view.findViewById(R.id.txt3);
            txt4 = (TextView) view.findViewById(R.id.txt4);
            l1 = (LinearLayout) view.findViewById(R.id.l1);

        }


    }

}
