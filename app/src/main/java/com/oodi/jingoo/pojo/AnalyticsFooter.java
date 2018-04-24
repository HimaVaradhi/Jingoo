package com.oodi.jingoo.pojo;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by pc on 6/2/17.
 */

public class AnalyticsFooter implements Serializable {

    public AnalyticsFooter(){}

    ArrayList<Child> child_list;
    String products_name ;
    String value1 ;
    String value2 ;
    String value3 , total ;

    public ArrayList<Child> getChild_list() {
        return child_list;
    }

    public void setChild_list(ArrayList<Child> child_list) {
        this.child_list = child_list;
    }

    public String getProducts_name() {
        return products_name;
    }

    public void setProducts_name(String products_name) {
        this.products_name = products_name;
    }

    public String getValue1() {
        return value1;
    }

    public void setValue1(String value1) {
        this.value1 = value1;
    }

    public String getValue2() {
        return value2;
    }

    public void setValue2(String value2) {
        this.value2 = value2;
    }

    public String getValue3() {
        return value3;
    }

    public void setValue3(String value3) {
        this.value3 = value3;
    }
}
