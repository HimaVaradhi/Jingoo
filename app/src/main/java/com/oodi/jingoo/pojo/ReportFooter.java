package com.oodi.jingoo.pojo;

import java.io.Serializable;

/**
 * Created by pc on 12/18/17.
 */

public class ReportFooter implements Serializable {

    String name;
    String qty;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }
}
