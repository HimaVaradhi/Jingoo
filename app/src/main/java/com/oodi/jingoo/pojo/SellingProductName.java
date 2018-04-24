package com.oodi.jingoo.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by pc on 9/16/17.
 */

public class SellingProductName implements Serializable {

    String name ;
    List<SellingPrise> sellingPriseList ;
    List<Child> childList ;

    public List<Child> getChildList() {
        return childList;
    }

    public void setChildList(List<Child> childList) {
        this.childList = childList;
    }

    public List<SellingPrise> getSellingPriseList() {
        return sellingPriseList;
    }

    public void setSellingPriseList(List<SellingPrise> sellingPriseList) {
        this.sellingPriseList = sellingPriseList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
