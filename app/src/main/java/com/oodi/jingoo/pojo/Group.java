package com.oodi.jingoo.pojo;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by pc on 7/24/17.
 */

public class Group implements Serializable {

    private ArrayList<Child> Items;
    private ArrayList<ChildReport> childReports;
    private ArrayList<AnalyticsFooter> analyticsFooters;

    String category;
    String category_img ;

    public ArrayList<ChildReport> getChildReports() {
        return childReports;
    }

    public void setChildReports(ArrayList<ChildReport> childReports) {
        this.childReports = childReports;
    }


    public ArrayList<AnalyticsFooter> getAnalyticsFooters() {
        return analyticsFooters;
    }

    public void setAnalyticsFooters(ArrayList<AnalyticsFooter> analyticsFooters) {
        this.analyticsFooters = analyticsFooters;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategory_img() {
        return category_img;
    }

    public void setCategory_img(String category_img) {
        this.category_img = category_img;
    }

    public ArrayList<Child> getItems() {
        return Items;
    }

    public void setItems(ArrayList<Child> items) {
        Items = items;
    }
}
