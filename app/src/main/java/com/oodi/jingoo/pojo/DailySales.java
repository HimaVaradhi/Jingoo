package com.oodi.jingoo.pojo;

import java.io.Serializable;

/**
 * Created by pc on 11/23/17.
 */

public class DailySales implements Serializable {

    String selling_qty ;
    String category_name ;

    public String getSelling_qty() {
        return selling_qty;
    }

    public void setSelling_qty(String selling_qty) {
        this.selling_qty = selling_qty;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }
}
