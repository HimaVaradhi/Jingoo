package com.oodi.jingoo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.oodi.jingoo.R;
import com.oodi.jingoo.pojo.NotificationFooter;
import com.oodi.jingoo.pojo.NotificationHeader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pc on 9/25/17.
 */

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context _context;
    List<NotificationHeader> _listDataHeader; // header titles
    // child data in format of header title, child title
    List<NotificationFooter> _listDataChild;

    public ExpandableListAdapter(Context _context, List<NotificationHeader> listDataHeader,
                                 List<NotificationFooter> listChildData) {
        this._context = _context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return 1;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.diet_footer_item, null);
        }

        TextView txtListChild = (TextView) convertView.findViewById(R.id.txtTitle);
        TextView txtDescription = (TextView) convertView.findViewById(R.id.txtDescription);

        txtListChild.setText(_listDataHeader.get(groupPosition).getNotificationFooterList().get(childPosition).getNotification_title());
        txtDescription.setText(_listDataHeader.get(groupPosition).getNotificationFooterList().get(childPosition).getNotification_desc());
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        ArrayList<NotificationFooter> chList = _listDataHeader.get(groupPosition)
                .getNotificationFooterList();

        return chList.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.diet_section_ex1_header, null);
        }
        ImageView imgIndicator = (ImageView) convertView.findViewById(R.id.imgIndicator);
        ImageView imgCircle = (ImageView) convertView.findViewById(R.id.imgCircle);

        TextView lblListHeader = (TextView) convertView.findViewById(R.id.txtDate);
        lblListHeader.setText(_listDataHeader.get(groupPosition).getDate());

        if (isExpanded) {
            imgIndicator.setImageResource(R.drawable.nup);
            imgCircle.setImageResource(R.drawable.non);
        } else {
            imgIndicator.setImageResource(R.drawable.ndown);
            imgCircle.setImageResource(R.drawable.noff);

        }

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}