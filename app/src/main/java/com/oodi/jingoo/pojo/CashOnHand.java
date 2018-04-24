package com.oodi.jingoo.pojo;

import java.io.Serializable;

/**
 * Created by pc on 11/6/17.
 */

public class CashOnHand implements Serializable {

    String products_name ;
    String ML750 ;
    String ML375 ;
    String ML180 ;

    public String getProducts_name() {
        return products_name;
    }

    public void setProducts_name(String products_name) {
        this.products_name = products_name;
    }

    public String getML750() {
        return ML750;
    }

    public void setML750(String ML750) {
        this.ML750 = ML750;
    }

    public String getML375() {
        return ML375;
    }

    public void setML375(String ML375) {
        this.ML375 = ML375;
    }

    public String getML180() {
        return ML180;
    }

    public void setML180(String ML180) {
        this.ML180 = ML180;
    }
}
