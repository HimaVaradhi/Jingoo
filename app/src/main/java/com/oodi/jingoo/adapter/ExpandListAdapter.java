package com.oodi.jingoo.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.oodi.jingoo.R;
import com.oodi.jingoo.activity.StoreAnalyticsActivity;
import com.oodi.jingoo.pojo.Child;
import com.oodi.jingoo.pojo.Group;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Lenovo on 24-05-2017.
 */

public class ExpandListAdapter extends BaseExpandableListAdapter {

    private StoreAnalyticsActivity context;
    private ArrayList<Group> groups;

    public ExpandListAdapter(StoreAnalyticsActivity context, ArrayList<Group> groups) {
        this.context = context;
        this.groups = groups;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        ArrayList<Child> chList = groups.get(groupPosition).getItems();
        return chList.get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final Child child = (Child) getChild(groupPosition,
                childPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context
                    .getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_item, null);
        }
        TextView txtName = (TextView) convertView.findViewById(R.id.txtName);
        PieChart pieChart = (PieChart) convertView.findViewById(R.id.piechart);

        txtName.setText(child.getProducts_name());

        pieChart.setUsePercentValues(false);

        float e1 , e2 , e3 ;

        float total = Float.parseFloat(child.getValue1()) + Float.parseFloat(child.getValue2()) + Float.parseFloat(child.getValue3()) ;

        try {
             e3 = (Float.parseFloat(child.getValue1()) / total )* 100 ;

        }catch (Exception e){
            e3 = 0 ;
        }

        try {
             e2 = (Float.parseFloat(child.getValue2()) / total) * 100 ;

        }catch (Exception e){
            e2 = 0 ;

        }

        try {
             e1 = (Float.parseFloat(child.getValue3()) / total) * 100 ;

        }catch (Exception e){
            e1 = 0;

        }


        if (String.valueOf(e1).equals("NaN")&& String.valueOf(e2).equals("NaN") && String.valueOf(e3).equals("NaN") ){
            e1 = 1 ;
            e2 = 1 ;
            e3 = 1 ;
        }

        ArrayList<Entry> yvalues = new ArrayList<Entry>();
        yvalues.add(new Entry(e3, 0));
        yvalues.add(new Entry(e2, 1));
        yvalues.add(new Entry(e1, 2));

        PieDataSet dataSet = new PieDataSet(yvalues, "");

        ArrayList<String> xVals = new ArrayList<String>();

        xVals.add(child.getValue1());
        xVals.add(child.getValue2());
        xVals.add(child.getValue3());

        PieData data = new PieData(xVals, dataSet);
        data.setDrawValues(false);
        pieChart.setDescription("");
        //data.setValueFormatter(new PercentFormatter());
        pieChart.setData(data);
        pieChart.getLegend().setEnabled(false);
/*
        pieChart.setCenterText("TOTAL \n100");
        pieChart.setCenterTextSize(18f);
        pieChart.setCenterTextColor(R.color.black);*/

        pieChart.setDrawHoleEnabled(true);
        pieChart.setTransparentCircleRadius(60f);
        pieChart.setHoleRadius(60f);

        dataSet.setColors(new int[] { R.color.ml250, R.color.ml500, R.color.ml750}, context);
        data.setValueTextSize(13f);
        data.setValueTextColor(Color.WHITE);

        pieChart.animateXY(1400, 1400);

        return convertView;

    }

    @Override
    public int getChildrenCount(int groupPosition) {
        ArrayList<Child> chList = groups.get(groupPosition)
                .getItems();

        return chList.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groups.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return groups.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        Group group = (Group) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater inf = (LayoutInflater) context
                    .getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = inf.inflate(R.layout.list_group, null);
        }

        TextView tv = (TextView) convertView.findViewById(R.id.group_name);
        ImageView imgCat = (ImageView) convertView.findViewById(R.id.imgCat);
        ImageView imgIndicator = (ImageView) convertView.findViewById(R.id.imgIndicator);

        tv.setText(group.getCategory());

        Picasso.with(context)
                .load(group.getCategory_img())
                .placeholder(R.drawable.cat_holder)
                .into(imgCat);

        if (isExpanded) {
            imgIndicator.setImageResource(R.drawable.ic_up);
        } else {
            imgIndicator.setImageResource(R.drawable.ic_down);
        }

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}